package org.jala.university.presentation.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.jala.university.application.dto.FormEntityDto;
import org.jala.university.application.dto.LoanEntityDto;
import org.jala.university.application.mapper.FormEntityMapper;
import org.jala.university.application.service.FormEntityService;
import org.jala.university.application.service.LoanEntityService;
import org.jala.university.domain.entity.enums.PaymentMethod;
import org.jala.university.domain.entity.enums.Status;
import org.jala.university.presentation.SpringFXMLLoader;
import org.jala.university.utils.CalculationUtil;
import org.jala.university.utils.DateFormmaterUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;

@Controller
public class PaymentsController {

    @FXML
    private Label dueDateLabel;

    @FXML
    private Button submitButton;

    @FXML
    private Slider loanAmountSlider;

    @FXML
    private ComboBox<Integer> installmentsComboBox;

    @FXML
    private ComboBox<PaymentMethod> paymentMethodComboBox;

    @FXML
    private Label loanAmountLabel;

    @Autowired
    @Qualifier("formEntityServiceImpl")
    private FormEntityService formService;

    @FXML
    private Label installmentValueLabel;

    @FXML
    private Pane mainPane;

    @Autowired
    private SpringFXMLLoader springFXMLLoader;

    @Autowired
    @Qualifier("loanEntityService")
    private LoanEntityService loanService;

    FormEntityDto formEntityDto;

    @Autowired
    FormEntityMapper formEntityMapper;

    private final DateFormmaterUtil dateFormatterUtil = new DateFormmaterUtil();

    // Variable to hold the installment value
    private Double valueOfInstallments;

    private Double totalInterest;

    @FXML
    public void initialize() {
        initializeDueDate();
        initializePaymentMethods();
        installmentsComboBox.setOnAction(event -> {
            updateDueDate();
            updateInstallmentValue();  // Updates installment value when number of installments changes
        });
        submitButton.setOnAction(event -> saveLoanToDatabase());
    }

    private void initializeDueDate() {
        String firstInstallmentDate = dateFormatterUtil.FirstInstallmentDueDate();
        dueDateLabel.setText(firstInstallmentDate);
    }

    private void initializePaymentMethods() {
        paymentMethodComboBox.getItems().setAll(PaymentMethod.values());
    }

    public void setFormEntity(FormEntityDto formEntityDto) {
        if (formEntityDto != null) {
            this.formEntityDto = formEntityDto;

            double income = formEntityDto.getIncome();
            double maximumAmount = formEntityDto.getMaximumAmount();

            loanAmountSlider.setMin(income);
            loanAmountSlider.setMax(maximumAmount);
            loanAmountSlider.setValue(income);

            loanAmountLabel.setText(String.format("R$ %.2f", income));

            loanAmountSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
                loanAmountLabel.setText(String.format("R$ %.2f", newValue.doubleValue()));
                updateInstallmentValue(); // Updates installment value when loan amount changes
            });
        }
    }

    @FXML
    private void updateInstallmentValue() {
        Double amountBorrowed = loanAmountSlider.getValue();
        Integer numberOfInstallments = installmentsComboBox.getValue();

        if (amountBorrowed != null && numberOfInstallments != null && numberOfInstallments > 0) {
            // Calcule o totalPayable usando CalculationUtil, conforme o cálculo de LoanEntity
            Double totalPayable = CalculationUtil.getTotalPayable(amountBorrowed, (double) numberOfInstallments);

            // Calcule o valor da parcela de forma consistente
            this.valueOfInstallments = totalPayable / numberOfInstallments;

            // Atualiza o rótulo com o valor calculado
            installmentValueLabel.setText(String.format("R$ %.2f por parcela", valueOfInstallments));
        } else {
            installmentValueLabel.setText("Selecione o número de parcelas");
        }
    }

    private void updateDueDate() {
        Integer selectedInstallments = installmentsComboBox.getValue();
        if (selectedInstallments != null) {
            String finalDueDate = dateFormatterUtil.FormattedLoanDueDate(selectedInstallments);
            String firstInstallmentDueDate = dateFormatterUtil.FirstInstallmentDueDate();

            String message = String.format(
                    "Your installment will be on the %d of each month.\nThe first installment will be due on %s.\nThe final due date will be %s.",
                    dateFormatterUtil.DueDateDay(), firstInstallmentDueDate, finalDueDate);

            dueDateLabel.setText(message);
        }
    }

    public static void loadPaymentsPane(Pane mainPane, SpringFXMLLoader springFXMLLoader, FormEntityDto formEntityDto) {
        try {
            FXMLLoader loader = springFXMLLoader.load("/Payments/payments.fxml");
            Pane paymentsPane = loader.load();

            PaymentsController controller = loader.getController();
            controller.setFormEntity(formEntityDto);

            if (mainPane != null) {
                mainPane.getChildren().clear();
                mainPane.getChildren().add(paymentsPane);
            } else {
                System.err.println("Erro: mainPane não foi inicializado no FormController.");
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar Payments.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadMyLoansPane() {
        try {
            FXMLLoader loader = springFXMLLoader.load("/Loans/myloans.fxml");
            Pane myLoansPane = loader.load();
            MyLoans controller = loader.getController();
            controller.loadLoanDetails();

            if (mainPane != null) {
                mainPane.getChildren().clear();
                mainPane.getChildren().add(myLoansPane);
            } else {
                System.err.println("Erro: mainPane não foi inicializado no PaymentsController.");
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar MyLoans.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void saveLoanToDatabase() {
        try {
            Double amountBorrowed = loanAmountSlider.getValue();
            Integer numberOfInstallments = installmentsComboBox.getValue();
            PaymentMethod paymentMethod = paymentMethodComboBox.getValue();

            if (amountBorrowed == null || numberOfInstallments == null || paymentMethod == null) {
                throw new IllegalArgumentException("One or more mandatory fields are empty: amountBorrowed, numberOfInstallments, or paymentMethod.");
            }

            LocalDate issueDate;
            LocalDate loanDueDate;
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                issueDate = LocalDate.parse(dateFormatterUtil.FormattedIssueDate(), formatter);
                loanDueDate = LocalDate.parse(dateFormatterUtil.FormattedLoanDueDate(numberOfInstallments), formatter);
            } catch (DateTimeParseException e) {
                System.err.println("Error parsing dates (issueDate or loanDueDate) to expected format: " + e.getMessage());
                e.printStackTrace();
                return;
            }

            LoanEntityDto loanDto = LoanEntityDto.builder()
                    .amountBorrowed(amountBorrowed)
                    .totalInterest(this.totalInterest)
                    .numberOfInstallments(numberOfInstallments)
                    .valueOfInstallments(this.valueOfInstallments)
                    .paymentMethod(paymentMethod)
                    .status(Status.REVIEW)
                    .issueDate(issueDate)
                    .loanDueDate(loanDueDate)
                    .form(formEntityMapper.mapFrom(formEntityDto))
                    .build();

            try {
                loanService.save(loanDto);
                System.out.println("Loan data successfully saved.");
                loadMyLoansPane();
            } catch (Exception e) {
                System.err.println("Error saving LoanEntityDto in service: " + e.getMessage());
                e.printStackTrace();
            }

        } catch (IllegalArgumentException | IllegalStateException e) {
            System.err.println("Validation error: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Unexpected error saving loan data: " + e.getMessage());
            e.printStackTrace();
        }
    }
}