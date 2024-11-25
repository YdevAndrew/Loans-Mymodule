package org.jala.university.domain.entity.enums;

import lombok.Getter;

/**
 * This enum represents the different payment methods available for loans.
 */
@Getter
public enum PaymentMethod {

    /**
     * Payment method using debit from the account.
     */
    DEBIT_ACCOUNT(1),

    /**
     * Payment method using a ticket.
     */
    TICKET(2);

    /**
     * The code associated with the payment method.
     */
    private final int code;

    /**
     * Constructor for the PaymentMethod enum.
     *
     * @param code The code to be assigned to the payment method.
     */
    PaymentMethod(final int code) {
        this.code = code;
    }

    /**
     * Returns the {@link PaymentMethod} enum value corresponding
     * to the given code.
     *
     * @param code The code of the payment method.
     * @return The {@link PaymentMethod} enum value.
     * @throws IllegalArgumentException If the code is invalid.
     */
    public static PaymentMethod valueOf(final int code) {
        for (PaymentMethod value : PaymentMethod.values()) {
            if (value.getCode() == code) {
                return value;
            }
        }
        throw new IllegalArgumentException("Invalid PaymentMethod 1  code: "
                + code);
    }
}