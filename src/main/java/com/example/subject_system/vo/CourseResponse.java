package com.example.subject_system.vo;

import com.example.subject_system.entity.Course;
import com.example.subject_system.entity.Student;

import java.util.List;

public class CourseResponse {
    private Course course;
    private Student student;
    private String message ;
    private List<Course> courseList;
    private List<StudentResponse> studentResponse;
//============================================================================================


    public CourseResponse(List<StudentResponse> studentResponse) {
        this.studentResponse = studentResponse;
    }

    public CourseResponse() {
    }

    public CourseResponse(String message, List<StudentResponse> studentResponse) {
        this.message = message;
        this.studentResponse = studentResponse;
    }

    public CourseResponse(Student student, String message) {
        this.student = student;
        this.message = message;
    }

    public CourseResponse(Course course, String message) {
        this.course = course;
        this.message = message;
    }

    public CourseResponse(List<Course> courseList,String message ) {
        this.message = message;
        this.courseList = courseList;
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

    public List<StudentResponse> getStudentResponse() {
        return studentResponse;
    }

    public void setStudentResponse(List<StudentResponse> studentResponse) {
        this.studentResponse = studentResponse;
    }
}
