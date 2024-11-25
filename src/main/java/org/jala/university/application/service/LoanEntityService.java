package org.jala.university.application.service;

import java.time.LocalDate;
import java.util.List;

import org.jala.university.application.dto.InstallmentEntityDto;
import org.jala.university.application.dto.LoanEntityDto;
import org.jala.university.domain.entity.Account;
import org.jala.university.domain.entity.LoanEntity;

/**
 * This interface defines the service contract for managing
 * {@link LoanEntity} objects.
 * It provides methods for creating, retrieving, updating,
 * and deleting loan entities,
 * as well as methods for managing loan installments and retrieving
 * loan-related information.
 */
public interface LoanEntityService {

    /**
     * Retrieves a {@link LoanEntityDto} by its unique identifier.
     *
     * @param id The unique identifier of the loan entity.
     * @return The {@link LoanEntityDto} object associated
     *     with the given identifier.
     */
    LoanEntityDto findById(Integer id);

    /**
     * Retrieves a list of all {@link LoanEntityDto} objects.
     *
     * @return A list of all {@link LoanEntityDto} objects.
     */
    List<LoanEntityDto> findAll();

    /**
     * Retrieves a {@link LoanEntity} by its unique identifier.
     *
     * @param id The unique identifier of the loan entity.
     * @return The {@link LoanEntity} object associated with the
     *     given identifier.
     */
    LoanEntity findEntityById(Integer id);

    /**
     * Saves a new {@link LoanEntity} using the data provided
     * in a {@link LoanEntityDto}.
     *
     * @param entityDto The {@link LoanEntityDto} object
     *                  containing the data for the new entity.
     * @return The {@link LoanEntityDto} object representing the
     *     saved entity.
     */
    LoanEntityDto save(LoanEntityDto entityDto);

    /**
     * Deletes a {@link LoanEntity} by its unique identifier.
     *
     * @param id The unique identifier of the loan entity to delete.
     */
    void deleteById(Integer id);

    /**
     * Deletes a {@link LoanEntity} based on the data provided in a
     * {@link LoanEntityDto}.
     *
     * @param entityDto The {@link LoanEntityDto} object containing
     *                  the data of the entity to delete.
     */
    void delete(LoanEntityDto entityDto);

    /**
     * Updates an existing {@link LoanEntity} with the data provided
     * in a {@link LoanEntityDto}.
     *
     * @param entityDto The {@link LoanEntityDto}
     *                  object containing the updated data.
     * @return The {@link LoanEntityDto} object representing the
     *     updated entity.
     */
    LoanEntityDto update(LoanEntityDto entityDto);

    /**
     * Retrieves a list of {@link LoanEntityDto} associated with the
     * current user's account.
     *
     * @return A list of {@link LoanEntityDto} for the current
     *     user's account.
     */
    List<LoanEntityDto> findLoansByAccountId();

    /**
     * Manually pays an installment for a loan and updates the
     * associated account balance.
     *
     * @param dto The {@link LoanEntityDto} object representing
     *            the loan for which to pay the installment.
     * @return The updated {@link Account} object after the payment.
     */
    Account payInstallmentManually(LoanEntityDto dto);

    /**
     * Retrieves the number of paid installments for a given loan.
     *
     * @param dto The {@link LoanEntityDto} object representing
     *            the loan.
     * @return The number of paid installments for the loan.
     */
    long getPaidInstallments(LoanEntityDto dto);

    /**
     * Retrieves the date of the first unpaid installment for a
     * given loan.
     *
     * @param dto The {@link LoanEntityDto} object representing
     *            the loan.
     * @return The date of the first unpaid installment
     *     for the loan.
     */
    LocalDate getFirstUnpaidInstallmentDate(LoanEntityDto dto);

    /**
     * Calculates and returns the outstanding balance for a loan.
     *
     * @param loanId The unique identifier of the loan.
     * @return The outstanding balance for the loan.
     */
    Double getOutstandingBalance(Integer loanId);

    /**
     * Retrieves the first unpaid installment for a given loan.
     *
     * @param loan The {@link LoanEntityDto} object
     *             representing the loan.
     * @return The {@link InstallmentEntityDto} object representing
     *     the first unpaid installment.
     */
    InstallmentEntityDto getFirstUnpaidInstallment(LoanEntityDto loan);
}