package org.jala.university.controller.MyLoans;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.jala.university.application.dto.LoanEntityDto;
import org.jala.university.application.service.LoanEntityService;
import org.jala.university.application.service.LoanResultsService;
import org.jala.university.domain.entity.enums.PaymentMethod;
import org.jala.university.domain.entity.enums.Status;
import org.jala.university.presentation.controller.MyLoans;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class MyLoansTest {

    private MyLoans myLoans;
    private LoanEntityService loanService;
    private LoanResultsService loanResultsService;

    @BeforeEach
    void setUp() {
        myLoans = new MyLoans();

        // Mock services
        loanService = mock(LoanEntityService.class);
        loanResultsService = mock(LoanResultsService.class);

        // Inject mocks
        myLoans.loanService = loanService;
        myLoans.loanResultsService = loanResultsService;

        // Mock JavaFX components
        Platform.runLater(() -> {
            myLoans.loansContainer = new FlowPane();
            myLoans.statusFilterComboBox = new ComboBox<>(FXCollections.observableArrayList("ALL", "APPROVED", "REJECTED", "REVIEW", "FINISHED"));
            myLoans.statusFilterComboBox.setValue("ALL");
            myLoans.filterBar = new HBox();
        });
    }

    @Test
    void testLoadLoanDetails_allStatuses() {
        LoanEntityDto loan1 = LoanEntityDto.builder()
                .id(1)
                .amountBorrowed(1000.0)
                .totalInterest(200.0)
                .numberOfInstallments(10)
                .valueOfInstallments(120.0)
                .totalPayable(1200.0)
                .paymentMethod(PaymentMethod.DEBIT_ACCOUNT)
                .status(Status.APPROVED)
                .issueDate(LocalDate.now())
                .loanDueDate(LocalDate.now().plusMonths(10))
                .form(null)
                .installments(Collections.emptyList())
                .account(null)
                .build();

        LoanEntityDto loan2 = LoanEntityDto.builder()
                .id(2)
                .amountBorrowed(500.0)
                .totalInterest(50.0)
                .numberOfInstallments(5)
                .valueOfInstallments(110.0)
                .totalPayable(550.0)
                .paymentMethod(PaymentMethod.TICKET)
                .status(Status.REVIEW)
                .issueDate(LocalDate.now())
                .loanDueDate(LocalDate.now().plusMonths(5))
                .form(null)
                .installments(Collections.emptyList())
                .account(null)
                .build();

        when(loanService.findAll()).thenReturn(Arrays.asList(loan1, loan2));

        Platform.runLater(() -> {

            myLoans.loadLoanDetails();

            assertEquals(2, myLoans.loansContainer.getChildren().size());
        });
    }

    @Test
    void testLoadLoanDetails_filteredByStatus() {
        // Mock loan data using builder
        LoanEntityDto loan1 = LoanEntityDto.builder()
                .id(1)
                .amountBorrowed(1000.0)
                .totalInterest(200.0)
                .numberOfInstallments(10)
                .valueOfInstallments(120.0)
                .totalPayable(1200.0)
                .paymentMethod(PaymentMethod.DEBIT_ACCOUNT)
                .status(Status.APPROVED)
                .issueDate(LocalDate.now())
                .loanDueDate(LocalDate.now().plusMonths(10))
                .build();

        LoanEntityDto loan2 = LoanEntityDto.builder()
                .id(2)
                .amountBorrowed(500.0)
                .totalInterest(50.0)
                .numberOfInstallments(5)
                .valueOfInstallments(110.0)
                .totalPayable(550.0)
                .paymentMethod(PaymentMethod.TICKET)
                .status(Status.REVIEW)
                .issueDate(LocalDate.now())
                .loanDueDate(LocalDate.now().plusMonths(5))
                .build();

        when(loanService.findAll()).thenReturn(Arrays.asList(loan1, loan2));

        Platform.runLater(() -> {

            myLoans.statusFilterComboBox.setValue("APPROVED");


            myLoans.loadLoanDetails();


            assertEquals(1, myLoans.loansContainer.getChildren().size());
        });
    }

    @Test
    void testCreateLoanBox() {

        LoanEntityDto loan = LoanEntityDto.builder()
                .id(1)
                .amountBorrowed(1000.0)
                .totalInterest(200.0)
                .numberOfInstallments(10)
                .valueOfInstallments(120.0)
                .totalPayable(1200.0)
                .paymentMethod(PaymentMethod.DEBIT_ACCOUNT)
                .status(Status.APPROVED)
                .issueDate(LocalDate.now())
                .loanDueDate(LocalDate.now().plusMonths(10))
                .build();

        Platform.runLater(() -> {
            // Create loan box
            VBox loanBox = myLoans.createLoanBox(loan, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

            // Verify loan box contains expected details
            assertEquals(8, loanBox.getChildren().size());
        });
    }
}