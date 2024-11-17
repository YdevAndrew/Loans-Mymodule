package org.jala.university;

import org.jala.university.presentation.MainView;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main entry point for the University Loans Module Application.
 * Combines Spring Boot and JavaFX to create a responsive and dynamic application.
 */
@SpringBootApplication
@EnableScheduling
public class MainApp {

    /**
     * The main method to launch the application.
     * Delegates to the JavaFX {@link MainView} class for launching the UI.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        MainView.launch(args);
    }

}
