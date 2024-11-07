package org.jala.university.presentation.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import org.jala.university.application.dto.LoanEntityDto;
import org.jala.university.application.dto.FormEntityDto;
import org.jala.university.application.service.FormEntityService;
import org.jala.university.application.service.LoanEntityService;
import org.jala.university.domain.entity.FormEntity;
import org.jala.university.domain.entity.enums.PaymentMethod;
import org.jala.university.domain.entity.enums.Status;
import org.jala.university.presentation.SpringFXMLLoader;
import org.jala.university.utils.CalculationUtil;
import org.jala.university.utils.DateFormmaterUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.UUID;

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
    private ComboBox<PaymentMethod> paymentMethodComboBox; // Alterado para usar enum PaymentMethod

    @FXML
    private Label loanAmountLabel;

    @Qualifier("formEntityServiceImpl")
    @Autowired
    private FormEntityService formService;

    private FormEntityDto formEntityDto;

    @FXML
    private Pane mainPane;

    @Autowired
    private LoanEntityService loanService;

    private final DateFormmaterUtil dateFormatterUtil = new DateFormmaterUtil();

    @FXML
    public void initialize() {
        initializeDueDate();
        initializePaymentMethods();
        installmentsComboBox.setOnAction(event -> updateDueDate());
        submitButton.setOnAction(event -> saveLoanToDatabase());
    }

    private void initializeDueDate() {
        String firstInstallmentDate = dateFormatterUtil.FirstInstallmentDueDate();
        dueDateLabel.setText(firstInstallmentDate);
    }

    private void initializePaymentMethods() {
        paymentMethodComboBox.getItems().setAll(PaymentMethod.values()); // Adiciona todos os métodos de pagamento
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

            loanAmountSlider.valueProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    loanAmountLabel.setText(String.format("R$ %.2f", newValue.doubleValue()));
                }
            });
        }
    }

    private void updateDueDate() {
        Integer selectedInstallments = installmentsComboBox.getValue();
        if (selectedInstallments != null) {
            String finalDueDate = dateFormatterUtil.FormattedLoanDueDate(selectedInstallments);
            String firstInstallmentDueDate = dateFormatterUtil.FirstInstallmentDueDate();

            String message = String.format(
                    "Your installment will be on the %d of each month.\nThe first installment will be due on %s.\nThe final due date will be %s.",
                    dateFormatterUtil.DueDateDay(), firstInstallmentDueDate, finalDueDate
            );

            dueDateLabel.setText(message);
        }
    }

    public static void loadPaymentsPane(Pane mainPane, SpringFXMLLoader springFXMLLoader, FormEntityDto formEntity) {
        try {
            FXMLLoader loader = springFXMLLoader.load("/Payments/payments.fxml");
            Pane paymentsPane = loader.load();

            PaymentsController controller = loader.getController();
            controller.setFormEntity(formEntity);

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

    private void saveLoanToDatabase() {
        try {

            Double amountBorrowed = loanAmountSlider.getValue();
            Integer numberOfInstallments = installmentsComboBox.getValue();
            PaymentMethod paymentMethod = paymentMethodComboBox.getValue();

            if (amountBorrowed == null || numberOfInstallments == null || paymentMethod == null) {
                throw new IllegalArgumentException("Um ou mais campos obrigatórios estão vazios: " +
                        "amountBorrowed, numberOfInstallments, ou paymentMethod.");
            }



            Double totalInterest;
            Double valueOfInstallments;
            try {
                totalInterest = CalculationUtil.getTotalInterest(amountBorrowed, (double) numberOfInstallments);
                valueOfInstallments = CalculationUtil.getValueOfInstallments(amountBorrowed, (double) numberOfInstallments);
            } catch (Exception e) {
                System.err.println("Erro ao calcular juros ou parcelas: " + e.getMessage());
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
                System.err.println("Erro ao converter datas (issueDate ou loanDueDate) para o formato esperado: " + e.getMessage());
                e.printStackTrace();
                return;
            }


            FormEntity formEntity = new FormEntity();
            LoanEntityDto loanDto = LoanEntityDto.builder()
                    .id(UUID.randomUUID())
                    .amountBorrowed(amountBorrowed)
                    .totalInterest(totalInterest)
                    .numberOfInstallments(numberOfInstallments)
                    .valueOfInstallments(valueOfInstallments)
                    .paymentMethod(paymentMethod)
                    .status(Status.REVIEW)
                    .issueDate(issueDate)
                    .installmentsDueDay(dateFormatterUtil.DueDateDay())
                    .loanDueDate(loanDueDate)
                    .form(formEntity)
                    .build();


            try {
                loanService.save(loanDto);
                System.out.println("Dados do empréstimo salvos com sucesso.");
            } catch (Exception e) {
                System.err.println("Erro ao salvar o LoanEntityDto no serviço: " + e.getMessage());
                e.printStackTrace();
            }

        } catch (IllegalArgumentException | IllegalStateException e) {
            System.err.println("Erro de validação: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Erro inesperado ao salvar dados do empréstimo: " + e.getMessage());
            e.printStackTrace();
        }
    }
}