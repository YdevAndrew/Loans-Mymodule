// src/test/java/org/jala/university/domain/entity/InstallmentEntityTest.java

package org.jala.university.domain.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;

class InstallmentEntityTest {

    @Test
    void testGetFormattedDueDate() {

        LocalDate dueDate = LocalDate.of(2024, 11, 24);
        InstallmentEntity installment = InstallmentEntity.builder()
                .dueDate(dueDate)
                .build();


        String formattedDate = installment.getFormattedDueDate();


        assertEquals("24/11/2024", formattedDate);
    }

    @Test
    void testInstallmentEntityBuilderAndAttributes() {

        LocalDate paymentDate = LocalDate.of(2024, 11, 10);
        LocalDate dueDate = LocalDate.of(2024, 12, 1);
        LoanEntity loan = new LoanEntity();
        loan.setId(1);


        InstallmentEntity installment = InstallmentEntity.builder()
                .id(100)
                .amount(500.75)
                .paid(true)
                .paymentDate(paymentDate)
                .dueDate(dueDate)
                .loan(loan)
                .build();


        assertNotNull(installment);
        assertEquals(100, installment.getId());
        assertEquals(500.75, installment.getAmount());
        assertTrue(installment.getPaid());
        assertEquals(paymentDate, installment.getPaymentDate());
        assertEquals(dueDate, installment.getDueDate());
        assertEquals(loan, installment.getLoan());
    }

    @Test
    void testInstallmentEntityDefaults() {

        InstallmentEntity installment = new InstallmentEntity();


        assertNull(installment.getId());
        assertNull(installment.getAmount());
        assertNull(installment.getPaid());
        assertNull(installment.getPaymentDate());
        assertNull(installment.getDueDate());
        assertNull(installment.getLoan());
    }
}
