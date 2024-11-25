package org.jala.university.application.mapper;

import org.jala.university.application.dto.InstallmentEntityDto;
import org.jala.university.commons.application.mapper.Mapper;
import org.jala.university.domain.entity.InstallmentEntity;
import org.springframework.stereotype.Component;

/**
 * This class implements the {@link Mapper} interface to provide mapping
 * functionality between {@link InstallmentEntity} and {@link InstallmentEntityDto}.
 */
@Component
public class InstallmentEntityMapper implements Mapper<InstallmentEntity, InstallmentEntityDto> {

    /**
     * Maps an {@link InstallmentEntity} object to an {@link InstallmentEntityDto} object.
     *
     * @param entity The {@link InstallmentEntity} object to map from.
     * @return The mapped {@link InstallmentEntityDto} object.
     */
    @Override
    public InstallmentEntityDto mapTo(InstallmentEntity entity) {
        return InstallmentEntityDto.builder()
                .id(entity.getId())
                .amount(entity.getAmount())
                .paid(entity.getPaid())
                .paymentDate(entity.getPaymentDate())
                .DueDate(entity.getDueDate())
                .build();
    }

    /**
     * Maps an {@link InstallmentEntityDto} object to an {@link InstallmentEntity} object.
     *
     * @param dto The {@link InstallmentEntityDto} object to map from.
     * @return The mapped {@link InstallmentEntity} object.
     */
    @Override
    public InstallmentEntity mapFrom(InstallmentEntityDto dto) {
        return InstallmentEntity.builder()
                .id(dto.getId())
                .amount(dto.getAmount())
                .paid(dto.getPaid())
                .paymentDate(dto.getPaymentDate())
                .dueDate(dto.getDueDate())
                .build();
    }
}