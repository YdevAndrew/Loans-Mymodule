package org.jala.university.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Configuration;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.jala.university.application.dto.LoanEntityDto;
import org.jala.university.application.mapper.LoanEntityMapper;
import org.jala.university.application.service.LoanEntityServiceImpl;
import org.jala.university.domain.entity.LoanEntity;
import org.jala.university.domain.entity.enums.PaymentMethod;
import org.jala.university.domain.entity.enums.Status;
import org.jala.university.domain.repository.LoanEntityRepository;
import org.jala.university.infrastructure.persistance.RepositoryFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Configuration
class LoanEntityServiceImplTest {

    @Mock
    private LoanEntityRepository loanEntityRepository;

    @Mock
    private LoanEntityMapper loanEntityMapper;

    @Mock
    private RepositoryFactory repositoryFactory;

    @InjectMocks
    private LoanEntityServiceImpl loanEntityService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); 
        when(repositoryFactory.createLoanEntityRepository()).thenReturn(loanEntityRepository);
        loanEntityService = new LoanEntityServiceImpl(repositoryFactory, loanEntityMapper);
    }

    @Test
    void testFindById_Success() {
        UUID id = UUID.randomUUID();
        LoanEntity entity = new LoanEntity();

        when(loanEntityRepository.findById(id)).thenReturn(entity);
        when(loanEntityMapper.mapTo(entity)).thenReturn(createSampleDto());

        LoanEntityDto result = loanEntityService.findById(id);

        assertNotNull(result);
        verify(loanEntityRepository).findById(id);
    }

    @Test
    void testFindById_NotFound() {
        UUID id = UUID.randomUUID();
        when(loanEntityRepository.findById(id)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> loanEntityService.findById(id));
    }

    @Test
    void testFindAll() {
        LoanEntity entity = new LoanEntity();

        when(loanEntityRepository.findAll()).thenReturn(List.of(entity));
        when(loanEntityMapper.mapTo(entity)).thenReturn(createSampleDto());

        List<LoanEntityDto> result = loanEntityService.findAll();

        assertEquals(1, result.size());
        verify(loanEntityRepository).findAll();
    }

    @Test
    void testSave() {
        LoanEntityDto dto = createSampleDto();
        LoanEntity entity = new LoanEntity();

        when(loanEntityMapper.mapFrom(dto)).thenReturn(entity);
        when(loanEntityRepository.save(entity)).thenReturn(entity);
        when(loanEntityMapper.mapTo(entity)).thenReturn(dto);

        LoanEntityDto result = loanEntityService.save(dto);

        assertNotNull(result);
        verify(loanEntityRepository).save(entity);
    }

    @Test
    void testDeleteById_Success() {
        UUID id = UUID.randomUUID();
        LoanEntity entity = new LoanEntity();

        when(loanEntityRepository.findById(id)).thenReturn(entity);

        loanEntityService.deleteById(id);

        verify(loanEntityRepository).delete(entity);
    }

    @Test
    void testDeleteById_NotFound() {
        UUID id = UUID.randomUUID();
        when(loanEntityRepository.findById(id)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> loanEntityService.deleteById(id));
    }

    @Test
    void testDelete_Success() {
        LoanEntityDto dto = createSampleDto();
        LoanEntity entity = new LoanEntity();

        when(loanEntityMapper.mapFrom(dto)).thenReturn(entity);
        when(loanEntityRepository.findById(entity.getId())).thenReturn(entity);

        loanEntityService.delete(dto);

        verify(loanEntityRepository).delete(entity);
    }

    @Test
    void testDelete_NotFound() {
        LoanEntityDto dto = createSampleDto();
        LoanEntity entity = new LoanEntity();

        when(loanEntityMapper.mapFrom(dto)).thenReturn(entity);
        when(loanEntityRepository.findById(entity.getId())).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> loanEntityService.delete(dto));
    }

    @Test
    void testUpdate_Success() {
        UUID id = UUID.randomUUID();
        LoanEntityDto dto = createSampleDto();
        LoanEntity existingEntity = new LoanEntity();
        LoanEntity updatedEntity = new LoanEntity();

        when(loanEntityRepository.findById(id)).thenReturn(existingEntity);
        when(loanEntityMapper.mapFrom(dto)).thenReturn(updatedEntity);
        when(loanEntityRepository.save(updatedEntity)).thenReturn(updatedEntity);
        when(loanEntityMapper.mapTo(updatedEntity)).thenReturn(dto);

        LoanEntityDto result = loanEntityService.update(id, dto);

        assertNotNull(result);
        verify(loanEntityRepository).save(updatedEntity);
    }

    @Test
    void testUpdate_NotFound() {
        UUID id = UUID.randomUUID();
        LoanEntityDto dto = createSampleDto();

        when(loanEntityRepository.findById(id)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> loanEntityService.update(id, dto));
    }

    private LoanEntityDto createSampleDto() {
        return LoanEntityDto.builder()
            .id(UUID.randomUUID())
            .maximumAmount(10000.0)
            .amountBorrowed(5000.0)
            .totalInterest(500.0)
            .numberOfInstallments(12)
            .valueOfInstallments(450.0)
            .paymentMethod(PaymentMethod.TICKET)
            .status(Status.APPROVED)
            .issueDate(new Date())
            .installmentsDueDate(new Date())
            .loanDueDate(new Date())
            .build();
    }
}