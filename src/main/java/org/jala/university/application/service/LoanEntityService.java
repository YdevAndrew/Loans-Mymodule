package org.jala.university.application.service;

import java.util.List;
import java.util.UUID;

import org.jala.university.application.dto.LoanEntityDto;

public interface LoanEntityService {

    LoanEntityDto findById(UUID id);
    List<LoanEntityDto> findAll();
    LoanEntityDto save(LoanEntityDto entityDto);
    void deleteById(UUID id);
    void delete(LoanEntityDto entityDto);
    LoanEntityDto update(UUID id, LoanEntityDto entityDto);
}
