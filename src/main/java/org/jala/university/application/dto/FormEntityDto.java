package org.jala.university.application.dto;

import lombok.Builder;
import lombok.Value;

/**
 * This class represents a Data Transfer Object (DTO) for a Form Entity.
 * It is used to transfer data between the application layer and the presentation layer.
 */
@Builder
@Value
public class FormEntityDto {

    /**
     * The unique identifier of the form entity.
     */
    Integer id;

    /**
     * The income of the applicant.
     */
    Double income;

    /**
     * The proof of income document, stored as a byte array.
     */
    byte[] proofOfIncome;

    /**
     * The maximum amount that can be granted.
     */
    Double maximumAmount;
}