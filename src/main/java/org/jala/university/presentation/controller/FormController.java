package org.jala.university.presentation.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import org.jala.university.application.dto.FormEntityDto;
import org.jala.university.application.service.FormEntityService;
import org.jala.university.domain.entity.FormEntity;
import org.jala.university.presentation.SpringFXMLLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.nio.file.Files;
import java.util.Base64;
import java.util.UUID;

@Controller
public class FormController {

    @Qualifier("formEntityServiceImpl")
    @Autowired
    private FormEntityService formService;

    @Autowired
    private SpringFXMLLoader springFXMLLoader;

    @FXML
    private Pane mainPane;

    @FXML
    private TextField salaryField;

    @FXML
    private Button incomeProofButton;

    private File incomeProofFile;

    @FXML
    private void initialize() {
        if (mainPane == null) {
            System.err.println("Warning: mainPane is not loaded properly.");
        }
    }

    @FXML
    private void chooseIncomeProof() {
        incomeProofFile = openFileChooser("Choose Proof of Income", incomeProofButton);
    }

    private File openFileChooser(String title, Button button) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.getExtensionFilters().add(new ExtensionFilter("Documents", "*.jpg", "*.jpeg", "*.png", "*.pdf"));
        File file = fileChooser.showOpenDialog(button.getScene().getWindow());

        if (file != null) {
            button.setText(file.getName());
        } else {
            button.setText("No files selected");
        }
        return file;
    }

    @FXML
    private void submitLoanRequest() {
        String salaryText = salaryField.getText();
        double salary;

        try {
            salary = Double.parseDouble(salaryText);
        } catch (NumberFormatException e) {
            showErrorPopup("Erro: Salário inválido.");
            return;
        }

        if (validateInputs(salary)) {
            try {
                String incomeProofBase64 = Base64.getEncoder().encodeToString(Files.readAllBytes(incomeProofFile.toPath()));

                FormEntityDto formDto = FormEntityDto.builder()
                        .income(salary)
                        .proofOfIncome(incomeProofBase64.getBytes())
                        .build();

                FormEntityDto savedFormDto = formService.save(formDto);

                // Verificação do ID salvo
                if (savedFormDto.getId() != null) {
                    System.out.println("ID do FormEntity salvo: " + savedFormDto.getId());
                } else {
                    System.out.println("Falha ao capturar o ID do FormEntity.");
                }

                showSuccessPopup("Solicitação enviada com sucesso!");
                PaymentsController.loadPaymentsPane(mainPane, springFXMLLoader, savedFormDto);

            } catch (Exception e) {
                e.printStackTrace();
                showErrorPopup("Erro ao processar o arquivo. Tente novamente.");
            }
        }
    }


    private boolean validateInputs(double salary) {
        if (incomeProofFile == null) {
            showErrorPopup("Comprovante de renda não foi selecionado.");
            return false;
        }
        if (salary < 1600) {
            showErrorPopup("Você não é qualificado para o empréstimo.");
            return false;
        }
        return true;
    }

    private void showErrorPopup(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Atenção!");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccessPopup(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Empréstimo Solicitado");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
