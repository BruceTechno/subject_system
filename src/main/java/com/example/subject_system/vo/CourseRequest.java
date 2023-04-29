package com.example.subject_system.vo;

import java.util.List;

public class CourseRequest {
    private String code;
    private String name ;
    private String day ;
    private int startTime;
    private int endTime;
    private int credit;
    ////////////////////
    private int studentNumber ;
    private String studentName;
    private List<String> courseCodeList;
//=============================================================================

    public CourseRequest() {
    }

    public CourseRequest(int studentNumber, String studentName, List<String> courseCodeList) {
        this.studentNumber = studentNumber;
        this.studentName = studentName;
        this.courseCodeList = courseCodeList;
    }
    //=============================================================================



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

    public List<String> getCourseCodeList() {
        return courseCodeList;
    }

    public void setCourseCodeList(List<String> courseCodeList) {
        this.courseCodeList = courseCodeList;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }
}
