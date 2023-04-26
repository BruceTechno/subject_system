package com.example.subject_system.repository;

import com.example.subject_system.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseDao extends JpaRepository<Course,String> {


}
