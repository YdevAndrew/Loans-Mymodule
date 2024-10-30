package org.jala.university.application.service;

import java.util.List;
import java.util.UUID;

import org.jala.university.application.dto.FormEntityDto;
import org.jala.university.application.dto.LoanEntityDto;
import org.jala.university.domain.entity.LoanEntity;

public interface LoanEntityService {

    LoanEntityDto findById(UUID id);
    List<LoanEntityDto> findAll();
    LoanEntityDto save(LoanEntityDto entityDto);
    void deleteById(UUID id);
    void delete(LoanEntityDto entityDto);
    LoanEntityDto update(UUID id, LoanEntityDto entityDto);
    List<LoanEntityDto> findLoansByAccountId(UUID id);
    LoanEntity associateForm(LoanEntityDto loanDto, FormEntityDto formdto);
    void markInstallmentAsPaid(LoanEntityDto dto);
    long getPaidInstallments(LoanEntityDto dto);
}
