/*package org.jala.university.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.jala.university.application.dto.FormEntityDto;
import org.jala.university.application.mapper.FormEntityMapper;
import org.jala.university.domain.entity.FormEntity;
import org.jala.university.domain.repository.FormEntityRepository;
import org.jala.university.infrastructure.persistance.RepositoryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class FormEntityServiceTest {

    @Mock
    private FormEntityRepository formEntityRepository;

    @Mock
    private FormEntityMapper formEntityMapper;

    @Mock
    private RepositoryFactory repositoryFactory;

    private FormEntityService formEntityService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(repositoryFactory.createFormEntityRepository()).thenReturn(formEntityRepository);
        formEntityService = new FormEntityServiceImpl(repositoryFactory, formEntityMapper);
    }

    @Test
    void testFindById_Success() {
        Integer id = Integer.randomInteger();
        FormEntity entity = FormEntity.builder().id(id).build();
        FormEntityDto dto = FormEntityDto.builder().id(id).build();

        when(formEntityRepository.findById(id)).thenReturn(entity);
        when(formEntityMapper.mapTo(entity)).thenReturn(dto);

        FormEntityDto result = formEntityService.findById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(formEntityRepository).findById(id);
    }

    @Test
    void testFindById_NotFound() {
        Integer id = Integer.randomInteger();

        when(formEntityRepository.findById(id)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> formEntityService.findById(id));

        verify(formEntityRepository).findById(id);
    }

    @Test
    void testFindById_NullId() {
        assertThrows(IllegalArgumentException.class, () -> formEntityService.findById(null));
    }

    @Test
    void testFindAll() {
        FormEntity entity = FormEntity.builder().id(Integer.randomInteger()).build();
        FormEntityDto dto = FormEntityDto.builder().id(entity.getId()).build();

        when(formEntityRepository.findAll()).thenReturn(List.of(entity));
        when(formEntityMapper.mapTo(entity)).thenReturn(dto);

        List<FormEntityDto> result = formEntityService.findAll();

        assertEquals(1, result.size());
        assertEquals(entity.getId(), result.get(0).getId());
        verify(formEntityRepository).findAll();
    }

    @Test
    void testSave() {
        FormEntityDto dto = createFormDto();
        FormEntity entity = FormEntity.builder().build();

        when(formEntityMapper.mapFrom(dto)).thenReturn(entity);
        when(formEntityRepository.save(entity)).thenReturn(entity);
        when(formEntityMapper.mapTo(entity)).thenReturn(dto);

        FormEntityDto result = formEntityService.save(dto);

        assertNotNull(result);
        verify(formEntityRepository).save(entity);
    }

    @Test
    void testDeleteById_Success() {
        Integer id = Integer.randomInteger();
        FormEntity entity = FormEntity.builder().id(id).build();

        when(formEntityRepository.findById(id)).thenReturn(entity);

        formEntityService.deleteById(id);

        verify(formEntityRepository).delete(entity);
    }

    @Test
    void testDeleteById_NotFound() {
        Integer id = Integer.randomInteger();

        when(formEntityRepository.findById(id)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> formEntityService.deleteById(id));

        verify(formEntityRepository).findById(id);
    }

    @Test
    void testDelete_Success() {
        FormEntityDto dto = createFormDto();
        FormEntity entity = FormEntity.builder().id(dto.getId()).build();

        when(formEntityMapper.mapFrom(dto)).thenReturn(entity);
        when(formEntityRepository.findById(entity.getId())).thenReturn(entity);

        formEntityService.delete(dto);

        verify(formEntityRepository).delete(entity);
    }

    @Test
    void testDelete_NotFound() {
        FormEntityDto dto = createFormDto();
        FormEntity entity = FormEntity.builder().id(dto.getId()).build();

        when(formEntityMapper.mapFrom(dto)).thenReturn(entity);
        when(formEntityRepository.findById(entity.getId())).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> formEntityService.delete(dto));

        verify(formEntityRepository).findById(entity.getId());
    }

    @Test
    void testUpdate_Success() {
        Integer id = Integer.randomInteger();

        FormEntityDto dto = FormEntityDto.builder()
                .id(id)
                .income(5000.00)
                .proofOfIncome(new byte[0])
                .build();

        FormEntity existingEntity = FormEntity.builder().id(id).build();
        FormEntity updatedEntity = FormEntity.builder().id(id).build();

        when(formEntityRepository.findById(id)).thenReturn(existingEntity);
        when(formEntityMapper.mapFrom(dto)).thenReturn(updatedEntity);
        when(formEntityRepository.save(updatedEntity)).thenReturn(updatedEntity);
        when(formEntityMapper.mapTo(updatedEntity)).thenReturn(dto);

        FormEntityDto result = formEntityService.update(id, dto);

        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(formEntityRepository).save(updatedEntity);
    }

    @Test
    void testUpdate_NotFound() {
        Integer id = Integer.randomInteger();
        FormEntityDto dto = createFormDto();

        when(formEntityRepository.findById(id)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> formEntityService.update(id, dto));
    }

    private FormEntityDto createFormDto() {
        return FormEntityDto.builder()
                .id(Integer.randomInteger())
                .income(5000.00)
                .proofOfIncome(new byte[0])
                .maximumAmount(100000.00)
                .build();
    }
}
*/