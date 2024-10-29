package org.jala.university.application.service;

import java.util.List;
import java.util.UUID;

import org.jala.university.application.dto.FormEntityDto;
import org.springframework.stereotype.Service;

@Service
public interface FormEntityService {

    FormEntityDto findById(UUID id);
    List<FormEntityDto> findAll();
    FormEntityDto save(FormEntityDto entityDto);
    void deleteById(UUID id);
    void delete(FormEntityDto entityDto);
    FormEntityDto update(UUID id, FormEntityDto entityDto);
}
