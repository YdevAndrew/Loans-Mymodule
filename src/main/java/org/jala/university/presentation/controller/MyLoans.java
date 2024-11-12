package org.jala.university.presentation.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.jala.university.application.dto.LoanEntityDto;
import org.jala.university.application.service.LoanEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class MyLoans {

    @Autowired
    @Qualifier("loanEntityService")
    private LoanEntityService loanService;

    @FXML
    private Label loanAmountLabel;

    @FXML
    private Label installmentValueLabel;

    @FXML
    private VBox loansContainer;

    public void loadLoanDetails() {
        // Search all loans
        List<LoanEntityDto> loans = loanService.findAll();

        // Checks for loans
        if (loans != null && !loans.isEmpty()) {
            loansContainer.getChildren().clear();

            for (LoanEntityDto loan : loans) {

                // Simplified formatting of the installment value with two decimal places
                String installmentValueFormatted = String.format("R$ %.2f", loan.getValueOfInstallments());

                Label loanLabel = new Label(String.format(
                        "Loan ID: %d | Amount: R$ %.2f | Installments: %d | Installment Amount: %s | Status: %s",
                        loan.getId(),
                        loan.getAmountBorrowed(),
                        loan.getNumberOfInstallments(),
                        installmentValueFormatted,
                        loan.getStatus().name()
                ));

                loansContainer.getChildren().add(loanLabel);
            }
        } else {
            Label noLoanLabel = new Label("No loans found.");
            loansContainer.getChildren().add(noLoanLabel);
        }
    }
}

