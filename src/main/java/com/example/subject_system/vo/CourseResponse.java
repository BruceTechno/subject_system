package com.example.subject_system.vo;

import com.example.subject_system.entity.Course;
import com.example.subject_system.entity.Student;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
@JsonInclude(JsonInclude.Include.NON_NULL)

public class CourseResponse {
    private Course course;
    private Student student;
    private String message ;
    private List<?> studentResponse;
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

    public CourseResponse(List<?> studentResponse, String message) {
        this.message = message;
        this.studentResponse = studentResponse;
    }

    public CourseResponse(String message) {
        this.message = message;
    }

    public CourseResponse(List<?> studentResponse) {
        this.studentResponse = studentResponse;
    }

    //====================================================================================================================


    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

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

    public List<?> getStudentResponse() {
        return studentResponse;
    }

    public void setStudentResponse(List<?> studentResponse) {
        this.studentResponse = studentResponse;
    }
}
