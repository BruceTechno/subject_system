package com.example.subject_system.service.impl;

import com.example.subject_system.constants.RtnCode;
import com.example.subject_system.entity.Course;
import com.example.subject_system.entity.Student;
import com.example.subject_system.repository.CourseDao;
import com.example.subject_system.repository.StudentDao;
import com.example.subject_system.service.ifs.StudentService;
import com.example.subject_system.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class StudentImpl implements StudentService {
    @Autowired
    private StudentDao studentDao;

    @Autowired
    private CourseDao courseDao;
    @Override
    public StudentAddResponse studentAdd(StudentAddRequest request) {
        int studentNumber = request.getStudentNumber();
        String studentName = request.getStudentName();
//     String studentCourseCode = request.getStudentCode()
//        List<String>  studentCourseCodeList = request.getStudentCode();
        if (studentNumber < 0) {
            return new StudentAddResponse(RtnCode.DATA_ERROR.getMessage());
        }
        if (!StringUtils.hasText(studentName)) {
            return new StudentAddResponse(RtnCode.CANNOT_EMPTY.getMessage());
        }

        if (studentDao.existsById(studentNumber)) {
            return new StudentAddResponse(RtnCode.DATA_ERROR.getMessage());//"student already exist"
        }
        Student result = new Student(studentNumber, studentName);
        studentDao.save(result);

        return new StudentAddResponse(result,RtnCode.SUCCESSFUL.getMessage());
    }
    @Override
    public StudentDeleteResponse studentDelete(StudentDeleteRequest request) {
//1.如果學生沒有選課 >> 直接刪
//2.如果有選課     >> 進到課程DB 把選課人數調整 之後 再刪掉
        int studentNumber = request.getStudentNumber();
        if (studentNumber < 0){
            return new StudentDeleteResponse(RtnCode.DATA_ERROR.getMessage());
        }
        Optional<Student> studentInfo = studentDao.findById(studentNumber);
        if (!studentInfo.isPresent()){
            return new StudentDeleteResponse(RtnCode.DATA_ERROR.getMessage());
        }
        String selectedCode = studentInfo.get().getCode();
        if (!StringUtils.hasText(selectedCode)){//如果學生代碼沒東西是空的
            studentDao.delete(studentInfo.get());
        }
        //如果還有選課的話
        String[]  strList = selectedCode.split(",");
        List<String> selectedCodeList = new ArrayList<>(Arrays.asList(strList));
        List<Course> selectedCourseInfo = courseDao.findAllById(selectedCodeList);

        for (Course item : selectedCourseInfo){
            item.setNumberOfStudent(item.getNumberOfStudent()-1);
        }
        courseDao.saveAll(selectedCourseInfo);
        studentDao.deleteById(studentNumber);
        return new StudentDeleteResponse(RtnCode.SUCCESSFUL.getMessage());
    }
    @Override
    public StudentCourseResponse studentCourseQuery(StudentCourseRequest request) {
        //輸入學生學號 撈出 課程代碼 ,依學號查詢，顯示學號、姓名、課程代碼、課程名稱、上課星期幾、上課開始時間、上課結束時間、學分
        //課程代碼去撈出課程 然後把資訊都秀出來
        int studentNumber = request.getStudentNumber();
        if (studentNumber < 0){
            return new StudentCourseResponse(RtnCode.DATA_ERROR.getMessage());
        }
        List<StudentResponse> result = studentDao.searchByStudentNumber(studentNumber);
        if (CollectionUtils.isEmpty(result)){
            return new StudentCourseResponse(RtnCode.NOT_FOUND.getMessage());
        }
        return new StudentCourseResponse(result);
    }
}
