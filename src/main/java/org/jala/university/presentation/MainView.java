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
 * Initializes and launches the JavaFX application with Spring Boot integration.
 */
@SpringBootApplication
@ComponentScan(basePackages = {"org.jala.university"})
public class MainView extends Application {

    private ConfigurableApplicationContext context;
    private SpringFXMLLoader springFXMLLoader;

    /**
     * Main method to launch the application.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Initializes the Spring application context and sets up the SpringFXMLLoader.
     *
     * @throws Exception if an error occurs during initialization
     */
    @Override
    public void init() throws Exception {
        context = new SpringApplicationBuilder(MainView.class).run();
        springFXMLLoader = context.getBean(SpringFXMLLoader.class);
    }

    /**
     * Starts the JavaFX application by loading the main FXML file and setting up the primary stage.
     *
     * @param primaryStage the primary stage for this JavaFX application
     * @throws Exception if an error occurs while loading the FXML file
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = springFXMLLoader.load("/board/DashboardApp.fxml");
        Scene scene = new Scene(loader.load());
        ViewSwitcher.setup(primaryStage, scene);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Loans Module Application");
        primaryStage.setMinWidth(1280);
        primaryStage.setMinHeight(720);
        primaryStage.show();
    }

    /**
     * Stops the application and closes the Spring application context.
     *
     * @throws Exception if an error occurs during shutdown
     */
    @Override
    public void stop() throws Exception {
        context.close();
    }
}
