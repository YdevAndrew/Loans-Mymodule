package org.jala.university.application.service;



import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import org.jala.university.application.dto.InstallmentEntityDto;
import org.jala.university.application.dto.LoanEntityDto;
import org.jala.university.application.mapper.FormEntityMapper;
import org.jala.university.application.mapper.InstallmentEntityMapper;
import org.jala.university.application.mapper.LoanEntityMapper;
import org.jala.university.domain.entity.Account;
import org.jala.university.domain.entity.InstallmentEntity;
import org.jala.university.domain.entity.LoanEntity;
import org.jala.university.domain.entity.enums.Status;
import org.jala.university.domain.repository.AccountRepository;
import org.jala.university.domain.repository.LoanEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;

/**
 * This class implements the {@link LoanEntityService} interface, providing concrete
 * implementations for managing {@link LoanEntity} objects. It handles operations
 * such as creating, retrieving, updating, deleting loan entities, and managing
 * loan installments and related information.
 */
@Service
public class LoanEntityServiceImpl implements LoanEntityService {

    /**
     * The repository for managing {@link LoanEntity} objects.
     */
    @Autowired
    private LoanEntityRepository loanEntityRepository;

    /**
     * The mapper for converting between {@link LoanEntity} and {@link LoanEntityDto} objects.
     */
    private final LoanEntityMapper loanEntityMapper;

    /**
     * The mapper for converting between {@link InstallmentEntity} and {@link InstallmentEntityDto} objects.
     */
    private final InstallmentEntityMapper installmentEntityMapper;

    /**
     * The task scheduler for scheduling tasks related to loans.
     */
    private final TaskScheduler taskScheduler;

    /**
     * The repository for managing {@link Account} objects.
     */
    @Autowired
    private AccountRepository accountRepository;

    /**
     * The service for handling loan results and related actions.
     */
    @Autowired
    private LoanResultsService loanResultsService;

    /**
     * The service for managing {@link org.jala.university.domain.entity.FormEntity} objects.
     */
    @Autowired
    private FormEntityService formEntityService;

    /**
     * The entity manager for handling persistent entities.
     */
    @Autowired
    private EntityManager entityManager;

    /**
     * Constructor for the LoanEntityServiceImpl class.
     *
     * @param loanEntityMapper        The loan entity mapper instance.
     * @param installmentEntityMapper The installment entity mapper instance.
     * @param formEntityMapper        The form entity mapper instance.
     * @param taskScheduler           The task scheduler instance.
     */
    public LoanEntityServiceImpl(final LoanEntityMapper loanEntityMapper,
                                 final InstallmentEntityMapper installmentEntityMapper,
                                 final FormEntityMapper formEntityMapper,
                                 final TaskScheduler taskScheduler) {
        this.loanEntityMapper = loanEntityMapper;
        this.installmentEntityMapper = installmentEntityMapper;
        this.taskScheduler = taskScheduler;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public LoanEntityDto findById(final Integer id) {
        LoanEntity entity = loanEntityRepository.findById(id).orElse(null);

        if (entity == null) {
            throw new IllegalArgumentException("Entity with ID " + id + " not found.");
        }
        return loanEntityMapper.mapTo(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public LoanEntity findEntityById(final Integer id) {
        LoanEntity entity = loanEntityRepository.findById(id).orElse(null);
        if (entity == null) {
            throw new IllegalArgumentException("FormEntity not found with ID: " + id);
        }
        return entityManager.merge(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<LoanEntityDto> findAll() {
        return loanEntityRepository.findAll().stream()
                .map(loanEntityMapper::mapTo)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public LoanEntityDto save(final LoanEntityDto entityDto) {
        LoanEntity entity = loanEntityMapper.mapFrom(entityDto);
        entity.recalculate();
        entity.generateInstallments();
        entity.generateAndSetDate();
        entity.setForm(formEntityService.findEntityById(entity.getForm().getId()));
        loanResultsService.verifyIfScheduled(entity);
        // When integrating with the Account module
        entity.setAccount(accountRepository.findById(/*getLoggedAccount().getId())*/ 1).orElse(null));
        if (entity.getStatus() == null) {
            entity.setStatus(entity.generateStatus());
        }
        LoanEntity savedEntity = loanEntityRepository.save(entity);

        if (savedEntity.getStatus().getCode() == Status.REVIEW.getCode()) {
            scheduleStatusChange(savedEntity);
        }

        return loanEntityMapper.mapTo(savedEntity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void deleteById(final Integer id) {
        LoanEntity entity = loanEntityRepository.findById(id).orElse(null);

        if (entity == null) {
            throw new IllegalArgumentException("Entity with ID " + id + " not found.");
        }

        loanEntityRepository.delete(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void delete(final LoanEntityDto entityDto) {
        LoanEntity entity = loanEntityMapper.mapFrom(entityDto);
        LoanEntity foundEntity = loanEntityRepository.findById(entity.getId()).orElse(null);

        if (foundEntity == null) {
            throw new IllegalArgumentException("Entity " + entityDto + " not found.");
        }
        loanEntityRepository.delete(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public LoanEntityDto update(final LoanEntityDto entityDto) {
        LoanEntity existingEntity = loanEntityRepository.findById(entityDto.getId()).orElse(null);

        if (existingEntity == null) {
            throw new IllegalArgumentException("Entity with ID " + entityDto.getId() + " not found.");
        }

        LoanEntity updatedEntity = loanEntityMapper.mapFrom(entityDto);
        updatedEntity.setId(entityDto.getId());

        LoanEntity savedEntity = loanEntityRepository.save(updatedEntity);
        return loanEntityMapper.mapTo(savedEntity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<LoanEntityDto> findLoansByAccountId() {
        Integer id = 1; // Adjust when integrating with the Account module to use the logged-in account ID
        List<LoanEntity> loans = loanEntityRepository.findByAccountId(id);
        loans.forEach(LoanEntity::updateStatusFinished);
        return loans.stream()
                .map(loanEntityMapper::mapTo)
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Account payInstallmentManually(final LoanEntityDto dto) {
        LoanEntity entity = findEntityById(dto.getId());
        Account account = loanResultsService.payInstallment(entity);
        if (account != null) {
            entity.markAsPaid();
            loanEntityRepository.save(entity);
        }
        return account;
    }

    /**
     * Schedules a task to change the status of a loan entity after a certain delay.
     *
     * @param loanEntity The loan entity to schedule the status change for.
     */
    private void scheduleStatusChange(final LoanEntity loanEntity) {
        Instant startTime = Instant.now().plus(Duration.ofSeconds(30)); // 2 minutes in the future

        taskScheduler.schedule(() -> changeStatusRandomly(loanEntity), startTime);
    }

    /**
     * Changes the status of a loan entity randomly and performs
     * additional actions based on the new status.
     *
     * @param loanEntity The loan entity to change the status for.
     */
    @Transactional
    private void changeStatusRandomly(final LoanEntity loanEntity) {
        Status newStatus = loanEntity.generateStatus();
        loanEntity.setStatus(newStatus);
        loanEntityRepository.save(loanEntity);

        if (newStatus == Status.APPROVED) {
            loanResultsService.sendAmountAccount(loanEntity);
            loanResultsService.verifyIfScheduled(loanEntity);
        }
    }

    /**
     * Adjusts the amount of overdue installments for loans with a
     * specific payment method.
     * This method is scheduled to run daily at midnight.
     */
    @Scheduled(cron = "0 0 0 * * ?") // Executa diariamente à meia-noite
    @Transactional
    void adjustOverdueInstallments() {
        // MagicNumber: Replace with constant or enum
        List<LoanEntity> loans = loanEntityRepository
                .findByStatusPaymentMethod(1, 2);

        for (LoanEntity loan : loans) {
            for (InstallmentEntity installment : loan.getInstallments()) {

                if (installment.getDueDate().isBefore(LocalDate.now())
                        && !installment.getPaid()) {

                    long daysOverdue = ChronoUnit.DAYS.between(
                            installment.getDueDate(), LocalDate.now());
                    double originalAmount = loan.getValueOfInstallments();
                    // MagicNumber: Replace with constant
                    double updatedAmount = originalAmount
                            + (originalAmount * 0.01 * daysOverdue);
                    //1% per day

                    installment.setAmount(updatedAmount);
                }
            }
        }

        loanEntityRepository.saveAll(loans);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public long getPaidInstallments(final LoanEntityDto dto) {
        LoanEntity entity = findEntityById(dto.getId());
        return entity.getNumberOfPaidInstallments();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double getOutstandingBalance(final Integer loanId) {
        Double outstandingBalance = loanEntityRepository
                .getOutstandingBalance(loanId);
        return outstandingBalance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InstallmentEntityDto getFirstUnpaidInstallment(
            final LoanEntityDto dto) {
        LoanEntity entity = loanEntityMapper.mapFrom(dto);
        if (entity == null || entity.getFirstUnpaidInstallment() == null) {
            return null;
        }
        return installmentEntityMapper
                .mapTo(entity.getFirstUnpaidInstallment());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalDate getFirstUnpaidInstallmentDate(
            final LoanEntityDto loan) {
        List<InstallmentEntity> installments = loan.getInstallments();

        return installments.stream()
                .filter(installment -> installment.getPaymentDate() == null)
                .map(InstallmentEntity::getDueDate)
                .findFirst()
                .orElse(null);
    }
}
