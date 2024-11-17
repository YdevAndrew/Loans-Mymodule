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

/**
 * Controller responsible for managing and displaying the user's loan details.
 */
@Controller
public class MyLoans {

    @Autowired
    @Qualifier("loanEntityService")
    private LoanEntityService loanService;

    @FXML
    private VBox loansContainer;

    private Label noLoanLabel; // Label to display when no loans are found.

    /**
     * Initializes the controller and sets up the "No loans found" label.
     */
    public void initialize() {
        // Initialize the "No loans found" label
        noLoanLabel = new Label("No loans found.");
        noLoanLabel.setStyle("-fx-font-size: 16; -fx-text-fill: gray;");
        loansContainer.getChildren().add(noLoanLabel); // Add initially
    }

    /**
     * Loads loan details and displays them in the user interface.
     * Fetches data from the service and dynamically updates the container with loan information.
     */
    public void loadLoanDetails() {
        // Fetch all loans
        List<LoanEntityDto> loans = loanService.findAll();

        // Clear previous items
        loansContainer.getChildren().clear();

        if (loans != null && !loans.isEmpty()) {
            // Define a formatter for the date
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            for (LoanEntityDto loan : loans) {
                // Status indicator row
                HBox statusRow = new HBox(10);

                // Label for loan status with custom styling
                Label statusLabel = new Label(" Request Status: " + loan.getStatus().name());
                statusLabel.setStyle("-fx-font-weight: bold; -fx-padding: 5 10; -fx-border-radius: 10; -fx-background-radius: 10;");

                // Set color and style based on status
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

                // Add the status label to the row
                statusRow.getChildren().add(statusLabel);

                // Add the status row to the loans container
                loansContainer.getChildren().add(statusRow);

                // Add a separator for the status
                loansContainer.getChildren().add(new Separator());

                // Format the issue date
                String formattedIssueDate = loan.getIssueDate().format(dateFormatter);

                // Create rows for each loan detail and add them to the container
                loansContainer.getChildren().add(createDetailRow("Date of Request:", formattedIssueDate));
                loansContainer.getChildren().add(createDetailRow("Amount Borrowed:", String.format("R$ %.2f", loan.getAmountBorrowed())));
                loansContainer.getChildren().add(createDetailRow("Installment Amount:", String.format("R$ %.2f", loan.getValueOfInstallments())));
                loansContainer.getChildren().add(createDetailRow("Total Interest:", String.format("R$ %.2f", loan.getTotalInterest())));
                loansContainer.getChildren().add(createDetailRow("Total Amount + Interest:", String.format("R$ %.2f", loan.getAmountBorrowed() + loan.getTotalInterest())));
                loansContainer.getChildren().add(createDetailRow("Payment Method:", loan.getPaymentMethod().name()));

                // Display the number of paid installments
                long paidInstallments = loanService.getPaidInstallments(loan);
                loansContainer.getChildren().add(createDetailRow("Paid Installments:", String.format("%d / %d", paidInstallments, loan.getNumberOfInstallments())));

                // Add a separator after the loan details
                loansContainer.getChildren().add(new Separator());
            }
        } else {
            // If no loans are found, display the "No loans found" label
            loansContainer.getChildren().add(noLoanLabel);
        }
    }

    /**
     * Creates a detail row with a label on the left and a value aligned to the right.
     *
     * @param label the label text
     * @param value the value text
     * @return an HBox containing the detail row
     */
    private HBox createDetailRow(String label, String value) {
        HBox detailRow = new HBox();
        detailRow.setSpacing(10);
        detailRow.setStyle("-fx-padding: 5;");

        Label labelLeft = new Label(label);
        labelLeft.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");
        Label valueRight = new Label(value);
        valueRight.setStyle("-fx-font-size: 14;");

        // Align the value to the right
        HBox.setHgrow(valueRight, javafx.scene.layout.Priority.ALWAYS);
        valueRight.setMaxWidth(Double.MAX_VALUE);
        valueRight.setStyle("-fx-alignment: center-right;");

        detailRow.getChildren().addAll(labelLeft, valueRight);
        return detailRow;
    }

}
