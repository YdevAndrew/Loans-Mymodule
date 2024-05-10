package org.jala.university.presentation.controller;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class ShowAlert {
    public void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }

    public void showInformationAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }

    public boolean showConfirmationAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
        return alert.getResult() == ButtonType.OK;
    }
}
