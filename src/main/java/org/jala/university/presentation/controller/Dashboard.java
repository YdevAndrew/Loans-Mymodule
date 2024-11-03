package org.jala.university.presentation.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.jala.university.commons.presentation.BaseController;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Controller
public class Dashboard extends BaseController {

    @FXML
    private VBox myCardsVBox;

    @FXML
    private VBox otherButtonsVBox;

    @FXML
    private VBox mainViewContainer; // Contêiner para carregar main-view.fxml

    @FXML
    private ImageView loansImage; // ImageView para loans.png

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

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main-view.fxml"));
            Node mainViewContent = loader.load();


            mainViewContainer.getChildren().clear();
            mainViewContainer.getChildren().add(mainViewContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
