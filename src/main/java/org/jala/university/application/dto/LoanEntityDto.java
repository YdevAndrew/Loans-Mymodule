package org.jala.university.application.dto;

import java.time.LocalDate;
import java.util.List;
import org.jala.university.domain.entity.Account;
import org.jala.university.domain.entity.FormEntity;
import org.jala.university.domain.entity.InstallmentEntity;
import org.jala.university.domain.entity.enums.PaymentMethod;
import org.jala.university.domain.entity.enums.Status;

import lombok.Builder;
import lombok.Value;

/**
 * This class represents a Data Transfer Object (DTO) for a Loan Entity.
 * It contains detailed information about a loan, including the amount borrowed,
 * interest, installments, payment method, and associated entities like form and account.
 */
@Builder
@Value
public class LoanEntityDto {

    /**
     * The unique identifier of the loan entity.
     */
    Integer id;

    /**
     * The amount of money borrowed in the loan.
     */
    Double amountBorrowed;

    /**
     * The total interest accrued on the loan.
     */
    Double totalInterest;

    /**
     * The number of installments for repaying the loan.
     */
    Integer numberOfInstallments;

    /**
     * The value of each individual installment.
     */
    Double valueOfInstallments;

    /**
     * The total amount payable for the loan, including principal and interest.
     */
    Double totalPayable;

    /**
     * The method of payment chosen for the loan.
     */
    PaymentMethod paymentMethod;

    /**
     * The current status of the loan (e.g., Pending, Approved, Rejected).
     */
    Status status;

    /**
     * The date when the loan was issued.
     */
    LocalDate issueDate;

    /**
     * The date when the loan is due for final repayment.
     */
    LocalDate loanDueDate;

    /**
     * The form entity associated with this loan application.
     */
    FormEntity form;

    /**
     * A list of installment entities associated with this loan.
     */
    List<InstallmentEntity> installments;

    /**
     * The account entity associated with this loan.
     */
    Account account;
}