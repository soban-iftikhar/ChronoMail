package javamailsender.chronomail.service;

import javamailsender.chronomail.entity.EmailLog;
import javamailsender.chronomail.model.EmailRequest;
import javamailsender.chronomail.repository.EmailLogRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private EmailLogRepository emailLogRepository;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendSimpleEmail(EmailRequest emailRequest) throws MessagingException {
        EmailLog emailLog = new EmailLog(emailRequest.getTo(), emailRequest.getSubject(), "PENDING");

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(emailRequest.getTo());
            message.setSubject(emailRequest.getSubject());
            message.setText(emailRequest.getText());

            mailSender.send(message);

            emailLog.setStatus("SUCCESS");
            emailLogRepository.save(emailLog);

        } catch (Exception e) {
            emailLog.setStatus("FAILED");
            emailLog.setErrorMessage(e.getMessage());
            emailLogRepository.save(emailLog);
            throw new MessagingException("Failed to send email: " + e.getMessage());
        }
    }

    public void sendHtmlEmail(EmailRequest emailRequest) throws MessagingException {
        EmailLog emailLog = new EmailLog(emailRequest.getTo(), emailRequest.getSubject(), "PENDING");

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

            helper.setFrom(fromEmail);
            helper.setTo(emailRequest.getTo());
            helper.setSubject(emailRequest.getSubject());
            helper.setText(emailRequest.getHtmlContent(), true);

            mailSender.send(message);

            emailLog.setStatus("SUCCESS");
            emailLogRepository.save(emailLog);

        } catch (Exception e) {
            emailLog.setStatus("FAILED");
            emailLog.setErrorMessage(e.getMessage());
            emailLogRepository.save(emailLog);
            throw new MessagingException("Failed to send HTML email: " + e.getMessage());
        }
    }

    public void sendEmailWithAttachments(EmailRequest emailRequest) throws MessagingException, IOException {
        EmailLog emailLog = new EmailLog(emailRequest.getTo(), emailRequest.getSubject(), "PENDING");
        emailLog.setHasAttachments(true);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());

            helper.setFrom(fromEmail);
            helper.setTo(emailRequest.getTo());
            helper.setSubject(emailRequest.getSubject());

            // Set content (HTML or plain text)
            if (emailRequest.getHtmlContent() != null && !emailRequest.getHtmlContent().isEmpty()) {
                helper.setText(emailRequest.getText(), emailRequest.getHtmlContent());
            } else {
                helper.setText(emailRequest.getText());
            }

            // Add attachments
            if (emailRequest.getAttachments() != null && !emailRequest.getAttachments().isEmpty()) {
                for (MultipartFile file : emailRequest.getAttachments()) {
                    helper.addAttachment(file.getOriginalFilename(), file);
                }
            }

            mailSender.send(message);

            emailLog.setStatus("SUCCESS");
            emailLogRepository.save(emailLog);

        } catch (Exception e) {
            emailLog.setStatus("FAILED");
            emailLog.setErrorMessage(e.getMessage());
            emailLogRepository.save(emailLog);
            throw new MessagingException("Failed to send email with attachments: " + e.getMessage());
        }
    }

    public void sendTemplateEmail(String to, String subject, String templateName, Object templateData) throws MessagingException {
        EmailLog emailLog = new EmailLog(to, subject, "PENDING");

        try {
            Context context = new Context();
            context.setVariable("data", templateData);

            String htmlContent = templateEngine.process(templateName, context);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            mailSender.send(message);

            emailLog.setStatus("SUCCESS");
            emailLogRepository.save(emailLog);

        } catch (Exception e) {
            emailLog.setStatus("FAILED");
            emailLog.setErrorMessage(e.getMessage());
            emailLogRepository.save(emailLog);
            throw new MessagingException("Failed to send template email: " + e.getMessage());
        }
    }

    public List<EmailLog> getEmailHistory() {
        return emailLogRepository.findAll();
    }

    public List<EmailLog> getEmailsByRecipient(String email) {
        return emailLogRepository.findByRecipientEmail(email);
    }
}
