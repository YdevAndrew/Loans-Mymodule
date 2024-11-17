package org.jala.university.presentation.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.jala.university.application.dto.LoanEntityDto;
import org.jala.university.application.service.LoanEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class MyLoans {

    @Autowired
    @Qualifier("loanEntityService")
    private LoanEntityService loanService;

    @FXML
    private VBox loansContainer;

    private Label noLoanLabel; // Label "No loans found"

    public void initialize() {
        // Initialize the "No loans found" label
        noLoanLabel = new Label("No loans found.");
        noLoanLabel.setStyle("-fx-font-size: 16; -fx-text-fill: gray;");
        loansContainer.getChildren().add(noLoanLabel); // Add initially
    }

    public void loadLoanDetails() {
        // Fetch all loans
        List<LoanEntityDto> loans = loanService.findAll();

        // Clear previous items
        loansContainer.getChildren().clear();

        if (loans != null && !loans.isEmpty()) {
            // Define um formatter para a data
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            for (LoanEntityDto loan : loans) {
                // Status indicator row
                HBox statusRow = new HBox(10);

                // Label para o status com estilo de borda colorida
                Label statusLabel = new Label(" Request Status: " + loan.getStatus().name());
                statusLabel.setStyle("-fx-font-weight: bold; -fx-padding: 5 10; -fx-border-radius: 10; -fx-background-radius: 10;");

                // Define a cor do status
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

                // Adiciona o Label de status
                statusRow.getChildren().add(statusLabel);

                // Adiciona o statusRow ao loansContainer
                loansContainer.getChildren().add(statusRow);

                // Separator para o status
                loansContainer.getChildren().add(new Separator());

                // Formata a data de emissão
                String formattedIssueDate = loan.getIssueDate().format(dateFormatter);

                // Criação das linhas de detalhes alinhadas à direita
                loansContainer.getChildren().add(createDetailRow("Date of Request:", formattedIssueDate));
                loansContainer.getChildren().add(createDetailRow("Amount Borrowed:", String.format("R$ %.2f", loan.getAmountBorrowed())));
                loansContainer.getChildren().add(createDetailRow("Installment Amount:", String.format("R$ %.2f", loan.getValueOfInstallments())));
                loansContainer.getChildren().add(createDetailRow("Total Interest:", String.format("R$ %.2f", loan.getTotalInterest())));
                loansContainer.getChildren().add(createDetailRow("Total Amount + Interest:", String.format("R$ %.2f", loan.getAmountBorrowed() + loan.getTotalInterest())));
                loansContainer.getChildren().add(createDetailRow("Payment Method:", loan.getPaymentMethod().name()));

                // Exibir o número de parcelas pagas
                long paidInstallments = loanService.getPaidInstallments(loan);
                loansContainer.getChildren().add(createDetailRow("Paid Installments:", String.format("%d / %d", paidInstallments, loan.getNumberOfInstallments())));

                // Adiciona um separator após os detalhes do empréstimo
                loansContainer.getChildren().add(new Separator());
            }
        } else {
            // Se não houver empréstimos, exibe o label "No loans found"
            loansContainer.getChildren().add(noLoanLabel);
        }
    }

    /**
     * Cria uma linha de detalhe alinhada com rótulo à esquerda e valor à direita.
     */
    private HBox createDetailRow(String label, String value) {
        HBox detailRow = new HBox();
        detailRow.setSpacing(10);
        detailRow.setStyle("-fx-padding: 5;");

        Label labelLeft = new Label(label);
        labelLeft.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");
        Label valueRight = new Label(value);
        valueRight.setStyle("-fx-font-size: 14;");

        // Alinha o valor à direita
        HBox.setHgrow(valueRight, javafx.scene.layout.Priority.ALWAYS);
        valueRight.setMaxWidth(Double.MAX_VALUE);
        valueRight.setStyle("-fx-alignment: center-right;");

        detailRow.getChildren().addAll(labelLeft, valueRight);
        return detailRow;
    }

}