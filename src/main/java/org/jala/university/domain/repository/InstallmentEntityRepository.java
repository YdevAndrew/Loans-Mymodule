package org.jala.university.domain.repository;

import java.util.List;

import org.jala.university.domain.entity.InstallmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InstallmentEntityRepository extends JpaRepository<InstallmentEntity, Integer> {

    @Query("SELECT i FROM InstallmentEntity i WHERE i.loan.id = :loanId")
    List<InstallmentEntity> findByLoanId(@Param("loanId") Integer loanId);

}
