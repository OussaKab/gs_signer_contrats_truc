package com.model;

import lombok.Data;

import java.util.List;

@Data
public class Supervisor extends User {

    private String matricule;

    private String department;

    private List<Student> studentList;
}
