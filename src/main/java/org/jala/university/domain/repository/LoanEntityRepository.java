package org.jala.university.domain.repository;

import java.util.List;

import org.jala.university.domain.entity.LoanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@org.springframework.stereotype.Repository
public interface LoanEntityRepository extends JpaRepository<LoanEntity, Integer> {

    // @Query("SELECT l FROM LoanEntity l WHERE l.account.id = :accountId")
    // List<LoanEntity> findByAccountId(@Param("accountId") Integer accountId);

    // @Query("SELECT COUNT(p) FROM PaymentHistoryEntity p WHERE p.scheduledPaymentId = :scheduledPaymentId")
    // long countPaymentsForLoan(@Param("scheduledPaymentId") Integer scheduledPaymentId);

    // @Query("SELECT COUNT(p) FROM PaymentHistoryEntity p WHERE p.scheduledPaymentId = :scheduledPaymentId AND p.accountReceiver = 'BANCO'")
    // long countPaymentsForLoanToAccount(@Param("scheduledPaymentId") Integer scheduledPaymentId);

    @Query("SELECT l FROM LoanEntity l WHERE l.status = :status AND l.paymentMethod = :paymentMethod")
    List<LoanEntity> findLoansScheduled(@Param("status") Integer status, @Param("paymentMethod") Integer paymentMethod);
}
