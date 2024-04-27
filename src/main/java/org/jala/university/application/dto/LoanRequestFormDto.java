package org.jala.university.application.dto;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
@Value
public class LoanRequestFormDto {
    UUID id;

    String namesApplicant;

    String lastnamesApplicant;

    String address;

    BigDecimal monthlyIncome;

    BigDecimal loanAmount;

    int desiredLoanPeriod;
}
