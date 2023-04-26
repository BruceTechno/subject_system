package com.example.subject_system.service.impl;

import com.example.subject_system.entity.Course;
import com.example.subject_system.entity.Student;
import com.example.subject_system.repository.CourseDao;
import com.example.subject_system.repository.StudentDao;
import com.example.subject_system.service.ifs.CourseService;
import com.example.subject_system.vo.CourseRequest;
import com.example.subject_system.vo.CourseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

import static java.util.Map.entry;

@Service
public class CourseImpl implements CourseService {
    @Autowired
    private CourseDao courseDao;
    @Autowired
    private StudentDao studentDao;

    @Override
    public CourseResponse courseAdd(CourseRequest request) {
        String courseCode = request.getCode();
        String courseName = request.getName();
        String courseDay = request.getDay();
        int courseStartTime = request.getStartTime();
        int courseEndTime = request.getEndTime();
        int courseCredit = request.getCredit();

        if (!StringUtils.hasText(courseCode) || !StringUtils.hasText(courseName) || !StringUtils.hasText(courseDay)) {
            return new CourseResponse("Error String ");
        }
        if (courseStartTime < 0 || courseEndTime < 0 || courseCredit < 0) {
            return new CourseResponse("Error int");
        }
        if (courseDao.existsById(courseCode)) {
            return new CourseResponse("course already exist");
        }
        Course resCourse = new Course(courseCode, courseName, courseDay, courseStartTime, courseEndTime, courseCredit);
        courseDao.save(resCourse);


        return new CourseResponse(resCourse, "新增課程成功");
    }

    @Override
    public CourseResponse studentAdd(CourseRequest request) {//先有學生姓名、學號就好
        int studentNumber = request.getStudentNumber();
        String studentName = request.getStudentName();
//     String studentCourseCode = request.getStudentCode()
//        List<String>  studentCourseCodeList = request.getStudentCode();
        if (studentNumber < 0) {
            return new CourseResponse("studentNumber error");
        }
        if (!StringUtils.hasText(studentName)) {
            return new CourseResponse("student name ");
        }

        if (studentDao.existsById(studentNumber)) {
            return new CourseResponse("student already exist");
        }
        Student result = new Student(studentNumber, studentName);
        studentDao.save(result);

        return new CourseResponse(result, "學生新增成功");
    }

    @Override
    public CourseResponse courseSelect(CourseRequest request) {
        //request帶進來的資訊
        String courseCodeFromRequest = request.getCode();
        int studentNumber = request.getStudentNumber();
        //依request 進來的資料 抓出course DB裡面所有課程    //與 所有可選課程 比對 看是否存在這堂課可以選
        Optional<Course> selectingCourseInfo = courseDao.findById(courseCodeFromRequest);
        if (!selectingCourseInfo.isPresent()) {
            return new CourseResponse("There is no this course ");
        }
        //為了抓出 學生所選上的課 (課程代碼) 以進一步做之後比對
        Optional<Student> studentInfo = studentDao.findById(studentNumber);//學號、學生姓名、學生已選課程代碼、學生已選課程名稱
        if (!studentInfo.isPresent()) {
            return new CourseResponse("There is no this student ");
        }

        //學生資訊
        List<String> studentCode = studentInfo.get().getCode(); //學生可選好幾門課 所以接出來的上課代碼 可能是多筆
        String studentName = studentInfo.get().getName();
        //int studentNumber = studentInfo.get().getNumber();學號 = request帶進來的那個

        //依照接出來的多筆代碼去把這些課程資訊搜出來 比對下列資訊
        List<Course> selectedCourseInfo = courseDao.findAllById(studentCode);

        //依照學生目前有的課程代碼去搜尋
        //存在的話 把資料拿出來比對之後存入
        //比對    1.要選的課程名稱不能(與學生資料庫內的課程名稱)相同
        //比對    2.不能衝堂 (時間) (課程時間)VS(學生資料庫內時間)
        //比對    3.學分總數不能超過10(學生資料庫) // student DB 多一個學分欄位???????????????????
        //比對    4.未選上的才可以選 拿輸入的courseCode 跟 學生資料庫裏面的 exist by --> 直接find by 找的到就後續處理 找不到就
        //比對    5.每門課只能三位學生 //course再多一個欄位 for 人數?????????????????????????????????????????

        //欲選課之資訊
        String selectingCourseName = selectingCourseInfo.get().getName();
        String selectingCourseDay = selectingCourseInfo.get().getDay();
        int selectingCourseStartTime = selectingCourseInfo.get().getStartTime();
        int selectingCourseEndTime = selectingCourseInfo.get().getEndTime();
        int selectingCourseCredit = selectingCourseInfo.get().getCredit();
        int selectingCourseNumberOfStudent =selectingCourseInfo.get().getNumberOfStudent();

        //已選之課          //目前已選的課程資訊 用以下list接出來
        List<String> selectedCourseNameList = new ArrayList<>();
        List<Integer> selectedCourseStartTimeList = new ArrayList<>();
        List<Integer> selectedCourseEndTimeList = new ArrayList<>();
        List<Integer> selectedCourseCreditList = new ArrayList<>();
        List<String> selectedCourseCodeList = new ArrayList<>();
        List<String> selectedCourseDayList = new ArrayList<>();
        for (Course item : selectedCourseInfo){
            selectedCourseNameList.add(item.getName());
            selectedCourseStartTimeList.add(item.getStartTime());
            selectedCourseEndTimeList.add(item.getEndTime());
            selectedCourseCreditList.add(item.getCredit());
            selectedCourseCodeList.add(item.getCode());
            selectedCourseDayList.add(item.getDay());// [一、二、三]有課
        }
        // 1.要選的課程名稱不能(與學生資料庫內的課程名稱)相同
        if (selectedCourseNameList.contains(selectingCourseName)){
            return new CourseResponse("This student already choose this course");
        }
//        2.不能衝堂 星期一樣之後再去判斷 欲選之課開始的時間 要大於 已選之課結束的時間 或 欲選之課結束的時間 要小於已選之刻開始的時間
        if (selectedCourseDayList.contains(selectingCourseDay)) { //加上 判斷 是否同一天
            TreeMap<Integer, Integer> selectedCourseTree = new TreeMap<>();
            TreeMap<Integer, Integer> selectingCourseTree = new TreeMap<>();
            //已選的課
            for (int start : selectedCourseStartTimeList) {//800 1000    1400 1600
                for (int end : selectedCourseEndTimeList) {//850 1050    1450 1650
                    //放到Tree Map 裡面 讓他排序 可以一對一
                    selectedCourseTree.put(start, end);
                }
            }
            //     TreeMap<Integer,Integer> testMap = new TreeMap<>();//800 850
            //1000  1050
            //1400  1450
            //1600  1650
            //欲選之課                                           // s1   e1
            // K :  V
            selectingCourseTree.put(selectingCourseStartTime, selectingCourseEndTime);//欲選之課程加入treeMap

            for (Map.Entry<Integer, Integer> selecting : selectingCourseTree.entrySet()) {//欲選之課 S2,E2
                for (Map.Entry<Integer, Integer> selected : selectedCourseTree.entrySet()) {//已選之課 S1,E1
                    if (selecting.getKey() <= selected.getValue() || selecting.getValue() >= selected.getKey()) {
                        return new CourseResponse("衝堂");
                    }
                }
            }
        }
        //3.學分總數不能超過10(學生資料庫)
        int creditSum = 0;
        for (int item : selectedCourseCreditList){
            creditSum += item;
        }
        if (selectingCourseCredit + creditSum > 10 ){
            return new CourseResponse("學分超過10");
        }
//        4.未選上的才可以選 拿輸入的courseCode 跟 學生資料庫裏面的 exist by
        if (selectedCourseCodeList.contains(courseCodeFromRequest)){
            return new CourseResponse("你已有蔗糖課(課程代碼重複)");
        }
//        5.每門課只能三位學生 //course再多一個欄位
        if (selectingCourseNumberOfStudent >= 3){
            return new CourseResponse("選課人數已達上限");
        }

//        Student resultStudent = new Student(studentNumber,studentName,studentCode.toString());
//        studentDao.save()

//studentDao.save  studentDao 建構方法帶進去課程代碼 課程名稱


        return null;//stuDao
    }
}
