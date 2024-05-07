package org.jala.university.application.mapper;

import org.jala.university.application.dto.LoanRequestFormDto;
import org.jala.university.commons.application.mapper.Mapper;
import org.jala.university.domain.entity.LoanRequestFormEntity;

import java.math.BigDecimal;
import java.sql.Blob;
import java.time.LocalDate;

public class LoanRequestFormMapper implements Mapper<LoanRequestFormEntity, LoanRequestFormDto> {

    @Override
    public LoanRequestFormDto mapTo(LoanRequestFormEntity input) {
        return LoanRequestFormDto.builder()
                .id(input.getId())
                .namesApplicant(input.getNamesApplicant())
                .lastnamesApplicant(input.getLastnamesApplicant())
                .identityCode(input.getIdentityCode())
                .dateOfBirth(input.getDateOfBirth())
                .email(input.getEmail())
                .phoneNumber(input.getPhoneNumber())
                .address(input.getAddress())
                .employerName(input.getEmployerName())
                .monthlyIncome(input.getMonthlyIncome())
                .jobTitle(input.getJobTitle())
                .yearsOfService(input.getYearsOfService())
                .loanAmount(input.getLoanAmount())
                .loanType(input.getLoanType())
                .desiredLoanPeriod(input.getDesiredLoanPeriod())
                .idCardPDF(input.getIdCardPDF())
                .proofAddressPDF(input.getProofAddressPDF())
                .proofIncomePDF(input.getProofIncomePDF())
                .laborCertificatePDF(input.getLaborCertificatePDF())
                .proofLengthServicePDF(input.getProofLengthServicePDF())
                .build();
    }

    @Override
    public LoanRequestFormEntity mapFrom(LoanRequestFormDto input) {
        return LoanRequestFormEntity.builder()
                .id(input.getId())
                .namesApplicant(input.getNamesApplicant())
                .lastnamesApplicant(input.getLastnamesApplicant())
                .identityCode(input.getIdentityCode())
                .dateOfBirth(input.getDateOfBirth())
                .email(input.getEmail())
                .phoneNumber(input.getPhoneNumber())
                .address(input.getAddress())
                .employerName(input.getEmployerName())
                .monthlyIncome(input.getMonthlyIncome())
                .jobTitle(input.getJobTitle())
                .yearsOfService(input.getYearsOfService())
                .loanAmount(input.getLoanAmount())
                .loanType(input.getLoanType())
                .desiredLoanPeriod(input.getDesiredLoanPeriod())
                .idCardPDF(input.getIdCardPDF())
                .proofAddressPDF(input.getProofAddressPDF())
                .proofIncomePDF(input.getProofIncomePDF())
                .laborCertificatePDF(input.getLaborCertificatePDF())
                .proofLengthServicePDF(input.getProofLengthServicePDF())
                .build();
    }
}
