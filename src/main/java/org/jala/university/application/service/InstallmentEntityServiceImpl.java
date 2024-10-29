package org.jala.university.application.service;

import java.util.List;
import java.util.UUID;

import org.jala.university.application.dto.InstallmentEntityDto;
import org.jala.university.application.mapper.InstallmentEntityMapper;
import org.jala.university.domain.entity.InstallmentEntity;
import org.jala.university.domain.repository.InstallmentEntityRepository;
import org.jala.university.infrastructure.persistance.RepositoryFactory;

public class InstallmentEntityServiceImpl {

    private final InstallmentEntityRepository repository;
    private final InstallmentEntityMapper mapper;

    public InstallmentEntityServiceImpl(RepositoryFactory factory, InstallmentEntityMapper mapper) {
        this.repository = factory.createInstallmentEntityRepository();
        this.mapper = mapper;
    }

    public List<InstallmentEntityDto> findByLoanId(UUID loanId) {
        List<InstallmentEntity> installments = repository.findByLoanId(loanId);
        return installments.stream()
                .map(mapper::mapTo) // Converte cada entidade para DTO
                .toList();
    }
}
