package com.model;

import lombok.Data;

import java.util.Date;

@Data
public class Contract {
    private Long id;

    private Date date = new Date();

    private String pathContract;

    private String managerSignature;

    private String monitorSignature;

    private String studentSignature;
}
