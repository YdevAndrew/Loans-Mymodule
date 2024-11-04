package org.jala.university.presentation.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import org.jala.university.presentation.SpringFXMLLoader;
import org.springframework.stereotype.Controller;
import java.io.IOException;

@Controller
public class PaymentsController {

    @FXML
    private Pane mainPane;

    @FXML
    public void initialize() {
        if (mainPane == null) {
            System.out.println("mainPane in PaymentsController is null.");
        } else {
            Label placeholderMessage = new Label("Plan Payments");
            placeholderMessage.setStyle("-fx-font-size: 20px; -fx-text-fill: #333;");
            mainPane.getChildren().add(placeholderMessage);
        }
    }

    public static void loadPaymentsPane(Pane mainPane, SpringFXMLLoader springFXMLLoader) {
        try {
            FXMLLoader loader = springFXMLLoader.load("/Payments/payments.fxml");
            Pane paymentsPane = loader.load();

            if (mainPane != null) {
                mainPane.getChildren().clear();
                mainPane.getChildren().add(paymentsPane);
            } else {
                System.err.println("Erro: mainPane não foi inicializado no FormController.");
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar Payments.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
