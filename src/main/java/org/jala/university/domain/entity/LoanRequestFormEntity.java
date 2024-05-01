package org.jala.university.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Value;
import org.jala.university.commons.domain.BaseEntity;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@Table(name = "loan_request_form")
public class LoanRequestFormEntity implements BaseEntity<UUID> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "UUID")
    UUID id;

    @Column
    String namesApplicant;

    @Column
    String lastnamesApplicant;

    @Column
    String address;

    @Column
    BigDecimal monthlyIncome;

    @Column
    BigDecimal loanAmount;

    @Column
    int desiredLoanPeriod;

    @CreatedDate
    Date created;

    @LastModifiedDate
    Date updated;

    @Override
    public UUID getId() {
        return id;
    }

}
