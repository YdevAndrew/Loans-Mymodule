package org.jala.university.application.service;

import java.math.BigDecimal;
import java.util.List;

import org.jala.university.domain.entity.Account;
import org.jala.university.domain.entity.InstallmentEntity;
import org.jala.university.domain.entity.LoanEntity;
import org.jala.university.domain.entity.ScheduledPaymentEntity;
import org.jala.university.domain.entity.enums.PaymentMethod;
import org.jala.university.domain.repository.AccountRepository;
import org.jala.university.domain.repository.LoanEntityRepository;
import org.jala.university.domain.repository.ScheduledPaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

/**
 * This class implements the {@link LoanResultsService} interface, providing concrete
 * implementations for handling loan results and related actions.
 */
@Service
public class LoanResultsServiceImpl implements LoanResultsService {

    @Autowired
    private LoanEntityRepository loanEntityRepository;

    @Autowired
    @Lazy
    private LoanEntityService loanEntityService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ScheduledPaymentRepository scheduledPaymentRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public Account sendAmountAccount(LoanEntity loanEntity) {
        Account account = accountRepository.findById(1 /*replace with method to get logged-in account */).orElse(null);
        Account savedAccount;

        if (account == null) {
            return null;
        }
        account.setBalance(account.getBalance().add(BigDecimal.valueOf(loanEntity.getAmountBorrowed())));
        savedAccount = accountRepository.save(account);
        return savedAccount;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Account payInstallment(LoanEntity loanEntity) {
        Account account = accountRepository.findById(1/* replace with method to get logged-in account */)
                .orElseThrow(() -> new IllegalStateException("Account not found"));

        InstallmentEntity firstUnpaidInstallment = loanEntity.getFirstUnpaidInstallment();
        if (firstUnpaidInstallment == null) {
            throw new IllegalStateException("No pending installment for payment.");
        }

        BigDecimal installmentAmount = BigDecimal.valueOf(firstUnpaidInstallment.getAmount());
        if (account.getBalance().compareTo(installmentAmount) < 0) {
            return null;
        }

        account.setBalance(account.getBalance().subtract(BigDecimal.valueOf(
                loanEntity.getFirstUnpaidInstallment().getAmount()
        )));

        return accountRepository.save(account);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void verifyIfScheduled(LoanEntity loanEntity) {
        if (loanEntity.getPaymentMethod().getCode() == PaymentMethod.DEBIT_ACCOUNT.getCode()) {
            schedulePayment(loanEntity);
        }
    }

    /**
     * Schedules the payment for a loan entity.
     * The method is not yet fully implemented as the scheduled payment
     * has not been implemented in the external payments module.
     *
     * @param loanEntity The loan entity to schedule the payment for.
     * @return True if the payment is scheduled successfully, false otherwise.
     */
    @Transactional
    boolean schedulePayment(LoanEntity loanEntity) {
        if (/*method != null*/true) {
            //replace with the ID returned by the method
            Integer id = 1;
            loanEntity.setScheduledPayment(scheduledPaymentRepository.findById(id).orElse(null));
            return true;
        }
        return false;
    }

    /**
     * Verifies and updates paid installments for loans with scheduled payments.
     * This method is scheduled to run every 24 hours.
     */
    @Scheduled(fixedRate = 86400000)
    void verifyAndUpdatePaidInstallments() {
        List<LoanEntity> loansInReview = loanEntityRepository.findByStatusPaymentMethod(1, 1);

        for (LoanEntity loan : loansInReview) {
            processLoanWithRetry(loan, 3);
        }
    }

    /**
     * Processes a loan with retries to handle potential errors during payment updates.
     *
     * @param loan        The loan entity to process.
     * @param retryCount The number of retry attempts.
     */
    void processLoanWithRetry(LoanEntity loan, int retryCount) {
        for (int attempt = 1; attempt <= retryCount; attempt++) {
            try {
                updateLoanPaidInstallments(loan);
                return;
            } catch (Exception e) {
                System.out.println("Failed processing loan: " + loan.getId() + " - tried: " + attempt + "times");
                if (attempt == retryCount) {
                    System.out.println("Failed processing loan" + loan.getId() + " tried: " + retryCount
                            + " times. Stopping try.");
                }
            }
        }
    }

    /**
     * Updates the paid installments for a loan entity based on the scheduled payment information.
     *
     * @param loanEntity The loan entity to update.
     */
    @Transactional
    void updateLoanPaidInstallments(LoanEntity loanEntity) {
        ScheduledPaymentEntity scheduledPaymentEntity = loanEntity.getScheduledPayment();
        long payments = loanEntityRepository.countCompletedPaymentsForLoan(scheduledPaymentEntity);
        long paidInstallments = loanEntity.getNumberOfPaidInstallments();

        if (payments > paidInstallments) {
            long installmentsToMarkAsPaid = payments - paidInstallments;
            LoanEntity loan = loanEntityService.findEntityById(loanEntity.getId());
            loan.markInstallmentsAsPaid(installmentsToMarkAsPaid);
            loanEntityRepository.save(loan);
        }
    }
}

