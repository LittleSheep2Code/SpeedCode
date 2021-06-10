package SpeedCodeBKD;

import SpeedCodeBKD.Services.CommandService;
import SpeedCodeBKD.Services.CompileUtilsService;
import SpeedCodeBKD.Utils.Processor.EmailSender;
import SpeedCodeBKD.Utils.Processor.IOReader;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.InputStream;

@Slf4j
@SpringBootTest
class SpeedCodeBackedApplicationTests {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    EmailSender emailProcessor;

    @Autowired
    CommandService commandService;

    @Autowired
    SSHCommandService sshCommandService;

    @Autowired
    CompileUtilsService compileUtilsService;
	
    @Test
    void emailTest() {
        log.info(" * Prepare Email System... ");
        log.info("================ SENDING STARTED EMAIL TO ADMIN ================");
        log.info(" * Send Server Booted Email...");
         emailProcessor.sendEmail(
                 "cadenjiang@outlook.com",
                 "SpeedCode Server Completed Start",
                 "SpeedCode Server Completed Start without error"
         );
        log.info(" * Completed");
        log.info("================================================================");
    }

    @SneakyThrows
    @Disabled
    @Test
    void compileTest() {
        // Compile file
        log.info(" * Getting start compile!");
        String address = new ClassPathResource("static/init.cpp").getFile().getCanonicalPath();
        String target = new ClassPathResource("static/init").getFile().getCanonicalPath();
        log.info(" * Target file: " + address);
        log.info(" * Output file: " + target);
        log.info(" * Execute command: " + "g++ " + address + " -o " + target);
        try { commandService.executeCommand("g++ " + address + " -o " + target); }
        catch(RuntimeException e) { log.error(" * Cannot compile file!"); return; }
        log.info(" * Completed!");

        // Execute file
        Process targetProcess = Runtime.getRuntime().exec(target);
        InputStream istream = targetProcess.getInputStream();
        InputStream estream = targetProcess.getErrorStream();
        String s = IOReader.read(istream);
        log.info(" * -------------------------- Result: ");
        log.info(s);
    }

    @SneakyThrows
    @Test
    void compileUtilsServiceTest() {
        // Compile file
        log.info(" * Getting start running!");
        JSONObject result = compileUtilsService.read2executeProgram("static/init.cpp", CompileUtilsService.LanguageList.CPP_GPP9, "9 2");
        log.info(" * Result stdout: " + result.getString("stdout"));
        log.info(" * Result stderr: " + result.getString("stderr"));
        log.info(" * Result compileError: " + result.getString("compile_output"));
    }

    @SneakyThrows
    @Test
    void SSHTest() {
        log.info(" * Starting execute command to Server!");
        sshCommandService.initConnection();
        String result = IOReader.read(sshCommandService.executeScript("whoami", ""));
        log.info(" * Completed!");
        log.info(" * Execute Result(`whoami`): " + result);
    }
}
