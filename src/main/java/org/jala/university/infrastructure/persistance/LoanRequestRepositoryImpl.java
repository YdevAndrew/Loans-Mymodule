package org.jala.university.infrastructure.persistance;

import jakarta.persistence.EntityManager;
import org.jala.university.commons.infrastructure.persistance.CrudRepository;
import org.jala.university.domain.entity.LoanRequestEntity;
import org.jala.university.domain.repository.LoanRequestRepository;

import java.util.UUID;


public class LoanRequestRepositoryImpl extends CrudRepository<LoanRequestEntity, UUID> implements LoanRequestRepository {
    public LoanRequestRepositoryImpl(EntityManager entityManager) {
        super(LoanRequestEntity.class, entityManager);
    }
}