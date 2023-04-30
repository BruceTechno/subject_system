package com.example.subject_system;

import com.example.subject_system.service.ifs.CourseService;
import com.example.subject_system.vo.CourseRequest;
import com.example.subject_system.vo.CourseResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class JavaDemoPracticeApplicationTests {
	@Autowired
	private CourseService courseService;
	@Test
	public void courseSelectTest() {
		List<String> list = new ArrayList<>();
		list.add("A");
		list.add("G");
//		list.add("C");
		CourseRequest courseRequest = new CourseRequest(10003,"陳聖和3",list);
		courseService.courseSelect(courseRequest);
	}

}
