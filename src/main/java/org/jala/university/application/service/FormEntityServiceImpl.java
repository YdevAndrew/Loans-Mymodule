package org.jala.university.application.service;

import java.util.List;
import java.util.stream.Collectors;

import org.jala.university.application.dto.FormEntityDto;
import org.jala.university.application.mapper.FormEntityMapper;
import org.jala.university.domain.entity.FormEntity;
import org.jala.university.domain.repository.FormEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class FormEntityServiceImpl implements FormEntityService {

    @Autowired
    private FormEntityRepository formEntityRepository;
    private final FormEntityMapper formEntityMapper;

    @PersistenceContext
    private EntityManager entityManager;

    public FormEntityServiceImpl(FormEntityMapper formEntityMapper) {
        this.formEntityMapper = formEntityMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public FormEntityDto findById(Integer id) {
        FormEntity entity = formEntityRepository.findById(id).orElse(null);

        if (entity == null) {
            throw new IllegalArgumentException("FormEntity not found with ID: " + id);
        }

        return formEntityMapper.mapTo(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public FormEntity findEntityById(Integer id) {
        FormEntity entity = formEntityRepository.findById(id).orElse(null);
        if (entity == null) {
            throw new IllegalArgumentException("FormEntity not found with ID: " + id);
        }
        return entityManager.merge(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FormEntityDto> findAll() {
        return formEntityRepository.findAll().stream()
                .map(formEntityMapper::mapTo)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public FormEntityDto save(FormEntityDto formEntityDto) {
        FormEntity formEntity = formEntityMapper.mapFrom(formEntityDto);
        formEntity.calculateMaximumAmount();

        FormEntity savedEntity = formEntityRepository.save(formEntity);

        return formEntityMapper.mapTo(savedEntity);
    }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        FormEntity entity = formEntityRepository.findById(id).orElse(null);

        if (entity == null) {
            throw new IllegalArgumentException("FormEntity not found with ID: " + id);
        }

        formEntityRepository.delete(entity);
    }

    @Override
    @Transactional
    public void delete(FormEntityDto formEntityDto) {
        if (formEntityDto == null || formEntityDto.getId() == null) {
            throw new IllegalArgumentException("Invalid FormEntityDto: ID must not be null.");
        }

        FormEntity entity = formEntityRepository.findById(formEntityDto.getId()).orElse(null);
        if (entity == null) {
            throw new IllegalArgumentException("FormEntity not found with ID: " + formEntityDto.getId());
        }

        formEntityRepository.delete(entity);
    }

    @Override
    @Transactional
    public FormEntityDto update(Integer id, FormEntityDto formEntityDto) {
        FormEntity existingEntity = formEntityRepository.findById(id).orElse(null);

        if (existingEntity == null) {
            throw new IllegalArgumentException("FormEntity not found with ID: " + id);
        }

        FormEntity updatedEntity = formEntityMapper.mapFrom(formEntityDto);
        updatedEntity.setId(id);

        FormEntity savedEntity = formEntityRepository.save(updatedEntity);
        return formEntityMapper.mapTo(savedEntity);
    }
}
