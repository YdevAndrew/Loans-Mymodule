package org.jala.university.presentation.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import org.jala.university.application.dto.FormEntityDto;
import org.jala.university.application.service.FormEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Controller
public class MainViewController {

    @Autowired
    @Qualifier("formEntityServiceImpl")
    private FormEntityService formService;

    @FXML
    private VBox loanSimulationPane;

    @FXML
    private TextField documentField, salaryField, incomeProofField;

    @FXML
    private Label errorMessage;

    @FXML
    private Button loanButton;

    private File documentFile;
    private File incomeProofFile;

    @FXML
    private List<ImageView> imageViews = new ArrayList<>();

    @FXML
    private ImageView image1, image2, image3, image4, image5, image6, image7, image8,
            image9, image10, image11, image12, image13, image14, image15, image16;

    public MainViewController() {}

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
        toggleVisibility(false); // Oculta as imagens e o botão de empréstimo
        loanSimulationPane.setVisible(true);
        loanSimulationPane.setManaged(true);
    }

    @FXML
    private void goBackToMenu() {
        toggleVisibility(true); // Exibe as imagens e o botão de empréstimo
        loanSimulationPane.setVisible(false);
        loanSimulationPane.setManaged(false);
    }

    private void toggleVisibility(boolean showInitial) {
        loanButton.setVisible(showInitial);
        loanButton.setManaged(showInitial);

        // Alterna a visibilidade de todas as imagens
        imageViews.forEach(image -> {
            image.setVisible(showInitial);
            image.setManaged(showInitial);
        });
    }

    @FXML
    private void chooseDocument() {
        documentFile = openFileChooser("Escolher Documento com Foto", documentField);
    }

    @FXML
    private void chooseIncomeProof() {
        incomeProofFile = openFileChooser("Escolher Comprovante de Renda", incomeProofField);
    }

    private File openFileChooser(String title, TextField field) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.getExtensionFilters().add(new ExtensionFilter("Document", "*.jpg", "*.jpeg", "*.png", "*.pdf"));
        File file = fileChooser.showOpenDialog(field.getScene().getWindow());

        field.setText(file != null ? file.getName() : "Nenhum arquivo selecionado");
        return file;
    }

    @FXML
    private void submitLoanRequest() {
        String salary = salaryField.getText();

        if (validateInputs(salary)) {
            errorMessage.setText("");
            try {
                if (documentFile == null || incomeProofFile == null) {
                    errorMessage.setText("Documentos não foram selecionados corretamente.");
                    return;
                }

                String documentPhotoBase64 = Base64.getEncoder().encodeToString(Files.readAllBytes(documentFile.toPath()));
                String incomeProofBase64 = Base64.getEncoder().encodeToString(Files.readAllBytes(incomeProofFile.toPath()));

                FormEntityDto formDto = FormEntityDto.builder()
                        .id(UUID.randomUUID())
                        .income(Double.valueOf(salary))
                        .documentPhoto(documentPhotoBase64.getBytes())
                        .proofOfIncome(incomeProofBase64.getBytes())
                        .build();

                formService.save(formDto);
                showSuccessPopup("Solicitação enviada com sucesso!");

            } catch (Exception e) {
                e.printStackTrace();
                errorMessage.setText("Erro ao processar os arquivos. Tente novamente.");
            }
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
