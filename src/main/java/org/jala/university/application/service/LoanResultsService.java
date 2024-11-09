package org.jala.university.application.service;

import org.jala.university.application.dto.PaymentHistoryDTO;
import org.jala.university.domain.entity.LoanEntity;

public interface LoanResultsService {

    PaymentHistoryDTO sendAmountAccount(LoanEntity loanEntity);
    PaymentHistoryDTO payInstallment(LoanEntity loanEntity);
    void verifyIfScheduled(LoanEntity loanEntity);
}
