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

@Service
public class LoanEntityServiceImpl implements LoanEntityService {

    @Autowired
    private LoanEntityRepository loanEntityRepository;
    private final LoanEntityMapper loanEntityMapper;
    private final InstallmentEntityMapper installmentEntityMapper;
    private final TaskScheduler taskScheduler;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    LoanResultsService loanResultsService;

    @Autowired
    private FormEntityService formEntityService;

    @Autowired
    private EntityManager entityManager;

    public LoanEntityServiceImpl(LoanEntityMapper loanEntityMapper, InstallmentEntityMapper installmentEntityMapper, FormEntityMapper formEntityMapper, TaskScheduler taskScheduler) {
        this.loanEntityMapper = loanEntityMapper;
        this.installmentEntityMapper = installmentEntityMapper;
        this.taskScheduler = taskScheduler;
    }

    @Override
    @Transactional(readOnly = true)
    public LoanEntityDto findById(Integer id) {
        LoanEntity entity = loanEntityRepository.findById(id).orElse(null);

        if (entity == null) {
            throw new IllegalArgumentException("Entity with ID " + id + " not found.");
        }
        return loanEntityMapper.mapTo(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public LoanEntity findEntityById(Integer id) {
        LoanEntity entity = loanEntityRepository.findById(id).orElse(null);
        if (entity == null) {
            throw new IllegalArgumentException("FormEntity not found with ID: " + id);
        }
        return entityManager.merge(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LoanEntityDto> findAll() {
        return loanEntityRepository.findAll().stream()
               .map(loanEntityMapper::mapTo)
               .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public LoanEntityDto save(LoanEntityDto entityDto) {
        LoanEntity entity = loanEntityMapper.mapFrom(entityDto);
        entity.recalculate();
        entity.generateInstallments();
        entity.generateAndSetDate();
        entity.setForm(formEntityService.findEntityById(entity.getForm().getId()));
        loanResultsService.verifyIfScheduled(entity);
        //Quando juntar com o módulo Account
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

    @Override
    @Transactional
    public void deleteById(Integer id) {
        LoanEntity entity = loanEntityRepository.findById(id).orElse(null);

        if (entity == null) {
            throw new IllegalArgumentException("Entity with ID " + id + " not found.");
        } 

        loanEntityRepository.delete(entity);
    }

    @Override
    @Transactional
    public void delete(LoanEntityDto entityDto) {
        LoanEntity entity = loanEntityMapper.mapFrom(entityDto);
        LoanEntity foundEntity = loanEntityRepository.findById(entity.getId()).orElse(null);
        
        if (foundEntity == null) {
            throw new IllegalArgumentException("Entity " + entityDto + " not found.");
        }
        loanEntityRepository.delete(entity);
    }

    @Override
    @Transactional
    public LoanEntityDto update(LoanEntityDto entityDto) {
        LoanEntity existingEntity = loanEntityRepository.findById(entityDto.getId()).orElse(null);

        if (existingEntity == null) {
            throw new IllegalArgumentException("Entity with ID " + entityDto.getId() + " not found.");
        }

        LoanEntity updatedEntity = loanEntityMapper.mapFrom(entityDto);
        updatedEntity.setId(entityDto.getId());

        LoanEntity savedEntity = loanEntityRepository.save(updatedEntity);
        return loanEntityMapper.mapTo(savedEntity);
    }

    //Recebe o id da conta e retorna os empréstimos da mesma.
    @Override
    @Transactional(readOnly = true)
    public List<LoanEntityDto> findLoansByAccountId() {
        Integer id = 1; //Ajustar quando juntar módulos para o id da conta logada
        //List<LoanEntity> loans = loanEntityRepository.findByAccountId(id); 
        List<LoanEntity> loans = null;
        loans.forEach(LoanEntity::updateStatusFinished);
        return loans.stream()
                .map(loanEntityMapper::mapTo) // Converte cada entidade para DTO
                .toList();
    }

    @Override
    @Transactional
    public boolean payInstallmentManually(LoanEntityDto dto) {
        LoanEntity entity = findEntityById(dto.getId());
        if (loanResultsService.payInstallment(entity) != null) {
            entity.markAsPaid();
            loanEntityRepository.save(entity);
            return true;
        }
        return false;
    }

    private void scheduleStatusChange(LoanEntity loanEntity) {
        Instant startTime = Instant.now().plus(Duration.ofSeconds(2)); // 2 minutos no futuro
    
        taskScheduler.schedule(() -> changeStatusRandomly(loanEntity), startTime);
    }

    @Transactional
    private void changeStatusRandomly(LoanEntity loanEntity) {
        Status newStatus = loanEntity.generateStatus();
        loanEntity.setStatus(newStatus);
        loanEntityRepository.save(loanEntity);

        if (newStatus == Status.APPROVED) {
            loanResultsService.sendAmountAccount(loanEntity);
            loanResultsService.verifyIfScheduled(loanEntity);
        }
    }

    @Scheduled(cron = "0 0 0 * * ?") // Executa diariamente à meia-noite
    @Transactional
    void adjustOverdueInstallments() {
        List<LoanEntity> loans = loanEntityRepository.findByStatusPaymentMethod(1, 2);
        
        for (LoanEntity loan : loans) {
            for (InstallmentEntity installment : loan.getInstallments()) {
                
                if (installment.getDueDate().isBefore(LocalDate.now()) && !installment.getPaid()) {
                    
                    long daysOverdue = ChronoUnit.DAYS.between(installment.getDueDate(), LocalDate.now());
                    double originalAmount = loan.getValueOfInstallments();
                    double updatedAmount = originalAmount + (originalAmount * 0.01 * daysOverdue); //1% per day
                    
                    installment.setAmount(updatedAmount);
                }
            }
        }
        
        loanEntityRepository.saveAll(loans);
    }

    @Override
    public long getPaidInstallments(LoanEntityDto dto) {
        LoanEntity entity = loanEntityMapper.mapFrom(dto);
        return entity.getNumberOfPaidInstallments();
    }

    // Retorna o quanto falta a pagar (Outstanding Balance)
    public Double getOutstandingBalance(Integer loanId) {
        return loanEntityRepository.getOutstandingBalance(loanId);
    }

    public InstallmentEntityDto getFirstUnpaidInstallment(LoanEntityDto dto) {
        LoanEntity entity = loanEntityMapper.mapFrom(dto);
        return installmentEntityMapper.mapTo(entity.getFirstUnpaidInstallment());
    }

    public LocalDate getFirstUnpaidInstallmentDate(LoanEntityDto dto) {
        LoanEntity entity = loanEntityMapper.mapFrom(dto);
        return entity.getFirstUnpaidInstallment().getDueDate();
    }
}
