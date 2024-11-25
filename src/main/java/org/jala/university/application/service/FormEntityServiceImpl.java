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

/**
 * This class implements the {@link FormEntityService} interface,
 * providing concrete implementations for managing
 * {@link FormEntity} objects.
 */
@Service
public class FormEntityServiceImpl implements FormEntityService {

    /**
     * The repository for managing {@link FormEntity} objects.
     */
    @Autowired
    private FormEntityRepository formEntityRepository;

    /**
     * The mapper for converting between {@link FormEntity}
     * and {@link FormEntityDto} objects.
     */
    private final FormEntityMapper formEntityMapper;

    /**
     * The entity manager for handling persistent entities.
     */
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Constructor for the FormEntityServiceImpl class.
     *
     * @param formEntityMapper The mapper instance to be used.
     */
    public FormEntityServiceImpl(final FormEntityMapper formEntityMapper) {
        this.formEntityMapper = formEntityMapper;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public FormEntityDto findById(final Integer id) {
        FormEntity entity = formEntityRepository.findById(id)
                .orElse(null);

        if (entity == null) {
            throw new IllegalArgumentException(
                    "FormEntity not found with ID: " + id);
        }

        return formEntityMapper.mapTo(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public FormEntity findEntityById(final Integer id) {
        FormEntity entity = formEntityRepository.findById(id)
                .orElse(null);
        if (entity == null) {
            throw new IllegalArgumentException(
                    "FormEntity not found with ID: " + id);
        }
        return entityManager.merge(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<FormEntityDto> findAll() {
        return formEntityRepository.findAll().stream()
                .map(formEntityMapper::mapTo)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public FormEntityDto save(final FormEntityDto formEntityDto) {
        FormEntity formEntity = formEntityMapper
                .mapFrom(formEntityDto);
        formEntity.calculateMaximumAmount();

        FormEntity savedEntity = formEntityRepository.save(formEntity);

        return formEntityMapper.mapTo(savedEntity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void deleteById(final Integer id) {
        FormEntity entity = formEntityRepository.findById(id)
                .orElse(null);

        if (entity == null) {
            throw new IllegalArgumentException(
                    "FormEntity not found with ID: " + id);
        }

        formEntityRepository.delete(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void delete(final FormEntityDto formEntityDto) {
        if (formEntityDto == null || formEntityDto.getId() == null) {
            throw new IllegalArgumentException(
                    "Invalid FormEntityDto: ID must not be null.");
        }

        FormEntity entity = formEntityRepository
                .findById(formEntityDto.getId()).orElse(null);
        if (entity == null) {
            throw new IllegalArgumentException("FormEntity not "
                    + "found with ID: " + formEntityDto.getId());
        }

        formEntityRepository.delete(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public FormEntityDto update(final Integer id,
                                final FormEntityDto formEntityDto) {
        FormEntity existingEntity = formEntityRepository.findById(id)
                .orElse(null);

        if (existingEntity == null) {
            throw new IllegalArgumentException(
                    "FormEntity not found with ID: " + id);
        }

        FormEntity updatedEntity = formEntityMapper
                .mapFrom(formEntityDto);
        updatedEntity.setId(id);

        FormEntity savedEntity = formEntityRepository
                .save(updatedEntity);
        return formEntityMapper.mapTo(savedEntity);
    }
}