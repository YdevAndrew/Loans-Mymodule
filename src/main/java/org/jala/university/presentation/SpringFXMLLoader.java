package org.jala.university.presentation;

import javafx.fxml.FXMLLoader;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Utility class to load FXML files with Spring dependency injection support.
 * Integrates Spring's {@link ApplicationContext} with JavaFX's {@link FXMLLoader}.
 */
@Component
public class SpringFXMLLoader {

    private final ApplicationContext context;

    /**
     * Constructor for SpringFXMLLoader.
     *
     * @param context the Spring application context used to resolve beans
     */
    public SpringFXMLLoader(ApplicationContext context) {
        this.context = context;
    }

    /**
     * Loads an FXML file and sets the controller factory to use Spring's application context.
     *
     * @param fxmlPath the path to the FXML file to be loaded
     * @return an instance of {@link FXMLLoader} with Spring integration
     */
    public FXMLLoader load(String fxmlPath) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        loader.setControllerFactory(context::getBean);
        return loader;
    }
}
