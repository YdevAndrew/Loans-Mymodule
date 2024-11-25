package org.jala.university.presentation.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import org.jala.university.presentation.SpringFXMLLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller for managing the main view of the loan application.
 */
@Controller
public class MainViewControllerLoan {

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
     * Initializes the controller after its root element has been loaded.
     * Populates the list of ImageView elements for easier management.
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
     * Clears the current pane and loads the FXML file responsible for the loan form.
     */
    @FXML
    public void startLoanSimulation() {
        try {
            FXMLLoader loader = springFXMLLoader.load("/FormLoan/form.fxml");
            Pane loanPane = loader.load();

            mainPane.getChildren().clear();
            mainPane.getChildren().add(loanPane);
        } catch (IOException e) {
            System.err.println("Error loading form.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Navigates back to the main menu, restoring the initial view.
     * Clears the pane and adds the initial UI elements back, toggling their visibility.
     */
    @FXML
    public void goBackToMenu() {
        mainPane.getChildren().clear();
        mainPane.getChildren().add(loanButton);
        toggleVisibility(true);
    }

    /**
     * Toggles the visibility of UI elements based on the provided flag.
     * Manages the visibility of the loan button and a list of image views.
     *
     * @param showInitial true to show the initial view, false to hide it
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