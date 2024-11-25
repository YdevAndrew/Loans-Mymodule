package org.jala.university.application.service;

import java.util.List;

import org.jala.university.application.dto.FormEntityDto;
import org.jala.university.domain.entity.FormEntity;
import org.springframework.stereotype.Service;

/**
 * This interface defines the service contract for managing {@link FormEntity} objects.
 * It provides methods for creating, retrieving, updating, and deleting form entities.
 */
@Service
public interface FormEntityService {

    /**
     * Retrieves a {@link FormEntityDto} by its unique identifier.
     *
     * @param id The unique identifier of the form entity.
     * @return The {@link FormEntityDto} object associated with the given identifier.
     */
    FormEntityDto findById(Integer id);

    /**
     * Retrieves a {@link FormEntity} by its unique identifier.
     *
     * @param id The unique identifier of the form entity.
     * @return The {@link FormEntity} object associated with the given identifier.
     */
    FormEntity findEntityById(Integer id);

    /**
     * Retrieves a list of all {@link FormEntityDto} objects.
     *
     * @return A list of all {@link FormEntityDto} objects.
     */
    List<FormEntityDto> findAll();

    /**
     * Saves a new {@link FormEntity} using the data provided in a {@link FormEntityDto}.
     *
     * @param entityDto The {@link FormEntityDto} object containing the data for the new entity.
     * @return The {@link FormEntityDto} object representing the saved entity.
     */
    FormEntityDto save(FormEntityDto entityDto);

    /**
     * Deletes a {@link FormEntity} by its unique identifier.
     *
     * @param id The unique identifier of the form entity to delete.
     */
    void deleteById(Integer id);

    /**
     * Deletes a {@link FormEntity} based on the data provided in a {@link FormEntityDto}.
     *
     * @param entityDto The {@link FormEntityDto} object containing the data of the entity to delete.
     */
    void delete(FormEntityDto entityDto);

    /**
     * Updates an existing {@link FormEntity} with the data provided in a {@link FormEntityDto}.
     *
     * @param id The unique identifier of the form entity to update.
     * @param entityDto The {@link FormEntityDto} object containing the updated data.
     * @return The {@link FormEntityDto} object representing the updated entity.
     */
    FormEntityDto update(Integer id, FormEntityDto entityDto);
}