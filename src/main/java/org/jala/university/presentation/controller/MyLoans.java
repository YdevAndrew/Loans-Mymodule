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
        // Busca todos os empréstimos
        List<LoanEntityDto> loans = loanService.findAll();

        // Verifica se há empréstimos
        if (loans != null && !loans.isEmpty()) {
            loansContainer.getChildren().clear();

            for (LoanEntityDto loan : loans) {

                // Formatação simplificada do valor da parcela com duas casas decimais
                String installmentValueFormatted = String.format("R$ %.2f", loan.getValueOfInstallments());

                Label loanLabel = new Label(String.format(
                        "Empréstimo ID: %d | Valor: R$ %.2f | Parcelas: %d | Valor da Parcela: %s | Status: %s",
                        loan.getId(),
                        loan.getAmountBorrowed(),
                        loan.getNumberOfInstallments(),
                        installmentValueFormatted,
                        loan.getStatus().name()
                ));

                loansContainer.getChildren().add(loanLabel);
            }
        } else {
            Label noLoanLabel = new Label("Nenhum empréstimo encontrado.");
            loansContainer.getChildren().add(noLoanLabel);
        }
    }
}

