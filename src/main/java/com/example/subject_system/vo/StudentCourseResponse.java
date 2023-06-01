package com.example.subject_system.vo;

import java.util.List;

public class StudentCourseResponse {
    private List<StudentResponse> studentResponseList;
    private String message;
//==


    public StudentCourseResponse() {
    }

    public StudentCourseResponse(String message) {
        this.message = message;
    }

    public StudentCourseResponse(List<StudentResponse> studentResponseList) {
        this.studentResponseList = studentResponseList;
    }
//==

    public List<StudentResponse> getStudentResponseList() {
        return studentResponseList;
    }

    public void setStudentResponseList(List<StudentResponse> studentResponseList) {
        this.studentResponseList = studentResponseList;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
