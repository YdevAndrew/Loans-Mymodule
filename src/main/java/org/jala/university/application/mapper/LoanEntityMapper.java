package org.jala.university.application.mapper;

import org.jala.university.application.dto.LoanEntityDto;
import org.jala.university.commons.application.mapper.Mapper;
import org.jala.university.domain.entity.LoanEntity;
import org.springframework.stereotype.Component;

@Component
public class LoanEntityMapper implements Mapper<LoanEntity, LoanEntityDto> {

    @Override
    public LoanEntityDto mapTo(LoanEntity loanEntity) {
        return LoanEntityDto.builder()
                .id(loanEntity.getId())
                .amountBorrowed(loanEntity.getAmountBorrowed())
                .totalInterest(loanEntity.getTotalInterest())
                .numberOfInstallments(loanEntity.getNumberOfInstallments())
                .valueOfInstallments(loanEntity.getValueOfInstallments())
                .totalPayable(loanEntity.getTotalPayable())
                .paymentMethod(loanEntity.getPaymentMethod())
                .status(loanEntity.getStatus())
                .issueDate(loanEntity.getIssueDate())
                .loanDueDate(loanEntity.getLoanDueDate())
                .form(loanEntity.getForm())
                .scheduledPaymentId(loanEntity.getScheduledPaymentId())
                //.account(loanEntity.getAccount())
                .build();
    }

    @Override
    public LoanEntity mapFrom(LoanEntityDto loanEntityDto) {
        return LoanEntity.builder()
                .id(loanEntityDto.getId())
                .amountBorrowed(loanEntityDto.getAmountBorrowed())
                .totalInterest(loanEntityDto.getTotalInterest())
                .numberOfInstallments(loanEntityDto.getNumberOfInstallments())
                .valueOfInstallments(loanEntityDto.getValueOfInstallments())
                .totalPayable(loanEntityDto.getTotalPayable())
                .paymentMethod(loanEntityDto.getPaymentMethod().getCode())
                .status(loanEntityDto.getStatus().getCode())
                .issueDate(loanEntityDto.getIssueDate())
                .loanDueDate(loanEntityDto.getLoanDueDate())
                .form(loanEntityDto.getForm())
                .scheduledPaymentId(loanEntityDto.getScheduledPaymentId())
                //.account(loanEntityDto.getAccount())
                .build();
    }
}
