package org.jala.university.presentation.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.stage.FileChooser;
import lombok.EqualsAndHashCode;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.jala.university.ServiceFactory;
import org.jala.university.application.dto.LoanRequestDto;
import org.jala.university.application.dto.LoanRequestFormDto;
import org.jala.university.application.mapper.LoanRequestFormMapper;
import org.jala.university.application.service.LoansService;
import org.jala.university.commons.presentation.BaseController;
import org.jala.university.domain.entity.LoanRequestFormEntity;
import org.jala.university.presentation.controller.context.RequestFormViewContext;

import javax.sql.rowset.serial.SerialBlob;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
public class LoanRequestFormPage3Controller extends BaseController implements Initializable {

    private final ShowAlert showAlert;

    private final LoanRequestFormMapper mapper;

    private HashMap<String, Object> personalData;

    @FXML
    private Hyperlink IdShowPDFAdrress;

    @FXML
    private Hyperlink IdShowPDFClientID;

    @FXML
    private Hyperlink IdShowPDFLaborCertificate;

    @FXML
    private Hyperlink IdShowPDFLengthService;

    @FXML
    private Hyperlink IdShowPDFProofIncome;

    @FXML
    private Button btnLoadIdCardPDF;

    @FXML
    private Button btnLoadProofAddressPDF;

    @FXML
    private Button btnLoadProofIncomePDF;

    @FXML
    private Button btnLoadProofLengthServicePDF;

    @FXML
    private Button btnLoadLaborCertificatePDF;

    @FXML
    private Button btnSaveForm;

    @FXML
    private Button btnSendForm;

    LoansService loansService;

    private Blob idCardPDFBytes;

    private Blob proofIncomePDFBytes;

    private Blob proofAddressPDFBytes;

    private Blob laborCertificatePDFBytes;

    private Blob lengthServicePDFBytes;

    private LoanRequestFormDto loanRequestFormDto;

    public LoanRequestFormPage3Controller() {
        this.showAlert = new ShowAlert();
        this.mapper = new LoanRequestFormMapper();
        this.personalData = new HashMap<>();
        this.loansService = ServiceFactory.loansService();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {

            new Thread(() -> {
                RequestFormViewContext context = (RequestFormViewContext) this.context;
                if (context != null) {
                    personalData = context.getFormData();
                } else {
                    showAlert.showErrorAlert("Error al guardar los datos anteriores");
                }
            }).start();

        });
    }


    private Blob uploadPDF(){
        File pdfFile = chooseFilePdf();
        if (pdfFile != null) {
            try {
                InputStream inputStream = new FileInputStream(pdfFile);
                byte[] bytesPDF = IOUtils.toByteArray(inputStream);
                if (validatePdfWeight(bytesPDF)){
                    return new SerialBlob(bytesPDF);
                }

                inputStream.close();
            } catch (IOException | SQLException e) {
                throw new RuntimeException(e);
            }

        }
        return null;

    }

    private boolean validatePdfWeight(byte[] bytesPDF) {
        ByteArrayInputStream bais = new ByteArrayInputStream(bytesPDF);
        int documentWeight = bais.available();
        int allowedWeightDocument = 50000000;
        if (documentWeight < allowedWeightDocument){
            System.out.println("Peso del documento PDF: " + documentWeight + " bytes");
            return true;
        } else {
            showAlert.showErrorAlert("El documento excede la cantidad permitida ");
            return false;
        }
    }

    private File chooseFilePdf() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar archivo PDF");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Archivos PDF", "*.pdf")
        );
        return fileChooser.showOpenDialog(null);
    }


    private void showPDF(Blob pdfBlob) {
        try  {
            byte[] pdfBytes = pdfBlob.getBytes(1, (int) pdfBlob.length());
            PDDocument document = Loader.loadPDF(pdfBytes);

            System.out.println("Número de páginas: " + document.getNumberOfPages());
            System.out.println(document.getVersion());


            PDFRenderer pdfRenderer = new PDFRenderer(document);

            JPanel pdfPanel = new JPanel();
            pdfPanel.setLayout(new BoxLayout(pdfPanel, BoxLayout.Y_AXIS));


            for (int i = 0; i < document.getNumberOfPages(); i++) {
                BufferedImage image = pdfRenderer.renderImage(i);
                JLabel pageLabel = new JLabel(new ImageIcon(image));
                pdfPanel.add(pageLabel);
            }


            JScrollPane scrollPane = new JScrollPane(pdfPanel);
            scrollPane.setPreferredSize(new Dimension(600, 600));
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);


            JFrame pdfFrame = new JFrame("Visor de archivos PDF del formulario de préstamos");
            pdfFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            pdfFrame.getContentPane().add(scrollPane);
            pdfFrame.pack();
            pdfFrame.setVisible(true);

        } catch (IOException e) {
            throw new RuntimeException();
        } catch (NullPointerException e) {
            showAlert.showErrorAlert("No hay PDF seleccionado");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void uploadLoadIdCardPDF(ActionEvent event) {
        idCardPDFBytes = uploadPDF();
    }

    @FXML
    private void uploadProofAddressPDF(ActionEvent event) {
        proofAddressPDFBytes = uploadPDF();
    }

    @FXML
    private void uploadProofIncomePDF(ActionEvent event) {
        proofIncomePDFBytes = uploadPDF();
    }

    @FXML
    private void uploadLaborCertificatePDF(ActionEvent event) {
        laborCertificatePDFBytes = uploadPDF();
    }

    @FXML
    private void uploadProofLengthServicePDF(ActionEvent event) {
        lengthServicePDFBytes = uploadPDF();
    }

    @FXML
    void showPDFClientID(ActionEvent event) {
        showPDF(idCardPDFBytes);
    }

    @FXML
    void showPDFAddress(ActionEvent event) {
        showPDF(proofAddressPDFBytes);
    }

    @FXML
    void showPDFLaborCertificate(ActionEvent event) {
        showPDF(laborCertificatePDFBytes);

    }

    @FXML
    void showPDFLengthService(ActionEvent event) {
        showPDF(lengthServicePDFBytes);
    }

    @FXML
    void showPDFProofIncome(ActionEvent event) {
        showPDF(proofIncomePDFBytes);
    }


    @FXML
    private void btnSaveFormOnAction(ActionEvent event) {
        if (validateUploadPdf()){
            loanRequestFormDto = getLoanRequestFormDto();
            if (loanRequestFormDto != null) {
                showAlert.showInformationAlert("Formulario guardado exitosamente");
            } else {
                showAlert.showErrorAlert("Error al guardar la solicitud");
            }
        }

    }

    public boolean validateUploadPdf() {
        if (idCardPDFBytes == null || proofIncomePDFBytes == null || proofAddressPDFBytes == null
                || laborCertificatePDFBytes == null || lengthServicePDFBytes == null){
            showAlert.showErrorAlert("Debe subir todos los pdf solicitados");
            return false;
        } else {
            return true;
        }
    }

    @FXML
    public void btnSendFormOnAction(ActionEvent actionEvent) {

        if (!validateUploadPdf()) {
            showAlert.showErrorAlert("Validación fallida. Verifique la entrada.");
            return;
        }

        if (loanRequestFormDto == null) {
            showAlert.showInformationAlert("Formulario guardado exitosamente");
        }

        boolean confirmation = showAlert.showConfirmationAlert("¿Está seguro de que desea enviar la solicitud?");
        if (confirmation) {
            if (loanRequestFormDto != null) {
                LoanRequestDto requestDto = createLoanRequestDto(loanRequestFormDto.getId(), "Enviado", loanRequestFormDto);
                LoanRequestDto requestSaved = loansService.saveRequest(requestDto);
                if (requestSaved != null) {
                    showAlert.showInformationAlert("La solicitud de préstamo ha sido enviada con éxito.");
                } else {
                    showAlert.showErrorAlert("Error al enviar la solicitud.");
                }
            } else {
                LoanRequestDto requestDto = createLoanRequestDto(getLoanRequestFormDto().getId(), "Enviado", getLoanRequestFormDto());
                LoanRequestDto requestSaved = loansService.saveRequest(requestDto);
                if (requestSaved != null) {
                    showAlert.showInformationAlert("La solicitud de préstamo ha sido enviada con éxito.");
                } else {
                    showAlert.showErrorAlert("Error al enviar la solicitud.");
                }
            }
        } else {
            showAlert.showInformationAlert("Envío cancelado.");

            LoanRequestDto requestDto = createLoanRequestDto(getLoanRequestFormDto().getId(), "Cancelado", getLoanRequestFormDto());
            LoanRequestDto requestSaved = loansService.saveRequest(requestDto);
            if (requestSaved != null) {
                showAlert.showInformationAlert("La solicitud de préstamo ha sido cancelada.");
            }

        }
    }



    public LoanRequestFormDto getLoanRequestFormDto() {
        return LoanRequestFormDto.builder()
                .namesApplicant(personalData.get("names").toString())
                .lastnamesApplicant(personalData.get("lastnames").toString())
                .identityCode(personalData.get("idCode").toString())
                .dateOfBirth((LocalDate) personalData.get("dateBirthDay"))
                .email(personalData.get("email").toString())
                .phoneNumber(personalData.get("phone").toString())
                .address(personalData.get("address").toString())
                .employerName(personalData.get("employerName").toString())
                .monthlyIncome((BigDecimal) personalData.get("monthlyIncome"))
                .jobTitle(personalData.get("jobTitle").toString())
                .yearsOfService((Integer) personalData.get("yearsOfService"))
                .loanAmount((BigDecimal) personalData.get("loanAmount"))
                .loanType(personalData.get("loanType").toString())
                .desiredLoanPeriod((Integer) personalData.get("desiredLoanPeriod"))
                .idCardPDF(idCardPDFBytes)
                .proofIncomePDF(proofIncomePDFBytes)
                .laborCertificatePDF(laborCertificatePDFBytes)
                .proofLengthServicePDF(lengthServicePDFBytes)
                .proofAddressPDF(proofAddressPDFBytes)
                .build();
    }
    private LoanRequestDto createLoanRequestDto(UUID id, String status, LoanRequestFormDto loanRequestFormDto) {
        return LoanRequestDto.builder()
                .id(id)
                .status(status)
                .loanRequestForm(loanRequestFormDto)
                .build();
    }
}
