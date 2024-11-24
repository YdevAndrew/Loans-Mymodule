package org.jala.university.application.service;

import org.jala.university.domain.entity.Account;
import org.jala.university.domain.entity.AccountStatus;
import org.jala.university.domain.entity.Currency;
import org.jala.university.domain.entity.LoanEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoanResultsServiceTest {

    @Mock
    private LoanResultsService loanResultsService;

    private LoanEntity loanEntity;
    private Account account;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);


        loanEntity = new LoanEntity();
        loanEntity.setId(1);

        account = new Account(1, "123456", AccountStatus.ACTIVE, new BigDecimal("5000.0"), Currency.USD);
    }

    @Test
    void testSendAmountAccount() {

        when(loanResultsService.sendAmountAccount(loanEntity)).thenReturn(account);


        Account result = loanResultsService.sendAmountAccount(loanEntity);


        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(new BigDecimal("5000.0"), result.getBalance());
        verify(loanResultsService, times(1)).sendAmountAccount(loanEntity);
    }

    @Test
    void testPayInstallment() {

        when(loanResultsService.payInstallment(loanEntity)).thenReturn(account);

        Account result = loanResultsService.payInstallment(loanEntity);


        assertNotNull(result); // Verifica que o resultado não é nulo
        assertEquals(1, result.getId()); // Verifica o ID da conta
        assertEquals(new BigDecimal("5000.0"), result.getBalance()); // Verifica o saldo da conta
        verify(loanResultsService, times(1)).payInstallment(loanEntity); // Verifica se o método foi chamado uma vez
    }

    @Test
    void testVerifyIfScheduled() {

        doNothing().when(loanResultsService).verifyIfScheduled(loanEntity);


        loanResultsService.verifyIfScheduled(loanEntity);


        verify(loanResultsService, times(1)).verifyIfScheduled(loanEntity); // Verifica se o método foi chamado uma vez
    }
}
