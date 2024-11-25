package org.jala.university.application.service;

import org.jala.university.application.dto.FormEntityDto;
import org.jala.university.domain.entity.FormEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FormEntityServiceTest {

    @Mock
    private FormEntityService formEntityService;

    private FormEntityDto formEntityDto;
    private FormEntity formEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);


        formEntity = FormEntity.builder()
                .id(1)
                .income(1000.0)
                .proofOfIncome("Test Proof".getBytes())
                .maximumAmount(2000.0)
                .build();

        formEntityDto = FormEntityDto.builder()
                .id(1)
                .income(1000.0)
                .proofOfIncome("Test Proof DTO".getBytes())
                .maximumAmount(2000.0)
                .build();
    }

    @Test
    void testFindById() {

        when(formEntityService.findById(1)).thenReturn(formEntityDto);


        FormEntityDto result = formEntityService.findById(1);


        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(1000.0, result.getIncome());
        verify(formEntityService, times(1)).findById(1);
    }

    @Test
    void testFindEntityById() {

        when(formEntityService.findEntityById(1)).thenReturn(formEntity);

        // Execute the method
        FormEntity result = formEntityService.findEntityById(1);


        assertNotNull(result);
        assertEquals(1, result.getId());
        verify(formEntityService, times(1)).findEntityById(1);
    }

    @Test
    void testFindAll() {

        when(formEntityService.findAll()).thenReturn(Arrays.asList(formEntityDto));


        List<FormEntityDto> result = formEntityService.findAll();


        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getId());
        verify(formEntityService, times(1)).findAll();
    }

    @Test
    void testSave() {

        when(formEntityService.save(formEntityDto)).thenReturn(formEntityDto);


        FormEntityDto result = formEntityService.save(formEntityDto);


        assertNotNull(result);
        assertEquals(1, result.getId());
        verify(formEntityService, times(1)).save(formEntityDto);
    }

    @Test
    void testDeleteById() {

        doNothing().when(formEntityService).deleteById(1);


        formEntityService.deleteById(1);


        verify(formEntityService, times(1)).deleteById(1);
    }

    @Test
    void testDelete() {

        doNothing().when(formEntityService).delete(formEntityDto);


        formEntityService.delete(formEntityDto);


        verify(formEntityService, times(1)).delete(formEntityDto);
    }

    @Test
    void testUpdate() {

        FormEntityDto updatedDto = FormEntityDto.builder()
                .id(1)
                .income(1500.0)
                .proofOfIncome("Updated Proof DTO".getBytes())
                .maximumAmount(2500.0)
                .build();

        when(formEntityService.update(1, formEntityDto)).thenReturn(updatedDto);


        FormEntityDto result = formEntityService.update(1, formEntityDto);


        assertNotNull(result);
        assertEquals(1500.0, result.getIncome());
        verify(formEntityService, times(1)).update(1, formEntityDto);
    }
}
