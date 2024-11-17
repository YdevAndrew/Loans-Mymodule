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

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;

/**
 * Controller responsible for handling the payments process.
 * Manages loan-related calculations, form submissions, and navigation between views.
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

    @Qualifier("formEntityServiceImpl")
    @Autowired
    private FormEntityService formService;

    @FXML
    private Pane mainPane;

    @Autowired
    private LoanEntityService loanService;

    private FormEntityDto formEntityDto;

    @Autowired
    private FormEntityMapper formEntityMapper;

    private final DateFormmaterUtil dateFormatterUtil = new DateFormmaterUtil();

    /**
     * Initializes the controller after the FXML file is loaded.
     * Sets up the due date, payment methods, and event handlers for UI components.
     */
    @FXML
    public void initialize() {
        initializeDueDate();
        initializePaymentMethods();
        installmentsComboBox.setOnAction(event -> updateDueDate());
        submitButton.setOnAction(event -> saveLoanToDatabase());
    }

    /**
     * Sets the initial due date for the first installment.
     * Populates the due date label with the formatted date.
     */
    private void initializeDueDate() {
        String firstInstallmentDate = dateFormatterUtil.FirstInstallmentDueDate();
        dueDateLabel.setText(firstInstallmentDate);
    }

    /**
     * Populates the payment method combo box with available payment methods.
     */
    private void initializePaymentMethods() {
        paymentMethodComboBox.getItems().setAll(PaymentMethod.values());
    }

    /**
     * Configures the form entity and initializes the loan amount slider and label.
     *
     * @param formEntityDto the form entity containing user-provided data.
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

            loanAmountSlider.valueProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    loanAmountLabel.setText(String.format("R$ %.2f", newValue.doubleValue()));
                }
            });
        }
    }

    /**
     * Updates the due date label based on the selected number of installments.
     * Displays detailed information about the first and final due dates.
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
     * Loads the payments pane and displays it in the main pane.
     *
     * @param mainPane the main pane to hold the payments view.
     * @param springFXMLLoader the loader to load the payments FXML file.
     * @param formEntityDto the form entity with user-provided data.
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
                System.err.println("Error: mainPane is not initialized in FormController.");
            }
        } catch (IOException e) {
            System.err.println("Error loading Payments.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Saves the loan details to the database.
     * Performs input validation, calculates interest and installment values, and creates a LoanEntityDto.
     */
    private void saveLoanToDatabase() {
        try {
            Double amountBorrowed = loanAmountSlider.getValue();
            Integer numberOfInstallments = installmentsComboBox.getValue();
            PaymentMethod paymentMethod = paymentMethodComboBox.getValue();

            if (amountBorrowed == null || numberOfInstallments == null || paymentMethod == null) {
                throw new IllegalArgumentException("One or more required fields are empty: amountBorrowed, numberOfInstallments, or paymentMethod.");
            }

            Double totalInterest;
            Double valueOfInstallments;
            try {
                totalInterest = CalculationUtil.getTotalInterest(amountBorrowed, (double) numberOfInstallments);
                valueOfInstallments = CalculationUtil.getValueOfInstallments(amountBorrowed, (double) numberOfInstallments);
            } catch (Exception e) {
                System.err.println("Error calculating interest or installments: " + e.getMessage());
                e.printStackTrace();
                return;
            }

            LocalDate issueDate;
            LocalDate loanDueDate;
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                issueDate = LocalDate.parse(dateFormatterUtil.FormattedIssueDate(), formatter);
                loanDueDate = LocalDate.parse(dateFormatterUtil.FormattedLoanDueDate(numberOfInstallments), formatter);
            } catch (DateTimeParseException e) {
                System.err.println("Error parsing dates (issueDate or loanDueDate): " + e.getMessage());
                e.printStackTrace();
                return;
            }

            LoanEntityDto loanDto = LoanEntityDto.builder()
                    .amountBorrowed(amountBorrowed)
                    .totalInterest(totalInterest)
                    .numberOfInstallments(numberOfInstallments)
                    .valueOfInstallments(valueOfInstallments)
                    .paymentMethod(paymentMethod)
                    .status(Status.REVIEW)
                    .issueDate(issueDate)
                    .installmentsDueDay(dateFormatterUtil.DueDateDay())
                    .loanDueDate(loanDueDate)
                    .form(formEntityMapper.mapFrom(formEntityDto))
                    .build();

            try {
                loanService.save(loanDto);
                System.out.println("Loan data saved successfully.");
            } catch (Exception e) {
                System.err.println("Error saving LoanEntityDto to service: " + e.getMessage());
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
