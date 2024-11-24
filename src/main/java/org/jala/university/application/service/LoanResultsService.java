package org.jala.university.application.service;

import org.jala.university.domain.entity.Account;
import org.jala.university.domain.entity.LoanEntity;

/**
 * This interface defines the service contract for handling loan results
 * and related actions, such as sending loan amounts to accounts,
 * processing installment payments, and verifying scheduled tasks.
 */
public interface LoanResultsService {

    /**
     * Sends the loan amount to the associated account.
     *
     * @param loanEntity The loan entity for which to send the amount.
     * @return The updated account after the loan amount is sent.
     */
    Account sendAmountAccount(LoanEntity loanEntity);

    /**
     * Processes the payment of an installment for a loan.
     *
     * @param loanEntity The loan entity for which to pay the installment.
     * @return The updated account after the installment payment is processed.
     */
    Account payInstallment(LoanEntity loanEntity);

    /**
     * Verifies and schedules any necessary tasks related to the loan,
     * such as installment due date reminders or automatic payments.
     *
     * @param loanEntity The loan entity to verify and schedule tasks for.
     */
    void verifyIfScheduled(LoanEntity loanEntity);

}