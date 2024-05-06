package org.jala.university.presentation.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.jala.university.application.service.LoansService;
import org.jala.university.application.service.LoansServiceImpl;
import org.jala.university.commons.presentation.BaseController;

import java.net.URL;
import java.util.ResourceBundle;


public class MainViewController extends BaseController implements Initializable {

    @FXML
    private SplitPane mainSplitPane;

    @FXML
    private AnchorPane menuViewPane;
    @FXML
    private LoanListViewController loanListViewController;
    private Node currentMenuView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        injectLoanListController();
        fixMainSplitDivider();
    }

    private void  injectLoanListController() {
        loanListViewController = new LoanListViewController();
        loanListViewController.load();
        loanListViewController.getView().setVisible(false);

        menuViewPane.getChildren().add(loanListViewController.getView());
    }

    private void fixMainSplitDivider() {
        final double DEFAULT_SPLIT_DIVIDER_POSITION = 0.3;
        mainSplitPane.setDividerPosition(0, DEFAULT_SPLIT_DIVIDER_POSITION);
        mainSplitPane.getDividers().get(0).positionProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                mainSplitPane.getDividers().get(0).setPosition(DEFAULT_SPLIT_DIVIDER_POSITION);
            }
        });;
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
