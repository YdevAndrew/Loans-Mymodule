package org.jala.university.domain.entity.enums;

import lombok.Getter;

@Getter
public enum Status {

    APPROVED(1),
    REJECTED(2),
    REVIEW(3),
    FINISHED(4);

    private int code;

    private Status(int code) {
        this.code = code;
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
