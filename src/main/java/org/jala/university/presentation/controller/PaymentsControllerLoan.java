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
public class PaymentsControllerLoan {

    @FXML
    public Label dueDateLabel;

    @FXML
    public Button submitButton;

    @FXML
    public Slider loanAmountSlider;

    @FXML
    public ComboBox<Integer> installmentsComboBox;

    @FXML
    public ComboBox<PaymentMethod> paymentMethodComboBox;

    @FXML
    public Label loanAmountLabel;

    @Autowired
    @Qualifier("formEntityServiceImpl")
    public FormEntityService formService;

    @FXML
    public Label installmentValueLabel;

    @FXML
    public Pane mainPane;

    @Autowired
    private SpringFXMLLoader springFXMLLoader;

    @Autowired
    @Qualifier("loanEntityService")
    public LoanEntityService loanService;

    private FormEntityDto formEntityDto;

    @Autowired
    public FormEntityMapper formEntityMapper;

    public DateFormmaterUtil dateFormatterUtil = new DateFormmaterUtil();

    private Double valueOfInstallments;

    private Double totalInterest;

    /**
     * Initializes the controller, setting up event listeners and default values for UI components.
     * Initializes due date, payment methods, and sets up listeners for combo boxes and buttons.
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
     * Initializes the due date label with the calculated first installment due date.
     */
    private void initializeDueDate() {
        String firstInstallmentDate = dateFormatterUtil.FirstInstallmentDueDate();
        dueDateLabel.setText(firstInstallmentDate);
    }

    /**
     * Initializes the payment method combo box with the available payment methods.
     */
    private void initializePaymentMethods() {
        paymentMethodComboBox.getItems().setAll(PaymentMethod.values());
    }

    /**
     * Sets the form entity data to be used for calculating loan details.
     * Configures the loan amount slider and label based on the provided form data.
     *
     * @param formEntityDto the form entity containing user income and maximum loan amount
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
     * Updates the installment value label based on the selected loan amount and number of installments.
     * Calculates the total payable amount and divides it by the number of installments to display
     * the installment value.
     */
    @FXML
    public void updateInstallmentValue() {
        Double amountBorrowed = loanAmountSlider.getValue();
        Integer numberOfInstallments = installmentsComboBox.getValue();

        if (amountBorrowed != null && numberOfInstallments != null && numberOfInstallments > 0) {
            Double totalPayable = CalculationUtil.getTotalPayable(amountBorrowed, numberOfInstallments);
            this.valueOfInstallments = totalPayable / numberOfInstallments;

            installmentValueLabel.setText(String.format("R$ %.2f per installment", valueOfInstallments));
        } else {
            installmentValueLabel.setText("Select the number of installments");
        }
    }

    /**
     * Updates the due date label with information about the installment due dates.
     * Calculates and displays the first installment due date and the final loan due date
     * based on the selected number of installments.
     */
    public void updateDueDate() {
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
     * @param mainPane        the main pane where the payments pane will be loaded
     * @param springFXMLLoader the loader used to load the FXML file
     * @param formEntityDto   the form entity containing loan-related data
     */
    public static void loadPaymentsPane(Pane mainPane, SpringFXMLLoader springFXMLLoader, FormEntityDto formEntityDto) {
        try {
            FXMLLoader loader = springFXMLLoader.load("/PaymentsLoan/payments.fxml");
            Pane paymentsPane = loader.load();

            PaymentsControllerLoan controller = loader.getController();
            controller.setFormEntity(formEntityDto);

            if (mainPane != null) {
                mainPane.getChildren().clear();
                mainPane.getChildren().add(paymentsPane);
            } else {
                System.err.println("Error: mainPane was not initialized in FormController.");
            }
        } catch (IOException e) {
            System.err.println("Error loading Payments.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Loads the "My Loans" pane into the main pane.
     * Clears the existing content of the main pane and loads the FXML file for the "My Loans" view.
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
                System.err.println("Error: mainPane was not initialized in PaymentsController.");
            }
        } catch (IOException e) {
            System.err.println("Error loading MyLoans.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Saves the loan data to the database.
     * Validates input fields, formats dates, and saves the loan data using the loan service.
     */
    public void saveLoanToDatabase() {
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
