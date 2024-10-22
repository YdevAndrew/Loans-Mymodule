package org.jala.university.domain.repository;

import java.util.UUID;

import org.jala.university.commons.domain.Repository;
import org.jala.university.domain.entity.FormEntity;

@org.springframework.stereotype.Repository
public interface FormEntityRepository extends Repository<FormEntity, UUID> {

}
