package com.example.server;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("index.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Socket-Chat | Server");
        stage.setScene(scene);
        stage.show();
    }
}