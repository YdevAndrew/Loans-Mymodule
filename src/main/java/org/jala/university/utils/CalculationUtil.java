package org.jala.university.utils;

/**
 * Utility class for performing loan-related financial calculations.
 * Includes methods for calculating total payable amount, installment value, and total interest.
 */
public class CalculationUtil {

    /**
     * Calculates the total amount payable based on the borrowed amount and the number of installments.
     * Applies a compound interest rate of 2% per month.
     *
     * @param amountBorrowed      the principal amount borrowed
     * @param numberOfInstallments the number of monthly installments
     * @return the total amount payable with compound interest
     */
    public static Double getTotalPayable(Double amountBorrowed, Double numberOfInstallments) {
        Double totalPayable;
        double monthlyInterestRate = 0.02;
        // (1 + 0.02)^n (compound interest calculation)
        double factor = Math.pow(1 + monthlyInterestRate, numberOfInstallments);
        // Total amount payable with compound interest
        totalPayable = amountBorrowed * factor;
        return totalPayable;
    }

    /**
     * Calculates the value of each installment based on the borrowed amount and the number of installments.
     *
     * @param amountBorrowed      the principal amount borrowed
     * @param numberOfInstallments the number of monthly installments
     * @return the value of each installment
     */
    public static Double getValueOfInstallments(Double amountBorrowed, Double numberOfInstallments) {
        Double valueOfInstallments;
        valueOfInstallments = getTotalPayable(amountBorrowed, numberOfInstallments) / numberOfInstallments;
        return valueOfInstallments;
    }

    /**
     * Calculates the total interest to be paid based on the borrowed amount and the number of installments.
     *
     * @param amountBorrowed      the principal amount borrowed
     * @param numberOfInstallments the number of monthly installments
     * @return the total interest to be paid
     */
    public static Double getTotalInterest(Double amountBorrowed, Double numberOfInstallments) {
        Double totalInterest;
        totalInterest = getTotalPayable(amountBorrowed, numberOfInstallments) - amountBorrowed;
        return totalInterest;
    }
}
