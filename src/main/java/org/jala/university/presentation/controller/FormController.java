package org.jala.university.presentation.controller;

import java.io.File;
import java.nio.file.Files;
import java.util.Base64;

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
 * Controller responsible for handling the loan request form.
 * Manages user input, file selection, and communication with the service layer for loan request submission.
 */
@Controller
public class FormController {

    @Qualifier("formEntityServiceImpl")
    @Autowired
    private FormEntityService formService;

    private FormEntityMapper mapper = new FormEntityMapper();

    @Autowired
    private SpringFXMLLoader springFXMLLoader;

    @FXML
    private Pane mainPane;

    @FXML
    private TextField salaryField;

    @FXML
    private Button incomeProofButton;

    private File incomeProofFile;

    /**
     * Initializes the controller after the FXML file has been loaded.
     * Ensures the mainPane is properly initialized.
     */
    @FXML
    private void initialize() {
        if (mainPane == null) {
            System.err.println("Warning: mainPane is not loaded properly.");
        }
    }

    /**
     * Opens a file chooser dialog to select a proof of income file.
     * Updates the button text with the selected file name.
     */
    @FXML
    private void chooseIncomeProof() {
        incomeProofFile = openFileChooser("Choose Proof of Income", incomeProofButton);
    }

    /**
     * Opens a file chooser dialog with specified title and button reference.
     *
     * @param title  the title of the file chooser dialog.
     * @param button the button associated with the file chooser.
     * @return the selected file, or null if no file is selected.
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
     * Handles the submission of the loan request.
     * Validates inputs, encodes the income proof file in Base64, and submits the request to the service layer.
     * Displays success or error popups based on the result.
     */
    @FXML
    private void submitLoanRequest() {
        String salaryText = salaryField.getText();
        double salary;

        try {
            salary = Double.parseDouble(salaryText);
        } catch (NumberFormatException e) {
            showErrorPopup("Error: Invalid salary.");
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

                showSuccessPopup("Request submitted successfully!");

                PaymentsController.loadPaymentsPane(mainPane, springFXMLLoader, savedFormDto);

            } catch (Exception e) {
                e.printStackTrace();
                showErrorPopup("Error processing the file. Please try again.");
            }
        }
    }

    /**
     * Validates the loan request inputs.
     * Ensures that the proof of income file is selected and the salary meets the minimum requirement.
     *
     * @param salary the salary input provided by the user.
     * @return true if inputs are valid; false otherwise.
     */
    private boolean validateInputs(double salary) {
        if (incomeProofFile == null) {
            showErrorPopup("Proof of income not selected.");
            return false;
        }
        if (salary < 1600) {
            showErrorPopup("You are not eligible for the loan.");
            return false;
        }
        return true;
    }

    /**
     * Displays an error popup with the specified message.
     *
     * @param message the error message to display.
     */
    private void showErrorPopup(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Attention!");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Displays a success popup with the specified message.
     *
     * @param message the success message to display.
     */
    private void showSuccessPopup(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Loan Request Submitted");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
