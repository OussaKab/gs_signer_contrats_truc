package com.services;

import com.model.Contract;
import com.model.OfferApplication;
import com.repository.ContractRepository;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

public class ContractService {
    private final ContractRepository contractRepository;
    private final FileService fileService;

    public ContractService(ContractRepository contractRepository, FileService fileService) {
        this.contractRepository = contractRepository;
        this.fileService = fileService;
    }

    public List<Contract> getAllByManagerSignatureNull() {
        return contractRepository.findAllByManagerSignatureNull();
    }

    public Contract createContract(OfferApplication offerApplication) throws IOException, InvalidFormatException {
        Contract contract = fileService.createContract(offerApplication);
        return contractRepository.save(contract);
    }
}
