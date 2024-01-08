package com.example.server;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class ServerController implements Initializable {
    @FXML
    public AnchorPane pane;
    @FXML
    public ScrollPane scrollPane;
    @FXML
    public VBox vBox;
    @FXML
    public TextField txtMsg;
    @FXML
    public Label txtLabel;

    private Server server;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            server = new Server(new ServerSocket(54123));
            System.out.println("Client connected.");
        } catch (IOException e) {
            System.err.println("Error creating server: " + e.getLocalizedMessage());
        }

        // Scroll VBox pane to the bottom when new message is received
        vBox.heightProperty().addListener((observableValue, oldValue, newValue) -> {
            scrollPane.setVvalue((Double) newValue);
        });

        /*
            Wil be run in the separate thread, because waiting for messages is blocking operation.
            With this approach program will be able to execute other functionality
            not only waiting for messages from client.
         */
        server.receiveMsgFromClient(vBox);
    }


    @FXML
    public void txtMsgOnAction(ActionEvent actionEvent) {
        sendButtonOnAction(actionEvent);
    }

    @FXML
    public void sendButtonOnAction(ActionEvent actionEvent) {
        sendMessage(txtMsg.getText());
    }

    public void sendMessage(String messageToSend) {
        if (!messageToSend.isEmpty()) {
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER_RIGHT);
            hBox.setPadding(new Insets(5, 5, 0, 10));

            Text text = new Text(messageToSend);
            text.setStyle("-fx-font-size: 14");
            TextFlow textFlow = new TextFlow(text);

            textFlow.setStyle("""
                        -fx-background-color: #0693e3;
                        -fx-font-weight: bold;
                        -fx-color: white;
                        -fx-background-radius: 20px;
                    """);

            textFlow.setPadding(new Insets(5, 10, 5, 10));
            text.setFill(Color.color(1, 1, 1));

            hBox.getChildren().add(textFlow);

            HBox hBoxTime = new HBox();
            hBoxTime.setAlignment(Pos.CENTER_RIGHT);
            hBoxTime.setPadding(new Insets(0, 5, 5, 10));
            String stringTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
            Text time = new Text(stringTime);
            time.setStyle("-fx-font-size: 8");

            hBoxTime.getChildren().add(time);

            vBox.getChildren().add(hBox);
            vBox.getChildren().add(hBoxTime);

            server.sendMsgToClient(messageToSend);

            txtMsg.clear();
        }
    }

    public static void receiveMessage(String messageToReceive, VBox vbox) {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(5, 5, 0, 10));

        Text text = new Text(messageToReceive);
        text.setStyle("-fx-font-size: 14");
        TextFlow textFlow = new TextFlow(text);

        textFlow.setStyle("""
                        -fx-background-color: #abb8c3;
                        -fx-font-weight: bold;
                        -fx-background-radius: 20px;
                    """);

        textFlow.setPadding(new Insets(5, 10, 5, 10));
        text.setFill(Color.color(1, 1, 1));

        hBox.getChildren().add(textFlow);

        HBox hBoxTime = new HBox();
        hBoxTime.setAlignment(Pos.CENTER_LEFT);
        hBoxTime.setPadding(new Insets(0, 5, 5, 10));
        String stringTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
        Text time = new Text(stringTime);
        time.setStyle("-fx-font-size: 8");

        hBoxTime.getChildren().add(time);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                vbox.getChildren().add(hBox);
                vbox.getChildren().add(hBoxTime);
            }
        });
    }

}
