package org.jala.university.application.mapper;

import org.jala.university.application.dto.FormEntityDto;
import org.jala.university.commons.application.mapper.Mapper;
import org.jala.university.domain.entity.FormEntity;
import org.springframework.stereotype.Component;

/**
 * This class implements the {@link Mapper} interface to provide mapping
 * functionality between {@link FormEntity} and {@link FormEntityDto}.
 */
@Component
public class FormEntityMapper implements Mapper<FormEntity, FormEntityDto> {

    /**
     * Maps a {@link FormEntity} object to a {@link FormEntityDto} object.
     *
     * @param formEntity The {@link FormEntity} object to map from.
     * @return The mapped {@link FormEntityDto} object.
     */
    @Override
    public FormEntityDto mapTo(final FormEntity formEntity) {
        return FormEntityDto.builder()
                .id(formEntity.getId())
                .income(formEntity.getIncome())
                .proofOfIncome(formEntity.getProofOfIncome())
                .maximumAmount(formEntity.getMaximumAmount())
                .build();
    }

    /**
     * Maps a {@link FormEntityDto} object to a {@link FormEntity} object.
     *
     * @param formEntityDto The {@link FormEntityDto} object to map from.
     * @return The mapped {@link FormEntity} object.
     */
    @Override
    public FormEntity mapFrom(final FormEntityDto formEntityDto) {
        return FormEntity.builder()
                .id(formEntityDto.getId())
                .income(formEntityDto.getIncome())
                .proofOfIncome(formEntityDto.getProofOfIncome())
                .maximumAmount(formEntityDto.getMaximumAmount())
                .build();
    }
}