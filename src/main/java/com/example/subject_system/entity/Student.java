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
    private String code ;


//==============================================================================================================

    public Student() {
    }

    public Student(int number, String name, String code) {
        this.number = number;
        this.name = name;
        this.code = code;
    }

    public Student(int number, String code) {
        this.number = number;
        this.code = code;
    }

//    public Student(int studentNumber, String studentName) {
//
//    }


//==============================================================================================================

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

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

}

