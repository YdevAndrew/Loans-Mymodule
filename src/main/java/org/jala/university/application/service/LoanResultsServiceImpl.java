package org.jala.university.application.service;

import java.util.List;

import org.jala.university.domain.entity.LoanEntity;
import org.jala.university.domain.entity.enums.PaymentMethod;
import org.jala.university.domain.repository.LoanEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class LoanResultsServiceImpl implements LoanResultsService {

    // @Autowired
    // private PaymentHistoryService paymentHistoryService;

    @Autowired
    private LoanEntityRepository loanEntityRepository;

    @Autowired
    @Lazy
    private LoanEntityService loanEntityService;

    // @Autowired
    // private AccountService accountService;

    // Chama o método de transferir e guardar histórico do módulo de transferência.
    // Para mandar o dinheiro emprestado para a conta.
    // @Override
    // public PaymentHistoryDTO sendAmountAccount(LoanEntity loanEntity) {

    //     Integer bankId = 12345;
    //     AccountDto accountBank = accountService.getAccount(bankId);
    //     if (accountBank == null) {
    //         AccountDto accountDto = AccountDto.builder()
    //                 .id(bankId)
    //                 .accountNumber("12345")
    //                 .balance(BigDecimal.valueOf(9999999999999999L))
    //                 .status(AccountStatus.ACTIVE)
    //                 .currency(org.jala.university.domain.entity.Currency.fromCode("USD"))
    //                 .build();
    //         accountBank = accountService.createAccount(accountDto);
    //     }

    //     PaymentHistoryDTO paymentHistoryDTO = PaymentHistoryDTO.builder()
    //             .accountId(AccountMapper.toEntity(accountBank))
    //             .amount(BigDecimal.valueOf(loanEntity.getAmountBorrowed()))
    //             .accountReceiver(loanEntity.getAccount().getAccountNumber())
    //             .transactionDate(LocalDateTime.now())
    //             .agencyReceiver("This")
    //             .nameReceiver("Loan applicant")
    //             .bankNameReceiver("This")
    //             .build();

    //     return paymentHistoryService.createPaymentHistory(paymentHistoryDTO);
    //     return null;
    // }

    // // Chama o método de transferir e guardar histórico do módulo de transferência.
    // // Para pagar a parcela manualmente.
    // @Override
    // public PaymentHistoryDTO payInstallment(LoanEntity loanEntity) {

    //     Integer bankId = 12345;
    //     AccountDto accountBank = accountService.getAccount(bankId);
    //     if (accountBank == null) {
    //         AccountDto accountDto = AccountDto.builder()
    //                 .id(bankId)
    //                 .accountNumber("12345")
    //                 .balance(BigDecimal.valueOf(9999999999999999L))
    //                 .status(AccountStatus.ACTIVE)
    //                 .currency(org.jala.university.domain.entity.Currency.fromCode("USD"))
    //                 .build();
    //         accountBank = accountService.createAccount(accountDto);
    //     }

    //     PaymentHistoryDTO paymentHistoryDTO = PaymentHistoryDTO.builder()
    //             .accountId(loanEntity.getAccount())
    //             .amount(BigDecimal.valueOf(loanEntity.getValueOfInstallments()))
    //             .accountReceiver(accountBank.getAccountNumber())
    //             .transactionDate(LocalDateTime.now())
    //             .agencyReceiver("This")
    //             .nameReceiver("Loan applicant")
    //             .bankNameReceiver("This")
    //             .build();

    //     return paymentHistoryService.createPaymentHistory(paymentHistoryDTO);
    //     return null;
    // }

    // Verifica se o método de pagamento é o automático e chama o agendamento.
    @Override
    public void verifyIfScheduled(LoanEntity loanEntity) {
        if (loanEntity.getPaymentMethod().getCode() == PaymentMethod.DEBIT_ACCOUNT.getCode()) {
            schedulePayment(loanEntity);
        }
    }

    // Chama o método de pagamento agendado.
    // O método ainda não está com a lógica completa porque o pagamento
    // agendado ainda não foi feito no módulo de pagamentos externos.
    @Transactional
    boolean schedulePayment(LoanEntity loanEntity) {
        // chamar o método do pagamento agendado para criar o agendamento.
        if (/*método != null*/true) {
            //colocar o id que o método retorna
            Integer id = 1;
            LoanEntity entity = loanEntityService.findEntityById(loanEntity.getId());
            entity.setScheduledPaymentId(id);
            loanEntityRepository.save(entity);
            return true;
        }
        return false;
    }

    // Agendamento da verificação a cada 24 horas
    @Scheduled(fixedRate = 86400000)
    void verifyAndUpdatePaidInstallments() {
        List<LoanEntity> loansInReview = loanEntityRepository.findByStatusPaymentMethod(1, 1);

        for (LoanEntity loan : loansInReview) {
            processLoanWithRetry(loan, 3);
        }
    }

    // Tenta processar o empréstimo em caso de falha
    void processLoanWithRetry(LoanEntity loan, int retryCount) {
        for (int attempt = 1; attempt <= retryCount; attempt++) {
            try {
                updateLoanPaidInstallments(loan);
                return;
            } catch (Exception e) {
                System.out.println("Failed processing loan: " + loan.getId() + " - tried: " + attempt + "times");
                if (attempt == retryCount) {
                    System.out.println("Failed processing loan" + loan.getId() + " tried: " + retryCount
                            + " times. Stopping try.");
                }
            }
        }
    }

    // Método para atualizar as parcelas pagas de um empréstimo
    @Transactional 
    void updateLoanPaidInstallments(LoanEntity loanEntity) {
        // Integer scheduledPaymentId = loanEntity.getScheduledPaymentId();
        // long payments = loanEntityRepository.countPaymentsForLoan(scheduledPaymentId);
        // long paidInstallments = loanEntity.getNumberOfPaidInstallments();
        
        // if (payments > paidInstallments) {
        //     long installmentsToMarkAsPaid = payments - paidInstallments;
        //     LoanEntity loan = loanEntityService.findEntityById(loanEntity.getId());
        //     loan.markInstallmentsAsPaid(installmentsToMarkAsPaid);
        //     loanEntityRepository.save(loan);
        // }
    }
}
