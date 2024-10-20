package org.jala.university.application.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.jala.university.application.dto.LoanEntityDto;
import org.jala.university.application.mapper.LoanEntityMapper;
import org.jala.university.domain.entity.LoanEntity;
import org.jala.university.domain.repository.LoanEntityRepository;
import org.jala.university.infrastructure.persistance.RepositoryFactory;
import org.springframework.stereotype.Service;

@Service
public class LoanEntityServiceImpl implements LoanEntityService {
    private final LoanEntityRepository loanEntityRepository;
    private final LoanEntityMapper loanEntityMapper;

    public LoanEntityServiceImpl(RepositoryFactory factory, LoanEntityMapper loanEntityMapper) {
        this.loanEntityRepository = factory.createLoanEntityRepository();
        this.loanEntityMapper = loanEntityMapper;
    }

    // Here should be added all the functionality to handle the business logic
    public LoanEntityDto findById(UUID id) {
        LoanEntity entity = loanEntityRepository.findById(id);
        if (entity == null) {
            throw new IllegalArgumentException("Entity with ID " + id + " not found.");
        }
        return loanEntityMapper.mapTo(entity);
    }

    public List<LoanEntityDto> findAll() {
        return loanEntityRepository.findAll().stream()
               .map(loanEntityMapper::mapTo)
               .collect(Collectors.toList());
    }

    @Override
    public LoanEntityDto save(LoanEntityDto sampleEntityDto) {
        LoanEntity saved = loanEntityRepository.save(loanEntityMapper.mapFrom(sampleEntityDto));
        return loanEntityMapper.mapTo(saved);
    }

    public void deleteById(UUID id) {
        LoanEntity entity = loanEntityRepository.findById(id);
        if (entity != null) {
            loanEntityRepository.delete(entity);
        } else {
            throw new IllegalArgumentException("Entity with ID " + id + " not found.");
        }
    }

    public void delete(LoanEntityDto entityDto) {
        LoanEntity entity = loanEntityMapper.mapFrom(entityDto);

        if (loanEntityRepository.findById(entity.getId()) != null) {
            loanEntityRepository.delete(entity);
        } else {
            throw new IllegalArgumentException("Entity " + entityDto + " not found.");
        }
    }

    public LoanEntityDto update(UUID id, LoanEntityDto updatedDto) {
        LoanEntity existingEntity = loanEntityRepository.findById(id);
        if (existingEntity == null) {
            throw new IllegalArgumentException("Entity with ID " + id + " not found.");
        }

        LoanEntity updatedEntity = loanEntityMapper.mapFrom(updatedDto);
        updatedEntity.setId(id);

        LoanEntity savedEntity = loanEntityRepository.save(updatedEntity);
        return loanEntityMapper.mapTo(savedEntity);
    }
}
