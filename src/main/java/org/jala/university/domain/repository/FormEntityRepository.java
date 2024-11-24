package org.jala.university.domain.repository;

import org.jala.university.domain.entity.FormEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * This interface defines a repository for managing
 * {@link FormEntity} objects.
 * It extends the {@link JpaRepository} interface, providing basic
 * CRUD operations and other database interactions for form entities.
 */
@Repository
public interface FormEntityRepository extends
        JpaRepository<FormEntity, Integer> {

}