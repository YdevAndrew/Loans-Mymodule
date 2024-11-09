package org.jala.university.application.service;

import java.util.List;

import org.jala.university.application.dto.FormEntityDto;
import org.jala.university.domain.entity.FormEntity;
import org.springframework.stereotype.Service;

@Service
public interface FormEntityService {

    FormEntityDto findById(Integer id);
    FormEntity findEntityById(Integer id);
    List<FormEntityDto> findAll();
    FormEntityDto save(FormEntityDto entityDto);
    void deleteById(Integer id);
    void delete(FormEntityDto entityDto);
    FormEntityDto update(Integer id, FormEntityDto entityDto);
}
