package org.jala.university.presentation;

import javafx.fxml.FXMLLoader;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * A utility class for loading FXML files with Spring's dependency injection support.
 * This loader integrates JavaFX's `FXMLLoader` with the Spring application context to resolve controllers.
 */
@Component
public class SpringFXMLLoader {

    private final ApplicationContext context;

    /**
     * Constructor for `SpringFXMLLoader`.
     *
     * @param context the Spring application context used to resolve FXML controllers.
     */
    public SpringFXMLLoader(ApplicationContext context) {
        this.context = context;
    }

    /**
     * Loads an FXML file and sets up its controller using Spring's dependency injection.
     *
     * @param fxmlPath the path to the FXML file to be loaded.
     * @return an `FXMLLoader` instance configured with the specified FXML file and Spring-managed controllers.
     */
    public FXMLLoader load(String fxmlPath) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        loader.setControllerFactory(context::getBean);
        return loader;
    }
}
