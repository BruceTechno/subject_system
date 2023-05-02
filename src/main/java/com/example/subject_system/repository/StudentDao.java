package com.example.subject_system.repository;

import com.example.subject_system.entity.Student;
import com.example.subject_system.vo.StudentResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface StudentDao extends JpaRepository<Student,Integer> {
    @Transactional
    @Modifying
    @Query("select new com.example.subject_system.vo.StudentResponse(s.number,s.name,c.code,c.name,c.day,c.startTime,c.endTime,c.credit)" +
            " from Course c join Student s on s.code" +
            " Like concat('%',c.code,'%')" +
            " where s.number = :newNumber ")
    public List<StudentResponse> searchByStudentNumber(@Param("newNumber")int newNumber);
}
