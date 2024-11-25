package org.jala.university.utils;

import java.time.format.DateTimeFormatter;
import java.time.LocalDate;

/**
 * Utility class for formatting and calculating dates related to loans.
 * Provides methods for formatting issue dates, due dates, and calculating installment dates.
 */
public class DateFormmaterUtil {

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Returns the current date formatted as a string in Brazilian date format (dd/MM/yyyy).
     *
     * @return the formatted issue date
     */
    public String FormattedIssueDate() {
        return LocalDate.now().format(formatter);
    }

    /**
     * Calculates and returns the loan's due date in Brazilian date format (dd/MM/yyyy),
     * based on the number of installments.
     *
     * @param numberOfInstallments the number of monthly installments
     * @return the formatted loan due date
     */
    public String FormattedLoanDueDate(int numberOfInstallments) {
        return LocalDate.now().plusMonths(numberOfInstallments).format(formatter);
    }

    /**
     * Calculates and returns the due date of the first installment in Brazilian date format (dd/MM/yyyy).
     *
     * @return the formatted due date of the first installment
     */
    public String FirstInstallmentDueDate() {
        return LocalDate.now().plusMonths(1).format(formatter);
    }

    /**
     * Returns the current day of the month as an integer.
     *
     * @return the current day of the month
     */
    public int DueDateDay() {
        return LocalDate.now().getDayOfMonth();
    }
}
