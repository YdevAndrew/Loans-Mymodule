package org.jala.university;

import org.jala.university.presentation.MainView;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Main entry point for the Jala University Loan Management Application.
 * This class initializes the Spring Boot application and launches the JavaFX interface.
 */
@SpringBootApplication
@EnableJpaRepositories(basePackages = "org.jala.university.domain.repository")
public class MainApp {

    /**
     * Main method to start the application.
     * Configures Spring Boot and launches the JavaFX main view.
     *
     * @param args command-line arguments passed during application startup.
     */
    public static void main(String[] args) {
        MainView.launch(args);
    }
}
