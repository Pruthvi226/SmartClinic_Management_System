package com.smartclinic.dto;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Standard error response DTO for API error handling
 */
public class ErrorResponse {
    private int status;
    private String error;
    private String message;
    private LocalDateTime timestamp;
    private String path;
    private Map<String, Object> details;

    public ErrorResponse() {
        this.timestamp = LocalDateTime.now();
        this.details = new HashMap<>();
    }

    public ErrorResponse(int status, String error, String message) {
        this();
        this.status = status;
        this.error = error;
        this.message = message;
    }

    public ErrorResponse(int status, String error, String message, String path) {
        this(status, error, message);
        this.path = path;
    }

    // Getters and Setters
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Map<String, Object> getDetails() {
        return details;
    }

    public void setDetails(Map<String, Object> details) {
        this.details = details;
    }

    public void addDetail(String key, Object value) {
        this.details.put(key, value);
    }
}
