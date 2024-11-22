package org.jala.university.presentation.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.util.Duration;
import org.jala.university.application.dto.LoanEntityDto;
import org.jala.university.application.service.LoanEntityService;
import org.jala.university.application.service.LoanResultsService;
import org.jala.university.domain.entity.Account;
import org.jala.university.domain.entity.InstallmentEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller class to manage and display user loans.
 */
@Controller
public class MyLoans {

    public HBox filterBar;

    public AnchorPane mainContainer;

    @FXML
    private FlowPane loansContainer;

    @FXML
    private ComboBox<String> statusFilterComboBox;

    private double currentOffset = 0;

    @Autowired
    private LoanResultsService loanResultsService;

    @Autowired
    @Qualifier("loanEntityService")
    private LoanEntityService loanService;

    private Label noLoanLabel;

    private final ObservableList<String> statuses = FXCollections.observableArrayList("ALL", "APPROVED", "REJECTED", "REVIEW", "FINISHED");

    /**
     * Initializes the controller, setting up UI components and loading initial data.
     */
    public void initialize() {
        noLoanLabel = new Label("No loans found.");
        noLoanLabel.setStyle("-fx-font-size: 16; -fx-text-fill: gray;");
        loansContainer.getChildren().add(noLoanLabel);
        mainContainer.setOnScroll(this::handleScroll);



        statusFilterComboBox.setItems(statuses);
        statusFilterComboBox.setValue("ALL");
        statusFilterComboBox.setOnAction(event -> loadLoanDetails());
    }

    /**
     * Handles the scrolling event for the loans container.
     *
     * @param event ScrollEvent triggered by the user.
     */
    private void handleScroll(ScrollEvent event) {

        double deltaY = event.getDeltaY() * 0.5;

        double newOffset = currentOffset - deltaY;


        double maxOffset = loansContainer.getHeight() - mainContainer.getPrefHeight();
        if (newOffset < 0) {
            newOffset = 0;
        } else if (newOffset > maxOffset) {
            newOffset = maxOffset;
        }


        loansContainer.setLayoutY(-newOffset);
        currentOffset = newOffset;

        event.consume();
    }

    /**
     * Loads the details of loans based on the selected filter status.
     */
    public void loadLoanDetails() {
        String selectedStatus = statusFilterComboBox.getValue();
        List<LoanEntityDto> loans = loanService.findAll();

        loansContainer.getChildren().clear();

        if (loans != null && !loans.isEmpty()) {
            if (!"ALL".equals(selectedStatus)) {
                loans = loans.stream()
                        .filter(loan -> loan.getStatus().name().equalsIgnoreCase(selectedStatus))
                        .collect(Collectors.toList());
            }

            if (loans.isEmpty()) {
                loansContainer.getChildren().add(noLoanLabel);
                return;
            }

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            for (LoanEntityDto loan : loans) {
                VBox loanBox = createLoanBox(loan, dateFormatter);
                loansContainer.getChildren().add(loanBox);
            }
        } else {
            loansContainer.getChildren().add(noLoanLabel);
        }
    }

    /**
     * Creates a VBox containing details of a loan.
     *
     * @param loan The loan entity to display.
     * @param dateFormatter DateTimeFormatter for formatting dates.
     * @return A styled VBox with loan details.
     */
    private VBox createLoanBox(LoanEntityDto loan, DateTimeFormatter dateFormatter) {
        VBox loanBox = new VBox();
        loanBox.setSpacing(10);
        loanBox.setPrefSize(350, 600);
        loanBox.setStyle("-fx-padding: 20; -fx-border-color: lightgray; -fx-border-width: 1; -fx-border-radius: 15; -fx-background-radius: 15; -fx-background-color: #ffffff;");

        Label statusLabel = createStatusLabel(loan);
        loanBox.getChildren().add(statusLabel);

        String formattedIssueDate = loan.getIssueDate().format(dateFormatter);
        addDetailToVBox(loanBox, "Date of Request:", formattedIssueDate);
        addDetailToVBox(loanBox, "Amount Borrowed:", String.format("R$ %.2f", loan.getAmountBorrowed()));
        addDetailToVBox(loanBox, "Installment Amount:", String.format("R$ %.2f", loan.getValueOfInstallments()));
        addDetailToVBox(loanBox, "Total Interest:", String.format("R$ %.2f", loan.getTotalInterest()));
        addDetailToVBox(loanBox, "Total Amount + Interest:", String.format("R$ %.2f", loan.getAmountBorrowed() + loan.getTotalInterest()));
        addDetailToVBox(loanBox, "Payment Method:", loan.getPaymentMethod().name());

        // Call the new method to get the outstanding balance
        Double outstandingBalance = getOutstandingBalance(loan.getId());
        addDetailToVBox(loanBox, "Outstanding Balance:", String.format("R$ %.2f", outstandingBalance));

        long paidInstallments = loanService.getPaidInstallments(loan);
        addDetailToVBox(loanBox, "Paid Installments:", String.format("%d / %d", paidInstallments, loan.getNumberOfInstallments()));

        // Add the payment section if the status is APPROVED
        if ("APPROVED".equalsIgnoreCase(loan.getStatus().name())) {
            VBox paymentSection = createPaymentSection(loan);
            loanBox.getChildren().add(paymentSection);
        }

        return loanBox;
    }

    /**
     * Gets the outstanding balance of a loan.
     *
     * @param loanId The ID of the loan.
     * @return The outstanding balance.
     */
    public Double getOutstandingBalance(Integer loanId) {
        return loanService.getOutstandingBalance(loanId); // Assuming LoanEntityService has this method
    }


    /**
     * Creates a payment section for an approved loan.
     *
     * @param loan The loan entity to create the payment section for.
     * @return A VBox containing the payment section.
     */
    private VBox createPaymentSection(LoanEntityDto loan) {
        VBox paymentBox = new VBox();
        paymentBox.setSpacing(10);
        paymentBox.setStyle("-fx-background-color: #F1F8FF; -fx-padding: 10; -fx-border-color: #CCE7FF; -fx-border-radius: 10; -fx-background-radius: 10;");

        Label installmentLabel = new Label(String.format("Next Installment: R$ %.2f", loan.getValueOfInstallments()));
        installmentLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

        Label dueDateLabel;
        try {
            LocalDate nextDueDate = loanService.getFirstUnpaidInstallmentDate(loan);
            if (nextDueDate != null) {
                String formattedDate = nextDueDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                dueDateLabel = new Label("Due Date: " + formattedDate);
            } else {
                dueDateLabel = new Label("No unpaid installments.");
            }
        } catch (Exception e) {

            System.err.println("Error fetching next unpaid installment date: " + e.getMessage());
            dueDateLabel = new Label("Error fetching due date.");
        }

        dueDateLabel.setStyle("-fx-font-size: 14;");

        Button payButton = new Button("Pay Installment");
        payButton.setStyle("-fx-background-color: #007BFF; -fx-text-fill: white; -fx-font-size: 14; -fx-padding: 5 10;");
        payButton.setOnAction(event -> handlePayInstallment(loan));

        paymentBox.getChildren().addAll(installmentLabel, dueDateLabel, payButton);

        return paymentBox;
    }
    /**
     * Handles the "Pay Installment" button click.
     *
     * @param loan The loan entity to process the payment for.
     */
    private void handlePayInstallment(LoanEntityDto loan) {
        try {
            Account account = loanService.payInstallmentManually(loan);
            if (account == null) {
                Label errorLabel = new Label("Saldo insuficiente para pagar a parcela.");
                errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 14;");
                loansContainer.getChildren().add(errorLabel);
                removeLabelAfterDelay(errorLabel, 3);

                return;
            }

            loadLoanDetails();
            Label successLabel = new Label("Parcela paga com sucesso!");
            successLabel.setStyle("-fx-text-fill: green; -fx-font-size: 14;");
            loansContainer.getChildren().add(successLabel);
            removeLabelAfterDelay(successLabel, 3);

        } catch (Exception e) {
            System.err.println("Erro ao processar pagamento: " + e.getMessage());
            Label errorLabel = new Label("Erro ao processar o pagamento. Tente novamente.");
            errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 14;");
            loansContainer.getChildren().add(errorLabel);
            removeLabelAfterDelay(errorLabel, 3);
        }
    }


    private void removeLabelAfterDelay(Label label, int seconds) {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(seconds), e -> loansContainer.getChildren().remove(label)));
        timeline.setCycleCount(1);
        timeline.play();
    }

    /**
     * Adds a detail line to a VBox with separation and right alignment.
     *
     * @param vbox  The VBox to add the detail to.
     * @param label The label for the detail.
     * @param value The value for the detail.
     */
    private void addDetailToVBox(VBox vbox, String label, String value) {

        HBox hBox = new HBox();
        hBox.setStyle("-fx-padding: 5;");
        hBox.setSpacing(10);


        Label labelName = new Label(label);
        labelName.setStyle("-fx-font-size: 14; -fx-font-weight: bold;");
        HBox.setHgrow(labelName, Priority.ALWAYS);


        Label labelValue = new Label(value);
        labelValue.setStyle("-fx-font-size: 14; -fx-text-alignment: right;");


        hBox.getChildren().addAll(labelName, labelValue);


        vbox.getChildren().add(hBox);


        Separator separator = new Separator();
        vbox.getChildren().add(separator);
    }

    /**
     * Creates a styled status label for a loan.
     *
     * @param loan The loan entity.
     * @return A Label styled based on the loan status.
     */
    private Label createStatusLabel(LoanEntityDto loan) {
        Label statusLabel = new Label(" Request Status: " + loan.getStatus().name());
        statusLabel.setStyle("-fx-font-weight: bold; -fx-padding: 5 10; -fx-border-radius: 10; -fx-background-radius: 10;");

        switch (loan.getStatus().name()) {
            case "APPROVED":
                statusLabel.setStyle(statusLabel.getStyle() + "-fx-background-color: #E6F4EA; -fx-border-color: green; -fx-text-fill: green;");
                break;
            case "REJECTED":
                statusLabel.setStyle(statusLabel.getStyle() + "-fx-background-color: #FDECEC; -fx-border-color: red; -fx-text-fill: red;");
                break;
            case "REVIEW":
                statusLabel.setStyle(statusLabel.getStyle() + "-fx-background-color: #FFF4E5; -fx-border-color: orange; -fx-text-fill: orange;");
                break;
            case "FINISHED":
                statusLabel.setStyle(statusLabel.getStyle() + "-fx-background-color: #E5F0FF; -fx-border-color: blue; -fx-text-fill: blue;");
                break;
            default:
                statusLabel.setStyle(statusLabel.getStyle() + "-fx-background-color: #F5F5F5; -fx-border-color: gray; -fx-text-fill: gray;");
                break;
        }

        return statusLabel;
    }
}
