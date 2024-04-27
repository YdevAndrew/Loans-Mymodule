package org.jala.university.application.mapper;

import org.jala.university.application.dto.LoanRequestFormDto;
import org.jala.university.commons.application.mapper.Mapper;
import org.jala.university.domain.entity.LoanRequestFormEntity;

public class LoanRequestFormMapper implements Mapper<LoanRequestFormEntity, LoanRequestFormDto> {

    @Override
    public LoanRequestFormDto mapTo(LoanRequestFormEntity input) {
        return LoanRequestFormDto.builder()
          .id(input.getId())
          .namesApplicant(input.getNamesApplicant())
          .lastnamesApplicant(input.getLastnamesApplicant())
          .address(input.getAddress())
          .monthlyIncome(input.getMonthlyIncome())
          .loanAmount(input.getLoanAmount())
          .desiredLoanPeriod(input.getDesiredLoanPeriod())
          .build();
    }

    @Override
    public LoanRequestFormEntity mapFrom(LoanRequestFormDto input) {
        return LoanRequestFormEntity.builder()
          .id(input.getId())
          .namesApplicant(input.getNamesApplicant())
          .lastnamesApplicant(input.getLastnamesApplicant())
          .address(input.getAddress())
          .monthlyIncome(input.getMonthlyIncome())
          .loanAmount(input.getLoanAmount())
          .desiredLoanPeriod(input.getDesiredLoanPeriod())
          .build();
    }
}
