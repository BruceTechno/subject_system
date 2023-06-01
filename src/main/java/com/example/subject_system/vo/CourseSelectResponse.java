package com.example.subject_system.vo;

public class CourseSelectResponse {
    private String message;
//===

    public CourseSelectResponse() {
    }

    public CourseSelectResponse(String message) {
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
