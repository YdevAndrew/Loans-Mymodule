package org.jala.university.application.service;

import org.jala.university.domain.entity.Account;
import org.jala.university.domain.entity.LoanEntity;

public interface LoanResultsService {

    Account sendAmountAccount(LoanEntity loanEntity);
    Account payInstallment(LoanEntity loanEntity);
    void verifyIfScheduled(LoanEntity loanEntity);
}
