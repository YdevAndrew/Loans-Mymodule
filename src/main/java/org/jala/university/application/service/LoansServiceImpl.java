package org.jala.university.application.service;

import org.jala.university.application.dto.LoanRequestFormDto;
import org.jala.university.application.mapper.LoanRequestFormMapper;
import org.jala.university.domain.entity.LoanRequestFormEntity;
import org.jala.university.domain.repository.LoanRequestFormRepository;

public class LoansServiceImpl implements LoansService {
    private final LoanRequestFormMapper loanRequestFormMapper;
    private final LoanRequestFormRepository loanRequestFormRepository;

    public LoansServiceImpl(LoanRequestFormMapper loanRequestFormMapper, LoanRequestFormRepository loanRequestFormRepository) {
        this.loanRequestFormMapper = loanRequestFormMapper;
        this.loanRequestFormRepository = loanRequestFormRepository;
    }
    // Here should be added all the functionality to handle the business logic

    @Override
    public LoanRequestFormDto saveForm(LoanRequestFormDto loanRequestFormDto) {
        LoanRequestFormEntity loanRequestFormEntity = loanRequestFormMapper.mapFrom(loanRequestFormDto);
        LoanRequestFormEntity saved = loanRequestFormRepository.save(loanRequestFormEntity);
        return loanRequestFormMapper.mapTo(saved);
    }

    @Override
    public void saveForm(FormDto formDto) {
        SampleEntity sampleEntity = sampleEntityMapper.mapFrom(SampleEntityDto.builder().build());
        sampleEntityRepository.save(sampleEntity);
    }
}
