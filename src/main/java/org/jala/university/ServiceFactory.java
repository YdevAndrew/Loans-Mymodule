package org.jala.university;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.jala.university.application.mapper.LoanRequestFormMapper;
import org.jala.university.application.mapper.LoanRequestMapper;
import org.jala.university.application.service.LoansService;
import org.jala.university.application.service.LoansServiceImpl;
import org.jala.university.domain.repository.LoanRequestFormRepository;
import org.jala.university.domain.repository.LoanRequestRepository;
import org.jala.university.infrastructure.persistance.LoanRequestFormRepositoryImpl;
import org.jala.university.infrastructure.persistance.LoanRequestRepositoryImpl;
import org.jala.university.infrastructure.persistance.mocks.LoanRequestFormMock;


public class ServiceFactory {
    private static LoansService service;

    private ServiceFactory() {
    }

    public static LoansService loansService() {
        if (service != null) {
            return service;
        }
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        LoanRequestFormRepository loanRequestFormRepository = new LoanRequestFormRepositoryImpl(entityManager);
        //LoanRequestFormRepository loanRequestFormRepository = new LoanRequestFormMock();
        LoanRequestFormMapper loanRequestFormMapper = new LoanRequestFormMapper();
        LoanRequestRepository loanRequestRepository = new LoanRequestRepositoryImpl(entityManager);
        LoanRequestMapper loanRequestMapper = new LoanRequestMapper();
        service = new LoansServiceImpl(loanRequestFormMapper, loanRequestFormRepository,loanRequestMapper, loanRequestRepository);
        return service;
    }
}
