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

    public void load() {
        try {
            FXMLLoader loader = new FXMLLoader(LoanListViewController.class.getClassLoader().getResource(LoansView.LOAN_LIST.getView().getFileName()));
            view = loader.load();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
