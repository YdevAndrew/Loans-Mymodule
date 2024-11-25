package org.jala.university.domain.entity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.jala.university.commons.domain.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This entity represents an installment payment for a loan.
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "INSTALLMENT")
public class InstallmentEntity implements BaseEntity<Integer> {

    /**
     * The unique identifier of the installment entity.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * The amount due for this installment.
     */
    @Column(name = "amount")
    private Double amount;

    /**
     * Indicates whether this installment has been paid.
     */
    @Column(name = "paid")
    private Boolean paid;

    /**
     * The date when this installment was paid.
     */
    @Column(name = "payment_date")
    private LocalDate paymentDate;

    /**
     * The due date for this installment.
     */
    @Column(name = "due_date")
    private LocalDate dueDate;

    /**
     * The loan to which this installment belongs.
     */
    @ManyToOne
    @JoinColumn(name = "loan_id", nullable = false)
    private LoanEntity loan;

    /**
     * Returns the due date formatted as "dd/MM/yyyy".
     *
     * @return The formatted due date string.
     */
    public String getFormattedDueDate() {
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return dueDate.format(formatter);
    }
}