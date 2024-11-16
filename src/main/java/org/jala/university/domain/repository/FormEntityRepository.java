package org.jala.university.domain.repository;


import org.jala.university.domain.entity.FormEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FormEntityRepository extends JpaRepository<FormEntity, Integer> {

}
