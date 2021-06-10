package SpeedCodeBKD.Services.Impl;

import SpeedCodeBKD.Services.CommandService;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Service
public class CommandServiceImpl implements CommandService, InitializingBean {
    @Value("${command-executor.thread-name:command-executor-thread}")
    private String threadName;

    @Value("${command-executor.task-queue-max:20}")
    private Integer taskQueueMaxStorage;

    @Value("${cmd.pool-size:4}")
    private Integer corePoolSize;

    @Value("${cmd.maximum-pool-size:8}")
    private Integer maximumPoolSize;

    @Value("${cmd.keep-alive-seconds:15}")
    private              Integer            keepAliveSeconds;
    private              ThreadPoolExecutor executor;
    private static final String             BASH       = "sh";
    private static final String             BASH_PARAM = "-c";

    // use thread pool to read streams
    @Override
    public void afterPropertiesSet() {
        executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveSeconds, TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(taskQueueMaxStorage),
                new ThreadFactory() {
                    public Thread newThread(Runnable r) {
                        return new Thread(r, threadName + r.hashCode());
                    }
                },
                new ThreadPoolExecutor.AbortPolicy());
    }

    @Override
    public String executeCommand(String command) {
        Process p = null;
        String res;
        try {
            // need to pass command as bash's param,
            // so that we can compatible with commands: "echo a >> b.txt" or "bash a && bash b"
            List<String> cmds = new ArrayList<>();
            cmds.add(BASH);
            cmds.add(BASH_PARAM);
            cmds.add(command);
            ProcessBuilder pb = new ProcessBuilder(cmds);
            p = pb.start();

            Future<String> errorFuture = executor.submit(new ReadTask(p.getErrorStream()));
            Future<String> resFuture = executor.submit(new ReadTask(p.getInputStream()));
            int exitValue = p.waitFor();
            if (exitValue > 0) {
                throw new RuntimeException(errorFuture.get());
            }
            res = resFuture.get();

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (p != null) {p.destroy();}
        }
        // remove System.lineSeparator() (actually it's '\n') in the end of res if exists
        if (StringUtils.isNotBlank(res) && res.endsWith(System.lineSeparator())) {
            res = res.substring(0, res.lastIndexOf(System.lineSeparator()));
        }
        return res;
    }

    class ReadTask implements Callable<String> {
        InputStream is;

        ReadTask(InputStream is) {
            this.is = is;
        }

        @Override
        public String call() throws Exception {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        }
    }
}
