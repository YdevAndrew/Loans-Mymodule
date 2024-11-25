package org.jala.university.domain.repository;

import java.util.List;

import org.jala.university.domain.entity.LoanEntity;
import org.jala.university.domain.entity.ScheduledPaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * This interface defines a repository for managing
 * {@link LoanEntity} objects.
 * It extends the {@link JpaRepository} interface, providing basic CRUD
 * operations and other database interactions for loan entities.
 */
@Repository
public interface LoanEntityRepository extends
        JpaRepository<LoanEntity, Integer> {

    /**
     * Retrieves a list of {@link LoanEntity} associated with a
     * specific account.
     *
     * @param accountId The unique identifier of the account.
     * @return A list of {@link LoanEntity} for the
     *     given account identifier.
     */
    @Query("SELECT l FROM LoanEntity l WHERE l.account.id = :accountId")
    List<LoanEntity> findByAccountId(
            @Param("accountId") Integer accountId);

    /**
     * Counts the number of completed payments for a loan associated
     * with a scheduled payment.
     *
     * @param scheduledPayment The scheduled payment entity.
     * @return The number of completed payments for the loan.
     */
    @Query("SELECT COUNT(p) FROM PaymentHistoryEntity p "
            + "WHERE p.scheduledPayment = :scheduledPayment "
            + "AND p.status.statusName = 'COMPLETED'")
    long countCompletedPaymentsForLoan(
            @Param("scheduledPayment")
            ScheduledPaymentEntity scheduledPayment);

    /**
     * Retrieves a list of {@link LoanEntity} with a specific status
     * and payment method.
     *
     * @param status        The status code of the loan.
     * @param paymentMethod The payment method code of the loan.
     * @return A list of {@link LoanEntity} matching the given
     *     criteria.
     */
    @Query("SELECT l FROM LoanEntity l "
            + "WHERE l.status = :status "
            + "AND l.paymentMethod = :paymentMethod")
    List<LoanEntity> findByStatusPaymentMethod(
            @Param("status") Integer status,
            @Param("paymentMethod") Integer paymentMethod);

    /**
     * Retrieves a list of {@link LoanEntity} associated with a
     * specific account and status code.
     *
     * @param accountId The unique identifier of the account.
     * @param status    The status code of the loan.
     * @return A list of {@link LoanEntity} matching the given
     *     criteria.
     */
    @Query("SELECT l FROM LoanEntity l "
            + "WHERE l.account.id = :accountId "
            + "AND l.status = :status")
    List<LoanEntity> findByAccountAndStatusCode(
            @Param("accountId") Integer accountId,
            @Param("status") Integer status);

    /**
     * Calculates and returns the outstanding balance for a loan.
     *
     * @param loanId The unique identifier of the loan.
     * @return The outstanding balance for the loan.
     */
    @Query("SELECT COALESCE(SUM(i.amount), 0) "
            + "FROM InstallmentEntity i "
            + "WHERE i.loan.id = :loanId AND i.paid = false")
    Double getOutstandingBalance(@Param("loanId") Integer loanId);

    /**
     * Retrieves a {@link LoanEntity} by its ID, including its
     * installments.
     *
     * @param id The unique identifier of the loan.
     * @return The {@link LoanEntity} with the given ID, including
     *     its installments.
     */
    @Query("SELECT l FROM LoanEntity l "
            + "LEFT JOIN FETCH l.installments WHERE l.id = :id")
    LoanEntity findByIdWithInstallments(@Param("id") Integer id);
}