package com.example.subject_system.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "class")
public class Course {

    @Id
    @Column(name = "code")
    private  String code;
    @Column(name = "name")
    private String name;
    @Column(name = "day")
    private String day;
    @Column(name = "start_time")
    private int startTime;
    @Column(name = "end_time")
    private int endTime;
    @Column(name = "credit")
    private int credit;
    @Column(name = "number_of_student")
    private int numberOfStudent ;

    //====================================================================================================

    public Course() {
    }

    public Course(String code, String name, String day, int startTime, int endTime, int credit) {
        this.code = code;
        this.name = name;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
        this.credit = credit;
    }
    //====================================================================================================

    public int getNumberOfStudent() {
        return numberOfStudent;
    }

    public void setNumberOfStudent(int numberOfStudent) {
        this.numberOfStudent = numberOfStudent;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }
}
