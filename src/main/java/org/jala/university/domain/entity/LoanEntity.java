package org.jala.university.domain.entity;

import java.time.LocalDate;
import java.util.Random;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.jala.university.commons.domain.BaseEntity;
import org.jala.university.domain.entity.enums.PaymentMethod;
import org.jala.university.domain.entity.enums.Status;
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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "form_id", nullable = false)
    private FormEntity form;

    @OneToMany(mappedBy = "loan", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<InstallmentEntity> installments = new ArrayList<>();

    /*
     * @ManyToOne
     *
     * @JoinColumn(name = "account_id", nullable = false)
     * private Account account;
     */

    public LoanEntity(Double amountBorrowed, Integer numberOfInstallments, FormEntity form,
                      PaymentMethod paymentMethod, LocalDate issueDate, LocalDate installmentsDueDate, LocalDate loanDueDate) {

        this.amountBorrowed = amountBorrowed;
        this.numberOfInstallments = numberOfInstallments;
        this.form = form;
        setPaymentMethod(paymentMethod);
        this.issueDate = LocalDate.now();
        this.installmentsDueDate = installmentsDueDate;
        this.loanDueDate = this.issueDate.plusMonths(this.numberOfInstallments);
        setStatus(Status.REVIEW);
        calculate();
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
        calculate();
        generateInstallments();
    }

    public void setamountBorrowed(Double amount) {
        this.amountBorrowed = amount;
        calculate();
        generateInstallments();
    }

    /*
     * Para o Front:
     */
    // Recebe o número de parcelas e retorna o valor das parcelas
    public Double getValueOfInstallments(int installments) {
        return totalPayable / installments;
    }

    // Retorna a data inicial em formato brasileiro
    public String getFormattedIssueDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return this.issueDate.format(formatter);
    }

    // Retorna a data final do empréstimo em formato brasileiro
    public String getFormattedLoanDueDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return this.loanDueDate.format(formatter);
    }

    // Retorna a data de vencimento da primeira parcela
    public String getFirstInstallmentDueDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return this.issueDate.plusMonths(1).format(formatter);
    }

    // Retorna o dia da data
    public int getDueDateDay() {
        return this.issueDate.getDayOfMonth();
    }

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

    // Retorna o quanto falta a pagar (Outstanding Balance)
    public Double getOutstandingBalance() {
        return totalPayable - getNumberOfPaidInstallments() * valueOfInstallments;
    }

    public Status generateStatus() {
        Status status = this.getStatus();

        if (/*requisitos para mudança de status*/status == null) {

            //status específico para os requisitos
        } else {
            status = new Random().nextBoolean() ? Status.APPROVED : Status.REJECTED;
        }

        return status;
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

    // Coloca a primeira parcela não paga da lista como paga
    public void markAsPaid() {
        // if (metododetransacao()) {
        for (InstallmentEntity installment : installments) {
            if (!installment.getPaid()) {
                installment.setPaid(true);
                installment.setPaymentDate(LocalDate.now());
                updateStatusFinished();
                break;
            }
        }
        /*
         * } else {
         * System.out.println("Payment failed.");
         * }
         */
    }

    /*
     * Verifica se todas as parcelas foram pagas,
     * se sim, define o empréstimo como FINISHED
     */
    private void updateStatusFinished() {
        if (installments.stream().allMatch(InstallmentEntity::getPaid)) {
            setStatus(Status.FINISHED);
        }
    }

    // Para pagamento automático
    public void paymentMethodLogic() {
        if (paymentMethod == PaymentMethod.DEBIT_ACCOUNT.getCode()) {
            // Chamar lógica de agendamento do pagamentos externos.
        }
    }
}
