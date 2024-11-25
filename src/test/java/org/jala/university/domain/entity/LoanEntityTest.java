// src/test/java/org/jala/university/domain/entity/LoanEntityTest.java

package org.jala.university.domain.entity;

import org.jala.university.domain.entity.enums.PaymentMethod;
import org.jala.university.domain.entity.enums.Status;
import org.jala.university.utils.CalculationUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LoanEntityTest {

    private LoanEntity loanEntity;

    @BeforeEach
    void setUp() {

        try (MockedStatic<CalculationUtil> calculationUtilMock = Mockito.mockStatic(CalculationUtil.class)) {
            calculationUtilMock.when(() -> CalculationUtil.getTotalPayable(1000.0, 10))
                    .thenReturn(1200.0);
            calculationUtilMock.when(() -> CalculationUtil.getTotalInterest(1000.0, 10))
                    .thenReturn(200.0);


            loanEntity = LoanEntity.builder()
                    .amountBorrowed(1000.0)
                    .numberOfInstallments(10)
                    .form(mock(FormEntity.class))
                    .paymentMethod(PaymentMethod.DEBIT_ACCOUNT.getCode())
                    .build();

            loanEntity.setStatus(Status.REVIEW);
            loanEntity.setIssueDate(LocalDate.now());


            loanEntity.recalculate();
            loanEntity.generateInstallments();
        }
    }

    @Test
    void testRecalculate() {
        // Assert calculated values
        assertEquals(1200.0, loanEntity.getTotalPayable());
        assertEquals(200.0, loanEntity.getTotalInterest());
        assertEquals(120.0, loanEntity.getValueOfInstallments());
    }

    @Test
    void testGenerateInstallments() {
        List<InstallmentEntity> installments = loanEntity.getInstallments();


        assertEquals(10, installments.size());


        for (int i = 0; i < installments.size(); i++) {
            InstallmentEntity installment = installments.get(i);
            assertEquals(120.0, installment.getAmount());
            assertFalse(installment.getPaid());
            assertEquals(LocalDate.now().plusMonths(i + 1), installment.getDueDate());
        }
    }

    @Test
    void testMarkAsPaid() {

        loanEntity.markAsPaid();


        InstallmentEntity firstInstallment = loanEntity.getInstallments().get(0);
        assertTrue(firstInstallment.getPaid());
        assertNotNull(firstInstallment.getPaymentDate());
        assertEquals(LocalDate.now(), firstInstallment.getPaymentDate());


        assertNotEquals(Status.FINISHED, loanEntity.getStatus());
    }

    @Test
    void testUpdateStatusFinished() {

        loanEntity.getInstallments().forEach(installment -> installment.setPaid(true));


        loanEntity.updateStatusFinished();


        assertEquals(Status.FINISHED, loanEntity.getStatus());
    }

    @Test
    void testGetFirstUnpaidInstallment() {

        InstallmentEntity unpaidInstallment = loanEntity.getFirstUnpaidInstallment();
        assertNotNull(unpaidInstallment);
        assertEquals(loanEntity.getInstallments().get(0), unpaidInstallment);


        loanEntity.markAsPaid();
        unpaidInstallment = loanEntity.getFirstUnpaidInstallment();
        assertNotNull(unpaidInstallment);
        assertEquals(loanEntity.getInstallments().get(1), unpaidInstallment);
    }

    @Test
    void testGenerateStatus() {

        FormEntity form = mock(FormEntity.class);
        when(form.getIncome()).thenReturn(5000.0);

        loanEntity.setForm(form);


        Status status = loanEntity.generateStatus();

        assertEquals(Status.APPROVED, status);
    }
}
