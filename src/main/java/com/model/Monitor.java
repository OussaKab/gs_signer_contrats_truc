package com.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Monitor extends User {

    private String department;

    private String address;

    private String city;

    private String postalCode;
}
