package com.example.subject_system.controller;

import com.example.subject_system.service.ifs.CourseService;
import com.example.subject_system.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class CourseController {
    @Autowired
    private CourseService courseService;
    @PostMapping(value = "course_add")
    public CourseAddResponse courseAdd(@RequestBody CourseAddRequest request){
        return  courseService.courseAdd(request);
    }

    @PostMapping(value = "course_select")
    public CourseSelectResponse courseSelect(@RequestBody CourseSelectRequest request){
        return courseService.courseSelect(request);
    }
    @PostMapping(value = "course_cancel")
    public CourseCancelResponse courseCancel(@RequestBody CourseCancelRequest request){
        return courseService.courseCancel(request);
    }

    @PostMapping(value = "get_course_info_by_code")
    public GetCourseResponse getCourseInfoByCode(@RequestBody CourseRequest request){
        return courseService.getCourseInfoByCode(request.getCode());
    }
    @PostMapping(value = "get_course_info_by_name")
    public GetCourseResponse getCourseInfoByCourseName(@RequestBody GetCourseRequest request){
        return courseService.getCourseInfoByCourseName(request);
    }

    @PostMapping(value = "course_delete")
    public CourseDeleteResponse courseDelete(@RequestBody CourseDeleteRequest request){
        return courseService.courseDelete(request);
    }
    @GetMapping(value = "get_all_course")
    public GetCourseResponse getAllCourse(){
        return courseService.getAllCourse();
    }
}
