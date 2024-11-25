package org.jala.university.domain.entity.enums;

import lombok.Getter;

/**
 * This enum represents the different statuses that a loan
 * application can have.
 */
@Getter
public enum Status {

    /**
     * The loan application has been approved.
     */
    APPROVED(1),

    /**
     * The loan application has been rejected.
     */
    REJECTED(2),

    /**
     * The loan application is currently under review.
     */
    REVIEW(3),

    /**
     * The loan has been fully paid off or otherwise finished.
     */
    FINISHED(4);

    /**
     * The code associated with the status.
     */
    private final int code;

    /**
     * Constructor for the Status enum.
     *
     * @param code The code to be assigned to the status.
     */
    Status(final int code) {
        this.code = code;
    }

    /**
     * Returns the {@link Status} enum value corresponding to the
     * given code.
     *
     * @param code The code of the status.
     * @return The {@link Status} enum value.
     * @throws IllegalArgumentException If the code is invalid.
     */
    public static Status valueOf(final int code) {
        for (Status value : Status.values()) {
            if (value.getCode() == code) {
                return value;
            }
        }
        throw new IllegalArgumentException("Invalid Status code: " + code);
    }
}