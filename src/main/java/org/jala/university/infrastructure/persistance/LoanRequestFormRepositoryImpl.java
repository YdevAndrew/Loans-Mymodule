package org.jala.university.infrastructure.persistance;

import jakarta.persistence.EntityManager;
import org.jala.university.commons.infrastructure.persistance.CrudRepository;
import org.jala.university.domain.entity.LoanRequestFormEntity;
import org.jala.university.domain.repository.LoanRequestFormRepository;

import java.util.UUID;

public class LoanRequestFormRepositoryImpl extends CrudRepository<LoanRequestFormEntity, UUID> implements LoanRequestFormRepository {
    public LoanRequestFormRepositoryImpl(EntityManager entityManager) {
        super(LoanRequestFormEntity.class, entityManager);
    }
}
