package org.jala.university.application.service;

import java.util.List;

import org.jala.university.application.dto.InstallmentEntityDto;
import org.springframework.stereotype.Service;

/**
 * This interface defines the service contract for managing
 * {@link org.jala.university.domain.entity.InstallmentEntity} objects
 * related to a specific loan.
 */
@Service
public interface InstallmentEntityService {

    /**
     * Retrieves a list of {@link InstallmentEntityDto}
     * associated with a specific loan.
     *
     * @param id The unique identifier of the loan.
     * @return A list of {@link InstallmentEntityDto}
     *     for the given loan identifier.
     */
    List<InstallmentEntityDto> findByLoanId(Integer id);
}