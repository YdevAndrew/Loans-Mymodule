package org.jala.university.infrastructure.persistance;

import java.util.List;
import java.util.UUID;

import org.jala.university.commons.infrastructure.persistance.CrudRepository;
import org.jala.university.domain.entity.InstallmentEntity;
import org.jala.university.domain.repository.InstallmentEntityRepository;

import jakarta.persistence.EntityManager;

public class InstallmentEntityRepositoryImpl extends CrudRepository<InstallmentEntity, UUID>
        implements InstallmentEntityRepository {

    protected InstallmentEntityRepositoryImpl(EntityManager entityManager) {
        super(InstallmentEntity.class, entityManager);
    }

    @Override
    public List<InstallmentEntity> findByLoanId(UUID loanId) {
        return entityManager.createQuery(
                "SELECT i FROM InstallmentEntity i WHERE i.loan.id = :loanId",
                InstallmentEntity.class)
                .setParameter("loanId", loanId)
                .getResultList();
    }
}
