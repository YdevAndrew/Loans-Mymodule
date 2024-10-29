package org.jala.university.application.service;

import java.util.List;
import java.util.UUID;

import org.jala.university.application.dto.InstallmentEntityDto;

public interface InstallmentEntityService {
    List<InstallmentEntityDto> findByLoanId(UUID id);
}
