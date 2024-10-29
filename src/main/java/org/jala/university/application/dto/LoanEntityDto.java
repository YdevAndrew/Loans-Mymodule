package org.jala.university.application.dto;

import java.time.LocalDate;
import java.util.UUID;

import org.jala.university.domain.entity.FormEntity;
import org.jala.university.domain.entity.enums.PaymentMethod;
import org.jala.university.domain.entity.enums.Status;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class LoanEntityDto {

    UUID id;
    Double maximumAmount;
    Double amountBorrowed;
    Double totalInterest;
    Integer numberOfInstallments;
    Double valueOfInstallments;
    PaymentMethod paymentMethod;
    Status status;
    LocalDate issueDate;
    LocalDate installmentsDueDate;
    LocalDate loanDueDate;
    FormEntity form;
}
