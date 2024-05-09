package org.jala.university.application.mapper;

import org.jala.university.application.dto.LoanRequestDto;
import org.jala.university.commons.application.mapper.Mapper;
import org.jala.university.domain.entity.LoanRequestEntity;

public class LoanRequestMapper implements Mapper<LoanRequestEntity, LoanRequestDto> {
    private static LoanRequestFormMapper loanRequestFormMapper = new LoanRequestFormMapper();
    @Override
    public LoanRequestDto mapTo(LoanRequestEntity loanRequestEntity) {
        return LoanRequestDto.builder()
                .id(loanRequestEntity.getId())
                .status(loanRequestEntity.getStatus())
                .loanRequestForm(loanRequestFormMapper.mapTo(loanRequestEntity.getLoanRequestForm()))
                .build();
    }

    @Override
    public LoanRequestEntity mapFrom(LoanRequestDto loanRequestDto) {
        return LoanRequestEntity.builder()
                .id(loanRequestDto.getId())
                .status(loanRequestDto.getStatus())
                .loanRequestForm(loanRequestFormMapper.mapFrom(loanRequestDto.getLoanRequestForm()))
                .build();
    }
}
