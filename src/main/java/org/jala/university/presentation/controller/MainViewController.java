package org.jala.university.presentation.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.jala.university.commons.presentation.BaseController;

public class MainViewController extends BaseController {

    @FXML
    private VBox initialPane;

    @FXML
    private VBox loanSimulationPane;

    @FXML
    private TextField nameField;

    @FXML
    private TextField loanAmountField;

    @FXML
    private TextField loanTermField;

    @FXML
    private Label errorMessage;

    // Método chamado ao clicar no botão "Simulação de Empréstimo"
    @FXML
    private void startLoanSimulation() {
        initialPane.setVisible(false);
        initialPane.setManaged(false);

        loanSimulationPane.setVisible(true);
        loanSimulationPane.setManaged(true);
    }

    @FXML
    private void submitLoanRequest() {
        String name = nameField.getText();
        String loanAmount = loanAmountField.getText();
        String loanTerm = loanTermField.getText();

        if (validateInputs(name, loanAmount, loanTerm)) {
            System.out.println("Solicitação de empréstimo enviada com sucesso.");
        } else {
            errorMessage.setText("Por favor, preencha todos os campos corretamente.");
        }
    }

    private boolean validateInputs(String name, String loanAmount, String loanTerm) {
        return !(name.isEmpty() || loanAmount.isEmpty() || loanTerm.isEmpty());
    }
}
