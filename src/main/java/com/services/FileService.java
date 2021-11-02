package com.gestionnaire_de_stage.service;

import com.gestionnaire_de_stage.model.Contract;
import com.gestionnaire_de_stage.model.Offer;
import com.gestionnaire_de_stage.model.OfferApplication;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import com.documents4j.api.DocumentType;
import com.documents4j.api.IConverter;
import com.documents4j.job.LocalConverter;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;


@Service
public class FileService {
    private final String TEMPLATES_DIR = "src/main/resources/templates";
    private final String INTERNSHIP_CONTRACT_FILENAME = "contratTemplate.docx";
    private final String OFFER_ADDRES_KEYWORD = "[offre_lieuStage]";
    private final String OFFER_SALARY_KEYWORD = "[offre_tauxHoraire]";
    private final String OFFER_DESCRIPTION_KEYWORD = "[offre_description]";
    private final String OFFER_START_DATE_KEYWORD = "Date de début: xx";
    private final String OFFER_END_DATE_KEYWORD = "Date de fin: xx";
    private final String OFFER_TOTAL_WEEKS_KEYWORD = "Nombre total de semaines : xx";
    private final String OFFER_WEEK_SCHEDULE_KEYWORD = "Horaire de travail: xx";
    private final String OFFER_TOTAL_HOURS_PER_WEEK_KEYWORD = "Nombre total d’heures par semaine : xxh";

    /**
     * Resolves a contract by a student id
     * @param matriculeEtudiant student id
     * @return the contract document in byte array form
     * @throws IOException if file cannot be read
     */
    public byte[] resolveContractStudent(String matriculeEtudiant) throws IOException {
        if (matriculeEtudiant == null || matriculeEtudiant.isBlank())
            throw new IllegalArgumentException("matriculeEtudiant ne doit pas être vide");

        Path path = Paths.get(TEMPLATES_DIR).resolve(matriculeEtudiant).resolve(INTERNSHIP_CONTRACT_FILENAME);

        if(!Files.exists(path))
            throw new IOException("Le fichier " + INTERNSHIP_CONTRACT_FILENAME + " n'existe pas");

        return Files.readAllBytes(path);
    }

    private void createFileIfNotExists(Path path) throws IOException {
        File file = path.getParent().toFile();

        Files.createDirectories(file.toPath());

        File fileToBeCreated = new File(file, path.getFileName().toString());

        if(!Files.exists(fileToBeCreated.toPath()))
            Files.createFile(fileToBeCreated.toPath());
    }

    /**
     * Create a contract document from a offerApplication and matriculeEtudiant
     * @param matriculeEtudiant matricule of the student
     * @param offerApplication offerApplication to be used to create the contract
     * @return the document filled with the offerApplication data
     * @throws IOException if file cannot be created
     * @throws IllegalArgumentException if matriculeEtudiant or offerApplication is null
     * @throws InvalidFormatException if the document is not a valid document
     */
    public Contract createContract(OfferApplication offerApplication) throws IOException, IllegalArgumentException, InvalidFormatException {
        String matriculeEtudiant = offerApplication.getMatricule();

        if (matriculeEtudiant == null || matriculeEtudiant.matches("//d{7}") || offerApplication == null)
            throw new IllegalArgumentException("idStudent et offerApplication ne doit pas être null");

        Path pathOutput = Paths.get(TEMPLATES_DIR).resolve(matriculeEtudiant).resolve(INTERNSHIP_CONTRACT_FILENAME);

        Path pathInput = Paths.get(TEMPLATES_DIR).resolve(INTERNSHIP_CONTRACT_FILENAME);

        createFileIfNotExists(pathOutput);

        XWPFDocument doc = new XWPFDocument(OPCPackage.open(pathInput.toFile()));

        Offer offer = offerApplication.getOffer();

        String[] offreLieuStage = {OFFER_ADDRES_KEYWORD, offer.getAddress()};
        String[] offre_taux_horaire = {OFFER_SALARY_KEYWORD, offer.getSalary() + "$/h"};
        String[] offre_description = {OFFER_DESCRIPTION_KEYWORD, offer.getDescription()};

        Stream.of(
                offreLieuStage,
                offre_taux_horaire,
                offre_description
        ).forEach(wordsToBeMatched -> replaceWordInDocumentByOther(doc, wordsToBeMatched[0], wordsToBeMatched[1]));

        doc.write(new FileOutputStream(pathOutput.toFile()));

        Contract contract = new Contract();
        contract.setPathContract(pathOutput.toString());

        return contract;
    }

    public byte[] convertWordToPdf(String pathWord) throws IOException, ExecutionException, InterruptedException {
        if(pathWord == null || pathWord.isBlank())
            throw new IOException("pathWord ne doit pas être vide");

        java.io.ByteArrayOutputStream bo = new ByteArrayOutputStream();

        File file = new File(Paths.get(pathWord).getParent().toFile(), Paths.get(pathWord).getFileName().toString());

        InputStream in = new BufferedInputStream(new FileInputStream(file));
        IConverter converter = LocalConverter.builder()
                .baseFolder(file.getParentFile())
                .workerPool(20, 25, 2, TimeUnit.SECONDS)
                .processTimeout(5, TimeUnit.SECONDS)
                .build();

        Future<Boolean> conversion = converter
                .convert(in).as(DocumentType.MS_WORD)
                .to(bo).as(DocumentType.PDF)
                .prioritizeWith(1000)
                .schedule();
        conversion.get();

        bo.writeTo(new FileOutputStream(pathWord.replace(".docx", ".pdf")));
        in.close();
        bo.close();

        return bo.toByteArray();
    }

    /**
     * Replace all occurrences of a word in a document by another word
     * @param doc document to be modified
     * @param word word to be replaced
     * @param other word to replace the original word
     */
    private void replaceWordInDocumentByOther(XWPFDocument doc, String word, String other) {
        if (doc == null || word == null || word.isBlank() || other == null || other.isBlank())
            throw new IllegalArgumentException("doc, word et other ne doivent pas être null");

        for (XWPFParagraph p : doc.getParagraphs()) {
            List<XWPFRun> runs = p.getRuns();
            if (runs != null) {
                for (XWPFRun r : runs) {
                    String text = r.getText(0);
                    if (text != null && text.contains(word))
                        r.setText(text.replace(word, other), 0);
                }
            }
        }
        for (XWPFTable tbl : doc.getTables()) {
            for (XWPFTableRow row : tbl.getRows()) {
                for (XWPFTableCell cell : row.getTableCells()) {
                    for (XWPFParagraph p : cell.getParagraphs()) {
                        for (XWPFRun r : p.getRuns()) {
                            String text = r.getText(0);
                            if (text != null && text.contains(word))
                                r.setText(text.replace(word, other), 0);
                        }
                    }
                }
            }
        }
    }
}
