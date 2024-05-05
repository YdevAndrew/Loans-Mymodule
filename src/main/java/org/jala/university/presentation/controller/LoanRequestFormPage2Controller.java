package org.jala.university.presentation.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import lombok.EqualsAndHashCode;
import org.jala.university.commons.presentation.BaseController;
import org.jala.university.commons.presentation.ViewSwitcher;
import org.jala.university.presentation.LoansView;
import org.jala.university.presentation.controller.context.RequestFormViewContext;

import java.math.BigDecimal;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

@EqualsAndHashCode(callSuper = true)
public class LoanRequestFormPage2Controller extends BaseController implements Initializable {

    @FXML
    private TextField txtEmployerName;

    @FXML
    private TextField txtMonthlyIncome;

    @FXML
    private TextField txtJobTitle;

    @FXML
    private TextField txtYearsOfService;

    @FXML
    private TextField txtLoanAmount;

    @FXML
    private ComboBox<Object> cBoxLoanType;

    @FXML
    private TextField txtDesiredLoanPeriod;

    private HashMap<String, Object> personalData = new HashMap<>();

    private final ShowAlert showAlert = new ShowAlert();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cBoxLoanType.getItems().addAll("Personal", "Estudios", "Hipotecario");
        Platform.runLater(() -> {
            RequestFormViewContext context = (RequestFormViewContext) this.context;
            if (context != null) {
                personalData = context.getFormData();
            } else {
                showAlert.showErrorAlert("Error al guardar los datos anteriores");
            }
        });
    }

    @FXML
    private void btnNext2OnAction(ActionEvent event) {
        boolean test = validateInput();
        System.out.println(test);
        if (validateInput()) {
            addNewFormData();
            RequestFormViewContext newContext = RequestFormViewContext.builder().formData(personalData).build();
            ViewSwitcher.switchTo(LoansView.LOAN_REQUEST_VIEW_PAGE3.getView(), newContext);
        }
    }

    private void addNewFormData() {
        personalData.put("employerName", txtEmployerName.getText());
        personalData.put("monthlyIncome", new BigDecimal(txtMonthlyIncome.getText()));
        personalData.put("jobTitle", txtJobTitle.getText());
        personalData.put("yearsOfService", Integer.parseInt(txtYearsOfService.getText()));
        personalData.put("loanAmount", new BigDecimal(txtLoanAmount.getText()));
        personalData.put("loanType", cBoxLoanType.getValue());
        personalData.put("desiredLoanPeriod", new BigDecimal(txtDesiredLoanPeriod.getText()));
    }

    private boolean validateInput() {

        if (!areCompleteFields()) {
            return false;
        }

        if (!validateNumericalFields()) {
            return false;
        }

        if (!arePositiveNumericFields()) {
            return false;
        }

        return validateLoanAmount() && validateDesiredLoanPeriod();
    }

    private boolean areCompleteFields() {
        boolean result = true;

        if (txtEmployerName.getText().isEmpty() || txtMonthlyIncome.getText().isEmpty()
                || txtJobTitle.getText().isEmpty() || txtYearsOfService.getText().isEmpty()
                || txtLoanAmount.getText().isEmpty() || cBoxLoanType.getValue() == null
                || txtDesiredLoanPeriod.getText().isEmpty()) {

            showAlert.showErrorAlert("Ningún campo debe estar vacío");
            result = false;
        }

        return result;
    }

    private boolean validateNumericalFields() {
        boolean result = true;

        try {
            new BigDecimal(txtMonthlyIncome.getText());
            new BigDecimal(txtLoanAmount.getText());
        } catch (NumberFormatException e) {
            showAlert.showErrorAlert("Los campos para Ingreso neto mensual y el Monto de prestamo deben ser números enteros o decimales válido");
            result = false;
        }

        try {
            Integer.parseInt(txtYearsOfService.getText());
            Integer.parseInt(txtDesiredLoanPeriod.getText());
        } catch (NumberFormatException e) {
            showAlert.showErrorAlert("Los campos para Años de antiguedad y el Plazo de pago deseado en meses solo aceptan números enteros.");
            result = false;

        }

        return result;
    }

    private boolean validateLoanAmount() {
        boolean result = true;
        BigDecimal loanAmount = new BigDecimal(txtLoanAmount.getText());

        if (loanAmount.compareTo(new BigDecimal(50000)) > 0) {
            showAlert.showErrorAlert("El monto del préstamo debe ser menor o igual a $ 50000 y positivo");
            result = false;
        }

        return result;
    }

    private boolean validateDesiredLoanPeriod() {
        boolean result = true;

        int desiredLoanPeriod = Integer.parseInt(txtDesiredLoanPeriod.getText());
        if (desiredLoanPeriod >= 48) {
            showAlert.showErrorAlert("El plazo de meses debe ser menor o igual a 48 meses y positivo");
            result = false;
        }

        return result;
    }

    private boolean arePositiveNumericFields() {
        boolean result = true;

        if (new BigDecimal(txtMonthlyIncome.getText()).compareTo(BigDecimal.ZERO) < 0 ||
                new BigDecimal(txtLoanAmount.getText()).compareTo(BigDecimal.ZERO) < 0 ||
                Integer.parseInt(txtYearsOfService.getText()) < 0 ||
                Integer.parseInt(txtDesiredLoanPeriod.getText()) < 0) {
            showAlert.showErrorAlert("Los campos numéricos deben ser valores positivos");
            result = false;
        }

        return result;
    }
}
