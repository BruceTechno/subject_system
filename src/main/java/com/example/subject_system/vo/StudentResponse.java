package com.example.subject_system.vo;

public class StudentResponse {
    private int sNumber;
    private String sName;
    private String courseCode;
    private String courseName;
    private String day;
    private int startTime;
    private int endTime;
    private int credit;
//================================================================================================
    public StudentResponse() {
    }

    public StudentResponse(int sNumber, String sName, String courseCode, String courseName, String day, int startTime, int endTime, int credit) {
        this.sNumber = sNumber;
        this.sName = sName;
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
        this.credit = credit;
    }
    //   ==========================================================================================

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public int getsNumber() {
        return sNumber;
    }

    public void setsNumber(int sNumber) {
        this.sNumber = sNumber;
    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
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
}
