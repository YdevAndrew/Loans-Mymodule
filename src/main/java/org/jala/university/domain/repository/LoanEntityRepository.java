package org.jala.university.domain.repository;

import java.util.List;

import org.jala.university.domain.entity.LoanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanEntityRepository extends JpaRepository<LoanEntity, Integer> {

    // @Query("SELECT l FROM LoanEntity l WHERE l.account.id = :accountId")
    // List<LoanEntity> findByAccountId(@Param("accountId") Integer accountId);

    // @Query("SELECT COUNT(p) FROM PaymentHistoryEntity p WHERE p.scheduledPaymentId = :scheduledPaymentId AND p.status = 'COMPLETED'")
    // long countCompletedPaymentsForLoan(@Param("scheduledPaymentId") Integer scheduledPaymentId);

    // @Query("SELECT COUNT(p) FROM PaymentHistoryEntity p WHERE p.scheduledPaymentId = :scheduledPaymentId AND p.accountReceiver = 'BANCO' AND p.status = 'COMPLETED'")
    // long countCompletedPaymentsForLoanToAccount(@Param("scheduledPaymentId") Integer scheduledPaymentId);

    @Query("SELECT l FROM LoanEntity l WHERE l.status = :status AND l.paymentMethod = :paymentMethod")
    List<LoanEntity> findByStatusPaymentMethod(@Param("status") Integer status, @Param("paymentMethod") Integer paymentMethod);

    // @Query("SELECT l FROM LoanEntity l WHERE l.account.id = :accountId AND l.status = :status")
    // List<LoanEntity> findByAccountAndStatusCode(@Param("accountId") Integer accountId, @Param("status") Integer status);

    @Query("SELECT COALESCE(SUM(i.amount), 0) FROM InstallmentEntity i WHERE i.loan.id = :loanId AND i.paid = false")
    Double getOutstandingBalance(@Param("loanId") Integer loanId);
}
