package org.jala.university.infrastructure.persistance;

import java.util.List;
import java.util.UUID;

import org.jala.university.commons.infrastructure.persistance.CrudRepository;
import org.jala.university.domain.entity.LoanEntity;
import org.jala.university.domain.repository.LoanEntityRepository;

import jakarta.persistence.EntityManager;

public class LoanEntityRepositoryImpl extends CrudRepository<LoanEntity, UUID> implements LoanEntityRepository {
    protected LoanEntityRepositoryImpl(EntityManager entityManager) {
        super(LoanEntity.class, entityManager);
    }

    @Override
    public List<LoanEntity> findLoansByAccountId(UUID accountId) {
        return entityManager.createQuery(
                "SELECT l FROM LoanEntity l WHERE l.account.id = :accountId",
                LoanEntity.class)
                .setParameter("accountId", accountId)
                .getResultList();
    }
}
