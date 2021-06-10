package SpeedCodeBKD.Utils.Processor;

import io.micrometer.core.lang.Nullable;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;

@Component
@Slf4j
public class EmailSender {

    @Autowired
    private JavaMailSender mailSender;

    private static final String DefaultEmail = "SmartSheep Studio · CodeCraft 官方<smartsheep.codecraft@outlook.com>";

    @SneakyThrows
    public void send(@Nullable String from, @Nullable Boolean rendered, String[] receive, String subject, String content) {
        MimeMessage mailMessage = mailSender.createMimeMessage();
        MimeMessageHelper mailHelper = new MimeMessageHelper(mailMessage);

        if(from == null) mailHelper.setFrom(EmailSender.DefaultEmail);
        if(from != null) mailHelper.setFrom(from);

        if(rendered == null) mailHelper.setText(content, false);
        if(rendered != null) mailHelper.setText(content, rendered);

        mailHelper.setTo(receive);
        mailHelper.setSubject(subject);
        mailSender.send(mailMessage);
    }
}