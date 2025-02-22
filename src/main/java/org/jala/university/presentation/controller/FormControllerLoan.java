package org.jala.university.presentation.controller;

import java.io.File;
import java.nio.file.Files;
import java.util.Base64;
import java.util.List;

import jakarta.xml.bind.ValidationException;
import javafx.application.Platform;
import org.jala.university.application.dto.FormEntityDto;
import org.jala.university.application.dto.LoanEntityDto;
import org.jala.university.application.mapper.FormEntityMapper;
import org.jala.university.application.service.FormEntityService;
import org.jala.university.application.service.LoanEntityService;
import org.jala.university.domain.entity.enums.Status;
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

import javax.naming.ServiceUnavailableException;

/**
 * Controller for managing the loan request form.
 */
@Controller
public class FormControllerLoan {

    @Qualifier("formEntityServiceImpl")
    @Autowired
    public FormEntityService formService;

    FormEntityMapper mapper = new FormEntityMapper();

    @Qualifier("loanEntityServiceImpl")
    @Autowired
    private LoanEntityService loanService;

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
        checkExistingApprovedLoan();
    }

    /**
     * Checks if the user has an approved loan and disables the loan request functionality if true.
     */
    private void checkExistingApprovedLoan() {
        try {
            List<LoanEntityDto> userLoans = loanService.findAll(); // TODO: Ajustar para buscar pelo usuário autenticado

            boolean hasBlockedLoan = userLoans.stream()
                    .anyMatch(loan -> loan.getStatus() == Status.APPROVED // Use o enum Status
                            || loan.getStatus() == Status.REVIEW);

            if (hasBlockedLoan) {
                disableLoanRequestButton("You cannot request a new loan because you have an active loan under review or approved.");
            }
        } catch (Exception e) { // Capture apenas exceções realmente relevantes
            logError("Unexpected error while checking loan status: ", e);
            showErrorPopup("An unexpected error occurred while checking loan status. Please try again.");
        }
    }

/**
     * Disables the loan request button and shows an informational popup.
     *
     * @param message the message to display in the popup
     */
    private void disableLoanRequestButton(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Request Blocked");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
        incomeProofButton.setDisable(true);
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
     * Submits the loan request by validating inputs, ensuring a file is linked,
     * and sending the data to the service layer.
     */
    @FXML
    public void submitLoanRequest() {
        String salaryText = salaryField.getText();
        double salary;

        // Validate salary input
        try {
            salary = Double.parseDouble(salaryText);
        } catch (NumberFormatException e) {
            showErrorPopup("Error: Invalid salary.");
            return;
        }

        // Check if a document is linked
        if (validateInputs(salary)) {
            try {
                // Encode the selected file to Base64
                String incomeProofBase64 = Base64.getEncoder().encodeToString(Files.readAllBytes(incomeProofFile.toPath()));

                FormEntityDto formDto = FormEntityDto.builder()
                        .income(salary)
                        .proofOfIncome(incomeProofBase64.getBytes())
                        .build();

                // Save the form DTO through the service layer
                FormEntityDto savedFormDto = formService.save(formDto);

                // Show success popup
                showSuccessPopup("Request sent successfully!");
                PaymentsControllerLoan.loadPaymentsPane(mainPane, springFXMLLoader, savedFormDto);
                resetFormState();
            } catch (Exception e) {
                // Show error if no file is linked
                logError("Error processing the loan request: ", e);
                showErrorPopup("Error processing the file. Please try again.");
            }
        }
    }

    /**
     * Resets the form state, clearing temporary variables and UI components.
     */
    private void resetFormState() {
        incomeProofFile = null;
        salaryField.clear();
        incomeProofButton.setText("No files selected");
    }

    /**
     * Validates the inputs for the loan request.
     *
     * @param salary the salary input to validate
     * @return true if inputs are valid, false otherwise
     */
    public boolean validateInputs(double salary) {
        if (incomeProofFile == null || !incomeProofFile.exists()) {
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

    /**
     * Displays a error popup with the specified message.
     *
     * @param message the error message to display
     * @param e the error message
     */
    private void logError(String message, Exception e) {
        System.err.println(message + e.getMessage());
        e.printStackTrace();
    }
}
