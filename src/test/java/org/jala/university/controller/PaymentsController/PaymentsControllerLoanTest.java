package org.jala.university.controller.PaymentsController;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import org.jala.university.application.dto.FormEntityDto;
import org.jala.university.application.dto.LoanEntityDto;
import org.jala.university.application.mapper.FormEntityMapper;
import org.jala.university.application.service.FormEntityService;
import org.jala.university.application.service.LoanEntityService;
import org.jala.university.domain.entity.FormEntity;
import org.jala.university.domain.entity.enums.PaymentMethod;
import org.jala.university.presentation.controller.PaymentsControllerLoan;
import org.jala.university.utils.CalculationUtil;
import org.jala.university.utils.DateFormmaterUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PaymentsControllerLoanTest {

    private PaymentsControllerLoan paymentsController;
    private FormEntityService formService;
    private LoanEntityService loanService;
    private FormEntityMapper formEntityMapper;
    private DateFormmaterUtil dateFormmaterUtil;

    @BeforeAll
    static void initToolkit() {
        if (!Platform.isFxApplicationThread()) {
            Platform.startup(() -> {});
        }
    }

    @BeforeEach
    void setUp() {
        paymentsController = new PaymentsControllerLoan();

        // Mocks
        formService = mock(FormEntityService.class);
        loanService = mock(LoanEntityService.class);
        formEntityMapper = mock(FormEntityMapper.class);
        dateFormmaterUtil = mock(DateFormmaterUtil.class);

        // Inject mocks
        paymentsController.formService = formService;
        paymentsController.loanService = loanService;
        paymentsController.formEntityMapper = formEntityMapper;
        paymentsController.dateFormatterUtil = dateFormmaterUtil;

        // Mock JavaFX components
        paymentsController.dueDateLabel = new Label();
        paymentsController.submitButton = new Button();
        paymentsController.loanAmountSlider = new Slider();
        paymentsController.installmentsComboBox = new ComboBox<>();
        paymentsController.paymentMethodComboBox = new ComboBox<>();
        paymentsController.loanAmountLabel = new Label();
        paymentsController.installmentValueLabel = new Label();
        paymentsController.mainPane = new Pane();
    }

    @Test
    void testInitialize() {
        // Arrange mocks
        when(dateFormmaterUtil.FirstInstallmentDueDate()).thenReturn("01/01/2024");

        // Call initialize method
        Platform.runLater(() -> {
            paymentsController.initialize();

            // Verify due date initialization
            assertEquals("01/01/2024", paymentsController.dueDateLabel.getText());
            assertTrue(paymentsController.paymentMethodComboBox.getItems().contains(PaymentMethod.DEBIT_ACCOUNT));
        });
    }

    @Test
    void testSetFormEntity() {
        FormEntityDto formEntityDto = FormEntityDto.builder()
                .income(3000.0)
                .maximumAmount(10000.0)
                .build();

        // Set form entity
        Platform.runLater(() -> {
            paymentsController.setFormEntity(formEntityDto);

            // Verify slider and label values
            assertEquals(3000.0, paymentsController.loanAmountSlider.getMin());
            assertEquals(10000.0, paymentsController.loanAmountSlider.getMax());
            assertEquals("R$ 3000,00", paymentsController.loanAmountLabel.getText());
        });
    }

    @Test
    void testUpdateInstallmentValue() {
        Platform.runLater(() -> {
            // Prepare mock values
            paymentsController.loanAmountSlider.setValue(5000.0);
            paymentsController.installmentsComboBox.getItems().addAll(6, 12);
            paymentsController.installmentsComboBox.setValue(6);

            when(CalculationUtil.getTotalPayable(5000.0, 6)).thenReturn(6000.0);

            // Call method
            paymentsController.updateInstallmentValue();

            // Verify installment value label
            assertEquals("R$ 1000,00 per installment", paymentsController.installmentValueLabel.getText());
        });
    }

    @Test
    void testSaveLoanToDatabase() {
        Platform.runLater(() -> {
            // Mock valores para os controles JavaFX
            paymentsController.loanAmountSlider.setValue(5000.0);
            paymentsController.installmentsComboBox.getItems().addAll(6, 12);
            paymentsController.installmentsComboBox.setValue(6);
            paymentsController.paymentMethodComboBox.getItems().add(PaymentMethod.DEBIT_ACCOUNT);
            paymentsController.paymentMethodComboBox.setValue(PaymentMethod.DEBIT_ACCOUNT);

            // Configurar mocks do mapper e formatter
            FormEntity mockFormEntity = mock(FormEntity.class);
            when(formEntityMapper.mapFrom(any(FormEntityDto.class))).thenReturn(mockFormEntity);
            when(dateFormmaterUtil.FormattedIssueDate()).thenReturn("01/01/2024");
            when(dateFormmaterUtil.FormattedLoanDueDate(6)).thenReturn("01/06/2024");

            // Chamar o método de salvar
            paymentsController.saveLoanToDatabase();

            // Verificar se o serviço de empréstimo foi chamado
            verify(loanService, times(1)).save(any(LoanEntityDto.class));
        });
    }

    @Test
    void testUpdateDueDate() {
        Platform.runLater(() -> {
            // Prepare mock values
            paymentsController.installmentsComboBox.getItems().addAll(6, 12);
            paymentsController.installmentsComboBox.setValue(6);

            when(dateFormmaterUtil.FormattedLoanDueDate(6)).thenReturn("01/06/2024");
            when(dateFormmaterUtil.FirstInstallmentDueDate()).thenReturn("01/01/2024");
            when(dateFormmaterUtil.DueDateDay()).thenReturn(5);

            // Call updateDueDate
            paymentsController.updateDueDate();

            // Verify due date label
            assertEquals(
                    "Your installment will be on the 5 of each month.\nThe first installment will be due on 01/01/2024.\nThe final due date will be 01/06/2024.",
                    paymentsController.dueDateLabel.getText()
            );
        });
    }
}
