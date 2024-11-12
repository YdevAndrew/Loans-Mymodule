package org.jala.university.application.service;

import java.util.List;

import org.jala.university.application.dto.LoanEntityDto;
import org.jala.university.domain.entity.LoanEntity;

public interface LoanEntityService {

    LoanEntityDto findById(Integer id);
    List<LoanEntityDto> findAll();
    LoanEntity findEntityById(Integer id);
    LoanEntityDto save(LoanEntityDto entityDto);
    void deleteById(Integer id);
    void delete(LoanEntityDto entityDto);
    LoanEntityDto update(LoanEntityDto entityDto);
    List<LoanEntityDto> findLoansByAccountId();
    boolean payInstallmentManually(LoanEntityDto dto);
    long getPaidInstallments(LoanEntityDto dto);
}
