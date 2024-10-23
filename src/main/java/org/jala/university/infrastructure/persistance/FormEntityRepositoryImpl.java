package org.jala.university.infrastructure.persistance;

import java.util.UUID;

import org.jala.university.commons.infrastructure.persistance.CrudRepository;
import org.jala.university.domain.entity.FormEntity;
import org.jala.university.domain.repository.FormEntityRepository;

import jakarta.persistence.EntityManager;

public class FormEntityRepositoryImpl extends CrudRepository<FormEntity, UUID> implements FormEntityRepository {
    protected FormEntityRepositoryImpl(EntityManager entityManager) {
        super(FormEntity.class, entityManager);
    }

}
