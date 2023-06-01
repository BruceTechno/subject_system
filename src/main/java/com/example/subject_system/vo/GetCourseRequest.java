package com.example.subject_system.vo;

public class GetCourseRequest {
    private String code;
    private String name;
//===

    public GetCourseRequest() {
    }
//===

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
}
