package org.jala.university.application.service;

import org.jala.university.application.dto.LoanRequestDto;
import org.jala.university.application.dto.LoanRequestFormDto;
import org.jala.university.application.mapper.LoanRequestFormMapper;
import org.jala.university.application.mapper.LoanRequestMapper;
import org.jala.university.domain.entity.LoanRequestEntity;
import org.jala.university.domain.entity.LoanRequestFormEntity;
import org.jala.university.domain.repository.LoanRequestFormRepository;
import org.jala.university.domain.repository.LoanRequestRepository;

public class LoansServiceImpl implements LoansService {
    private final LoanRequestFormMapper loanRequestFormMapper;
    private final LoanRequestFormRepository loanRequestFormRepository;
    private final LoanRequestMapper loanRequestMapper;
    private final LoanRequestRepository loanRequestRepository;

    public LoansServiceImpl(LoanRequestFormMapper loanRequestFormMapper, LoanRequestFormRepository loanRequestFormRepository, LoanRequestMapper loanRequestMapper, LoanRequestRepository loanRequestRepository) {
        this.loanRequestFormMapper = loanRequestFormMapper;
        this.loanRequestFormRepository = loanRequestFormRepository;
        this.loanRequestMapper = loanRequestMapper;
        this.loanRequestRepository = loanRequestRepository;
    }
    // Here should be added all the functionality to handle the business logic

    @Override
    public LoanRequestFormDto saveForm(LoanRequestFormDto loanRequestFormDto) {
        LoanRequestFormEntity loanRequestFormEntity = loanRequestFormMapper.mapFrom(loanRequestFormDto);
        LoanRequestFormEntity saved = loanRequestFormRepository.save(loanRequestFormEntity);
        return loanRequestFormMapper.mapTo(saved);
    }
    @Override
    public LoanRequestDto saveRequest(LoanRequestDto loanRequestDto) {
        LoanRequestEntity loanRequestEntity = loanRequestMapper.mapFrom(loanRequestDto);
        LoanRequestEntity saved = loanRequestRepository.save(loanRequestEntity);
        return loanRequestMapper.mapTo(saved);
    }
}
