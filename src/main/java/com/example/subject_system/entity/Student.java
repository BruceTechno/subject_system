package com.example.subject_system.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "student")
public class Student {

    @Id
    @Column(name = "number" )
    private int number;
    @Column(name ="name" )
    private String name;
    @Column(name = "code")
    private List<String> code ;


//==============================================================================================================

    public Student() {
    }

    public Student(int number, String name, List<String> code) {
        this.number = number;
        this.name = name;
        this.code = code;
    }

    public Student(int number, String name) {
        this.number = number;
        this.name = name;
    }
    //    public Student(int studentNumber, String studentName) {
//
//    }


//==============================================================================================================



    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getCode() {
        return code;
    }

    public void setCode(List<String> code) {
        this.code = code;
    }
}

