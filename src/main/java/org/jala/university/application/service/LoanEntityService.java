package org.jala.university.application.service;

import java.util.List;

import org.jala.university.application.dto.FormEntityDto;
import org.jala.university.application.dto.LoanEntityDto;
import org.jala.university.domain.entity.LoanEntity;

public interface LoanEntityService {

    LoanEntityDto findById(Integer id);
    List<LoanEntityDto> findAll();
    LoanEntityDto save(LoanEntityDto entityDto);
    void deleteById(Integer id);
    void delete(LoanEntityDto entityDto);
    LoanEntityDto update(Integer id, LoanEntityDto entityDto);
    List<LoanEntityDto> findLoansByAccountId();
    LoanEntity associateForm(LoanEntityDto loanDto, FormEntityDto formdto);
    void markInstallmentAsPaid(LoanEntityDto dto);
    long getPaidInstallments(LoanEntityDto dto);
}
