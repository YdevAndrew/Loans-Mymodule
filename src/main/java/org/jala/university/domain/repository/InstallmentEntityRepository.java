package org.jala.university.domain.repository;

import java.util.List;

import org.jala.university.domain.entity.InstallmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * This interface defines a repository for managing
 * {@link InstallmentEntity} objects.
 * It extends the {@link JpaRepository} interface, providing basic CRUD
 * operations and other database interactions for installment entities.
 */
@Repository
public interface InstallmentEntityRepository extends
        JpaRepository<InstallmentEntity, Integer> {

    /**
     * Retrieves a list of {@link InstallmentEntity} associated with a
     * specific loan.
     *
     * @param loanId The unique identifier of the loan.
     * @return A list of {@link InstallmentEntity} for the given
     *     loan identifier.
     */
    @Query("SELECT i FROM InstallmentEntity i WHERE i.loan.id = :loanId")
    List<InstallmentEntity> findByLoanId(@Param("loanId") Integer loanId);

}