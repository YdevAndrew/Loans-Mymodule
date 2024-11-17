package org.jala.university.presentation.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.jala.university.presentation.SpringFXMLLoader;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Controller responsible for managing the logic and event handling in the Dashboard.
 * It handles UI elements such as component visibility and displaying information.
 */
@Controller
public class Dashboard {

    private final SpringFXMLLoader springFXMLLoader;

    /**
     * Constructor for the Dashboard.
     *
     * @param springFXMLLoader Spring loader to load FXML files.
     */
    public Dashboard(SpringFXMLLoader springFXMLLoader) {
        this.springFXMLLoader = springFXMLLoader;
    }

    @FXML
    public VBox myCardsVBox;

    @FXML
    public VBox otherButtonsVBox;

    @FXML
    public Button toggleButton;

    @FXML
    public Label balanceLabel;

    @FXML
    private AnchorPane mainViewContainer;

    @FXML
    public ImageView eyeIcon;

    @FXML
    public Label dateLabel;

    private boolean isBalanceVisible = false;
    private double balance = 1234.56;

    /**
     * Initialization method automatically called after the FXML is loaded.
     * Sets up initial events and adjusts the interface according to the initial state.
     */
    @FXML
    public void initialize() {
        toggleButton.setOnAction(event -> toggleBalanceVisibility());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM, dd", Locale.ENGLISH);
        dateLabel.setText(LocalDate.now().format(formatter));
        dateLabel.getStyleClass().add("balance-date");
    }

    /**
     * Handles the click event on the "My Cards" button.
     * Toggles the visibility of the cards section and adjusts the layout accordingly.
     */
    @FXML
    public void handleMyCardsClick() {
        boolean isVisible = myCardsVBox.isVisible();
        myCardsVBox.setVisible(!isVisible);
        myCardsVBox.setManaged(!isVisible);

        if (isVisible) {
            otherButtonsVBox.setVisible(true);
        } else {
            otherButtonsVBox.setVisible(true);
        }
    }

    /**
     * Toggles the visibility of the displayed account balance in the UI.
     * Either shows or hides the balance value and updates the corresponding eye icon.
     */
    @FXML
    public void toggleBalanceVisibility() {
        if (balanceLabel.getText().equals("R$ ********")) {
            balanceLabel.setText(String.format("R$ %.2f", balance));
            eyeIcon.setImage(new Image(getClass().getResource("/images/eye_open.png").toExternalForm()));
        } else {
            balanceLabel.setText("R$ ********");
            eyeIcon.setImage(new Image(getClass().getResource("/images/eye.png").toExternalForm()));
        }
    }

    /**
     * Loads the main view content from the "main-view.fxml" file.
     * Replaces the current container content with the newly loaded view.
     */
    @FXML
    public void loadMainView() {
        try {
            System.out.println("Attempting to load main-view.fxml...");

            FXMLLoader loader = springFXMLLoader.load("/main-view.fxml");
            Node mainViewContent = loader.load();

            mainViewContainer.getChildren().clear();
            mainViewContainer.getChildren().add(mainViewContent);

            System.out.println("main-view.fxml loaded successfully!");
        } catch (IOException e) {
            System.err.println("Error loading main-view.fxml: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
