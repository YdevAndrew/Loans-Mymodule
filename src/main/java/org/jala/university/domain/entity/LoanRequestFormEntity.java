package org.jala.university.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.jala.university.commons.domain.BaseEntity;

import java.math.BigDecimal;
import java.sql.Blob;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@Table(name = "loan_request_form")
public class LoanRequestFormEntity implements BaseEntity<UUID> {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;
    @Column
    String namesApplicant;
    @Column
    String lastnamesApplicant;
    @Column
    String identityCode;
    @Column
    LocalDate dateOfBirth;
    @Column
    String email;
    @Column
    String phoneNumber;
    @Column
    String address;

    @Column
    String employerName;
    @Column
    BigDecimal monthlyIncome;
    @Column
    String jobTitle;
    @Column
    Integer yearsOfService;
    @Column
    BigDecimal loanAmount;
    @Column
    String loanType;
    @Column
    Integer desiredLoanPeriod;

    @Lob
    @Column
    Blob idCardPDF;
    @Lob
    @Column
    Blob proofAddressPDF;
    @Lob
    @Column
    Blob proofIncomePDF;
    @Lob
    @Column
    Blob laborCertificatePDF;
    @Lob
    @Column
    Blob proofLengthServicePDF;

    public LoanRequestFormEntity() {
        super();
    }

    @Override
    public UUID getId() {
        return id;
    }

}
