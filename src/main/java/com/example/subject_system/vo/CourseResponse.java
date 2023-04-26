package com.example.subject_system.vo;

import com.example.subject_system.entity.Course;
import com.example.subject_system.entity.Student;

public class CourseResponse {
    private Course course;
    private Student student;
    private String message ;
//============================================================================================


    public CourseResponse() {
    }

    public CourseResponse(Student student, String message) {
        this.student = student;
        this.message = message;
    }

    public CourseResponse(Course course, String message) {
        this.course = course;
        this.message = message;
    }

    public CourseResponse(String message) {
        this.message = message;
    }

    //====================================================================================================================

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
