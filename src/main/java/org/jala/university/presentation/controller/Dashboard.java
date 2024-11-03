package org.jala.university.presentation.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.jala.university.presentation.SpringFXMLLoader;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Controller
public class Dashboard {

    private final SpringFXMLLoader springFXMLLoader;

    public Dashboard(SpringFXMLLoader springFXMLLoader) {
        this.springFXMLLoader = springFXMLLoader;
    }

    @FXML
    private VBox myCardsVBox;

    @FXML
    private VBox otherButtonsVBox;

    @FXML
    private VBox mainViewContainer;

    @FXML
    private ImageView loansImage;

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

    @FXML
    public void loadMainView() {
        try {
            FXMLLoader loader = springFXMLLoader.load("/main-view.fxml");
            Node mainViewContent = loader.load();

            mainViewContainer.getChildren().clear();
            mainViewContainer.getChildren().add(mainViewContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
