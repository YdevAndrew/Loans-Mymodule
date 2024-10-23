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
import org.springframework.transaction.annotation.Transactional;

@Service
public class LoanEntityServiceImpl implements LoanEntityService {
    private final LoanEntityRepository loanEntityRepository;
    private final LoanEntityMapper loanEntityMapper;

    public LoanEntityServiceImpl(RepositoryFactory factory, LoanEntityMapper loanEntityMapper) {
        this.loanEntityRepository = factory.createLoanEntityRepository();
        this.loanEntityMapper = loanEntityMapper;
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
        LoanEntity savedEntity = loanEntityRepository.save(loanEntityMapper.mapFrom(entityDto));
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
}
