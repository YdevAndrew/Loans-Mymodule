package org.jala.university.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import org.jala.university.commons.domain.BaseEntity;
import org.jala.university.domain.entity.enums.PaymentMethod;
import org.jala.university.domain.entity.enums.Status;
import org.springframework.data.annotation.CreatedDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "LOAN")
public class LoanEntity implements BaseEntity<UUID> {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "maximum_amount")
    private BigDecimal maximumAmount;

    @Column(name = "amount_borrowed")
    private BigDecimal amountBorrowed;

    @Column(name = "total_interest")
    private BigDecimal totalInterest;

    @Column(name = "number_of_installments")
    private Integer numberOfInstallments;

    @Column(name = "value_of_installments")
    private BigDecimal valueOfInstallments;

    @Column(name = "payment_method")
    private Integer paymentMethod;

    @Column(name = "status")
    private Integer status;

    @Column(name = "issue_date")
    @CreatedDate
    LocalDate issueDate;

    @Column(name = "installments_due_date")
    LocalDate installmentsDueDate;

    @Column(name = "loan_due_date")
    LocalDate loanDueDate;

    /*@OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "form_id", nullable = false)
    private Form form;*/

    public LoanEntity(UUID id, BigDecimal maximumAmount, BigDecimal amountBorrowed, BigDecimal totalInterest,
            Integer numberOfInstallments, BigDecimal valueOfInstallments, PaymentMethod paymentMethod, Status status,
            LocalDate issueDate, LocalDate installmentsDueDate, LocalDate loanDueDate) {

        this.id = id;
        this.maximumAmount = maximumAmount;
        this.amountBorrowed = amountBorrowed;
        this.totalInterest = totalInterest;
        this.numberOfInstallments = numberOfInstallments;
        this.valueOfInstallments = valueOfInstallments;
        setPaymentMethod(paymentMethod);
        setStatus(status);
        this.issueDate = issueDate;
        this.installmentsDueDate = installmentsDueDate;
        this.loanDueDate = loanDueDate;
    }

    public PaymentMethod getPaymentMethod() {
        return PaymentMethod.valueOf(paymentMethod);
    }

    public Status getStatus() {
        return Status.valueOf(status);
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        if (paymentMethod != null) {
            this.paymentMethod = paymentMethod.getCode();
        }
    }

    public void setStatus(Status status) {
        if (status != null) {
            this.status = status.getCode();
        }
    }
}
