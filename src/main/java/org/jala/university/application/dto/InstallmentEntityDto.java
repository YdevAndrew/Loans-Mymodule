package org.jala.university.application.dto;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Value;

/**
 * This class represents a Data Transfer Object (DTO) for an Installment Entity.
 * It holds information about a specific installment, such as its amount,
 * payment status, and due date.
 */
@Builder
@Value
public class InstallmentEntityDto {

    /**
     * The unique identifier of the installment entity.
     */
    Integer id;

    /**
     * The amount of the installment.
     */
    Double amount;

    /**
     * Indicates whether the installment has been paid or not.
     */
    Boolean paid;

    /**
     * The date when the installment was paid.
     */
    LocalDate paymentDate;

    /**
     * The due date of the installment.
     */
    LocalDate DueDate;
}