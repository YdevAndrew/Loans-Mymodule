package org.jala.university.application.dto;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class FormEntityDto {

    UUID id;
    byte[] documentPhoto;
    BigDecimal income;
    byte[] proofOfIncome;
}
