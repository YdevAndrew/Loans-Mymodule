package org.jala.university.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;

import org.jala.university.application.dto.LoanEntityDto;
import org.jala.university.application.mapper.LoanEntityMapper;
import org.jala.university.domain.entity.LoanEntity;
import org.jala.university.domain.repository.LoanEntityRepository;
import org.jala.university.infrastructure.persistance.RepositoryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.scheduling.TaskScheduler;

class LoanEntityServiceTest {

    @Mock
    private LoanEntityRepository loanEntityRepository;

    @Mock
    private LoanEntityMapper loanEntityMapper;

    @Mock
    private RepositoryFactory repositoryFactory;

    private LoanEntityService loanEntityService;

    @Mock
    private TaskScheduler taskScheduler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(repositoryFactory.createLoanEntityRepository()).thenReturn(loanEntityRepository);
        loanEntityService = new LoanEntityServiceImpl(repositoryFactory, loanEntityMapper, taskScheduler);
    }

    @Test
    void testFindById_Success() {
        UUID id = UUID.randomUUID();
        LoanEntity entity = LoanEntity.builder().id(id).build();
        LoanEntityDto dto = LoanEntityDto.builder().id(id).build();

        when(loanEntityRepository.findById(id)).thenReturn(entity);
        when(loanEntityMapper.mapTo(entity)).thenReturn(dto);

        LoanEntityDto result = loanEntityService.findById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(loanEntityRepository).findById(id);
    }

    @Test
    void testFindById_NotFound() {
        UUID id = UUID.randomUUID();
        when(loanEntityRepository.findById(id)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> loanEntityService.findById(id));
    }

    @Test
    void testFindById_NullId() {
        assertThrows(IllegalArgumentException.class, () -> loanEntityService.findById(null));
    }

    @Test
    void testFindAll() {
        LoanEntity entity = LoanEntity.builder().id(UUID.randomUUID()).build();
        LoanEntityDto dto = LoanEntityDto.builder().id(entity.getId()).build();

        when(loanEntityRepository.findAll()).thenReturn(List.of(entity));
        when(loanEntityMapper.mapTo(entity)).thenReturn(dto);

        List<LoanEntityDto> result = loanEntityService.findAll();

        assertEquals(1, result.size());
        assertEquals(entity.getId(), result.get(0).getId());
        verify(loanEntityRepository).findAll();
    }

    @Test
    void testSave() {
        LoanEntityDto dto = createLoanDto();
        LoanEntity entity = LoanEntity.builder().build();

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
        LoanEntity entity = LoanEntity.builder().id(id).build();

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
        LoanEntityDto dto = createLoanDto();
        LoanEntity entity = LoanEntity.builder().id(dto.getId()).build();

        when(loanEntityMapper.mapFrom(dto)).thenReturn(entity);
        when(loanEntityRepository.findById(entity.getId())).thenReturn(entity);

        loanEntityService.delete(dto);

        verify(loanEntityRepository).delete(entity);
    }

    @Test
    void testDelete_NotFound() {
        LoanEntityDto dto = createLoanDto();
        LoanEntity entity = LoanEntity.builder().id(dto.getId()).build();

        when(loanEntityMapper.mapFrom(dto)).thenReturn(entity);
        when(loanEntityRepository.findById(entity.getId())).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> loanEntityService.delete(dto));
    }

    @Test
    void testUpdate_Success() {
        UUID id = UUID.randomUUID();

        LoanEntityDto dto = LoanEntityDto.builder()
                .id(id)
                .maximumAmount(10000.00)
                .amountBorrowed(5000.00)
                .totalInterest(500.00)
                .numberOfInstallments(12)
                .valueOfInstallments(450.00)
                .build();

        LoanEntity existingEntity = LoanEntity.builder().id(id).build();
        LoanEntity updatedEntity = LoanEntity.builder().id(id).build();

        when(loanEntityRepository.findById(id)).thenReturn(existingEntity);
        when(loanEntityMapper.mapFrom(dto)).thenReturn(updatedEntity);
        when(loanEntityRepository.save(updatedEntity)).thenReturn(updatedEntity);
        when(loanEntityMapper.mapTo(updatedEntity)).thenReturn(dto);

        LoanEntityDto result = loanEntityService.update(id, dto);

        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(loanEntityRepository).save(updatedEntity);
    }

    @Test
    void testUpdate_NotFound() {
        UUID id = UUID.randomUUID();
        LoanEntityDto dto = createLoanDto();

        when(loanEntityRepository.findById(id)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> loanEntityService.update(id, dto));
    }

    private LoanEntityDto createLoanDto() {
        return LoanEntityDto.builder()
                .id(UUID.randomUUID())
                .amountBorrowed(5000.00)
                .totalInterest(500.00)
                .numberOfInstallments(12)
                .valueOfInstallments(450.00)
                .build();
    }

}
