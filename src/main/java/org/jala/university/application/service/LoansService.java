package org.jala.university.application.service;

import org.jala.university.application.dto.LoanRequestDto;
import org.jala.university.application.dto.LoanRequestFormDto;

public interface LoansService {
    // Here should be added all the required methods that will handle business logic
    LoanRequestFormDto saveForm(LoanRequestFormDto loanRequestFormDto);
    LoanRequestDto saveRequest(LoanRequestDto requestDto);
}
