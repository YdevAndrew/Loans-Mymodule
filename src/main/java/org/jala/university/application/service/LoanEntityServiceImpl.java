package org.jala.university.application.service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.jala.university.application.dto.FormEntityDto;
import org.jala.university.application.dto.LoanEntityDto;
import org.jala.university.application.mapper.FormEntityMapper;
import org.jala.university.application.mapper.LoanEntityMapper;
import org.jala.university.domain.entity.FormEntity;
import org.jala.university.domain.entity.LoanEntity;
import org.jala.university.domain.entity.enums.Status;
import org.jala.university.domain.repository.LoanEntityRepository;
import org.jala.university.infrastructure.persistance.RepositoryFactory;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LoanEntityServiceImpl implements LoanEntityService {
    private final LoanEntityRepository loanEntityRepository;
    private final LoanEntityMapper loanEntityMapper;
    private final FormEntityMapper formEntityMapper;
    private final TaskScheduler taskScheduler;

    public LoanEntityServiceImpl(RepositoryFactory factory, LoanEntityMapper loanEntityMapper, FormEntityMapper formEntityMapper, TaskScheduler taskScheduler) {
        this.loanEntityRepository = factory.createLoanEntityRepository();
        this.loanEntityMapper = loanEntityMapper;
        this.formEntityMapper = formEntityMapper;
        this.taskScheduler = taskScheduler;
    }

    @Override
    @Transactional(readOnly = true)
    public LoanEntityDto findById(UUID id) {
        LoanEntity entity = loanEntityRepository.findById(id);

        if (entity == null) {
            throw new IllegalArgumentException("Entity with ID " + id + " not found.");
        }
        return loanEntityMapper.mapTo(entity);
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
        //Quando juntar com o módulo Account
        //entity.setAccount(getLoggedAccount());
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
    public void deleteById(UUID id) {
        LoanEntity entity = loanEntityRepository.findById(id);

        if (entity == null) {
            throw new IllegalArgumentException("Entity with ID " + id + " not found.");
        } 

        loanEntityRepository.delete(entity);
    }

    @Override
    @Transactional
    public void delete(LoanEntityDto entityDto) {
        LoanEntity entity = loanEntityMapper.mapFrom(entityDto);

        if (loanEntityRepository.findById(entity.getId()) != null) {
            loanEntityRepository.delete(entity);
        } else {
            throw new IllegalArgumentException("Entity " + entityDto + " not found.");
        }
    }

    @Override
    @Transactional
    public LoanEntityDto update(UUID id, LoanEntityDto entityDto) {
        LoanEntity existingEntity = loanEntityRepository.findById(id);

        if (existingEntity == null) {
            throw new IllegalArgumentException("Entity with ID " + id + " not found.");
        }

        LoanEntity updatedEntity = loanEntityMapper.mapFrom(entityDto);
        updatedEntity.setId(id);

        LoanEntity savedEntity = loanEntityRepository.save(updatedEntity);
        return loanEntityMapper.mapTo(savedEntity);
    }

    //Recebe o id da conta e retorna os empréstimos da mesma.
    @Override
    public List<LoanEntityDto> findLoansByAccountId() {
        UUID id = UUID.randomUUID();/*retirar quando juntar os módulos */
        List<LoanEntity> loans = loanEntityRepository.findLoansByAccountId(/*getLoggedAccount().get*/id);/*Ajustar quando juntar módulos */
        return loans.stream()
                .map(loanEntityMapper::mapTo) // Converte cada entidade para DTO
                .toList();
    }  

    /*Recebe o FormDto e retorna o LoanEntity com o form associado, use ele
     * se estiver salvando o empréstimo no banco pela primeira vez, exemplo: 
     * loanWithForm = associateForm(loanEntityDto, formEntityDto);
     * loanEntityService.save(loanWithForm).
     */
    @Override
    public LoanEntity associateForm(LoanEntityDto loanDto, FormEntityDto formdto) {
        LoanEntity loanEntity = loanEntityMapper.mapFrom(loanDto);
        FormEntity formEntity = formEntityMapper.mapFrom(formdto);
        loanEntity.setForm(formEntity);
        return loanEntity;
    }

    private void scheduleStatusChange(LoanEntity loanEntity) {
        Instant startTime = Instant.now().plus(Duration.ofMinutes(2)); // 2 minutos no futuro
    
        taskScheduler.schedule(() -> changeStatusRandomly(loanEntity), startTime);
    }

    private void changeStatusRandomly(LoanEntity loanEntity) {
        Status newStatus = loanEntity.generateStatus();
        if (newStatus == Status.APPROVED) {
            sendAmountAccount();
            loanEntity.paymentMethodLogic();
        }

        loanEntity.setStatus(newStatus);
        loanEntityRepository.save(loanEntity);  // Atualiza o status no banco
    }

    private void sendAmountAccount() {
        //Lógica de transação para mandar o dinheiro para a conta
    }
    
    @Override
    public void markInstallmentAsPaid(LoanEntityDto dto) {
        LoanEntity entity = loanEntityMapper.mapFrom(dto);
        entity.markAsPaid();
        loanEntityRepository.save(entity);
    }

    @Override
    public long getPaidInstallments(LoanEntityDto dto) {
        LoanEntity entity = loanEntityMapper.mapFrom(dto);
        return entity.getNumberOfPaidInstallments();
    }
}
