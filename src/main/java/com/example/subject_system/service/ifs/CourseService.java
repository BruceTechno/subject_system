package com.example.subject_system.service.ifs;

import com.example.subject_system.vo.*;

public interface CourseService {    //request會用到的鮮血


    public CourseAddResponse courseAdd (CourseAddRequest request);//新增課程 新增學生
    public CourseDeleteResponse courseDelete(CourseDeleteRequest request);
    public CourseSelectResponse courseSelect(CourseSelectRequest request);
    public CourseCancelResponse courseCancel(CourseCancelRequest request);
    public GetCourseResponse getCourseInfoByCode(String courseCode);
    public GetCourseResponse getCourseInfoByCourseName(GetCourseRequest request);
    public GetCourseResponse getAllCourse ();





//
//    public CourseResponse allSubjectQuery(CourseRequest request);

}
