package org.jala.university.application.mapper;

import org.jala.university.application.dto.LoanEntityDto;
import org.jala.university.commons.application.mapper.Mapper;
import org.jala.university.domain.entity.LoanEntity;
import org.jala.university.domain.entity.enums.Status;
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
                .paymentMethod(loanEntity.getPaymentMethod())
                .status(loanEntity.getStatus())
                .issueDate(loanEntity.getIssueDate())
                .installmentsDueDate(loanEntity.getInstallmentsDueDate())
                .loanDueDate(loanEntity.getLoanDueDate())
                .build();
    }

    @Override
    public LoanEntity mapFrom(LoanEntityDto loanEntityDto) {
        LoanEntity entity = LoanEntity.builder()
                .id(loanEntityDto.getId())
                .amountBorrowed(loanEntityDto.getAmountBorrowed())
                .paymentMethod(loanEntityDto.getPaymentMethod().getCode())
                .numberOfInstallments(loanEntityDto.getNumberOfInstallments())
                .build();

                if (loanEntityDto.getStatus() != null) {
                    entity.setStatus(loanEntityDto.getStatus());
                } else {
                    entity.setStatus(Status.REVIEW);
                }

                return entity;
    }
}
