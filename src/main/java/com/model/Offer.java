package com.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
public class Offer {

    private Long id;

    private Monitor creator;

    private String department;

    private String title;

    private String description;

    private Date created = new Date();

    private String address;

    private double salary;

    private Boolean valid;

    public String creatorEmail() {
        if (creator == null)
            return null;
        return creator.getEmail();
    }
}
