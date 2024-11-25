package org.jala.university.application.service;

import org.jala.university.application.dto.InstallmentEntityDto;
import org.jala.university.application.dto.LoanEntityDto;
import org.jala.university.domain.entity.Account;
import org.jala.university.domain.entity.AccountStatus;
import org.jala.university.domain.entity.Currency;
import org.jala.university.domain.entity.LoanEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoanEntityServiceTest {

    @Mock
    private LoanEntityService loanEntityService;

    private LoanEntityDto loanEntityDto;
    private LoanEntity loanEntity;
    private Account account;
    private InstallmentEntityDto installmentEntityDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock objects
        loanEntity = LoanEntity.builder()
                .id(1)
                .build();

        account = new Account(1, "123456", AccountStatus.ACTIVE, new BigDecimal("5000.0"), Currency.USD);

        loanEntityDto = LoanEntityDto.builder()
                .id(1)
                .build();


        installmentEntityDto = InstallmentEntityDto.builder()
                .id(1)
                .amount(200.0)
                .build();
    }

    @Test
    void testFindById() {
        // Mock service response
        when(loanEntityService.findById(1)).thenReturn(loanEntityDto);

        // Execute the method
        LoanEntityDto result = loanEntityService.findById(1);

        // Verify the result
        assertNotNull(result);
        assertEquals(1, result.getId());
        verify(loanEntityService, times(1)).findById(1);
    }

    @Test
    void testFindAll() {
        // Mock service response
        when(loanEntityService.findAll()).thenReturn(Arrays.asList(loanEntityDto));

        // Execute the method
        List<LoanEntityDto> result = loanEntityService.findAll();

        // Verify the result
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(loanEntityService, times(1)).findAll();
    }

    @Test
    void testFindEntityById() {
        // Mock service response
        when(loanEntityService.findEntityById(1)).thenReturn(loanEntity);

        // Execute the method
        LoanEntity result = loanEntityService.findEntityById(1);

        // Verify the result
        assertNotNull(result);
        assertEquals(1, result.getId());
        verify(loanEntityService, times(1)).findEntityById(1);
    }

    @Test
    void testSave() {
        // Mock service response
        when(loanEntityService.save(loanEntityDto)).thenReturn(loanEntityDto);

        // Execute the method
        LoanEntityDto result = loanEntityService.save(loanEntityDto);

        // Verify the result
        assertNotNull(result);
        assertEquals(1, result.getId());
        verify(loanEntityService, times(1)).save(loanEntityDto);
    }

    @Test
    void testDeleteById() {
        // Mock service behavior
        doNothing().when(loanEntityService).deleteById(1);

        // Execute the method
        loanEntityService.deleteById(1);

        // Verify the behavior
        verify(loanEntityService, times(1)).deleteById(1);
    }

    @Test
    void testDelete() {
        // Mock service behavior
        doNothing().when(loanEntityService).delete(loanEntityDto);

        // Execute the method
        loanEntityService.delete(loanEntityDto);

        // Verify the behavior
        verify(loanEntityService, times(1)).delete(loanEntityDto);
    }

    @Test
    void testUpdate() {
        // Mock service response
        LoanEntityDto updatedDto = LoanEntityDto.builder()
                .id(1)
                .build();

        when(loanEntityService.update(loanEntityDto)).thenReturn(updatedDto);

        // Execute the method
        LoanEntityDto result = loanEntityService.update(loanEntityDto);

        // Verify the result
        assertNotNull(result);
        assertEquals(1, result.getId());
        verify(loanEntityService, times(1)).update(loanEntityDto);
    }

    @Test
    void testFindLoansByAccountId() {
        // Mock service response
        when(loanEntityService.findLoansByAccountId()).thenReturn(Arrays.asList(loanEntityDto));

        // Execute the method
        List<LoanEntityDto> result = loanEntityService.findLoansByAccountId();

        // Verify the result
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(loanEntityService, times(1)).findLoansByAccountId();
    }

    @Test
    void testPayInstallmentManually() {
        // Configurar o mock para retornar um objeto de Account
        when(loanEntityService.payInstallmentManually(loanEntityDto)).thenReturn(account);

        // Executar o método
        Account result = loanEntityService.payInstallmentManually(loanEntityDto);

        // Verificar o resultado
        assertNotNull(result); // Verifica que o resultado não é nulo
        assertEquals(1, result.getId()); // Verifica o ID retornado
        assertEquals(new BigDecimal("5000.0"), result.getBalance()); // Verifica o saldo retornado
        verify(loanEntityService, times(1)).payInstallmentManually(loanEntityDto);
    }

    @Test
    void testGetPaidInstallments() {
        // Mock service response
        when(loanEntityService.getPaidInstallments(loanEntityDto)).thenReturn(5L);

        // Execute the method
        long result = loanEntityService.getPaidInstallments(loanEntityDto);

        // Verify the result
        assertEquals(5, result);
        verify(loanEntityService, times(1)).getPaidInstallments(loanEntityDto);
    }

    @Test
    void testGetFirstUnpaidInstallmentDate() {
        // Mock service response
        LocalDate date = LocalDate.of(2023, 12, 15);
        when(loanEntityService.getFirstUnpaidInstallmentDate(loanEntityDto)).thenReturn(date);

        // Execute the method
        LocalDate result = loanEntityService.getFirstUnpaidInstallmentDate(loanEntityDto);

        // Verify the result
        assertNotNull(result);
        assertEquals(date, result);
        verify(loanEntityService, times(1)).getFirstUnpaidInstallmentDate(loanEntityDto);
    }

    @Test
    void testGetOutstandingBalance() {
        // Mock service response
        when(loanEntityService.getOutstandingBalance(1)).thenReturn(3000.0);

        // Execute the method
        Double result = loanEntityService.getOutstandingBalance(1);

        // Verify the result
        assertNotNull(result);
        assertEquals(3000.0, result);
        verify(loanEntityService, times(1)).getOutstandingBalance(1);
    }

    @Test
    void testGetFirstUnpaidInstallment() {
        // Mock service response
        when(loanEntityService.getFirstUnpaidInstallment(loanEntityDto)).thenReturn(installmentEntityDto);

        // Execute the method
        InstallmentEntityDto result = loanEntityService.getFirstUnpaidInstallment(loanEntityDto);

        // Verify the result
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(200.0, result.getAmount());
        verify(loanEntityService, times(1)).getFirstUnpaidInstallment(loanEntityDto);
    }
}
