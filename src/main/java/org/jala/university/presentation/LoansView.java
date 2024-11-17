package org.jala.university.presentation;

import lombok.Getter;
import org.jala.university.commons.presentation.View;

/**
 * Enum representing the different views available for loan-related functionality.
 * Provides a mapping between view names and their corresponding FXML file paths.
 */
@Getter
public enum LoansView {

    /**
     * The main view for loans.
     */
    MAIN("main-view.fxml");

    private final View view;

    /**
     * Constructor for the LoansView enum.
     *
     * @param fileName the name of the FXML file associated with the view.
     */
    LoansView(String fileName) {
        this.view = new View(fileName);
    }
}
