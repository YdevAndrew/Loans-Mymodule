package org.jala.university.domain.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.jala.university.commons.domain.BaseEntity;
import org.jala.university.domain.entity.enums.PaymentMethod;
import org.jala.university.domain.entity.enums.Status;
import org.jala.university.utils.CalculationUtil;
import org.springframework.data.annotation.CreatedDate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This entity represents a loan taken by a user.
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "LOAN")
public class LoanEntity implements BaseEntity<Integer> {

    /**
     * The unique identifier of the loan entity.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * The amount of money borrowed in the loan.
     */
    @Column(name = "amount_borrowed")
    private Double amountBorrowed;

    /**
     * The total interest accrued on the loan.
     */
    @Column(name = "total_interest")
    private Double totalInterest;

    /**
     * The number of installments for repaying the loan.
     */
    @Column(name = "number_of_installments")
    private Integer numberOfInstallments;

    /**
     * The value of each individual installment.
     */
    @Column(name = "value_of_installments")
    private Double valueOfInstallments;

    /**
     * The total amount payable for the loan, including principal
     * and interest.
     */
    @Column(name = "total_payable")
    private Double totalPayable;

    /**
     * The payment method code for the loan.
     */
    @Column(name = "payment_method")
    private Integer paymentMethod;

    /**
     * The status code for the loan.
     */
    @Column(name = "status")
    private Integer status;

    /**
     * The date when the loan was issued.
     */
    @Column(name = "issue_date")
    @CreatedDate
    private LocalDate issueDate;

    /**
     * The date when the loan is due for final repayment.
     */
    @Column(name = "loan_due_date")
    private LocalDate loanDueDate;

    /**
     * The form entity associated with this loan application.
     */
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "form_id", nullable = true)
    private FormEntity form;

    /**
     * The scheduled payment entity associated with this loan,
     * if applicable.
     */
    @OneToOne
    @JoinColumn(name = "Scheduled_Payment_id", nullable = true)
    private ScheduledPaymentEntity scheduledPayment;

    /**
     * The list of installment entities associated with this loan.
     */
    @OneToMany(mappedBy = "loan", cascade = CascadeType.ALL,
            orphanRemoval = true)
    private final List<InstallmentEntity> installments = new ArrayList<>();

    /**
     * The account entity associated with this loan.
     */
    @ManyToOne
    @JoinColumn(name = "account_id", nullable = true)
    private Account account;

    /**
     * Constructor for creating a new LoanEntity with specified
     * parameters.
     *
     * @param amountBorrowed     The amount of money borrowed.
     * @param numberOfInstallments The number of installments
     *                             for repayment.
     * @param form                The associated form entity.
     * @param paymentMethod       The chosen payment method.
     */
    public LoanEntity(final Double amountBorrowed,
                      final Integer numberOfInstallments,
                      final FormEntity form,
                      final PaymentMethod paymentMethod) {

        this.amountBorrowed = amountBorrowed;
        this.numberOfInstallments = numberOfInstallments;
        this.form = form;
        setPaymentMethod(paymentMethod);
        this.issueDate = LocalDate.now();
        this.loanDueDate = this.issueDate
                .plusMonths(this.numberOfInstallments);
        setStatus(Status.REVIEW);
        recalculate();
        generateInstallments();
    }

    /**
     * Returns the payment method enum value based on the stored code.
     *
     * @return The payment method enum value.
     */
    public PaymentMethod getPaymentMethod() {
        return PaymentMethod.valueOf(paymentMethod);
    }

    /**
     * Returns the status enum value based on the stored code.
     *
     * @return The status enum value.
     */
    public Status getStatus() {
        return Status.valueOf(status);
    }

    /**
     * Sets the payment method code based on the provided enum value.
     *
     * @param paymentMethod The payment method enum value.
     */
    public void setPaymentMethod(final PaymentMethod paymentMethod) {
        if (paymentMethod != null) {
            this.paymentMethod = paymentMethod.getCode();
        }
    }

    /**
     * Sets the status code based on the provided enum value.
     *
     * @param status The status enum value.
     */
    public void setStatus(final Status status) {
        if (status != null) {
            this.status = status.getCode();
        }
    }

    /**
     * Sets the number of installments and recalculates loan values.
     *
     * @param number The new number of installments.
     */
    public void setNumberOfInstallments(final Integer number) {
        this.numberOfInstallments = number;
        recalculate();
        generateInstallments();
    }

    /**
     * Sets the amount borrowed and recalculates loan values.
     *
     * @param amount The new amount borrowed.
     */
    public void setAmountBorrowed(final Double amount) {
        this.amountBorrowed = amount;
        recalculate();
        generateInstallments();
    }

    /**
     * Retrieves the first unpaid installment for this loan.
     *
     * @return The first unpaid installment entity, or null
     *     if none found.
     */
    public InstallmentEntity getFirstUnpaidInstallment() {
        return installments.stream()
                .filter(installment -> !installment.getPaid())
                .findFirst()
                .orElse(null);
    }

    /**
     * Calculates and returns the number of paid installments
     * for this loan.
     *
     * @return The number of paid installments.
     */
    public long getNumberOfPaidInstallments() {
        return installments.stream()
                .filter(InstallmentEntity::getPaid)
                .count();
    }

    /**
     * Generates and returns a status for the loan application
     * based on certain criteria.
     *
     * @return The generated status enum value.
     */
    public Status generateStatus() {
        Status status = this.getStatus();

        if (status == null) {
            status = Status.REVIEW;

        } else if (status == Status.REVIEW) {
            double feeCommitment = valueOfInstallments / form.getIncome();
            // MagicNumber: Replace with a named constant
            double incomeAnnual = form.getIncome() * 12;
            double proportionLoan = amountBorrowed / incomeAnnual;
            // MagicNumber: Replace with named constants
            if (feeCommitment > 0.30 || proportionLoan > 0.50) {
                status = Status.REJECTED;

            } else {
                status = Status.APPROVED;
            }
        }
        return status;
    }

    /**
     * Generates and sets the issue date and loan due date based
     * on the number of installments.
     */
    public void generateAndSetDate() {
        this.issueDate = LocalDate.now();
        this.loanDueDate = issueDate.plusMonths(numberOfInstallments);
    }

    /**
     * Recalculates the total payable amount, installment value,
     * and total interest for the loan.
     */
    public void recalculate() {
        this.totalPayable = CalculationUtil.getTotalPayable(
                amountBorrowed, numberOfInstallments);
        this.valueOfInstallments = totalPayable / numberOfInstallments;
        this.totalInterest = CalculationUtil.getTotalInterest(
                amountBorrowed, numberOfInstallments);
    }

    /**
     * Generates the installment entities for this loan based on the calculated values.
     */
    public void generateInstallments() {
        this.installments.clear();

        double installmentAmount = totalPayable / numberOfInstallments;

        for (int i = 1; i <= numberOfInstallments; i++) {
            LocalDate dueDate = issueDate.plusMonths(i);

            InstallmentEntity installment = InstallmentEntity.builder()
                    .amount(installmentAmount)
                    .paid(false)
                    .dueDate(dueDate)
                    .loan(this)
                    .build();

            this.installments.add(installment);
        }
    }

    /**
     * Marks a specified number of installments as paid for this loan.
     *
     * @param toMarkAsPaid The number of installments to mark as paid.
     */
    public void markInstallmentsAsPaid(final long toMarkAsPaid) {
        for (int i = 0; i < toMarkAsPaid; i++) {
            markAsPaid();
        }
    }

    /**
     * Marks the next unpaid installment for this loan as paid.
     */
    public void markAsPaid() {
        for (InstallmentEntity installment : installments) {
            if (!installment.getPaid()) {
                installment.setPaid(true);
                installment.setPaymentDate(LocalDate.now());
                updateStatusFinished();
                break;
            }
        }
    }

    /**
     * Updates the status of the loan to FINISHED if all installments are paid.
     */
    public void updateStatusFinished() {
        if (installments.stream().allMatch(InstallmentEntity::getPaid)) {
            setStatus(Status.FINISHED);
        }
    }
}