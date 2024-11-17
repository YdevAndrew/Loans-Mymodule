package org.jala.university.utils;

/**
 * Utility class for performing financial calculations related to loans.
 * Includes methods for calculating total payable amounts, installment values, and total interest.
 */
public class CalculationUtil {

    /**
     * Calculates the total payable amount for a loan using compound interest.
     *
     * @param amountBorrowed      the principal loan amount borrowed.
     * @param numberOfInstallments the number of installments for the loan.
     * @return the total amount payable including compound interest.
     */
    public static Double getTotalPayable(Double amountBorrowed, Double numberOfInstallments) {
        Double totalPayable;
        double monthlyInterestRate = 0.02; // Fixed monthly interest rate
        // Compound interest formula: (1 + monthlyInterestRate)^numberOfInstallments
        double factor = Math.pow(1 + monthlyInterestRate, numberOfInstallments);
        // Total payable amount with compound interest
        totalPayable = amountBorrowed * factor;
        return totalPayable;
    }

    /**
     * Calculates the value of each installment for a loan.
     *
     * @param amountBorrowed      the principal loan amount borrowed.
     * @param numberOfInstallments the number of installments for the loan.
     * @return the value of each installment.
     */
    public static Double getValueOfInstallments(Double amountBorrowed, Double numberOfInstallments) {
        Double valueOfInstallments;
        valueOfInstallments = getTotalPayable(amountBorrowed, numberOfInstallments) / numberOfInstallments;
        return valueOfInstallments;
    }

    /**
     * Calculates the total interest payable for a loan.
     *
     * @param amountBorrowed      the principal loan amount borrowed.
     * @param numberOfInstallments the number of installments for the loan.
     * @return the total interest payable over the loan period.
     */
    public static Double getTotalInterest(Double amountBorrowed, Double numberOfInstallments) {
        Double totalInterest;
        totalInterest = getTotalPayable(amountBorrowed, numberOfInstallments) - amountBorrowed;
        return totalInterest;
    }
}
