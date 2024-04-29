package org.jala.university.presentation.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import org.jala.university.application.dto.LoanRequestFormDto;


import java.math.BigDecimal;

public class LoanRequestFormController {
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

    public void btnSaveOnAction(ActionEvent actionEvent) {
        if (validateInput()) {
            String address = txtStreetAndNumber.getText() + ", " + txtColony.getText() + ", " + txtState.getText() + ", " +txtCity.getText();
            LoanRequestFormDto loanRequestFormDto = LoanRequestFormDto.builder()
              .namesApplicant(txtNames.getText())
              .lastnamesApplicant(txtLastnames.getText())
              .address(address)
              .monthlyIncome(new BigDecimal(txtMonthlyIncome.getText()))
              .loanAmount( new BigDecimal(txtLoanAmount.getText()))
              .desiredLoanPeriod(Integer.parseInt(txtDesiredLoanPeriod.getText()))
              .build();
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
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Ningún campo debe estar vacío");
            alert.show();

            result = false;
        }

        try {
            Double.parseDouble(txtLoanAmount.getText());
            Double.parseDouble(txtMonthlyIncome.getText());
            Integer.parseInt(txtDesiredLoanPeriod.getText());
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Los campos para los ingresos mensuales, el monto del prestamo y el plazo de pago deseado deben ser solo números.");
            alert.show();
            result = false;
        }

        return result;
    }


}
