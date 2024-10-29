package org.jala.university.domain.repository;

import java.util.List;
import java.util.UUID;

import org.jala.university.commons.domain.Repository;
import org.jala.university.domain.entity.InstallmentEntity;

@org.springframework.stereotype.Repository
public interface InstallmentEntityRepository extends Repository<InstallmentEntity, UUID> {
    List<InstallmentEntity> findByLoanId(UUID loanId);
}
