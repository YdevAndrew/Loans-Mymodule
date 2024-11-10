package org.jala.university.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

import org.jala.university.application.dto.AccountDto;
import org.jala.university.application.dto.PaymentHistoryDTO;
import org.jala.university.application.mapper.AccountMapper;
import org.jala.university.domain.entity.Account;
import org.jala.university.domain.entity.AccountStatus;
import org.jala.university.domain.entity.LoanEntity;
import org.jala.university.domain.entity.enums.PaymentMethod;
import org.jala.university.domain.repository.LoanEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class LoanResultsServiceImplTest {

    @InjectMocks
    private LoanResultsServiceImpl loanResultsService;

    @Mock
    private PaymentHistoryService paymentHistoryService;

    @Mock
    private LoanEntityRepository loanEntityRepository;

    @Mock
    private LoanEntityService loanEntityService;

    @Mock
    private AccountService accountService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendAmountAccount_WithExistingAccount() {
        LoanEntity loanEntity = createTestLoanEntity();
        AccountDto existingAccount = createTestAccountDto();

        when(accountService.getAccount(anyInt())).thenReturn(existingAccount);
        when(paymentHistoryService.createPaymentHistory(any(PaymentHistoryDTO.class)))
                .thenReturn(createTestPaymentHistoryDTO(existingAccount));

        PaymentHistoryDTO result = loanResultsService.sendAmountAccount(loanEntity);

        assertNotNull(result);
        assertPaymentHistoryDTO(result, existingAccount);
        verify(paymentHistoryService, times(1)).createPaymentHistory(any(PaymentHistoryDTO.class));
        verify(accountService, never()).createAccount(any());
    }

    @Test
    void testSendAmountAccount_WithNonExistingAccount() {
        LoanEntity loanEntity = createTestLoanEntity();
        when(accountService.getAccount(anyInt())).thenReturn(null);
        AccountDto newAccount = createTestAccountDto();

        when(accountService.createAccount(any())).thenReturn(newAccount);
        when(paymentHistoryService.createPaymentHistory(any(PaymentHistoryDTO.class)))
                .thenReturn(createTestPaymentHistoryDTO(newAccount));

        PaymentHistoryDTO result = loanResultsService.sendAmountAccount(loanEntity);

        assertNotNull(result);
        assertPaymentHistoryDTO(result, newAccount);
        verify(accountService, times(1)).createAccount(any(AccountDto.class));
        verify(paymentHistoryService, times(1)).createPaymentHistory(any(PaymentHistoryDTO.class));
    }

    @Test
    void testPayInstallment_WithExistingAccount() {
        LoanEntity loanEntity = createTestLoanEntity();
        AccountDto accountBank = createTestAccountDto();

        when(accountService.getAccount(anyInt())).thenReturn(accountBank);
        when(paymentHistoryService.createPaymentHistory(any(PaymentHistoryDTO.class)))
                .thenReturn(createTestPaymentHistoryDTO(accountBank));

        PaymentHistoryDTO result = loanResultsService.payInstallment(loanEntity);

        assertNotNull(result);
        assertPaymentHistoryDTO(result, accountBank);
        verify(paymentHistoryService, times(1)).createPaymentHistory(any(PaymentHistoryDTO.class));
    }

    @Test
    void testPayInstallment_WithNonExistingAccount() {
        LoanEntity loanEntity = createTestLoanEntity();
        when(accountService.getAccount(anyInt())).thenReturn(null);
        AccountDto newAccount = createTestAccountDto();

        when(accountService.createAccount(any())).thenReturn(newAccount);
        when(paymentHistoryService.createPaymentHistory(any(PaymentHistoryDTO.class)))
                .thenReturn(createTestPaymentHistoryDTO(newAccount));

        PaymentHistoryDTO result = loanResultsService.payInstallment(loanEntity);

        assertNotNull(result);
        assertPaymentHistoryDTO(result, newAccount);
        verify(accountService, times(1)).createAccount(any(AccountDto.class));
        verify(paymentHistoryService, times(1)).createPaymentHistory(any(PaymentHistoryDTO.class));
    }

    @Test
    void testVerifyIfScheduled_PaymentAutoSchedule() {
        LoanEntity loanEntity = createTestLoanEntity();
        loanEntity.setPaymentMethod(PaymentMethod.DEBIT_ACCOUNT);

        loanResultsService.verifyIfScheduled(loanEntity);

        verify(loanEntityService, times(1)).findEntityById(anyInt());
        verify(loanEntityRepository, times(1)).save(any());
    }

    @Test
    void testVerifyAndUpdatePaidInstallments_SuccessfulProcess() {
        LoanEntity loan1 = createTestLoanEntity();
        LoanEntity loan2 = createTestLoanEntity();
        when(loanEntityRepository.findLoansScheduled(1, 1))
                .thenReturn(Arrays.asList(loan1, loan2));

        loanResultsService.verifyAndUpdatePaidInstallments();

        verify(loanEntityRepository, times(2)).save(any());
    }

    @Test
    void testSchedulePayment_SavesScheduledPaymentId() {
        LoanEntity loanEntity = createTestLoanEntity();
        when(loanEntityService.findEntityById(anyInt())).thenReturn(loanEntity);

        boolean result = loanResultsService.schedulePayment(loanEntity);

        assertTrue(result);
        verify(loanEntity).setScheduledPaymentId(1);
        verify(loanEntityRepository, times(1)).save(loanEntity);
    }

    @Test
    void testProcessLoanWithRetry_SuccessOnFirstTry() {
        LoanEntity loan = createTestLoanEntity();
        doNothing().when(loanResultsService).updateLoanPaidInstallments(loan);

        loanResultsService.processLoanWithRetry(loan, 3);

        verify(loanResultsService, times(1)).updateLoanPaidInstallments(loan);
    }

    @Test
    void testProcessLoanWithRetry_SuccessOnLastTry() {
        LoanEntity loan = createTestLoanEntity();
        doThrow(new RuntimeException())
                .doThrow(new RuntimeException())
                .doNothing()
                .when(loanResultsService).updateLoanPaidInstallments(loan);

        loanResultsService.processLoanWithRetry(loan, 3);

        verify(loanResultsService, times(3)).updateLoanPaidInstallments(loan);
    }

    @Test
    void testUpdateLoanPaidInstallments_MarkInstallmentsAsPaid() {
        LoanEntity loanEntity = createTestLoanEntity();
        when(loanEntity.getScheduledPaymentId()).thenReturn(1);
        when(loanEntity.getNumberOfPaidInstallments()).thenReturn(1L);
        when(loanEntityRepository.countPaymentsForLoan(1)).thenReturn(3L);

        loanResultsService.updateLoanPaidInstallments(loanEntity);

        verify(loanEntityRepository, times(1)).save(loanEntity);
    }

    @Test
    void testUpdateLoanPaidInstallments_NoNewInstallmentsPaid() {
        LoanEntity loanEntity = createTestLoanEntity();
        when(loanEntity.getScheduledPaymentId()).thenReturn(1);
        when(loanEntity.getNumberOfPaidInstallments()).thenReturn(3L);
        when(loanEntityRepository.countPaymentsForLoan(1)).thenReturn(3L);

        loanResultsService.updateLoanPaidInstallments(loanEntity);

        verify(loanEntityRepository, never()).save(any());
    }


    private LoanEntity createTestLoanEntity() {
        LoanEntity loanEntity = new LoanEntity();
        loanEntity.setId(1);
        loanEntity.setAmountBorrowed(1000.00);
        loanEntity.setValueOfInstallments(100.00);
        loanEntity.setPaymentMethod(PaymentMethod.DEBIT_ACCOUNT);
        loanEntity.setAccount(new Account());
        return loanEntity;
    }

    private AccountDto createTestAccountDto() {
        return AccountDto.builder()
                .id(12345)
                .accountNumber("12345")
                .balance(BigDecimal.valueOf(9999999999999999L))
                .status(AccountStatus.ACTIVE)
                .currency(org.jala.university.domain.entity.Currency.USD)
                .build();
    }

    private PaymentHistoryDTO createTestPaymentHistoryDTO(AccountDto account) {
        return PaymentHistoryDTO.builder()
                .id(1)
                .accountId(AccountMapper.toEntity(account))
                .amount(BigDecimal.valueOf(100.00))
                .transactionDate(LocalDateTime.now())
                .agencyReceiver("001")
                .accountReceiver("12345-6")
                .nameReceiver("Receiver Name")
                .bankNameReceiver("Bank Name")
                .build();
    }

    private void assertPaymentHistoryDTO(PaymentHistoryDTO result, AccountDto account) {
        assertEquals(account.getId(), result.getAccountId().getId());
        assertEquals("100.00", result.getAmount().toString());
        assertEquals("Loan applicant", result.getNameReceiver());
    }
}
