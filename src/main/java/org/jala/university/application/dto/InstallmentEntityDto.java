package org.jala.university.application.dto;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class InstallmentEntityDto {

    Integer id;
    Double amount;
    Boolean paid;
    LocalDate paymentDate;
    LocalDate DueDate;
}
