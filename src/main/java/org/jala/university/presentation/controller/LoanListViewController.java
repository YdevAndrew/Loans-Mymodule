package org.jala.university.presentation.controller;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import lombok.Getter;
import org.jala.university.commons.presentation.ViewSwitcher;
import org.jala.university.presentation.LoansView;

import java.awt.event.MouseEvent;
import java.io.IOException;

public class LoanListViewController {

    @Getter
    private Parent view;

    @FXML
    private Node additionalOptionsPane;

    private Node selectedLoanCard;

    public void load() {
        try {
            FXMLLoader loader = new FXMLLoader(LoanListViewController.class.getClassLoader().getResource(LoansView.LOAN_LIST.getView().getFileName()));
            view = loader.load();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        hideAdditionalOptionsPane();
    }


    private void toggleAdditionalOptionsPane(boolean toggle) {
        if (toggle) {
            showAdditionalOptionsPane();
        } else {
            hideAdditionalOptionsPane();
        }
    }
    private void hideAdditionalOptionsPane() {
        additionalOptionsPane.setDisable(true);
    }

    private void showAdditionalOptionsPane() {
        additionalOptionsPane.setDisable(false);
    }

    @FXML
    public void toggleLoanCard(Event event) {
        Node loanCard = (Node) event.getSource();
        if (loanCard.equals(selectedLoanCard)) {
            selectedLoanCard.setStyle(
                    "-fx-background-color: #E0E0E0;" +
                    "-fx-background-radius: 10px;");
            selectedLoanCard = null;
            toggleAdditionalOptionsPane(false);
        }
        else {
            selectedLoanCard = loanCard;
            selectedLoanCard.setStyle(
                    "-fx-background-color: #BEBEBE;" +
                    "-fx-background-radius: 5px;");
            toggleAdditionalOptionsPane(true);
        }
    }

}
