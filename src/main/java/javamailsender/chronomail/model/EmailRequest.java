package javamailsender.chronomail.model;

import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public class EmailRequest {
    private String to;
    private String subject;
    private String text;
    private String htmlContent;
    private List<MultipartFile> attachments;
    private String templateName;
    private Object templateData;

    // Constructors
    public EmailRequest() {}

    public EmailRequest(String to, String subject, String text) {
        this.to = to;
        this.subject = subject;
        this.text = text;
    }

    // Getters and Setters
    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getHtmlContent() {
        return htmlContent;
    }

    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }

    public List<MultipartFile> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<MultipartFile> attachments) {
        this.attachments = attachments;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public Object getTemplateData() {
        return templateData;
    }

    public void setTemplateData(Object templateData) {
        this.templateData = templateData;
    }
}
