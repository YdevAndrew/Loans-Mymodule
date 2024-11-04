package org.jala.university.domain.entity;

import java.util.UUID;

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

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "FORM")
public class FormEntity implements BaseEntity<UUID> {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;


    @Column(name = "income")
    private Double income;

    @Lob
    @Column(name = "proof_of_income")
    private byte[] proofOfIncome;

    @Column(name = "maximum_amount")
    private Double maximumAmount;

    public void setIncome(Double income) {
        this.income = income;
        calculateMaximumAmount();
    }

    public FormEntity(Double income, byte[] proofOfIncome) {

        this.income = income;
        this.proofOfIncome = proofOfIncome;
        calculateMaximumAmount();
    }

    //maximumAmount = income + (0.8 * income).
    public void calculateMaximumAmount() {
        this.maximumAmount = income + income * 0.8;
    }    
}
