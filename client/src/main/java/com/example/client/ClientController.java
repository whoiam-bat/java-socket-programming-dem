package com.example.client;

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

import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class ClientController implements Initializable {
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

    private Client client;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        client = new Client();

        txtLabel.setText(client.getClientName());

        // Scroll VBox pane to the bottom when new message is received
        vBox.heightProperty().addListener((observableValue, oldValue, newValue) -> {
            scrollPane.setVvalue((Double) newValue);
        });

        /*
            Wil be run in the separate thread, because waiting for messages is blocking operation.
            With this approach program will be able to execute other functionality
            not only waiting for messages from client.
         */
        client.receiveMsgFromServer(vBox);
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
            HBox hBox = createMessageBox(
                    messageToSend,
                    Pos.CENTER_RIGHT,
                    """
                                -fx-background-color: #0693e3;
                                -fx-font-weight: bold;
                                -fx-color: white;
                                -fx-background-radius: 20px;
                            """
            );

            HBox hBoxTime = createTimeBox(Pos.CENTER_RIGHT);

            vBox.getChildren().add(hBox);
            vBox.getChildren().add(hBoxTime);

            client.sendMsgToServer(messageToSend);

            txtMsg.clear();
        }
    }

    public static void receiveMessage(String messageToReceive, VBox vbox) {
        HBox hBox = createMessageBox(
                messageToReceive,
                Pos.CENTER_LEFT,
                """
                            -fx-background-color: #abb8c3;
                            -fx-font-weight: bold;
                            -fx-background-radius: 20px;
                        """
        );

        HBox hBoxTime = createTimeBox(Pos.CENTER_LEFT);

        Platform.runLater(() -> {
            vbox.getChildren().add(hBox);
            vbox.getChildren().add(hBoxTime);
        });
    }

    private static HBox createMessageBox(String message, Pos alignment, String styles) {
        HBox hBox = new HBox();
        hBox.setAlignment(alignment);
        hBox.setPadding(new Insets(5, 5, 0, 10));

        Text text = new Text(message);
        text.setStyle("-fx-font-size: 14");
        TextFlow textFlow = new TextFlow(text);

        textFlow.setStyle(styles);
        textFlow.setPadding(new Insets(5, 10, 5, 10));
        text.setFill(Color.color(1, 1, 1));

        hBox.getChildren().add(textFlow);

        return hBox;
    }

    private static HBox createTimeBox(Pos alignment) {
        HBox hBoxTime = new HBox();
        hBoxTime.setAlignment(alignment);
        hBoxTime.setPadding(new Insets(0, 5, 5, 10));
        String stringTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
        Text time = new Text(stringTime);
        time.setStyle("-fx-font-size: 8");

        hBoxTime.getChildren().add(time);

        return hBoxTime;
    }

}