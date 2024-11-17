package org.jala.university.presentation;

import lombok.Getter;
import org.jala.university.commons.presentation.View;

/**
 * Enum representing the views related to loans in the application.
 * Each enum constant corresponds to a specific FXML file.
 */
@Getter
public enum LoansView {

    /**
     * Represents the main view of the loans feature.
     */
    MAIN("main-view.fxml");

    private final View view;

    /**
     * Constructor to associate a specific FXML file with the view.
     *
     * @param fileName the name of the FXML file associated with the view
     */
    LoansView(String fileName) {
        this.view = new View(fileName);
    }
}
