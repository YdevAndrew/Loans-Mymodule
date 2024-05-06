package org.jala.university.presentation;

import lombok.Getter;
import org.jala.university.commons.presentation.View;

@Getter
public enum LoansView {
    MAIN("main-view.fxml"),
<<<<<<< src/main/java/org/jala/university/presentation/LoansView.java
    LOAN_REQUEST_VIEW_PAGE1("loan-request-form-view.fxml"),
    LOAN_REQUEST_VIEW_PAGE2("loan-request-form-page2-view.fxml"),
    LOAN_REQUEST_VIEW_PAGE3("loan-request-form-page3-view.fxml");
    LOAN_LIST("loan-list-view.fxml");
=======
    LOAN_LIST("loan-list-view.fxml");
>>>>>>> src/main/java/org/jala/university/presentation/LoansView.java

    private final View view;

    LoansView(String fileName) {
        this.view = new View(fileName);
    }
}
