package org.jala.university.application.dto;

import java.time.LocalDate;
import java.util.List;
import org.jala.university.domain.entity.Account;
// import org.jala.university.domain.entity.Account;
import org.jala.university.domain.entity.FormEntity;
import org.jala.university.domain.entity.InstallmentEntity;
import org.jala.university.domain.entity.enums.PaymentMethod;
import org.jala.university.domain.entity.enums.Status;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class LoanEntityDto {

    Integer id;
    Double amountBorrowed;
    Double totalInterest;
    Integer numberOfInstallments;
    Double valueOfInstallments;
    Double totalPayable;
    PaymentMethod paymentMethod;
    Status status;
    LocalDate issueDate;
    LocalDate loanDueDate;
    FormEntity form;
    Integer scheduledPaymentId;
    List<InstallmentEntity> installments;
    Account account;
}