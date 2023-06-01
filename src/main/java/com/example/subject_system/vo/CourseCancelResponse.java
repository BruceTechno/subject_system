package com.example.subject_system.vo;

public class CourseCancelResponse {
    private String message;
//===

    public CourseCancelResponse() {
    }

    public CourseCancelResponse(String message) {
        this.message = message;
    }
    //===

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
