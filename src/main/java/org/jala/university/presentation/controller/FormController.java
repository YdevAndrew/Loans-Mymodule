package org.jala.university.presentation.controller;

import java.io.File;
import java.nio.file.Files;
import java.util.Base64;

import javafx.application.Platform;
import org.jala.university.application.dto.FormEntityDto;
import org.jala.university.application.mapper.FormEntityMapper;
import org.jala.university.application.service.FormEntityService;
import org.jala.university.presentation.SpringFXMLLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

/**
 * Controller for managing the loan request form.
 */
@Controller
public class FormController {

    @Qualifier("formEntityServiceImpl")
    @Autowired
    public FormEntityService formService;

    FormEntityMapper mapper = new FormEntityMapper();

    @Autowired
    private SpringFXMLLoader springFXMLLoader;

    @FXML
    private Pane mainPane;

    @FXML
    public TextField salaryField;

    @FXML
    public Button incomeProofButton;

    public File incomeProofFile;

    /**
     * Initializes the controller after its root element has been loaded.
     */
    @FXML
    private void initialize() {
        if (mainPane == null) {
            System.err.println("Warning: mainPane is not loaded properly.");
        }
    }

    /**
     * Opens a file chooser for selecting proof of income.
     */
    @FXML
    private void chooseIncomeProof() {
        incomeProofFile = openFileChooser("Choose Proof of Income", incomeProofButton);
    }

    /**
     * Opens a file chooser dialog with specified title and updates the button text.
     *
     * @param title  the title of the file chooser dialog
     * @param button the button to update with the selected file's name
     * @return the selected file, or null if no file was chosen
     */
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

    /**
     * Submits the loan request by validating inputs and sending the data to the service layer.
     */
    @FXML
    public void submitLoanRequest() {
        String salaryText = salaryField.getText();
        double salary;

        try {
            salary = Double.parseDouble(salaryText);
        } catch (NumberFormatException e) {
            showErrorPopup("Erro: Invalid salary.");
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

                showSuccessPopup("Request sent successfully!");

                PaymentsController.loadPaymentsPane(mainPane, springFXMLLoader, savedFormDto);

            } catch (Exception e) {
                e.printStackTrace();
                showErrorPopup("Error processing the file. Please try again.");
            }
        }
    }

    /**
     * Validates the inputs for the loan request.
     *
     * @param salary the salary input to validate
     * @return true if inputs are valid, false otherwise
     */
    public boolean validateInputs(double salary) {
        if (incomeProofFile == null) {
            showErrorPopup("Proof of income was not selected.");
            return false;
        }
        if (salary < 1600) {
            showErrorPopup("You are not qualified for the loan.");
            return false;
        }
        return true;
    }

    /**
     * Displays an error popup with the specified message.
     *
     * @param message the error message to display
     */
    private void showErrorPopup(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Attention!");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    /**
     * Displays a success popup with the specified message.
     *
     * @param message the success message to display
     */
    private void showSuccessPopup(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Loan Requested");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
}
