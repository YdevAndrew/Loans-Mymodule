package org.jala.university.domain.entity.enums;

public enum PaymentMethod {

    DEBIT_ACCOUNT(1),
    TICKET(2);

    private int code;

    private PaymentMethod(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static PaymentMethod valueOf(int code) {
        for (PaymentMethod value : PaymentMethod.values()) {
            if (value.getCode() == code) {
                return value;
            }
        }
        throw new IllegalArgumentException("Invalid PaymentMethod code: " + code);
    }
}
