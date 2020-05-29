package com.bnhp.gitlabci.devsecops.pipeline.integrations.mail;

import com.bnhp.gitlabci.devsecops.pipeline.objects.ReportObject;
import com.bnhp.gitlabci.devsecops.pipeline.objects.ScanObject;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Objects;

@Component("mail")
public class MailIntegrationService {

    private final JavaMailSender mailSender;
    private final Environment environment;

    public MailIntegrationService(JavaMailSender mailSender, Environment environment) {
        this.mailSender = mailSender;
        this.environment = environment;
    }

    public void send(ScanObject obj, String templateContent) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        try {

            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setSubject("Your " + obj.getSource() + " report is ready");
            mimeMessageHelper.setFrom(Objects.requireNonNull(environment.getProperty("devsecops.integration.mail.from")));
            mimeMessageHelper.setTo(obj.getRecipients());
            mimeMessageHelper.setText(templateContent,true);
            for (ReportObject attachment: obj.getReports()) {
                File tempFile = File.createTempFile(FilenameUtils.getBaseName(attachment.getFilename()), "." + FilenameUtils.getExtension(attachment.getFilename()));
                mimeMessageHelper.addAttachment(tempFile.getName(), tempFile);
            }

            mailSender.send(mimeMessageHelper.getMimeMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
