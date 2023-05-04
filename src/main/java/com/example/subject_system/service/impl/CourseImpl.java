package com.example.subject_system.service.impl;

import com.example.subject_system.constants.RtnCode;
import com.example.subject_system.entity.Course;
import com.example.subject_system.entity.Student;
import com.example.subject_system.repository.CourseDao;
import com.example.subject_system.repository.StudentDao;
import com.example.subject_system.service.ifs.CourseService;
import com.example.subject_system.vo.CourseRequest;
import com.example.subject_system.vo.CourseResponse;
import com.example.subject_system.vo.StudentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;

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
            return new CourseResponse(RtnCode.CANNOT_EMPTY.getMessage());
        }
        if (courseStartTime <= 0 || courseEndTime <= 0 || courseCredit <= 0) {
            return new CourseResponse(RtnCode.DATA_ERROR.getMessage());
        }//上面兩個串一起
        if (courseDao.existsById(courseCode)) {
            return new CourseResponse(RtnCode.CANNOT_EMPTY.getMessage());//"course already exist"
        }
        Course resCourse = new Course(courseCode, courseName, courseDay, courseStartTime, courseEndTime, courseCredit);
        courseDao.save(resCourse);

        return new CourseResponse(resCourse, RtnCode.SUCCESSFUL.getMessage());
    }

    @Override
    public CourseResponse studentAdd(CourseRequest request) {
        int studentNumber = request.getStudentNumber();
        String studentName = request.getStudentName();
//     String studentCourseCode = request.getStudentCode()
//        List<String>  studentCourseCodeList = request.getStudentCode();
        if (studentNumber < 0) {
            return new CourseResponse(RtnCode.DATA_ERROR.getMessage());
        }
        if (!StringUtils.hasText(studentName)) {
            return new CourseResponse(RtnCode.CANNOT_EMPTY.getMessage());
        }

        if (studentDao.existsById(studentNumber)) {
            return new CourseResponse(RtnCode.DATA_ERROR.getMessage());//"student already exist"
        }
        Student result = new Student(studentNumber, studentName);
        studentDao.save(result);

        return new CourseResponse(result,RtnCode.SUCCESSFUL.getMessage());
    }

    @Override
    public CourseResponse courseSelect(CourseRequest request) {//List 選課代碼 帶進來
        int studentNumber = request.getStudentNumber();//欲選課之學生學號
        List<String> selectingCourseCodeList = request.getCourseCodeList();// request輸入 要選的課 [A,B,C]
        String studentName = request.getStudentName();
        if (studentNumber < 0 || CollectionUtils.isEmpty(selectingCourseCodeList) || !StringUtils.hasText(studentName) ) {
            return new CourseResponse(RtnCode.DATA_ERROR.getMessage());
        }
        //三個串一起 done

        //欲選之課  的資訊
        List<Course> selectingCourseInfo = courseDao.findAllById(selectingCourseCodeList);//把該學生欲選之課 所有資訊撈出來
        if (CollectionUtils.isEmpty(selectingCourseInfo)) {
            return new CourseResponse(RtnCode.DATA_ERROR.getMessage());//"課程表內沒有你輸入的這些課程"
        }
        //欲選課之學生資訊
        Optional<Student> studentInfo = studentDao.findById(studentNumber);
        if (!studentInfo.isPresent()){
            return new CourseResponse(RtnCode.DATA_ERROR.getMessage());//資料庫內沒這個學生
        }
        String selectedCourseCode = studentInfo.get().getCode();

        //如果此學生完全沒選過課的話 課程代碼為空 //直接對 selectingCourseCodeList 做檢查================================
        if (!StringUtils.hasText(selectedCourseCode)) {
            int selectingCreditSum = 0;
            for (int i = 0; i < selectingCourseInfo.size(); i++) {//用兩個迴圈檢查進來的多筆選課資料 0vs1 1vs2 2vs3...
                selectingCreditSum += selectingCourseInfo.get(i).getCredit();//把每一個欲選課的學分累加

                if (selectingCourseInfo.get(i).getNumberOfStudent() >= 3 || selectingCreditSum > 10 ) {//檢查每一節課的選課人數
                    return new CourseResponse(RtnCode.DATA_ERROR.getMessage());//"修課人數已滿"          //檢查學分數有無超過10
                }
                //上面兩個if寫在一起 done
                for (int j = i + 1; j < selectingCourseInfo.size(); j++) {
                    if (selectingCourseInfo.get(i).getName().equals(selectingCourseInfo.get(j).getName())) {//0vs1 1vs2 2vs3
                        return new CourseResponse(RtnCode.DATA_ERROR.getMessage());//"選課清單內有相同名稱課程"
                    }//比對要選的這一堆課程內名稱有無相同的
                    boolean checkCourseConflict = checkCourseConflict(selectingCourseInfo.get(i),selectingCourseInfo.get(j));
                    if (checkCourseConflict) {
                        return new CourseResponse(RtnCode.DATA_ERROR.getMessage());//衝堂
                    }
                }
            }//"A,D"
            String resultCourseCodeForEmpty = String.join(",", selectingCourseCodeList);
            Student resultForEmpty = new Student(studentNumber, studentName,resultCourseCodeForEmpty);
            studentDao.save(resultForEmpty);
            for (Course item : selectingCourseInfo) {
                item.setNumberOfStudent(item.getNumberOfStudent() + 1);
            }
            courseDao.saveAll(selectingCourseInfo);
        }
////---------------------------------------------------   如果此學生完全沒選過課的話 課程代碼為空 以上
        if (StringUtils.hasText(selectedCourseCode)) {
//selectedCourseCode  A,D>>"A,D"
            String[] strList = selectedCourseCode.split(",");//把學生代碼string轉 陣列  //strList[A,D]
            List<String> selectedCourseCodeList = new ArrayList<>(Arrays.asList(strList));
            //已選上課程之資訊
            List<Course> selectedCourseInfo = courseDao.findAllById(selectedCourseCodeList);//A、B、C課程的內容資訊
                                                                         //試試看直接帶137 strList
            //to do
            int selectingCreditSum = 0;
            int selectedCreditSum = 0;
            for (Course selecting : selectingCourseInfo) {//選 A.B.C  //B
                if (selectedCourseCodeList.contains(selecting.getCode())) {
                    return new CourseResponse(RtnCode.DATA_ERROR.getMessage());//"選課代碼重複 這門課妳已經有了"
                }
                if (selecting.getNumberOfStudent() >= 3) {
                    return new CourseResponse(RtnCode.DATA_ERROR.getMessage());//"修課人數已滿"
                }

                for (Course selected : selectedCourseInfo) {//有C.D.E -- 1,1,1
                    //名稱 衝堂
                    if (selected.getName().equals(selecting.getName())) {
                        return new CourseResponse(RtnCode.DATA_ERROR.getMessage());//"課程名稱重複"
                    }
                    boolean checkCourseConflict = checkCourseConflict(selecting,selected);
                    if (checkCourseConflict) {
                        return new CourseResponse(RtnCode.DATA_ERROR.getMessage());//衝堂
                    }
                    selectedCreditSum += selected.getCredit();
                }
                selecting.setNumberOfStudent(selecting.getNumberOfStudent() + 1);

                selectingCreditSum += selecting.getCredit();
                selectedCourseCodeList.add(selecting.getCode());


            }
//        下面selected學分加總拉進上面迴圈  168>>141 少一個foreach
//            for (Course item : selectedCourseInfo) {
//                selectedCreditSum += item.getCredit();
//            }
            if (selectedCreditSum + selectingCreditSum > 10) {
                return new CourseResponse(RtnCode.DATA_ERROR.getMessage());//"修習總學分數超過10"
            }
            String resultCourseCode = String.join(",", selectedCourseCodeList);

            Student result = new Student(studentNumber, resultCourseCode);
            studentDao.save(result);

            courseDao.saveAll(selectingCourseInfo);
            //比對    1.要選的課程名稱不能(與學生資料庫內的課程名稱)相同
            //比對    2.不能衝堂 (時間) (課程時間)VS(學生資料庫內時間)
            //比對    3.學分總數不能超過10(學生資料庫) // student DB 多一個學分欄位???????????????????
            //比對    4.未選上的才可以選 拿輸入的courseCode 跟 學生資料庫裏面的 exist by --> 直接find by 找的到就後續處理 找不到就
            //比對    5.每門課只能三位學生 //course再多一個欄位 for 人數?????????????????????????????????????????
        }
        return new CourseResponse(RtnCode.SUCCESSFUL.getMessage());

    }

    /*
     * course1.getEndTime() <= course2.getStartTime() || course1.getStartTime() >= course2.getEndTime()
     * 上述結果為true 表示不衝堂
     * 所以+!()反向 表false 配合 方法名稱 false代表 沒有衝堂
     */
    private boolean checkCourseConflict(Course course1, Course course2){
        if (course1.getDay().equals(course2.getDay())) {//如果星期一樣
            return !(course1.getEndTime() <= course2.getStartTime() //
                    || course1.getStartTime() >= course2.getEndTime());
        }
        return false;
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
            return new CourseResponse(RtnCode.DATA_ERROR.getMessage());//"學生不存在"
        }
        String selectedCourseCode = studentInfo.get().getCode();
        if (!StringUtils.hasText(selectedCourseCode)) {
            return new CourseResponse(RtnCode.DATA_ERROR.getMessage());//"尚未選課 無法退選"
        }
//-------------------------------------------
        if (StringUtils.hasText(selectedCourseCode)) {
            String[] strList = selectedCourseCode.split(",");//String to List

            List<String> selectedCourseCodeList = new ArrayList<>(Arrays.asList(strList));//String to List

            List<String> canceledCodeList = new ArrayList<>();
            for (int i = 0; i < cancelingCodeList.size(); i++) {
                for (int j = 0; j < selectedCourseCodeList.size(); j++) {
                    if (cancelingCodeList.get(i).equals(selectedCourseCodeList.get(j))) { //退A,B,C 有B,C,D >兩個list內一樣的刪
                        canceledCodeList.add(selectedCourseCodeList.get(j));              //存B C
                        selectedCourseCodeList.remove(j);
                    }
                }
            }
            //問題1 輸入要退的這些課 已選課清單都沒有
            //問題2 輸入要退的這些課 包含沒選過的課
            if (canceledCodeList.size() == 0) {//要退選的課程list 跟 已選課程的list 沒有相同的內容 //
                return new CourseResponse(RtnCode.DATA_ERROR.getMessage());//"這些課你都沒選 無法退選"
            }

            String resultCourseCode = String.join(",", selectedCourseCodeList);//List to String
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
        return new CourseResponse(RtnCode.SUCCESSFUL.getMessage());
    }

    @Override
    public CourseResponse studentCourseQuery(CourseRequest request) {
        //輸入學生學號 撈出 課程代碼 ,依學號查詢，顯示學號、姓名、課程代碼、課程名稱、上課星期幾、上課開始時間、上課結束時間、學分
        //課程代碼去撈出課程 然後把資訊都秀出來
        int studentNumber = request.getStudentNumber();
        if (studentNumber < 0){
            return new CourseResponse(RtnCode.DATA_ERROR.getMessage());
        }
        List<StudentResponse> result = studentDao.searchByStudentNumber(studentNumber);
        if (CollectionUtils.isEmpty(result)){
            return new CourseResponse(RtnCode.NOT_FOUND.getMessage());
        }
        return new CourseResponse(result);
    }
    public CourseResponse getCourseInfoByCode (String code){
    if (!StringUtils.hasText(code)){
        return new CourseResponse(RtnCode.CANNOT_EMPTY.getMessage());
    }
        Optional<Course> courseInfo = courseDao.findById(code);
    if (!courseInfo.isPresent()){
        return new CourseResponse(RtnCode.NOT_FOUND.getMessage());
    }
        return new CourseResponse(courseInfo.get(),RtnCode.SUCCESSFUL.getMessage());
    }

    @Override
    public CourseResponse getCourseInfoByCourseName(CourseRequest request) {
        String courseName = request.getName();
        if (!StringUtils.hasText(courseName)){
            return new CourseResponse(RtnCode.CANNOT_EMPTY.getMessage());
        }
        List<Course> courseInfo = courseDao.findByName(courseName);
        if (CollectionUtils.isEmpty(courseInfo)){
            return new CourseResponse(RtnCode.CANNOT_EMPTY.getMessage());
        }

        return new CourseResponse(courseInfo,RtnCode.SUCCESSFUL.getMessage());
    }

    @Override
    public CourseResponse studentDelete(CourseRequest request) {
//1.如果學生沒有選課 >> 直接刪
//2.如果有選課     >> 進到課程DB 把選課人數調整 之後 再刪掉
        int studentNumber = request.getStudentNumber();
        if (studentNumber < 0){
            return new CourseResponse(RtnCode.DATA_ERROR.getMessage());
        }
        Optional<Student> studentInfo = studentDao.findById(studentNumber);
        if (!studentInfo.isPresent()){
            return new CourseResponse(RtnCode.DATA_ERROR.getMessage());
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

        return new CourseResponse(RtnCode.SUCCESSFUL.getMessage());
    }

    @Override
    public CourseResponse courseDelete(CourseRequest request) {
        String ingCode = request.getCode();
        if (!StringUtils.hasText(ingCode)){
            return new CourseResponse(RtnCode.DATA_ERROR.getMessage());
        }
        Optional<Course> ingCourseInfo = courseDao.findById(ingCode);
        if (!ingCourseInfo.isPresent()){
            return new CourseResponse(RtnCode.NOT_FOUND.getMessage());
        }
        if (ingCourseInfo.get().getNumberOfStudent() == 0){
            courseDao.deleteById(ingCode);
        }
        if (ingCourseInfo.get().getNumberOfStudent() > 0 ){
            return new CourseResponse(RtnCode.DATA_ERROR.getMessage());//還有人選課無法退選
        }
        return new CourseResponse(RtnCode.SUCCESSFUL.getMessage());
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