package com.example.subject_system.vo;

import com.example.subject_system.entity.Course;

import java.util.List;

public class GetCourseResponse {
    private Course course;
    private List<Course> courseList;
    private String message;

//==

    public GetCourseResponse() {
    }

    public GetCourseResponse(String message) {
        this.message = message;
    }

    public GetCourseResponse(List<Course> courseList) {
        this.courseList = courseList;
    }

    public GetCourseResponse(List<Course> courseList, String message) {
        this.courseList = courseList;
        this.message = message;
    }

    public GetCourseResponse(Course course, String message) {
        this.course = course;
        this.message = message;
    }
//==

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public List<Course> getCourseList() {
        return courseList;
    }

    public void setCourseList(List<Course> courseList) {
        this.courseList = courseList;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
