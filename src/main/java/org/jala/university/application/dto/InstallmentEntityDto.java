package org.jala.university.application.dto;

import java.time.LocalDate;
import java.util.UUID;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class InstallmentEntityDto {

    UUID id;
    Double amount;
    Boolean paid;
    LocalDate paymentDate;
    LocalDate DueDate;
}
