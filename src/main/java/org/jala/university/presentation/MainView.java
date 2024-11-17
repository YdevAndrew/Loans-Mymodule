package org.jala.university.presentation;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jala.university.commons.presentation.ViewSwitcher;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * Main entry point for the Loans Module Application.
 * This class initializes and launches the JavaFX application, integrating with the Spring Boot framework.
 */
@SpringBootApplication
@ComponentScan(basePackages = {"org.jala.university"})
public class MainView extends Application {

    private ConfigurableApplicationContext context;
    private SpringFXMLLoader springFXMLLoader;

    /**
     * Main method to launch the application.
     *
     * @param args command-line arguments passed during application startup.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Initializes the Spring application context and loads the SpringFXMLLoader bean.
     *
     * @throws Exception if the context or loader fails to initialize.
     */
    @Override
    public void init() throws Exception {
        context = new SpringApplicationBuilder(MainView.class).run();
        springFXMLLoader = context.getBean(SpringFXMLLoader.class);
    }

    /**
     * Starts the JavaFX application by setting up the primary stage.
     * Loads the dashboard FXML file, configures the scene, and displays the main window.
     *
     * @param primaryStage the main stage for the JavaFX application.
     * @throws Exception if there is an error loading the FXML file.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = springFXMLLoader.load("/board/DashboardApp.fxml");
        Scene scene = new Scene(loader.load());
        ViewSwitcher.setup(primaryStage, scene);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Loans Module Application");
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);
        primaryStage.setMaximized(true); // Expands the window to full screen
        primaryStage.show();
    }

    /**
     * Stops the application and closes the Spring application context.
     *
     * @throws Exception if there is an error closing the context.
     */
    @Override
    public void stop() throws Exception {
        context.close();
    }
}
