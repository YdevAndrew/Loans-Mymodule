//package org.jala.university.controller.FormController;
//
//import static org.mockito.Mockito.*;
//import static org.junit.jupiter.api.Assertions.*;
//
//import java.io.File;
//import java.io.IOException;
//import java.lang.reflect.Field;
//import java.nio.file.Files;
//
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.stage.Stage;
//import org.jala.university.application.dto.FormEntityDto;
//import org.jala.university.application.service.FormEntityService;
//import org.jala.university.application.service.LoanEntityService;
//import org.jala.university.presentation.SpringFXMLLoader;
//import org.jala.university.presentation.controller.FormControllerLoan;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.testfx.api.FxRobot;
//import org.testfx.framework.junit5.ApplicationExtension;
//import org.testfx.framework.junit5.Start;
//
//@ExtendWith(ApplicationExtension.class)
//public class FormControllerTest extends FxRobot {
//
//    @Mock
//    private SpringFXMLLoader springFXMLLoader;
//
//    @Mock
//    private FormEntityService formService;
//
//    @InjectMocks
//    private FormControllerLoan formController;
//
//    private File incomeProofFile;
//
//    @Start
//    public void start(Stage stage) throws Exception {
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FormLoan/form.fxml"));
//        Parent root = loader.load();
//        formController = loader.getController();
//
//        // Inject mocks into the controller
//        injectMocksIntoController();
//
//        stage.setScene(new Scene(root));
//        stage.show();
//    }
//
//    @BeforeEach
//    public void setUp() throws Exception {
//        MockitoAnnotations.openMocks(this);
//
//        incomeProofFile = File.createTempFile("incomeProof", ".pdf");
//        Files.write(incomeProofFile.toPath(), "sample content".getBytes());
//        formController.incomeProofFile = incomeProofFile;
//    }
//
//    private void injectMocksIntoController() throws Exception {
//
//        // Injeta o mock do formService
//        Field formServiceField = formController.getClass().getDeclaredField("formService");
////        formServiceField.setAccessible(true);
//        formServiceField.set(formController, formService);
//
//        // Injeta o mock do loanService
//        Field loanServiceField = formController.getClass().getDeclaredField("loanService");
//        loanServiceField.setAccessible(true);
//        loanServiceField.set(formController, mock(LoanEntityService.class));
//
//        Field springFXMLLoaderField = formController.getClass().getDeclaredField("springFXMLLoader");
//        springFXMLLoaderField.setAccessible(true);
//        springFXMLLoaderField.set(formController, springFXMLLoader);
//
//
//    }
//
//    @Test
//    public void testSubmitLoanRequestWithInvalidSalary() {
//        clickOn("#salaryField").write("invalid_salary");
//        clickOn("#incomeProofButton"); // Simulate choosing a file
//        clickOn("#submitButton");
//
//        verify(formService, times(0)).save(any(FormEntityDto.class));
//    }
//
//    @Test
//    public void testSubmitLoanRequestWithoutIncomeProof() {
//        clickOn("#salaryField").write("2000");
//        clickOn("#submitButton");
//
//        verify(formService, times(0)).save(any(FormEntityDto.class));
//    }
//}