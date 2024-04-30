package org.jala.university.presentation.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.jala.university.application.service.LoansService;
import org.jala.university.application.service.LoansServiceImpl;


public class MainViewController {

    @FXML
    private AnchorPane menuViewPane;
    @FXML
    private LoanListViewController loanListViewController;
    private Node currentMenuView;
    @FXML
    public void initialize() {
        loanListViewController = new LoanListViewController();
        loanListViewController.load();

        menuViewPane.getChildren().add(loanListViewController.getView());
        loanListViewController.getView().setVisible(false);
    }

    private void setCurrentMenuView(Node node) {
        if (node.equals(currentMenuView)) {
            return;
        }
        currentMenuView = node;
    }

    private void setInvisibleCurrentMenuView() {
        if (currentMenuView == null) {
            return;
        }
        currentMenuView.setVisible(false);
    }

    @FXML
    public void handleLoanListButton(ActionEvent event) {
        setInvisibleCurrentMenuView();
        setCurrentMenuView(loanListViewController.getView());
        currentMenuView.setVisible(true);
    }
}
