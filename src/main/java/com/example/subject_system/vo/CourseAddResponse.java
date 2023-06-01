package com.example.subject_system.vo;

import com.example.subject_system.entity.Course;

public class CourseAddResponse {
    private String message;
    private Course course;
//================================================================

    public CourseAddResponse(String message) {
        this.message = message;
    }

    public CourseAddResponse() {
    }

    public CourseAddResponse( Course course , String message)  {
        this.message = message;
        this.course = course;
    }
    //================================================================

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
