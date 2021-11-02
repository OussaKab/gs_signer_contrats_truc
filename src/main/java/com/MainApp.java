package com;

import com.enums.Status;
import com.model.*;
import com.services.FileService;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class MainApp {
    public static void main(String[] args) throws IOException, InvalidFormatException {
        FileService fileService = new FileService();
        Monitor monitor = new Monitor();
        monitor.setAddress("1000, avenue Bouchard");
        monitor.setCity("Montreal");
        monitor.setPostalCode("H3G2J5");
        monitor.setEmail("monitor@email.com");
        monitor.setFirstName("John");
        monitor.setLastName("Doe");
        monitor.setPassword("UnMotDePasse12345");
        monitor.setPhone("514-123-4567");

        Offer offer = new Offer();
        offer.setAddress("1000, avenue Bouchard");
        offer.setCreated(new Date());
        offer.setDepartment("informatique");
        offer.setId(null);
        offer.setTitle("Développeur FrontEnd");
        offer.setSalary(18);
        offer.setDescription("Une Description");
        offer.setValid(true);
        offer.setCreator(monitor);

        byte[] bytes = Files.readAllBytes(Paths.get("src/main/resources/templates/contratTemplate.docx"));

        Student student = new Student();
        student.setAddress("1100, avenue Bouchard");
        student.setCity("Lachine");
        student.setPostalCode("H3G2J5");
        student.setDepartment("informatique");
        student.setEmail("student@email.com");
        student.setLastName("Does");
        student.setFirstName("John");
        student.setPassword("UnMotDePasse1234");
        student.setPhone("514-123-4567");
        student.setPrincipalCurriculum(null);

        Curriculum curriculum = new Curriculum("contratTemplate.docx", "application/vnd.com.documents4j.any-msword", bytes, student);
s
        OfferApplication offerApplication = new OfferApplication();
        offerApplication.setOffer(offer);
        offerApplication.setCurriculum(curriculum);
        offerApplication.setEntrevueDate(Date.from(LocalDate.now().minusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        offerApplication.setStatus(Status.STAGE_TROUVE);

        fileService.createContract(offerApplication);
    }
}
