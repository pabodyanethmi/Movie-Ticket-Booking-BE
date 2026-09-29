package lk.ijse.backend.exception;

import java.time.LocalDateTime;
import java.util.List;

public class ErrorDetails {
    private int status;
    private String message;
    private String details;
    private List<String> fieldErrors;
    private LocalDateTime timestamp;

    public ErrorDetails() {
        this.timestamp = LocalDateTime.now();
    }

    public ErrorDetails(int status, String message, String details) {
        this.status = status;
        this.message = message;
        this.details = details;
        this.timestamp = LocalDateTime.now();
    }

    public ErrorDetails(int status, String message, String details, List<String> fieldErrors, LocalDateTime timestamp) {
        this.status = status;
        this.message = message;
        this.details = details;
        this.fieldErrors = fieldErrors;
        this.timestamp = timestamp != null ? timestamp : LocalDateTime.now();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private int status;
        private String message;
        private String details;
        private List<String> fieldErrors;
        private LocalDateTime timestamp = LocalDateTime.now();

        public Builder status(int status) {
            this.status = status;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder details(String details) {
            this.details = details;
            return this;
        }

        public Builder fieldErrors(List<String> fieldErrors) {
            this.fieldErrors = fieldErrors;
            return this;
        }

        public Builder timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public ErrorDetails build() {
            return new ErrorDetails(status, message, details, fieldErrors, timestamp);
        }
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public List<String> getFieldErrors() {
        return fieldErrors;
    }

    public void setFieldErrors(List<String> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
