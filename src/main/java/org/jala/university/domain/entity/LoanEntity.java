package org.jala.university.domain.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
import jakarta.persistence.OneToMany;
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
public class LoanEntity implements BaseEntity<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

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

    @Column(name = "loan_due_date")
    LocalDate loanDueDate;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "form_id", nullable = true)
    private FormEntity form;

    // @OneToOne
    // @JoinColumn(name = "Scheduled_Payment_id", nullable = true)
    // private ScheduledPayment scheduledPayment;

    private Integer scheduledPaymentId;

    @OneToMany(mappedBy = "loan", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<InstallmentEntity> installments = new ArrayList<>();

    // @ManyToOne
    // @JoinColumn(name = "account_id", nullable = true)
    // private Account account;
    
    public LoanEntity(Double amountBorrowed, Integer numberOfInstallments, FormEntity form,
            PaymentMethod paymentMethod) {

        this.amountBorrowed = amountBorrowed;
        this.numberOfInstallments = numberOfInstallments;
        this.form = form;
        setPaymentMethod(paymentMethod);
        this.issueDate = LocalDate.now();
        this.loanDueDate = this.issueDate.plusMonths(this.numberOfInstallments);
        setStatus(Status.REVIEW);
        recalculate();
        generateInstallments();
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
        recalculate();
        generateInstallments();
    }

    public void setAmountBorrowed(Double amount) {
        this.amountBorrowed = amount;
        recalculate();
        generateInstallments();
    }

    /*
     * Para o Front:
     */

    // Retorna a primeira parcela não paga da lista
    public InstallmentEntity getFirstUnpaidInstallment() {
        return installments.stream()
                .filter(installment -> !installment.getPaid()) // Filtra as parcelas não pagas
                .findFirst() // Retorna a primeira encontrada
                .orElse(null); // Retorna null se todas estiverem pagas
    }

    // Retorna o número de parcelas pagas
    public long getNumberOfPaidInstallments() {
        return installments.stream()
                .filter(InstallmentEntity::getPaid)
                .count();
    }

    public Status generateStatus() {
        Status status = this.getStatus();

        if (status == null) {
            status = Status.REVIEW;

        } else if (status == Status.REVIEW) {
            status = new Random().nextBoolean() ? Status.APPROVED : Status.REJECTED;
        }
        return status;
    }

    public void generateAndSetDate() {
        this.issueDate = LocalDate.now();
        this.loanDueDate = issueDate.plusMonths(numberOfInstallments);
    }

    public void recalculate() {
        // Calcule totalPayable com base no montante emprestado e no número de parcelas
        this.totalPayable = CalculationUtil.getTotalPayable(amountBorrowed, (double) numberOfInstallments);

        // Calcule o valor de cada parcela
        this.valueOfInstallments = totalPayable / numberOfInstallments;

        // Calcule o total de juros com base no montante emprestado
        this.totalInterest = CalculationUtil.getTotalInterest(amountBorrowed, this.valueOfInstallments);
    }

    public void generateInstallments() {
        this.installments.clear(); // Limpa a lista anterior, se houver

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
    
    public void markInstallmentsAsPaid(long toMarkAsPaid) {
        for (int i=0; i<toMarkAsPaid; i++) {
            markAsPaid();
        }
    }

    // Coloca a primeira parcela não paga da lista como paga
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

    /*
     * Verifica se todas as parcelas foram pagas,
     * se sim, define o empréstimo como FINISHED
     */
    public void updateStatusFinished() {
        if (installments.stream().allMatch(InstallmentEntity::getPaid)) {
            setStatus(Status.FINISHED);
        }
    }

    //Se for receber resposta do pagamentos externos.
    /*public void verifyIfItsPaid() {
        if (método que retorna se o pagamento agendado foi feito == true) {
            this.markAsPaid();
        }
    }*/
}
