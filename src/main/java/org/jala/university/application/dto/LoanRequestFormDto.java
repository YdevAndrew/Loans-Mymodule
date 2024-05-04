package org.jala.university.application.dto;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;
import java.time.LocalDate;
import java.math.BigDecimal;
import java.sql.Blob;

@Builder
@Value
public class LoanRequestFormDto {
    UUID id;
    String namesApplicant;
    String lastnamesApplicant;
    String identityCode;
    LocalDate dateOfBirth;
    String email;
    String phoneNumber;
    String address;

    String employerName;
    BigDecimal monthlyIncome;
    String jobTitle;
    Integer yearsOfService;
    BigDecimal loanAmount;
    String loanType;
    Integer desiredLoanPeriod;

    Blob idCardPDF;
    Blob proofAddressPDF;
    Blob proofIncomePDF;
    Blob laborCertificatePDF;
    Blob proofLengthServicePDF;
}
