package org.jala.university.application.dto;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class FormEntityDto {

    Integer id;
    Double income;
    byte[] proofOfIncome;
    Double maximumAmount;
}
