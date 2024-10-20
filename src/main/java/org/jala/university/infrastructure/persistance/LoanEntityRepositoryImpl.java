package org.jala.university.infrastructure.persistance;

import java.util.UUID;

import org.jala.university.commons.infrastructure.persistance.CrudRepository;
import org.jala.university.domain.entity.LoanEntity;
import org.jala.university.domain.repository.LoanEntityRepository;

import jakarta.persistence.EntityManager;

public class LoanEntityRepositoryImpl extends CrudRepository<LoanEntity, UUID> implements LoanEntityRepository {
    protected LoanEntityRepositoryImpl(EntityManager entityManager) {
        super(LoanEntity.class, entityManager);
    }
}
