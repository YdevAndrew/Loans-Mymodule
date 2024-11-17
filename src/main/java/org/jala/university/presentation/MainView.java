package org.jala.university.presentation;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jala.university.commons.presentation.ViewSwitcher;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan(basePackages = {"org.jala.university"})
public class MainView extends Application {

    private ConfigurableApplicationContext context;
    private SpringFXMLLoader springFXMLLoader;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() throws Exception {
        context = new SpringApplicationBuilder(MainView.class).run();
        springFXMLLoader = context.getBean(SpringFXMLLoader.class);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = springFXMLLoader.load("/board/DashboardApp.fxml");
        Scene scene = new Scene(loader.load());
        ViewSwitcher.setup(primaryStage, scene);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Loans Module Application");
        primaryStage.setMinWidth(1280);
        primaryStage.setMinHeight(720);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        context.close();
    }
}
