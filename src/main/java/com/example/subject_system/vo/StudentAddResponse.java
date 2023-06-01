package com.example.subject_system.vo;

import com.example.subject_system.entity.Student;

public class StudentAddResponse {
    private Student student;
    private String message;
//==

    public StudentAddResponse(Student student, String message) {
        this.student = student;
        this.message = message;
    }

    public StudentAddResponse(String message) {
        this.message = message;
    }

    public StudentAddResponse() {
    }
//==

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
