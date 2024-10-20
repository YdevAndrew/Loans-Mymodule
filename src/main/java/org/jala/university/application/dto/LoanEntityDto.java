package org.jala.university.application.dto;

import java.util.Date;
import java.util.UUID;

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
    Date issueDate;
    Date installmentsDueDate;
    Date loanDueDate;
    //Form form;
}
