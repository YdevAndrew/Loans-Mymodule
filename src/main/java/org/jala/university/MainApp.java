package org.jala.university;

import org.jala.university.presentation.MainView;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "org.jala.university.domain.repository")
public class MainApp {
    public static void main(String[] args) {
        MainView.launch(args);
    }

}