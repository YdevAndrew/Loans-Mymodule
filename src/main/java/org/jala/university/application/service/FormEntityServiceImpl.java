package org.jala.university.application.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.jala.university.application.dto.FormEntityDto;
import org.jala.university.application.mapper.FormEntityMapper;
import org.jala.university.domain.entity.FormEntity;
import org.jala.university.domain.repository.FormEntityRepository;
import org.jala.university.infrastructure.persistance.RepositoryFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FormEntityServiceImpl implements FormEntityService {

    private final FormEntityRepository formEntityRepository;
    private final FormEntityMapper formEntityMapper;

    public FormEntityServiceImpl(RepositoryFactory factory, FormEntityMapper formEntityMapper) {
        this.formEntityRepository = factory.createFormEntityRepository();
        this.formEntityMapper = formEntityMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public FormEntityDto findById(UUID id) {
        FormEntity entity = formEntityRepository.findById(id);

        if (entity == null) {
            throw new IllegalArgumentException("FormEntity not found with ID: " + id);
        }

        return formEntityMapper.mapTo(entity);
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
        // Mapeia o DTO para a entidade
        FormEntity formEntity = formEntityMapper.mapFrom(formEntityDto);
        formEntity.calculateMaximumAmount();

        // Salva a entidade e captura o UUID gerado automaticamente
        FormEntity savedEntity = formEntityRepository.save(formEntity);

        // Mapeia a entidade salva de volta para o DTO, agora com o UUID incluído
        return formEntityMapper.mapTo(savedEntity);
    }


    @Override
    @Transactional
    public void deleteById(UUID id) {
        FormEntity entity = formEntityRepository.findById(id);

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

        FormEntity entity = formEntityRepository.findById(formEntityDto.getId());
        if (entity == null) {
            throw new IllegalArgumentException("FormEntity not found with ID: " + formEntityDto.getId());
        }

        formEntityRepository.delete(entity);
    }

    @Override
    @Transactional
    public FormEntityDto update(UUID id, FormEntityDto formEntityDto) {
        FormEntity existingEntity = formEntityRepository.findById(id);

        if (existingEntity == null) {
            throw new IllegalArgumentException("FormEntity not found with ID: " + id);
        }

        FormEntity updatedEntity = formEntityMapper.mapFrom(formEntityDto);
        updatedEntity.setId(id);

        FormEntity savedEntity = formEntityRepository.save(updatedEntity);
        return formEntityMapper.mapTo(savedEntity);
    }
    
}
