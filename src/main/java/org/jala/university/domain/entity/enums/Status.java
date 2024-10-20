package org.jala.university.domain.entity.enums;

public enum Status {

    APPROVED(1),
    REJECTED(2),
    UNDER_ANALYSIS(3);

    private int code;

    private Status(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static Status valueOf(int code) {
        for (Status value : Status.values()) {
            if (value.getCode() == code) {
                return value;
            }
        }
        throw new IllegalArgumentException("Invalid PaymentMethod code: " + code);
    }
}
