package com.example.subject_system.service.ifs;

import com.example.subject_system.vo.CourseRequest;
import com.example.subject_system.vo.CourseResponse;

public interface CourseService {    //request會用到的鮮血


    public CourseResponse courseAdd (CourseRequest request);//新增課程 新增學生

    public CourseResponse studentAdd(CourseRequest request);
    public CourseResponse courseSelect(CourseRequest request);//比對 上課名稱 上課時間(for衝堂) 比學分 比有沒有選過

    public CourseResponse courseCancel(CourseRequest request);
//
    public CourseResponse studentCourseQuery(CourseRequest request);
    public CourseResponse getCourseInfoByCode(String courseCode);
    public CourseResponse getCourseInfoByCourseName(CourseRequest request);
//
//    public CourseResponse allSubjectQuery(CourseRequest request);

}
