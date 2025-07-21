package javamailsender.chronomail.model;

public class EmailResponse {
    private boolean success;
    private String message;
    private String timestamp;

    public EmailResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
        this.timestamp = java.time.LocalDateTime.now().toString();
    }

    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
