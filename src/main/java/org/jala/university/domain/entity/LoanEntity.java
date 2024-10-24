package org.jala.university.domain.entity;

import java.time.LocalDate;
import java.util.UUID;

import org.jala.university.commons.domain.BaseEntity;
import org.jala.university.domain.entity.enums.PaymentMethod;
import org.jala.university.domain.entity.enums.Status;
import org.springframework.data.annotation.CreatedDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "LOAN")
public class LoanEntity implements BaseEntity<UUID> {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "amount_borrowed")
    private Double amountBorrowed;

    @Column(name = "total_interest")
    private Double totalInterest;

    @Column(name = "number_of_installments")
    private Integer numberOfInstallments;

    @Column(name = "value_of_installments")
    private Double valueOfInstallments;

    @Column(name = "total_payable")
    private Double totalPayable;

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

    @JoinColumn(name = "form_id", nullable = false)
    private FormEntity form;

    public LoanEntity(Double amountBorrowed, /* BigDecimal totalInterest */
            Integer numberOfInstallments, FormEntity form,
            /* BigDecimal valueOfInstallments */ PaymentMethod paymentMethod,
            LocalDate issueDate, LocalDate installmentsDueDate, LocalDate loanDueDate) {

        this.amountBorrowed = amountBorrowed;
        this.numberOfInstallments = numberOfInstallments;
        this.form = form;
        setPaymentMethod(paymentMethod);
        this.issueDate = issueDate;
        this.installmentsDueDate = installmentsDueDate;
        this.loanDueDate = loanDueDate;
        calculate();
        generateStatus();
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

    public void setNumberOfInstallments(Integer number) {
        this.numberOfInstallments = number;
        calculate();
    }

    public void setamountBorrowed(Double amount) {
        this.amountBorrowed = amount;
        calculate();
    }

    /*
     * Gera o status automaticamente para aprovação imediata caso um dos
     * requisitos seja cumprido. Ex: amountBorrowed < form.income * 6.
     */
    private void generateStatus() {
        if (this.status == null || this.status == Status.UNDER_ANALYSIS.getCode()) {
            if (form != null && form.getIncome() > 2000 && amountBorrowed < 8000) {
                this.status = Status.APPROVED.getCode();
            } else {
                this.status = Status.UNDER_ANALYSIS.getCode();
            }
        }
    }
    
    
    public void calculate() {
        double monthlyInterestRate = 0.02;
    
        // (1 + 0.02)^n (cálculo de juros compostos)
        double factor = Math.pow(1 + monthlyInterestRate, numberOfInstallments);
    
        // Valor total a pagar com juros compostos
        this.totalPayable = amountBorrowed * factor;
    
        // Valor das parcelas mensais
        this.valueOfInstallments = totalPayable / numberOfInstallments;

        this.totalInterest = totalPayable - amountBorrowed;
    }
    

    /*
     * lista de parcelas só precisaria de um for com o número de meses,
     * o total dividido por ele e a data acrescentando +1 mês por for.
     */
}
