package com.example.subject_system.vo;

public class CourseDeleteResponse {
    private String message;
//==

    public CourseDeleteResponse() {
    }

    public CourseDeleteResponse(String message) {
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
