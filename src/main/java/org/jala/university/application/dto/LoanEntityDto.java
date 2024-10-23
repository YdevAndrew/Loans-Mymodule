package org.jala.university.application.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import org.jala.university.domain.entity.enums.PaymentMethod;
import org.jala.university.domain.entity.enums.Status;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class LoanEntityDto {

    UUID id;
    BigDecimal maximumAmount;
    BigDecimal amountBorrowed;
    BigDecimal totalInterest;
    Integer numberOfInstallments;
    BigDecimal valueOfInstallments;
    PaymentMethod paymentMethod;
    Status status;
    LocalDate issueDate;
    LocalDate installmentsDueDate;
    LocalDate loanDueDate;
    //Form form;
}
