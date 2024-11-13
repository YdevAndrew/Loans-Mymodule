package org.jala.university.controller.FormController;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.nio.file.Files;
import java.util.Base64;
import java.lang.reflect.Field;

import javafx.fxml.FXMLLoader;
import org.jala.university.application.dto.FormEntityDto;
import org.jala.university.application.service.FormEntityService;
import org.jala.university.presentation.SpringFXMLLoader;
import org.jala.university.presentation.controller.FormController;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.util.WaitForAsyncUtils;

@ExtendWith(ApplicationExtension.class)
public class FormControllerTest {

    @Mock
    private SpringFXMLLoader springFXMLLoader;

    @Mock
    private FXMLLoader loader;

    @Mock
    private FormEntityService formService;

    @Mock
    private TextField salaryField;

    @Mock
    private Button incomeProofButton;

    @Mock
    private Pane mainPane;

    @InjectMocks
    private FormController formController;

    private File incomeProofFile;

    @BeforeAll
    public static void initToolkit() {
        // Inicia o JavaFX toolkit antes de qualquer teste
        Platform.startup(() -> {});
    }

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        
        when(loader.load()).thenReturn(new Pane());
        when(salaryField.getText()).thenReturn("2000");

       
        incomeProofFile = File.createTempFile("incomeProof", ".pdf");
        Files.write(incomeProofFile.toPath(), "sample content".getBytes());

        formController.incomeProofFile = incomeProofFile;

        Field salaryFieldField = formController.getClass().getDeclaredField("salaryField");
        salaryFieldField.setAccessible(true);
        salaryFieldField.set(formController, salaryField);

        Field formServiceField = formController.getClass().getDeclaredField("formService");
        formServiceField.setAccessible(true);
        formServiceField.set(formController, formService);
    }

    @Test
    public void testSubmitLoanRequestWithInvalidSalary() {
        when(salaryField.getText()).thenReturn("invalid_salary");

        Platform.runLater(() -> formController.submitLoanRequest());
        WaitForAsyncUtils.waitForFxEvents();

        verify(formService, times(0)).save(any(FormEntityDto.class));
    }

    @Test
    public void testSubmitLoanRequestWithoutIncomeProof() {
        formController.incomeProofFile = null;

        Platform.runLater(() -> formController.submitLoanRequest());
        WaitForAsyncUtils.waitForFxEvents();

        verify(formService, times(0)).save(any(FormEntityDto.class));
    }

    @Test
    public void testValidateInputsWithLowSalary() {
        when(salaryField.getText()).thenReturn("1500");

        Platform.runLater(() -> {
            boolean isValid = formController.validateInputs(1500);
            assertFalse(isValid, "Expected validation to fail due to low salary");
        });
        WaitForAsyncUtils.waitForFxEvents();
    }
}
