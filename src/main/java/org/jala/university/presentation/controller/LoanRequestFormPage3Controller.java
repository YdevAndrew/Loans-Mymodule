package org.jala.university.presentation.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import lombok.EqualsAndHashCode;
import org.jala.university.ServiceFactory;
import org.jala.university.application.dto.LoanRequestFormDto;
import org.jala.university.application.mapper.LoanRequestFormMapper;
import org.jala.university.application.service.LoansService;
import org.jala.university.commons.presentation.BaseController;
import org.jala.university.presentation.controller.context.RequestFormViewContext;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.ResourceBundle;

@EqualsAndHashCode(callSuper = true)
public class LoanRequestFormPage3Controller extends BaseController implements Initializable {

    private final ShowAlert showAlert;

    private final LoanRequestFormMapper mapper;

    private HashMap<String, Object> personalData;

    @FXML
    private Button btnLoadIdCardPDF;

    @FXML
    private Button btnLoadProofAddressPDF;

    @FXML
    private Button btnLoadProofIncomePDF;

    @FXML
    private Button btnLoadProofLengthServicePDF;

    @FXML
    private Button btnLoadLaborCertificatePDF;

    @FXML
    private Button btnSaveForm;

    @FXML
    private Button btnSendForm;

    LoansService loansService;

    public LoanRequestFormPage3Controller() {
        this.showAlert = new ShowAlert();
        this.mapper = new LoanRequestFormMapper();
        this.personalData = new HashMap<>();
        this.loansService = ServiceFactory.loansService();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {

            new Thread(() -> {
                RequestFormViewContext context = (RequestFormViewContext) this.context;
                if (context != null) {
                    personalData = context.getFormData();
                } else {
                    showAlert.showErrorAlert("Error al guardar los datos anteriores");
                }
            }).start();

        });
    }

    @FXML
    private void btnLoadIdCardPDFOnAction(ActionEvent event) {

    }

    @FXML
    private void btnLoadProofAddressPDFOnAction(ActionEvent event) {

    }

    @FXML
    private void btnLoadProofIncomePDFOnAction(ActionEvent event) {

    }

    @FXML
    private void btnLoadLaborCertificatePDFOnAction(ActionEvent event) {

    }

    @FXML
    private void btnLoadProofLengthServicePDFOnAction(ActionEvent event) {

    }

    @FXML
    private void btnSaveFormOnAction(ActionEvent event) {
        LoanRequestFormDto loanRequestFormDto = getLoanRequestFormDto();
        LoanRequestFormDto saved = loansService.saveForm(loanRequestFormDto);
        if (saved != null) {
            showAlert.showInformationAlert("Formulario guardado exitosamente");
        } else {
            showAlert.showErrorAlert("Error saving the request");
        }
    }

    @FXML
    private void btnSendFormOnAction(ActionEvent event) {

    }

    public LoanRequestFormDto getLoanRequestFormDto() {
        return LoanRequestFormDto.builder()
                .namesApplicant(personalData.get("names").toString())
                .lastnamesApplicant(personalData.get("lastnames").toString())
                .identityCode(personalData.get("idCode").toString())
                .dateOfBirth((LocalDate) personalData.get("dateBirthDay"))
                .email(personalData.get("email").toString())
                .phoneNumber(personalData.get("phone").toString())
                .address(personalData.get("address").toString())
                .employerName(personalData.get("employerName").toString())
                .monthlyIncome((BigDecimal) personalData.get("monthlyIncome"))
                .jobTitle(personalData.get("jobTitle").toString())
                .yearsOfService((Integer) personalData.get("yearsOfService"))
                .loanAmount((BigDecimal) personalData.get("loanAmount"))
                .loanType(personalData.get("loanType").toString())
                .desiredLoanPeriod((Integer) personalData.get("desiredLoanPeriod"))
                .build();
    }
}
