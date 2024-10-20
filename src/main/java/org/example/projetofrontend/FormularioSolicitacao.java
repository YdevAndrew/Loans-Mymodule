package org.example.projetofrontend;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;

public class FormularioSolicitacao {

    private File documentoFoto;
    private File comprovanteRenda;

    public void mostrarFormulario() {
        // Criar uma nova janela (Stage)
        Stage formularioStage = new Stage();
        formularioStage.initModality(Modality.APPLICATION_MODAL);
        formularioStage.setTitle("Formulário de Solicitação de Empréstimo");

        // Layout principal (BorderPane)
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));

        // Cabeçalho com Logo
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(10));
        header.setStyle("-fx-background-color: #002D7A;");

        ImageView logo = new ImageView(new Image(getClass().getResource("/logo.png").toExternalForm()));
        logo.setFitHeight(50);
        logo.setFitWidth(50);

        header.getChildren().add(logo);

        // Título e Subtítulo
        VBox tituloBox = new VBox(5);
        tituloBox.setAlignment(Pos.CENTER_LEFT);
        tituloBox.setPadding(new Insets(10, 0, 20, 0));

        Label titulo = new Label("Preencha o formulário");
        titulo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Label subtitulo = new Label("*Todas as informações são obrigatórias");
        subtitulo.setStyle("-fx-font-size: 14px; -fx-text-fill: gray;");

        tituloBox.getChildren().addAll(titulo, subtitulo);

        // Botões para anexar arquivos
        Button btnAnexarDocumento = new Button("Anexar Documento com Foto");
        Label lblDocumento = new Label("Nenhum arquivo selecionado");
        btnAnexarDocumento.setOnAction(e -> {
            documentoFoto = selecionarArquivo(formularioStage);
            if (documentoFoto != null) {
                lblDocumento.setText("Arquivo: " + documentoFoto.getName());
            }
        });

        Button btnAnexarComprovante = new Button("Anexar Comprovante de Renda");
        Label lblComprovante = new Label("Nenhum arquivo selecionado");
        btnAnexarComprovante.setOnAction(e -> {
            comprovanteRenda = selecionarArquivo(formularioStage);
            if (comprovanteRenda != null) {
                lblComprovante.setText("Arquivo: " + comprovanteRenda.getName());
            }
        });

        // Campo de texto para salário
        TextField salarioField = new TextField();
        salarioField.setPromptText("Salário R$");
        salarioField.setPrefWidth(300);

        // Layout para os campos e botões
        VBox formulario = new VBox(15,
                btnAnexarDocumento, lblDocumento,
                btnAnexarComprovante, lblComprovante,
                salarioField);
        formulario.setAlignment(Pos.CENTER);
        formulario.setPadding(new Insets(20));

        // Botão de Envio
        Button btnEnviar = new Button("Enviar dados");
        btnEnviar.setStyle(
                "-fx-background-color: #004C99; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 16px; " +
                        "-fx-pref-width: 250px; " +
                        "-fx-pref-height: 45px; " +
                        "-fx-background-radius: 10px;"
        );
        btnEnviar.setOnAction(e -> enviarFormulario());

        VBox layoutBotao = new VBox(btnEnviar);
        layoutBotao.setAlignment(Pos.CENTER);
        layoutBotao.setPadding(new Insets(20));

        // Layout central
        VBox centro = new VBox(20, tituloBox, formulario, layoutBotao);
        centro.setAlignment(Pos.CENTER);
        root.setTop(header);
        root.setCenter(centro);

        // Configuração da cena e exibição do formulário
        Scene scene = new Scene(root, 650, 500); // Ajustamos a largura e altura da janela
        formularioStage.setScene(scene);
        formularioStage.showAndWait();
    }

    // Método para abrir o seletor de arquivos
    private File selecionarArquivo(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selecione um arquivo");
        return fileChooser.showOpenDialog(stage);
    }

    // Método para tratar o envio do formulário
    private void enviarFormulario() {
        if (documentoFoto == null || comprovanteRenda == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Por favor, anexe todos os documentos obrigatórios.",
                    ButtonType.OK);
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION,
                    "Formulário enviado com sucesso!",
                    ButtonType.OK);
            alert.showAndWait();
        }
    }
}
