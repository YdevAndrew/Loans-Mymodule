package org.jala.university.presentation.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import lombok.EqualsAndHashCode;
import org.jala.university.commons.presentation.BaseController;
import org.jala.university.commons.presentation.ViewSwitcher;
import org.jala.university.presentation.LoansView;
import org.jala.university.presentation.controller.context.RequestFormViewContext;

import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@EqualsAndHashCode(callSuper = true)
public class LoanRequestFormPage1Controller extends BaseController implements Initializable {
    @FXML
    private TextField txtNames;

    @FXML
    private TextField txtLastnames;

    @FXML
    private TextField txtIdCode;

    @FXML
    private DatePicker dpBirthDay;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtPhone;

    @FXML
    private TextField txtStreetAndNumber;

    @FXML
    private TextField txtColony;

    @FXML
    private TextField txtState;

    @FXML
    private TextField txtCity;

    private ShowAlert showAlert;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        disableUnavailableDates();
        showAlert = new ShowAlert();
    }

    @FXML
    public void btnNextOnAction(ActionEvent actionEvent) {
        if (validateInput()) {
            RequestFormViewContext context = RequestFormViewContext.builder().formData(saveFormData()).build();
            ViewSwitcher.switchTo(LoansView.LOAN_REQUEST_VIEW_PAGE2.getView(), context);
        }
    }

    private HashMap<String, Object > saveFormData() {
        HashMap<String, Object > personalData = new HashMap<>();
        String address = txtStreetAndNumber.getText() + ", " + txtColony.getText() + ", " + txtState.getText() + ", " +txtCity.getText();
        personalData.put("names", txtNames.getText());
        personalData.put("lastnames", txtLastnames.getText());
        personalData.put("idCode", txtIdCode.getText());
        personalData.put("dateBirthDay", dpBirthDay.getValue());
        personalData.put("email", txtEmail.getText());
        personalData.put("phone", txtPhone.getText());
        personalData.put("address", address);
        return personalData;
    }

    private boolean validateInput() {
        if (!areCompleteFields()) {
            return false;
        }

        if (!validateEmail()) {
            return false;
        }

        return validatePhone();
    }

    private void disableUnavailableDates() {
        LocalDate date18yearsBefore = LocalDate.now().minusYears(18);
        dpBirthDay.setValue(date18yearsBefore);

        dpBirthDay.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                int age = LocalDate.now().getYear() - item.getYear();
                setDisable(age < 18);
            }
        });
    }

    private boolean areCompleteFields() {
        boolean result = true;
        if (txtNames.getText().isEmpty() || txtLastnames.getText().isEmpty()
                || txtIdCode.getText().isEmpty() || dpBirthDay.getValue() == null
                || txtEmail.getText().isEmpty() || txtPhone.getText().isEmpty()
                || txtStreetAndNumber.getText().isEmpty() || txtColony.getText().isEmpty()
                || txtState.getText().isEmpty() || txtCity.getText().isEmpty()) {

            showAlert.showErrorAlert("Ningún campo debe estar vacío");
            result = false;

        }
        return result;
    }

    private boolean validateEmail() {
        String stringMatch = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(stringMatch);
        Matcher matcher = pattern.matcher(txtEmail.getText());

        if (!matcher.matches()) {
            showAlert.showErrorAlert("Email invalido. Por favor ingresa un email válido");
        }

        return matcher.matches();
    }

    private boolean validatePhone() {
        String stringMatch = "^[0-9]{1,15}$";
        Pattern pattern = Pattern.compile(stringMatch);
        Matcher matcher = pattern.matcher(txtPhone.getText());

        if (!matcher.matches()) {
            showAlert.showErrorAlert("El número de teléfono debe ser menor o igual a 15 carácteres numéricos");
        }

        return matcher.matches();
    }


}