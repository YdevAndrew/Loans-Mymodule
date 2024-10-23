package org.jala.university.application.mapper;

import org.jala.university.application.dto.FormEntityDto;
import org.jala.university.commons.application.mapper.Mapper;
import org.jala.university.domain.entity.FormEntity;
import org.springframework.stereotype.Component;

@Component
public class FormEntityMapper implements Mapper<FormEntity, FormEntityDto> {

    @Override
    public FormEntityDto mapTo(FormEntity formEntity) {
        return FormEntityDto.builder()
                .id(formEntity.getId())
                .documentPhoto(formEntity.getDocumentPhoto())
                .income(formEntity.getIncome())
                .proofOfIncome(formEntity.getProofOfIncome())
                .maximumAmount(formEntity.getMaximumAmount())
                .build();
    }

    @Override
    public FormEntity mapFrom(FormEntityDto formEntityDto) {
        return FormEntity.builder()
                .id(formEntityDto.getId())
                .documentPhoto(formEntityDto.getDocumentPhoto())
                .income(formEntityDto.getIncome())
                .proofOfIncome(formEntityDto.getProofOfIncome())
                .maximumAmount(formEntityDto.getMaximumAmount())
                .build();
    }
}
