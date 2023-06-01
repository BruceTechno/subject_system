package com.example.subject_system.vo;

import java.util.List;

public class CourseSelectRequest {
    private int studentNumber;
    private List<String> courseCodeList;
    private String studentName;
//==

    public CourseSelectRequest() {
    }
//====

    public int getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(int studentNumber) {
        this.studentNumber = studentNumber;
    }

    public List<String> getCourseCodeList() {
        return courseCodeList;
    }

    public void setCourseCodeList(List<String> courseCodeList) {
        this.courseCodeList = courseCodeList;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
}
