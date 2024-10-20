package org.example.projetofrontend;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class EmprestimosJalaU extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Layout principal (BorderPane)
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));

        // Cabeçalho com logo e título
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(10));
        header.setSpacing(10);
        header.setStyle("-fx-background-color: #002D7A;");

        ImageView logo = new ImageView(new Image(getClass().getResource("/logo.png").toExternalForm()));
        logo.setFitHeight(50);
        logo.setFitWidth(50);

        Label titulo = new Label("Empréstimos JalaU");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        titulo.setTextFill(Color.WHITE);

        header.getChildren().addAll(logo, titulo);

        // Subtítulo
        Label subtitulo = new Label("Transparente, seguro e do seu jeito");
        subtitulo.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        subtitulo.setPadding(new Insets(10, 0, 20, 0));

        // Texto explicativo
        Label texto = new Label(
                "Seja para viajar, fazer um curso ou resolver alguma emergência financeira.\n"
                        + "Na hora de decidir por um empréstimo, ter transparência nas condições,\n"
                        + "clareza nas parcelas e praticidade na contratação e pagamento são pontos fundamentais\n"
                        + "para tomar uma decisão com tranquilidade e segurança. É isso que o empréstimo\n"
                        + "pessoal do JalaU oferece.");
        texto.setFont(Font.font("Arial", 16));
        texto.setWrapText(true);

        // Imagem do tablet
        ImageView tabletImage = new ImageView(new Image(getClass().getResource("/tablet.png").toExternalForm()));
        tabletImage.setFitHeight(150);
        tabletImage.setFitWidth(200);

        // Botões de ação com estilo
        VBox botoes = new VBox(10);
        botoes.setAlignment(Pos.TOP_CENTER);
        botoes.setPadding(new Insets(10));

        String estiloBotao = "-fx-background-color: #0073E6; -fx-text-fill: white; -fx-pref-width: 200px;";

        Button btnTaxa = new Button("Taxa fixa de 2% a.m");
        btnTaxa.setStyle(estiloBotao);

        Button btnParcelamento = new Button("Parcelamento em até 36x");
        btnParcelamento.setStyle(estiloBotao);

        Button btnResultado = new Button("Resultado na hora");
        btnResultado.setStyle(estiloBotao);

        botoes.getChildren().addAll(btnTaxa, btnParcelamento, btnResultado);

        // Layout vertical para imagem e botões
        VBox lateral = new VBox(20, tabletImage, botoes);
        lateral.setAlignment(Pos.TOP_CENTER);
        lateral.setPadding(new Insets(20));

        // Botão principal que abre o formulário
        Button btnSolicitar = new Button("Solicite o seu empréstimo");
        btnSolicitar.setStyle("-fx-background-color: #004C99; -fx-text-fill: white;");
        btnSolicitar.setPadding(new Insets(10));

        // Vinculando ao método para abrir o formulário
        btnSolicitar.setOnAction(e -> {
            FormularioSolicitacao formulario = new FormularioSolicitacao();
            formulario.mostrarFormulario();
        });

        VBox centro = new VBox(10, subtitulo, texto, btnSolicitar);
        centro.setAlignment(Pos.CENTER_LEFT);
        centro.setPadding(new Insets(10));

        // Adicionar o cabeçalho e o conteúdo ao layout principal
        root.setTop(header);
        root.setCenter(centro);
        root.setRight(lateral);

        // Configuração da cena e do stage
        Scene scene = new Scene(root, 800, 500);
        primaryStage.setTitle("Empréstimos JalaU");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
