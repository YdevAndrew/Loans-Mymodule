package org.jala.university.presentation.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import org.jala.university.presentation.SpringFXMLLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller responsible for managing the main view of the application.
 * Handles navigation between the main menu and loan simulation views, as well as visibility toggling for UI components.
 */
@Controller
public class MainViewController {

    @Autowired
    public SpringFXMLLoader springFXMLLoader;

    @FXML
    public Pane mainPane;

    @FXML
    public Button loanButton;

    @FXML
    public List<ImageView> imageViews = new ArrayList<>();

    @FXML
    private ImageView image1, image2, image3, image4, image5, image6, image7, image8,
            image9, image10, image11, image12, image13, image14, image15, image16;

    /**
     * Default constructor for MainViewController.
     */
    public MainViewController() {
    }

    /**
     * Initializes the controller after the FXML file has been loaded.
     * Adds all declared ImageView components to the imageViews list for easy management.
     */
    @FXML
    public void initialize() {
        imageViews.add(image1);
        imageViews.add(image2);
        imageViews.add(image3);
        imageViews.add(image4);
        imageViews.add(image5);
        imageViews.add(image6);
        imageViews.add(image7);
        imageViews.add(image8);
        imageViews.add(image9);
        imageViews.add(image10);
        imageViews.add(image11);
        imageViews.add(image12);
        imageViews.add(image13);
        imageViews.add(image14);
        imageViews.add(image15);
        imageViews.add(image16);
    }

    /**
     * Starts the loan simulation process by loading the loan form view.
     * Clears the main pane and replaces its content with the loan form pane.
     */
    @FXML
    public void startLoanSimulation() {
        try {
            FXMLLoader loader = springFXMLLoader.load("/Form/form.fxml");
            Pane loanPane = loader.load();

            mainPane.getChildren().clear();
            mainPane.getChildren().add(loanPane);
        } catch (IOException e) {
            System.err.println("Error loading form.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Returns the user to the main menu by clearing the main pane and restoring the main menu components.
     */
    @FXML
    public void goBackToMenu() {
        mainPane.getChildren().clear();
        mainPane.getChildren().add(loanButton);
        toggleVisibility(true);
    }

    /**
     * Toggles the visibility and layout management of the loan button and image views.
     *
     * @param showInitial true to show the main menu components; false to hide them.
     */
    public void toggleVisibility(boolean showInitial) {
        loanButton.setVisible(showInitial);
        loanButton.setManaged(showInitial);

        imageViews.forEach(image -> {
            image.setVisible(showInitial);
            image.setManaged(showInitial);
        });
    }
}
