package com.services;

package com.gestionnaire_de_stage.service;

import com.gestionnaire_de_stage.enums.Status;
import com.gestionnaire_de_stage.model.Contract;
import com.gestionnaire_de_stage.model.Curriculum;
import com.gestionnaire_de_stage.model.Offer;
import com.gestionnaire_de_stage.model.OfferApplication;
import com.gestionnaire_de_stage.repository.ContractRepository;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ContractServiceTest {

    @InjectMocks
    private ContractService contractService;

    @Mock
    private ContractRepository contractRepository;

    @InjectMocks
    private com.gestionnaire_de_stage.service.FileService fileService;

    @Test
    public void testGetAllByManagerSignatureNull() {
        when(contractRepository.findAllByManagerSignatureNull()).thenReturn(Collections.emptyList());

        List<Contract> allByManagerSignatureNull = contractService.getAllByManagerSignatureNull();

        assertThat(allByManagerSignatureNull).isEmpty();
    }

    @Test
    public void testGetAllByManagerSignatureNullNotEmpty() {
        List<Contract> contracts = List.of(new Contract());
        when(contractRepository.findAllByManagerSignatureNull()).thenReturn(contracts);

        List<Contract> allByManagerSignatureNull = contractService.getAllByManagerSignatureNull();

        assertThat(allByManagerSignatureNull).isNotEmpty();
    }

    @Test
    public void testCreateContract() throws Exception {
        Contract contract = new Contract();
        contract.setId(1L);

        OfferApplication offerApplication = getDummyOfferApplication();
        offerApplication.setId(null);

        when(contractRepository.save(any())).thenReturn(contract);
        when(fileService.createContract(any())).thenReturn(contract);

        Contract savedContract = contractService.createContract(offerApplication);

        assertThat(savedContract).isNotNull();
        assertThat(savedContract.getId())
                .isNotNull()
                .isGreaterThanOrEqualTo(1L);
    }


    private OfferApplication getDummyOfferApplication() {
        OfferApplication offerApplication = new OfferApplication();
        offerApplication.setId(1L);
        offerApplication.setOffer(new Offer());
        offerApplication.setCurriculum(new Curriculum());
        offerApplication.setStatus(Status.STAGE_TROUVE);
        offerApplication.setEntrevueDate(Date.from(LocalDate.now().minusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        return offerApplication;
    }
}
