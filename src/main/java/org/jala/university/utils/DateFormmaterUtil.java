package org.jala.university.utils;

import java.time.format.DateTimeFormatter;
import java.time.LocalDate;

public class DateFormmaterUtil {
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Retorna a data inicial em formato brasileiro
    public String FormattedIssueDate() {
        return LocalDate.now().format(formatter);
    }

    // Retorna a data final do empréstimo em formato brasileiro
    public String FormattedLoanDueDate(int numberOfInstallments) {
        return LocalDate.now().plusMonths(numberOfInstallments).format(formatter);
    }

    // Retorna a data de vencimento da primeira parcela
    public String FirstInstallmentDueDate() {
        return LocalDate.now().plusMonths(1).format(formatter);
    }

    // Retorna o dia de vencimento
    public int DueDateDay() {
        return LocalDate.now().getDayOfMonth();
    }
}
