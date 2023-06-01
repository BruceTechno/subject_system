package com.example.subject_system.vo;

import java.util.List;

public class CourseCancelRequest {
    private int studentNumber;
    private List<String> courseCodeList;
//====

    public CourseCancelRequest() {
    }
//===

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
}
