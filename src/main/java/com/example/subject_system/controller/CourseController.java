package com.example.subject_system.controller;

import com.example.subject_system.service.ifs.CourseService;
import com.example.subject_system.vo.CourseRequest;
import com.example.subject_system.vo.CourseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CourseController {
    @Autowired
    private CourseService courseService;
    @PostMapping(value = "course_add")
    public CourseResponse courseAdd(@RequestBody CourseRequest request){
        return  courseService.courseAdd(request);
    }
    @PostMapping(value = "student_add")
    public CourseResponse studentAdd(@RequestBody CourseRequest request){
        return courseService.studentAdd(request);
    }


}
