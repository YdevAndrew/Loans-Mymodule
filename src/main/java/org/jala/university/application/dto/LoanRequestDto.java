package org.jala.university.application.dto;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Builder
@Value
public class LoanRequestDto {

    UUID id;  // El mismo UUID de la solicitud de préstamo
    String status;  // "Enviado" o "Cancelado"
    LoanRequestFormDto loanRequestForm; // "ID de la solicitud"
}
