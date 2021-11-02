package com.model;

import com.enums.Status;
import lombok.Data;

import javax.persistence.Transient;
import java.util.Date;

@Data
public class OfferApplication {
    private Long id;

    private Status status;

    private Offer offer;

    private Curriculum curriculum;

    private Date entrevueDate;


    @Transient
    public String getMatricule(){
        Student student = curriculum != null ? curriculum.getStudent() : null;

        return student != null ? student.getMatricule() : null;
    }
}

