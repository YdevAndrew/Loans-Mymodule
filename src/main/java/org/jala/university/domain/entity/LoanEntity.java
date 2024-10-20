package org.jala.university.domain.entity;

import java.util.Date;
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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoanEntity implements BaseEntity<UUID> {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column
    private Double maximumAmount;

    @Column
    private Double amountBorrowed;

    @Column
    private Double totalInterest;

    @Column
    private Integer numberOfInstallments;

    @Column
    private Double valueOfInstallments;

    @Column
    private Integer paymentMethod;

    @Column
    private Integer status;

    @CreatedDate
    Date issueDate;

    @Column
    Date installmentsDueDate;

    @Column
    Date loanDueDate;

    /*@OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "form_id", nullable = false)
    private Form form;*/

    public LoanEntity(UUID id, Double maximumAmount, Double amountBorrowed, Double totalInterest,
            Integer numberOfInstallments, Double valueOfInstallments, PaymentMethod paymentMethod, Status status,
            Date issueDate, Date installmentsDueDate, Date loanDueDate) {

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
