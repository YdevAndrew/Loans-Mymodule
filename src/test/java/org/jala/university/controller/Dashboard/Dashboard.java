package org.jala.university.controller.Dashboard;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.jala.university.presentation.SpringFXMLLoader;
import org.jala.university.presentation.controller.Dashboard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.framework.junit5.Start;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DashboardTest extends ApplicationTest {

    @Mock
    private SpringFXMLLoader springFXMLLoader;

    @InjectMocks
    private Dashboard dashboard;

    private Button toggleButton;
    private Label balanceLabel;
    private Label dateLabel;
    private ImageView eyeIcon;
    private VBox myCardsVBox;
    private VBox otherButtonsVBox;

    @Start
    public void start(javafx.stage.Stage stage) {
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        // Inicializando os componentes JavaFX mockados manualmente.
        toggleButton = new Button();
        balanceLabel = new Label("R$ ********");
        dateLabel = new Label();
        eyeIcon = new ImageView();
        myCardsVBox = new VBox();
        otherButtonsVBox = new VBox();

        // Setando os componentes no controller.
        dashboard.toggleButton = toggleButton;
        dashboard.balanceLabel = balanceLabel;
        dashboard.dateLabel = dateLabel;
        dashboard.eyeIcon = eyeIcon;
        dashboard.myCardsVBox = myCardsVBox;
        dashboard.otherButtonsVBox = otherButtonsVBox;

        // Chamando o método de inicialização.
        dashboard.initialize();
    }

    @Test
    void testToggleBalanceVisibility_ShowsBalance() {
        // Ação: Clicar no botão para exibir o saldo.
        toggleButton.fire();

        // Verificação: Checar se o texto e o ícone foram atualizados.
        assertEquals("R$ 1234.56", balanceLabel.getText());
        assertEquals("/images/eye_open.png", eyeIcon.getImage().getUrl());
    }

    @Test
    void testToggleBalanceVisibility_HidesBalance() {
        // Ação: Primeiro, exibe o saldo.
        toggleButton.fire();
        // Ação: Oculta o saldo.
        toggleButton.fire();

        // Verificação: Checar se o saldo e o ícone estão ocultos.
        assertEquals("R$ ********", balanceLabel.getText());
        assertEquals("/images/eye.png", eyeIcon.getImage().getUrl());
    }

    @Test
    void testInitialize_SetsDateLabel() {
        // Verificação: Checar se o rótulo de data está no formato correto.
        assertTrue(dateLabel.getText().matches("^[A-Za-z]+, \\d{2}$"));
    }

    @Test
    void testHandleMyCardsClick_TogglesVisibility() {
        // Inicialmente, os cartões não devem estar visíveis.
        myCardsVBox.setVisible(false);
        otherButtonsVBox.setVisible(true);

        // Ação: Clicar para exibir os cartões.
        dashboard.handleMyCardsClick();

        // Verificação: Checar se a visibilidade foi alterada.
        assertTrue(myCardsVBox.isVisible());
        assertTrue(otherButtonsVBox.isVisible());
    }
}
