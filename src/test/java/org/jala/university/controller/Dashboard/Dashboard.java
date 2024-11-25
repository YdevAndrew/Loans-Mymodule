package org.jala.university.controller.Dashboard;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritablePixelFormat;
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

import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

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
        MockitoAnnotations.openMocks(this);

        // Mockando os componentes JavaFX
        toggleButton = new Button();
        balanceLabel = new Label("R$ ********");
        dateLabel = new Label();
        eyeIcon = new ImageView();
        myCardsVBox = new VBox();
        otherButtonsVBox = new VBox();

        // Injetando os componentes no controlador
        dashboard.toggleButton = toggleButton;
        dashboard.balanceLabel = balanceLabel;
        dashboard.dateLabel = dateLabel;
        dashboard.eyeIcon = eyeIcon;
        dashboard.myCardsVBox = myCardsVBox;
        dashboard.otherButtonsVBox = otherButtonsVBox;

        // Inicialização do controlador
        dashboard.initialize();
    }

    @Test
    void testToggleBalanceVisibility_ShowsBalance() {
        toggleButton.fire();
        Image expectedImage = loadImage("/images/eye_open.png");
        assertTrue(imagesAreEqual(expectedImage, eyeIcon.getImage()));
        assertEquals("R$ 1234,56", balanceLabel.getText());
    }

    @Test
    void testToggleBalanceVisibility_HidesBalance() {
        toggleButton.fire();
        toggleButton.fire();
        Image expectedImage = loadImage("/images/eye.png");
        assertTrue(imagesAreEqual(expectedImage, eyeIcon.getImage()));
        assertEquals("R$ ********", balanceLabel.getText());
    }

    @Test
    void testInitialize_SetsDateLabel() {
        assertTrue(dateLabel.getText().matches("^[A-Za-z]+, \\d{2}$"));
    }

    @Test
    void testHandleMyCardsClick_TogglesVisibility() {
        myCardsVBox.setVisible(false);
        otherButtonsVBox.setVisible(true);
        dashboard.handleMyCardsClick();
        assertTrue(myCardsVBox.isVisible());
        assertTrue(otherButtonsVBox.isVisible());
    }

    // Funções auxiliares
    private boolean imagesAreEqual(Image img1, Image img2) {
        if (img1.getWidth() != img2.getWidth() || img1.getHeight() != img2.getHeight()) {
            return false; // Imagens de tamanhos diferentes
        }

        PixelReader reader1 = img1.getPixelReader();
        PixelReader reader2 = img2.getPixelReader();

        WritablePixelFormat<java.nio.ByteBuffer> pixelFormat = WritablePixelFormat.getByteBgraInstance();
        int width = (int) img1.getWidth();
        int height = (int) img1.getHeight();
        byte[] pixels1 = new byte[width * height * 4];
        byte[] pixels2 = new byte[width * height * 4];

        reader1.getPixels(0, 0, width, height, pixelFormat, pixels1, 0, width * 4);
        reader2.getPixels(0, 0, width, height, pixelFormat, pixels2, 0, width * 4);

        return Arrays.equals(pixels1, pixels2);
    }

    private Image loadImage(String path) {
        return new Image(Objects.requireNonNull(getClass().getResourceAsStream(path)));
    }
}
