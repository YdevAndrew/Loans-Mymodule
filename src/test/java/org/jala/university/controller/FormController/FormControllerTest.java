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
        Platform.startup(() -> {});
    }

    @BeforeEach
    public void setUp() throws Exception {
        // Inicializando os mocks
        MockitoAnnotations.openMocks(this);
        when(loader.load()).thenReturn(new Pane());

        // Criando um arquivo temporário como prova de renda
        incomeProofFile = File.createTempFile("incomeProof", ".pdf");
        Files.write(incomeProofFile.toPath(), "sample content".getBytes());

        // Configurando o campo de salário com valor simulado
        when(salaryField.getText()).thenReturn("2000");

        // Configurando o arquivo de prova de renda diretamente no formController
        formController.incomeProofFile = incomeProofFile;

        // Injetando o mock de formService no formController usando reflexões
        Field field = formController.getClass().getDeclaredField("formService");
        field.setAccessible(true);
        field.set(formController, formService);
    }

    @Test
    public void testSubmitLoanRequestWithInvalidSalary() {
        // Configurando um valor inválido para o campo de salário
        when(salaryField.getText()).thenReturn("invalid_salary");

        // Executando o método de envio da solicitação de empréstimo
        Platform.runLater(() -> formController.submitLoanRequest());
        WaitForAsyncUtils.waitForFxEvents();

        // Verificando que o método save não foi chamado devido ao salário inválido
        verify(formService, times(0)).save(any(FormEntityDto.class));
    }

    @Test
    public void testSubmitLoanRequestWithoutIncomeProof() {
        // Removendo a prova de renda para este teste
        formController.incomeProofFile = null;

        // Executando o método de envio da solicitação de empréstimo
        Platform.runLater(() -> formController.submitLoanRequest());
        WaitForAsyncUtils.waitForFxEvents();

        // Verificando que o método save não foi chamado devido à falta de prova de renda
        verify(formService, times(0)).save(any(FormEntityDto.class));
    }

    @Test
    public void testValidateInputsWithLowSalary() {
        // Configurando um salário baixo para teste de validação
        when(salaryField.getText()).thenReturn("1500");

        // Validando os inputs
        Platform.runLater(() -> {
            boolean isValid = formController.validateInputs(1500);
            assertFalse(isValid, "Expected validation to fail due to low salary");
        });
        WaitForAsyncUtils.waitForFxEvents();
    }
}
