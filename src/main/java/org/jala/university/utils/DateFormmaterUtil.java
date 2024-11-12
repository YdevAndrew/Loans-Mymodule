package org.jala.university.utils;

import java.time.format.DateTimeFormatter;
import java.time.LocalDate;

public class DateFormmaterUtil {
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Returns the starting date in Brazilian format
    public String FormattedIssueDate() {
        return LocalDate.now().format(formatter);
    }

    // Returns the end date of the loan in Brazilian format
    public String FormattedLoanDueDate(int numberOfInstallments) {
        return LocalDate.now().plusMonths(numberOfInstallments).format(formatter);
    }

    // Returns the due date of the first installment
    public String FirstInstallmentDueDate() {
        return LocalDate.now().plusMonths(1).format(formatter);
    }

    // Returns the due date
    public int DueDateDay() {
        return LocalDate.now().getDayOfMonth();
    }
}
