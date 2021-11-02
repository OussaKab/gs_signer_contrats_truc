package com.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class Curriculum implements Serializable {
    private Long id;

    private String name;

    private String type;

    private byte[] data;

    private Student student;

    private Boolean isValid;

    public Curriculum(String name, String type, byte[] data, Student student) {
        this.name = name;
        this.type = type;
        this.data = data;
        this.student = student;
        this.isValid = null;
    }
}
