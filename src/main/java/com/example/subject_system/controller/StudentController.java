package com.example.subject_system.controller;


import com.example.subject_system.service.ifs.StudentService;
import com.example.subject_system.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class StudentController {
    @Autowired
    private StudentService studentService;

    @PostMapping(value = "find_student_course_by_id")
    public StudentCourseResponse studentCourseQuery (@RequestBody StudentCourseRequest request){
        return studentService.studentCourseQuery(request);
    }
    @PostMapping(value = "student_delete")
    public StudentDeleteResponse studentDelete(@RequestBody StudentDeleteRequest request){
        return studentService.studentDelete(request);
    }
    @PostMapping(value = "student_add")
    public StudentAddResponse studentAdd(@RequestBody StudentAddRequest request){
        return studentService.studentAdd(request);
    }
}
