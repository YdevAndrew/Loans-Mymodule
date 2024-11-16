package org.jala.university;

import org.jala.university.presentation.MainView;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MainApp {
    public static void main(String[] args) {
        MainView.launch(args);
    }

}