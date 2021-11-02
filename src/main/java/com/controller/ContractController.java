package com.controller;

import com.dto.ResponseMessage;
import com.gestionnaire_de_stage.model.Contract;
import com.gestionnaire_de_stage.model.OfferApplication;
import com.gestionnaire_de_stage.service.ContractService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("/contracts")
public class ContractController {

    private final ContractService contractService;

    public ContractController(ContractService contractService) {
        this.contractService = contractService;
    }

    @GetMapping("/ready_to_sign")
    public List<Contract> readyToSign() {
        return contractService.getAllByManagerSignatureNull();
    }


    @PostMapping
    public ResponseEntity<?> sign(@RequestBody OfferApplication offerApplication) {
        Contract contract;
        try {
            contract = contractService.createContract(offerApplication);
            return ResponseEntity.ok(contract);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseMessage(e.getMessage()));
        }
    }
}

