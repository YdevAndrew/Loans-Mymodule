package org.jala.university.application.dto;

import java.util.UUID;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class FormEntityDto {

    UUID id;
    Double income;
    byte[] proofOfIncome;
    Double maximumAmount;
}
