package org.jala.university;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.jala.university.application.mapper.LoanRequestFormMapper;
import org.jala.university.application.service.LoansService;
import org.jala.university.application.service.LoansServiceImpl;
import org.jala.university.domain.repository.LoanRequestFormRepository;
import org.jala.university.infrastructure.persistance.LoanRequestFormMock;
import org.jala.university.infrastructure.persistance.LoanRequestFormRepositoryImpl;

public class ServiceFactory {
    private static LoansService service;

    private ServiceFactory() {
    }

    public static LoansService loansService() {
        if (service != null) {
            return service;
        }
        // EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        // EntityManager entityManager = entityManagerFactory.createEntityManager();
        // LoanRequestFormRepository loanRequestFormRepository = new LoanRequestFormRepositoryImpl(entityManager);
        LoanRequestFormRepository loanRequestFormRepository = new LoanRequestFormMock();
        LoanRequestFormMapper accountMapper = new LoanRequestFormMapper();
        service = new LoansServiceImpl(accountMapper, loanRequestFormRepository);
        return service;
    }
}
