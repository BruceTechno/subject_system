package com.example.subject_system;

import com.example.subject_system.constants.RtnCode;
import com.example.subject_system.entity.Course;
import com.example.subject_system.service.ifs.CourseService;
import com.example.subject_system.vo.CourseRequest;
import com.example.subject_system.vo.CourseResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class JavaDemoPracticeApplicationTests {
    @Autowired
    private CourseService courseService;

    @Test
    public void courseAddTest() {
        CourseRequest codeIsNull = new CourseRequest("", "電子學", "星期二", 900, 950, 1, 0);
        CourseResponse res = courseService.courseAdd(codeIsNull);
        Assert.isTrue(res.getMessage().equalsIgnoreCase(RtnCode.CANNOT_EMPTY.getMessage()), "錯誤");

        CourseRequest nameNull = new CourseRequest("I", "", "星期二", 900, 950, 1, 0);
        res = courseService.courseAdd(nameNull);
        Assert.isTrue(res.getMessage().equalsIgnoreCase(RtnCode.CANNOT_EMPTY.getMessage()), "錯誤");

        CourseRequest dayNull = new CourseRequest("I", "電子學", "", 900, 950, 1, 0);
        res = courseService.courseAdd(dayNull);
        Assert.isTrue(res.getMessage().equalsIgnoreCase(RtnCode.CANNOT_EMPTY.getMessage()), "錯誤");

        CourseRequest startTimeError = new CourseRequest("I", "電子學", "星期二", -1, 950, 1, 0);
        res = courseService.courseAdd(startTimeError);
        Assert.isTrue(res.getMessage().equalsIgnoreCase(RtnCode.CANNOT_EMPTY.getMessage()), "錯誤");

        CourseRequest endTimeError = new CourseRequest("I", "電子學", "星期二", 900, -1, 1, 0);
        res = courseService.courseAdd(endTimeError);
        Assert.isTrue(res.getMessage().equalsIgnoreCase(RtnCode.CANNOT_EMPTY.getMessage()), "錯誤");

        CourseRequest creditError = new CourseRequest("I", "電子學", "星期二", 900, 950, -1, 0);
        res = courseService.courseAdd(creditError);
        Assert.isTrue(res.getMessage().equalsIgnoreCase(RtnCode.CANNOT_EMPTY.getMessage()), "錯誤");
    //exist還沒寫
    }
    @Test
    public void studentAddTest(){
        CourseRequest studentNumberError = new CourseRequest(-1,"陳聖和");
        CourseResponse res = courseService.studentAdd(studentNumberError);
        Assert.isTrue(res.getMessage().equalsIgnoreCase(RtnCode.CANNOT_EMPTY.getMessage()), "錯誤");

        CourseRequest studentNameError = new CourseRequest(10001,"");
          res = courseService.studentAdd(studentNameError);
        Assert.isTrue(res.getMessage().equalsIgnoreCase(RtnCode.CANNOT_EMPTY.getMessage()), "錯誤");

        //exist還沒
    }

    @Test
    public void courseSelectTest() {
        List<String> list = new ArrayList<>();
//		list.add("A");
        list.add("K");
//		list.add("C");
        CourseRequest courseRequest = new CourseRequest(10002, "陳聖和2", list);
        courseService.courseSelect(courseRequest);
    }

    @Test
    public void courseCancelTest() {
        List<String> list = new ArrayList<>();
        list.add("A");
        list.add("D");
//		list.add("C");
        CourseRequest courseRequest = new CourseRequest(10003, "陳聖和3", list);
        courseService.courseCancel(courseRequest);
    }

}
