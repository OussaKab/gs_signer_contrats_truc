package com.model;

import lombok.Data;

@Data
public class Student extends User {

    private String matricule;

    private String department;

    private String address;

    private String city;

    private String postalCode;

    private Curriculum principalCurriculum;
}
