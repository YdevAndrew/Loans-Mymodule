package org.jala.university.application.service;

import java.util.List;

import org.jala.university.application.dto.InstallmentEntityDto;
import org.jala.university.application.mapper.InstallmentEntityMapper;
import org.jala.university.domain.entity.InstallmentEntity;
import org.jala.university.domain.repository.InstallmentEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This class implements the {@link InstallmentEntityService} interface,
 * providing a concrete implementation for retrieving installments
 * associated with a loan.
 */
@Service
public class InstallmentEntityServiceImpl
        implements InstallmentEntityService {

    /**
     * The repository for accessing installment entities.
     */
    @Autowired
    private InstallmentEntityRepository installmentEntityRepository;

    /**
     * The mapper for converting between installment entities
     * and DTOs.
     */
    private final InstallmentEntityMapper mapper;

    /**
     * Constructor for InstallmentEntityServiceImpl.
     *
     * @param mapper The mapper instance to be used.
     */
    public InstallmentEntityServiceImpl(
            final InstallmentEntityMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<InstallmentEntityDto> findByLoanId(final Integer loanId) {
        List<InstallmentEntity> installments =
                installmentEntityRepository.findByLoanId(loanId);
        return installments.stream()
                .map(mapper::mapTo)
                .toList();
    }
}