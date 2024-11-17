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

/**
 * Controller responsible for managing the payment flow for loans.
 */
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

    private FormEntityDto formEntityDto;

    @Autowired
    private FormEntityMapper formEntityMapper;

    private final DateFormmaterUtil dateFormatterUtil = new DateFormmaterUtil();

    private Double valueOfInstallments;

    private Double totalInterest;

    /**
     * Initializes the controller, setting up event listeners and default values.
     */
    @FXML
    public void initialize() {
        initializeDueDate();
        initializePaymentMethods();
        installmentsComboBox.setOnAction(event -> {
            updateDueDate();
            updateInstallmentValue();
        });
        submitButton.setOnAction(event -> saveLoanToDatabase());
    }

    /**
     * Initializes the due date for the first installment.
     */
    private void initializeDueDate() {
        String firstInstallmentDate = dateFormatterUtil.FirstInstallmentDueDate();
        dueDateLabel.setText(firstInstallmentDate);
    }

    /**
     * Populates the payment method combo box with available options.
     */
    private void initializePaymentMethods() {
        paymentMethodComboBox.getItems().setAll(PaymentMethod.values());
    }

    /**
     * Sets the form entity data to configure the payment options.
     *
     * @param formEntityDto the form entity containing loan-related data
     */
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
                updateInstallmentValue();
            });
        }
    }

    /**
     * Updates the installment value based on the loan amount and the number of installments.
     */
    @FXML
    private void updateInstallmentValue() {
        Double amountBorrowed = loanAmountSlider.getValue();
        Integer numberOfInstallments = installmentsComboBox.getValue();

        if (amountBorrowed != null && numberOfInstallments != null && numberOfInstallments > 0) {
            Double totalPayable = CalculationUtil.getTotalPayable(amountBorrowed, (double) numberOfInstallments);
            this.valueOfInstallments = totalPayable / numberOfInstallments;

            installmentValueLabel.setText(String.format("R$ %.2f per installment", valueOfInstallments));
        } else {
            installmentValueLabel.setText("Select the number of installments");
        }
    }

    /**
     * Updates the due date details based on the selected number of installments.
     */
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

    /**
     * Loads the payments pane into the specified main pane.
     *
     * @param mainPane       the main pane where the payments pane will be loaded
     * @param springFXMLLoader the loader used to load the FXML file
     * @param formEntityDto  the form entity containing loan-related data
     */
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
                System.err.println("Erro:mainPane was not initialized in FormController.");
            }
        } catch (IOException e) {
            System.err.println("Error loading Payments.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Loads the "My Loans" pane into the main pane.
     */
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
                System.err.println("Erro: mainPane was not initialized in PaymentsController.");
            }
        } catch (IOException e) {
            System.err.println("Error loading MyLoans.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Saves the loan data to the database.
     * Validates input fields and formats dates before saving.
     */
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
