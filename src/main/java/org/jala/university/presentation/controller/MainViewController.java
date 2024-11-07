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

@Controller
public class MainViewController {

    @Autowired
    private SpringFXMLLoader springFXMLLoader;

    @FXML
    private Pane mainPane;

    @FXML
    private Button loanButton;

    @FXML
    private List<ImageView> imageViews = new ArrayList<>();

    @FXML
    private ImageView image1, image2, image3, image4, image5, image6, image7, image8,
            image9, image10, image11, image12, image13, image14, image15, image16;

    public MainViewController() {
    }

    @FXML
    private void initialize() {
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

    @FXML
    private void startLoanSimulation() {
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

    @FXML
    private void goBackToMenu() {

        mainPane.getChildren().clear();
        mainPane.getChildren().add(loanButton);
        toggleVisibility(true);
    }

    private void toggleVisibility(boolean showInitial) {
        loanButton.setVisible(showInitial);
        loanButton.setManaged(showInitial);

        imageViews.forEach(image -> {
            image.setVisible(showInitial);
            image.setManaged(showInitial);
        });
    }
}