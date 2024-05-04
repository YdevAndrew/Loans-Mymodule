package org.jala.university.presentation.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import org.jala.university.ServiceFactory;
import org.jala.university.application.dto.LoanRequestFormDto;
import org.jala.university.application.service.LoansService;
import org.jala.university.commons.presentation.BaseController;


import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

public class LoanRequestFormPage1Controller extends BaseController implements Initializable {
    @FXML
    private TextField txtNames;

    @FXML
    private TextField txtLastnames;

    @FXML
    private TextField txtStreetAndNumber;

    @FXML
    private TextField txtColony;

    @FXML
    private TextField txtState;

    @FXML
    private TextField txtCity;

    @FXML
    private TextField txtMonthlyIncome;

    @FXML
    private TextField txtLoanAmount;

    @FXML
    private TextField txtDesiredLoanPeriod;

    LoansService loansService;

    public LoanRequestFormPage1Controller() {
        this.loansService = ServiceFactory.loansService();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void btnSaveOnAction(ActionEvent actionEvent) {
        if (validateInput()) {
            LoanRequestFormDto loanRequestFormDto = createLoanRequestFormDto();
            LoanRequestFormDto saved = loansService.saveForm(loanRequestFormDto);
            if (saved != null) {
                showInformationAlert("Loan request successfully saved");
            } else {
                showErrorAlert("Error saving the request");
            }
        }
    }

    public void btnSendOnAction(ActionEvent e) {

    }

    private boolean validateInput() {
        boolean result = true;

        if (txtNames.getText().isEmpty() || txtLastnames.getText().isEmpty()
            || txtStreetAndNumber.getText().isEmpty() || txtColony.getText().isEmpty()
            || txtState.getText().isEmpty() || txtCity.getText().isEmpty()
            || txtMonthlyIncome.getText().isEmpty() || txtLoanAmount.getText().isEmpty()
            || txtDesiredLoanPeriod.getText().isEmpty()) {

            showErrorAlert("Ningún campo debe estar vacío");
            result = false;

        } else {
            try {
                Double.parseDouble(txtLoanAmount.getText());
                Double.parseDouble(txtMonthlyIncome.getText());
                Integer.parseInt(txtDesiredLoanPeriod.getText());
            } catch (NumberFormatException e) {
                showErrorAlert("Los campos para los ingresos mensuales, el monto del prestamo y el plazo de pago deseado deben ser solo números.");
                result = false;
            }
        }

        return result;
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }

    private void showInformationAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }

    private LoanRequestFormDto createLoanRequestFormDto() {
        String address = txtStreetAndNumber.getText() + ", " + txtColony.getText() + ", " + txtState.getText() + ", " +txtCity.getText();

        return LoanRequestFormDto.builder()
          .namesApplicant(txtNames.getText())
          .lastnamesApplicant(txtLastnames.getText())
          .address(address)
          .monthlyIncome(new BigDecimal(txtMonthlyIncome.getText()))
          .loanAmount( new BigDecimal(txtLoanAmount.getText()))
          .desiredLoanPeriod(Integer.parseInt(txtDesiredLoanPeriod.getText()))
          .build();
    }
}
