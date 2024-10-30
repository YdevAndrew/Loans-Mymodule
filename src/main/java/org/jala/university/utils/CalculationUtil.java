package org.jala.university.utils;

public class CalculationUtil {

    public static Double getTotalPayable(Double amountBorrowed, Double numberOfInstallments) {
        Double totalPayable;
        double monthlyInterestRate = 0.02;
        // (1 + 0.02)^n (cálculo de juros compostos)
        double factor = Math.pow(1 + monthlyInterestRate, numberOfInstallments);
        // Valor total a pagar com juros compostos
        totalPayable = amountBorrowed * factor;
        return totalPayable;
    }

    public static Double getValueOfInstallments(Double amountBorrowed, Double numberOfInstallments) {
        Double valueOfInstallments;
        valueOfInstallments = getTotalPayable(amountBorrowed, numberOfInstallments) / numberOfInstallments;
        return valueOfInstallments;
    }

    public static Double getTotalInterest(Double amountBorrowed, Double numberOfInstallments) {
        Double totalInterest;
        totalInterest = getTotalPayable(amountBorrowed, numberOfInstallments) - amountBorrowed;
        return totalInterest;
    }
}
