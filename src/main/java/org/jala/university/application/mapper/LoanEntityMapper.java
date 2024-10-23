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
                .valueOfInstallments(loanEntity.getValueOfInstallments())
                .paymentMethod(loanEntity.getPaymentMethod())
                .status(loanEntity.getStatus())
                .issueDate(loanEntity.getIssueDate())
                .installmentsDueDate(loanEntity.getInstallmentsDueDate())
                .loanDueDate(loanEntity.getLoanDueDate())
                .build();
    }

    @Override
    public LoanEntity mapFrom(LoanEntityDto loanEntityDto) {
        return LoanEntity.builder()
                .id(loanEntityDto.getId())
                .amountBorrowed(loanEntityDto.getAmountBorrowed())
                .totalInterest(loanEntityDto.getTotalInterest())
                .valueOfInstallments(loanEntityDto.getValueOfInstallments())
                .paymentMethod(loanEntityDto.getPaymentMethod().getCode())
                .status(loanEntityDto.getStatus().getCode())
                .issueDate(loanEntityDto.getIssueDate())
                .installmentsDueDate(loanEntityDto.getInstallmentsDueDate())
                .loanDueDate(loanEntityDto.getLoanDueDate())
                .build();
    }
}
