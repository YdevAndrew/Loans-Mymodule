package org.jala.university.domain.repository;

import java.util.UUID;

import org.jala.university.commons.domain.Repository;
import org.jala.university.domain.entity.LoanEntity;

@org.springframework.stereotype.Repository
public interface LoanEntityRepository extends Repository<LoanEntity, UUID> {

}
