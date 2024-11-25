package org.jala.university.domain.entity;

import org.jala.university.commons.domain.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This entity represents a form submitted by a user, containing
 * information about their income and proof of income.
 */
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "FORM")
public class FormEntity implements BaseEntity<Integer> {

    /**
     * The unique identifier of the form entity.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * The declared income of the user.
     */
    @Column(name = "income")
    private Double income;

    /**
     * The proof of income document, stored as a byte array.
     */
    @Lob
    @Column(name = "proof_of_income")
    private byte[] proofOfIncome;

    /**
     * The calculated maximum loan amount based on the income.
     */
    @Column(name = "maximum_amount")
    private Double maximumAmount;

    /**
     * Sets the income and recalculates the maximum loan amount.
     *
     * @param income The new income value.
     */
    public void setIncome(final Double income) {
        this.income = income;
        calculateMaximumAmount();
    }

    /**
     * Constructor for creating a new FormEntity with income and
     * proof of income.
     *
     * @param income         The declared income.
     * @param proofOfIncome The proof of income document.
     */
    public FormEntity(final Double income, final byte[] proofOfIncome) {
        this.income = income;
        this.proofOfIncome = proofOfIncome;
        calculateMaximumAmount();
    }

    /**
     * Calculates the maximum loan amount based on the income.
     * The formula used is: maximumAmount = income + (0.8 * income).
     */
    public void calculateMaximumAmount() {
        // MagicNumber: Replace with a named constant
        this.maximumAmount = income + income * 0.8;
    }
}