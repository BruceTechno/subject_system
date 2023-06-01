package com.example.subject_system.vo;

public class StudentAddRequest {
    private int studentNumber ;
    private String studentName;
//==

    public StudentAddRequest() {
    }
//==

    public int getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(int studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
}
