package com.model;

import lombok.Data;

import java.io.Serializable;

@Data
public abstract class User implements Serializable {

    private Long id;

    private String lastName;

    private String firstName;

    private String email;

    private String phone;

    private String password;
}
