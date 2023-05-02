package com.example.subject_system.service.impl;

import com.example.subject_system.entity.Course;
import com.example.subject_system.entity.Student;
import com.example.subject_system.repository.CourseDao;
import com.example.subject_system.repository.StudentDao;
import com.example.subject_system.service.ifs.CourseService;
import com.example.subject_system.vo.CourseRequest;
import com.example.subject_system.vo.CourseResponse;
import com.example.subject_system.vo.StudentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
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
    public CourseResponse courseSelect(CourseRequest request) {//List 選課代碼 帶進來 拆成一堂一堂加?
        //欲選之課 的資訊
        //已選的課 的資訊

        int studentNumber = request.getStudentNumber();//欲選課之學生學號
//        String studentNae = request.getStudentName();//欲選課之學生姓名
        List<String> selectingCourseCodeList = request.getCourseCodeList();// request輸入 要選的課 [A,B,C]
        if (studentNumber < 0) {
            return new CourseResponse("學號輸入錯誤");
        }
//        if (!StringUtils.hasText(studentName)){
//            return new CourseResponse("學生姓名為空");
//        }
        if (CollectionUtils.isEmpty(selectingCourseCodeList)) {
            return new CourseResponse("輸入要選的課程List為空");
        }
        //欲選之課  的資訊
        List<Course> selectingCourseInfo = courseDao.findAllById(selectingCourseCodeList);//把該學生欲選之課 所有資訊撈出來
        if (CollectionUtils.isEmpty(selectingCourseInfo)) {
            return new CourseResponse("課程表內沒有你輸入的這些課程");
        }
        //欲選課之學生資訊
        Optional<Student> studentInfo = studentDao.findById(studentNumber);
        String selectedCourseCode = studentInfo.get().getCode();

        //如果此學生完全沒選過課的話 課程代碼為空 //直接對 selectingCourseCodeList 做檢查================================
        if (!StringUtils.hasText(selectedCourseCode)) {
            int selectingCreditSum = 0;
            for (int i = 0; i < selectingCourseInfo.size(); i++) {//0 ; 3  = 0 1 2
                if (selectingCourseInfo.get(i).getNumberOfStudent() >= 3) {
                    return new CourseResponse("修課人數已滿");
                }
                selectingCreditSum += selectingCourseInfo.get(i).getCredit();
                if (selectingCreditSum > 10) {
                    return new CourseResponse("credit over 10");
                }
                for (int j = i + 1; j < selectingCourseInfo.size(); j++) {
                    if (selectingCourseInfo.get(i).getName().equals(selectingCourseInfo.get(j).getName())) {
                        return new CourseResponse("選課清單內有相同名稱課程");
                    }
                    if (selectingCourseInfo.get(i).getDay().equals(selectingCourseInfo.get(j).getDay())) {
                        if (!(selectingCourseInfo.get(i).getEndTime() <= selectingCourseInfo.get(j).getStartTime()
                                || selectingCourseInfo.get(i).getStartTime() >= selectingCourseInfo.get(j).getEndTime())) {
                            return new CourseResponse("衝堂");
                        }
                    }
                }

            }
            String resultCourseCodeForEmpty = String.join(",", selectingCourseCodeList);
            Student resultForEmpty = new Student(studentNumber, resultCourseCodeForEmpty);
            studentDao.save(resultForEmpty);
            for (Course item : selectingCourseInfo) {
                item.setNumberOfStudent(item.getNumberOfStudent() + 1);
            }
            courseDao.saveAll(selectingCourseInfo);
        }
////---------------------------------------------------   如果此學生完全沒選過課的話 課程代碼為空 以上
        if (StringUtils.hasText(selectedCourseCode)) {

            String[] strList = selectedCourseCode.split(",");//把
            List<String> selectedCourseCodeList = new ArrayList<>(Arrays.asList(strList));
            //已選上課程之資訊
            List<Course> selectedCourseInfo = courseDao.findAllById(selectedCourseCodeList);//A、B、C課程的內容資訊
            int selectingCreditSum = 0;
            for (Course selecting : selectingCourseInfo) {//選 A.B.C
                if (selectedCourseCodeList.contains(selecting.getCode())) {
                    return new CourseResponse("選課代碼重複 這門課妳已經有了");
                }
                if (selecting.getNumberOfStudent() >= 3) {
                    return new CourseResponse("修課人數已滿");
                }

                for (Course selected : selectedCourseInfo) {//有C.D.E -- 1,1,1
                    //名稱 衝堂
                    if (selected.getName().equals(selecting.getName())) {
                        return new CourseResponse("課程名稱重複");
                    }
                    if (selecting.getDay().equals(selected.getDay())) {
                        if (selecting.getEndTime() <= selected.getStartTime() //欲選的開始時間大於已選的結束時間 或 欲選的結束時間小於已選的開始時間 才不衝堂
                                || selecting.getStartTime() >= selected.getEndTime()) {//850 <= 900    900 >= 850
                            continue;
                        }
                        return new CourseResponse("衝堂");
                    }
                }
                selectingCreditSum += selecting.getCredit();
                selectedCourseCodeList.add(selecting.getCode());
            }
            int selectedCreditSum = 0;
            for (Course item : selectedCourseInfo) {
                selectedCreditSum += item.getCredit();
            }
            if (selectedCreditSum + selectingCreditSum > 10) {
                return new CourseResponse("修習總學分數超過10");
            }
            String resultCourseCode = String.join(",", selectedCourseCodeList);

            Student result = new Student(studentNumber, resultCourseCode);
            studentDao.save(result);
            for (Course item : selectingCourseInfo) {
                item.setNumberOfStudent(item.getNumberOfStudent() + 1);
            }
            courseDao.saveAll(selectingCourseInfo);
            //比對    1.要選的課程名稱不能(與學生資料庫內的課程名稱)相同
            //比對    2.不能衝堂 (時間) (課程時間)VS(學生資料庫內時間)
            //比對    3.學分總數不能超過10(學生資料庫) // student DB 多一個學分欄位???????????????????
            //比對    4.未選上的才可以選 拿輸入的courseCode 跟 學生資料庫裏面的 exist by --> 直接find by 找的到就後續處理 找不到就
            //比對    5.每門課只能三位學生 //course再多一個欄位 for 人數?????????????????????????????????????????
        }
        return new CourseResponse("選課成功");

    }

    @Override
    public CourseResponse courseCancel(CourseRequest request) {
        //學生代碼 選課代碼近來
        //1.學生根本沒選課 學生選課代碼為空>>無法退選 因為沒選課
        /*2.學生沒選這門課  學生資料表內沒這個代碼>> 無法退選 因為沒選課
          3.學生有這門課 學生自料表內String拿出來 變成list 之後list Contain 慾退選之list
        * */
        int studentNumber = request.getStudentNumber();
        List<String> cancelingCodeList = request.getCourseCodeList();
        Optional<Student> studentInfo = studentDao.findById(studentNumber);
        if (!studentInfo.isPresent()) {
            return new CourseResponse("學生不存在");
        }
        String selectedCourseCode = studentInfo.get().getCode();
        if (!StringUtils.hasText(selectedCourseCode)) {
            return new CourseResponse("尚未選課 無法退選");
        }
//-------------------------------------------
        if (StringUtils.hasText(selectedCourseCode)) {
            String[] strList = selectedCourseCode.split(",");
            List<String> selectedCourseCodeList = new ArrayList<>(Arrays.asList(strList));

            List<String> canceledCodeList = new ArrayList<>();
            List<String> lastSelectedCodeList = new ArrayList<>();
            for (int i = 0; i < cancelingCodeList.size(); i++) {
                for (int j = 0; j < selectedCourseCodeList.size(); j++) {
                    if (cancelingCodeList.get(i).equals(selectedCourseCodeList.get(j))) {  //退A,B,C  有B,C,D
                        canceledCodeList.add(selectedCourseCodeList.get(j));              //存B C
                        selectedCourseCodeList.remove(j);
                    }
                }
            }
            //問題1 輸入要退的這些課 已選課清單都沒有
            //問題2 輸入要退的這些課 包含沒選過的課
            if (canceledCodeList.size() == 0) {
                return new CourseResponse("這些課你都沒選 無法退選");
            }

            String resultCourseCode = String.join(",", selectedCourseCodeList);
            Student result = new Student(studentNumber, resultCourseCode);
            studentDao.save(result);
            List<Course> canceledCourseInfo = courseDao.findAllById(canceledCodeList);
            for (Course item : canceledCourseInfo) {
                item.setNumberOfStudent(item.getNumberOfStudent() - 1);
            }
            courseDao.saveAll(canceledCourseInfo);
        }
        //學生表 學生課程代碼減少
        //課程表 選修人數減少
        return new CourseResponse("退選成功");
    }

    @Override
    public CourseResponse studentCourseQuery(CourseRequest request) {
        //輸入學生學號 撈出 課程代碼 ,依學號查詢，顯示學號、姓名、課程代碼、課程名稱、上課星期幾、上課開始時間、上課結束時間、學分
        //課程代碼去撈出課程 然後把資訊都秀出來
        int studentNumber = request.getStudentNumber();

        List<StudentResponse> result = studentDao.searchByStudentNumber(studentNumber);
        return new CourseResponse(result);
    }
}
/*
*    //request帶進來的資訊
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

*/