package com.example.subject_system.service.ifs;

import com.example.subject_system.vo.*;

public interface StudentService {

    public StudentAddResponse studentAdd(StudentAddRequest request);
    public StudentDeleteResponse studentDelete(StudentDeleteRequest request);
    public StudentCourseResponse studentCourseQuery(StudentCourseRequest request);
}
