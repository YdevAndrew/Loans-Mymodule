package org.jala.university.infrastructure.persistance;

import org.jala.university.domain.repository.LoanEntityRepository;
import org.jala.university.infrastructure.persistance.database.Connection;
import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManager;

@Component
public class RepositoryFactory {

    private EntityManager entityManager = Connection.getConnection();

    public LoanEntityRepository createLoanEntityRepository() {
        return new LoanEntityRepositoryImpl(entityManager);
    }
}
