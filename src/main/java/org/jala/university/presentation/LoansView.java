package org.jala.university.presentation;

import lombok.Getter;
import org.jala.university.commons.presentation.View;

@Getter
public enum LoansView {
    MAIN("main-view.fxml"),

    LOAN_REQUEST_VIEW("loan-request-form-view.fxml"),
    LOAN_REQUEST_VIEW_PAGE2("loan-request-form-page2-view.fxml"),
    LOAN_REQUEST_VIEW_PAGE3("loan-request-form-page3-view.fxml"),
    LOAN_LIST("loan-list-view.fxml");

    private final View view;

    LoansView(String fileName) {
        this.view = new View(fileName);
    }
}
