package org.jala.university.presentation.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import org.jala.university.commons.presentation.BaseController;

import java.io.File;

public class MainViewController extends BaseController {

    @FXML
    private VBox initialPane;

    @FXML
    private VBox loanSimulationPane;

    @FXML
    private TextField documentField;

    @FXML
    private TextField salaryField;

    @FXML
    private TextField incomeProofField;

    @FXML
    private Label errorMessage;

    @FXML
    private Button loanButton;  // Botão referenciado pelo ID

    private File documentFile;
    private File incomeProofFile;

    @FXML
    private TabPane mainTabPane;

    // Imagens que serão ocultadas ao iniciar a simulação de empréstimo
    @FXML
    ImageView image1, image2, image3, image4, image5, image6, image7, image8, image9, image10, image11,image12,image13,image14,image15,image16;

    @FXML
    private void startLoanSimulation() {
        initialPane.setVisible(false);
        loanSimulationPane.setVisible(true);
        loanSimulationPane.setManaged(true);
        loanButton.setVisible(false);   // Esconde o botão visualmente
        loanButton.setManaged(false);   // Remove o espaço ocupado pelo botão no layout

        image1.setVisible(false);
        image2.setVisible(false);
        image3.setVisible(false);
        image4.setVisible(false);
        image5.setVisible(false);
        image6.setVisible(false);
        image7.setVisible(false);
        image8.setVisible(false);
        image9.setVisible(false);
        image10.setVisible(false);
        image11.setVisible(false);
        image12.setVisible(false);
        image13.setVisible(false);
        image14.setVisible(false);
        image15.setVisible(false);
        image16.setVisible(false);

        // Exibir o painel de simulação de empréstimo
        loanSimulationPane.setVisible(true);
        loanSimulationPane.setManaged(true);
    }

    @FXML
    private void goBackToMenu() {
        // Voltar para o menu inicial
        loanSimulationPane.setVisible(false);
        loanSimulationPane.setManaged(false);
        loanButton.setVisible(true);   // Esconde o botão visualmente
        loanButton.setManaged(true);
        initialPane.setVisible(true);

        // Tornar todas as imagens visíveis novamente
        image1.setVisible(true);
        image2.setVisible(true);
        image3.setVisible(true);
        image4.setVisible(true);
        image5.setVisible(true);
        image6.setVisible(true);
        image7.setVisible(true);
        image8.setVisible(true);
        image9.setVisible(true);
        image10.setVisible(true);
        image11.setVisible(true);
        image12.setVisible(true);
        image13.setVisible(true);
        image14.setVisible(true);
        image15.setVisible(true);
        image16.setVisible(true);

        // Ocultar o painel de simulação de empréstimo
        loanSimulationPane.setVisible(false);
        loanSimulationPane.setManaged(false);
    }

    @FXML
    private void chooseDocument() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Escolher Documento com Foto");
        fileChooser.getExtensionFilters().add(
                new ExtensionFilter("Imagens", "*.jpg", "*.jpeg", "*.png")
        );
        documentFile = fileChooser.showOpenDialog(documentField.getScene().getWindow());

        if (documentFile != null) {
            documentField.setText(documentFile.getName());
        } else {
            documentField.setText("Nenhum arquivo selecionado");
        }
    }

    @FXML
    private void chooseIncomeProof() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Escolher Comprovante de Renda");
        fileChooser.getExtensionFilters().add(
                new ExtensionFilter("Imagens", "*.jpg", "*.jpeg", "*.png")
        );
        incomeProofFile = fileChooser.showOpenDialog(incomeProofField.getScene().getWindow());

        if (incomeProofFile != null) {
            incomeProofField.setText(incomeProofFile.getName());
        } else {
            incomeProofField.setText("Nenhum arquivo selecionado");
        }
    }

    @FXML
    private void submitLoanRequest() {
        String salary = salaryField.getText();

        if (validateInputs(salary)) {
            errorMessage.setText("");
            showSuccessPopup("Solicitação enviada com sucesso!");
        } else {
            errorMessage.setText("Por favor, preencha todos os campos corretamente.");
        }
    }

    private boolean validateInputs(String salary) {
        return documentFile != null && incomeProofFile != null && !salary.isEmpty();
    }

    private void showSuccessPopup(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Empréstimo Solicitado");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
