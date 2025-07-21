package javamailsender.chronomail.controller;

import javamailsender.chronomail.entity.EmailLog;
import javamailsender.chronomail.model.EmailRequest;
import javamailsender.chronomail.model.EmailResponse;
import javamailsender.chronomail.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/email")
@CrossOrigin(origins = "*")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/send-simple")
    public ResponseEntity<EmailResponse> sendSimpleEmail(@RequestBody EmailRequest emailRequest) {
        try {
            emailService.sendSimpleEmail(emailRequest);
            return ResponseEntity.ok(new EmailResponse(true, "Simple email sent successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new EmailResponse(false, "Failed to send email: " + e.getMessage()));
        }
    }

    @PostMapping("/send-html")
    public ResponseEntity<EmailResponse> sendHtmlEmail(@RequestBody EmailRequest emailRequest) {
        try {
            emailService.sendHtmlEmail(emailRequest);
            return ResponseEntity.ok(new EmailResponse(true, "HTML email sent successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new EmailResponse(false, "Failed to send HTML email: " + e.getMessage()));
        }
    }

    @PostMapping("/send-with-attachments")
    public ResponseEntity<EmailResponse> sendEmailWithAttachments(
            @RequestParam("to") String to,
            @RequestParam("subject") String subject,
            @RequestParam("text") String text,
            @RequestParam(value = "htmlContent", required = false) String htmlContent,
            @RequestParam(value = "attachments", required = false) List<MultipartFile> attachments) {

        try {
            EmailRequest emailRequest = new EmailRequest();
            emailRequest.setTo(to);
            emailRequest.setSubject(subject);
            emailRequest.setText(text);
            emailRequest.setHtmlContent(htmlContent);
            emailRequest.setAttachments(attachments);

            emailService.sendEmailWithAttachments(emailRequest);
            return ResponseEntity.ok(new EmailResponse(true, "Email with attachments sent successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new EmailResponse(false, "Failed to send email with attachments: " + e.getMessage()));
        }
    }

    @PostMapping("/send-template")
    public ResponseEntity<EmailResponse> sendTemplateEmail(
            @RequestParam("to") String to,
            @RequestParam("subject") String subject,
            @RequestParam("templateName") String templateName,
            @RequestBody(required = false) Map<String, Object> templateData) {

        try {
            emailService.sendTemplateEmail(to, subject, templateName, templateData);
            return ResponseEntity.ok(new EmailResponse(true, "Template email sent successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new EmailResponse(false, "Failed to send template email: " + e.getMessage()));
        }
    }

    @GetMapping("/history")
    public ResponseEntity<List<EmailLog>> getEmailHistory() {
        try {
            List<EmailLog> history = emailService.getEmailHistory();
            return ResponseEntity.ok(history);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/history/{email}")
    public ResponseEntity<List<EmailLog>> getEmailsByRecipient(@PathVariable String email) {
        try {
            List<EmailLog> emails = emailService.getEmailsByRecipient(email);
            return ResponseEntity.ok(emails);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Legacy endpoints for backward compatibility
    @GetMapping("/sendEmail")
    public ResponseEntity<EmailResponse> sendEmailLegacy() {
        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setTo("popstock1001@gmail.com");
        emailRequest.setSubject("Test Email");
        emailRequest.setText("This is a test email sent from ChronoMail application.");

        try {
            emailService.sendSimpleEmail(emailRequest);
            return ResponseEntity.ok(new EmailResponse(true, "Email sent successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new EmailResponse(false, "Failed to send email: " + e.getMessage()));
        }
    }
}
