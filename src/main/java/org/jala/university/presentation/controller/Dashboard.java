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
 * Dashboard controller responsible for managing the main view of the application.
 */
@Controller
public class Dashboard {

    private final SpringFXMLLoader springFXMLLoader;

    /**
     * Constructor for the Dashboard controller.
     *
     * @param springFXMLLoader an instance of SpringFXMLLoader used to load FXML views.
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
     * Initializes the controller after its root element has been completely processed.
     */
    @FXML
    public void initialize() {
        toggleButton.setOnAction(event -> toggleBalanceVisibility());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM, dd", Locale.ENGLISH);
        dateLabel.setText(LocalDate.now().format(formatter));
        dateLabel.getStyleClass().add("balance-date");
    }

    /**
     * Handles the click event for the "My Cards" button.
     * Toggles the visibility of the MyCards VBox and adjusts the other buttons accordingly.
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
     * Toggles the visibility of the account balance.
     * Updates the balance label and changes the eye icon image accordingly.
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
     * Loads the main view into the container.
     * Prints debug messages in case of errors during the process.
     */
    @FXML
    public void loadMainView() {
        try {
            System.out.println("Trying to load main-view.fxml...");

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

    /**
     * Loads the Loans view into the container and initializes its controller.
     */
    @FXML
    private void loadLoansView() {
        try {
            FXMLLoader loader = springFXMLLoader.load("/Loans/myloans.fxml");
            Node loansView = loader.load();

            MyLoans controller = loader.getController();
            controller.loadLoanDetails();

            mainViewContainer.getChildren().setAll(loansView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
