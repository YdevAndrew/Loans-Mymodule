package org.jala.university.controller.MainViewController;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import org.jala.university.presentation.SpringFXMLLoader;
import org.jala.university.presentation.controller.MainViewControllerLoan;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testfx.framework.junit5.ApplicationExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(ApplicationExtension.class)
class MainViewControllerTest {

    @Mock
    private SpringFXMLLoader springFXMLLoader;

    private Pane mainPane;
    private Button loanButton;
    private List<ImageView> imageViews;

    private MainViewControllerLoan mainViewController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);


        mainPane = new Pane();
        loanButton = new Button();
        imageViews = new ArrayList<>();

        for (int i = 0; i < 0; i++) {
            imageViews.add(new ImageView());
        }

        // Configura o controlador com os componentes reais
        mainViewController = new MainViewControllerLoan();
        mainViewController.mainPane = mainPane;
        mainViewController.loanButton = loanButton;
        mainViewController.imageViews = imageViews;


        mainViewController.springFXMLLoader = springFXMLLoader;
    }

    @Test
    void testInitialize() {
        mainViewController.initialize();
        assertEquals(16, mainViewController.imageViews.size(), "Deveria haver 16 imagens na lista de imageViews.");
    }

    @Test
    void testStartLoanSimulation() throws Exception {

        FXMLLoader loaderMock = mock(FXMLLoader.class);
        Pane loanPane = new Pane();

        when(springFXMLLoader.load("/FormLoan/form.fxml")).thenReturn(loaderMock);
        when(loaderMock.load()).thenReturn(loanPane);

        mainViewController.startLoanSimulation();

        assertEquals(1, mainPane.getChildren().size(), "mainPane deveria conter o loanPane.");
        assertEquals(loanPane, mainPane.getChildren().get(0), "loanPane deveria ter sido adicionado ao mainPane.");
    }

    @Test
    void testGoBackToMenu() {
        mainViewController.goBackToMenu();

        assertEquals(1, mainPane.getChildren().size(), "mainPane deveria conter apenas o loanButton.");
        assertEquals(loanButton, mainPane.getChildren().get(0), "loanButton deveria estar visível.");

        mainViewController.imageViews.forEach(image -> {
            assertEquals(true, image.isVisible(), "Imagem deve estar visível.");
            assertEquals(true, image.isManaged(), "Imagem deve estar gerenciada.");
        });
    }

    @Test
    void testToggleVisibility() {
        mainViewController.toggleVisibility(false);

        assertEquals(false, loanButton.isVisible(), "O botão de empréstimo deve estar invisível.");
        assertEquals(false, loanButton.isManaged(), "O botão de empréstimo deve estar fora do layout.");

        mainViewController.imageViews.forEach(image -> {
            assertEquals(false, image.isVisible(), "As imagens devem estar invisíveis.");
            assertEquals(false, image.isManaged(), "As imagens devem estar fora do layout.");
        });
    }
}