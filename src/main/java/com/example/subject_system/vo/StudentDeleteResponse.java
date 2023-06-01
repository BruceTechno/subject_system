package com.example.subject_system.vo;

public class StudentDeleteResponse {
    private String message;
//==

    public StudentDeleteResponse() {
    }

    public StudentDeleteResponse(String message) {
        this.message = message;
    }
    //==

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
