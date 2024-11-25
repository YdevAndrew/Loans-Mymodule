package org.jala.university.application.mapper;

import org.hibernate.Hibernate;
import org.jala.university.application.dto.LoanEntityDto;
import org.jala.university.commons.application.mapper.Mapper;
import org.jala.university.domain.entity.LoanEntity;
import org.springframework.stereotype.Component;

/**
 * This class implements the {@link Mapper} interface to provide mapping
 * functionality between {@link LoanEntity} and {@link LoanEntityDto}.
 */
@Component
public class LoanEntityMapper implements Mapper<LoanEntity, LoanEntityDto> {

    /**
     * Maps a {@link LoanEntity} object to a {@link LoanEntityDto} object.
     *
     * @param loanEntity The {@link LoanEntity} object to map from.
     * @return The mapped {@link LoanEntityDto} object.
     */
    @Override
    public LoanEntityDto mapTo(final LoanEntity loanEntity) {
        // Explicitly initialize the installments collection
        // to avoid lazy loading issues.
        if (loanEntity.getInstallments() != null) {
            Hibernate.initialize(loanEntity.getInstallments());
        }

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
                .account(loanEntity.getAccount())
                .installments(loanEntity.getInstallments())
                .build();
    }

    /**
     * Maps a {@link LoanEntityDto} object to a
     * {@link LoanEntity} object.
     *
     * @param loanEntityDto The {@link LoanEntityDto} object to map from.
     * @return The mapped {@link LoanEntity} object.
     */
    @Override
    public LoanEntity mapFrom(final LoanEntityDto loanEntityDto) {
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
                .account(loanEntityDto.getAccount())
                .build();
    }
}