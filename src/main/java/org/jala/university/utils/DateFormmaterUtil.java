package org.jala.university.utils;

import java.time.format.DateTimeFormatter;
import java.time.LocalDate;

/**
 * Utility class for handling date formatting and calculations related to loan schedules.
 * Provides methods to generate formatted dates and calculate due dates.
 */
public class DateFormmaterUtil {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Returns the current date formatted as "dd/MM/yyyy".
     * Used to represent the issue date of a loan.
     *
     * @return the formatted current date.
     */
    public String FormattedIssueDate() {
        return LocalDate.now().format(formatter);
    }

    /**
     * Calculates and returns the loan's final due date based on the number of installments.
     * The date is formatted as "dd/MM/yyyy".
     *
     * @param numberOfInstallments the number of months to add for the final due date.
     * @return the formatted loan due date.
     */
    public String FormattedLoanDueDate(int numberOfInstallments) {
        return LocalDate.now().plusMonths(numberOfInstallments).format(formatter);
    }

    /**
     * Calculates and returns the due date of the first installment.
     * The date is one month from the current date and is formatted as "dd/MM/yyyy".
     *
     * @return the formatted first installment due date.
     */
    public String FirstInstallmentDueDate() {
        return LocalDate.now().plusMonths(1).format(formatter);
    }

    /**
     * Returns the day of the month for the current date.
     * Used to determine the fixed due date day for installments.
     *
     * @return the day of the month as an integer.
     */
    public int DueDateDay() {
        return LocalDate.now().getDayOfMonth();
    }
}
