package com.example.client;

import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.Setter;

import java.io.*;
import java.net.Socket;
import java.util.Properties;

public class Client {

    @Getter
    @Setter
    private String clientName;

    @Getter
    @Setter
    private int port;

    @Getter
    @Setter
    private String host;

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;


    public Client() {
        try {
            loadProperties();
            this.socket = new Socket(host, port);
            System.out.println("Client " + clientName + " is running on: " + host + " with " + port + " port.");

            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            System.out.println("Connected to server.");
        } catch (IOException e) {
            System.err.println("Error creating client: " + e.getLocalizedMessage());
            closeResources();
        }
    }

    public void sendMsgToServer(String messageToServer) {
        try {
            bufferedWriter.write(messageToServer);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            ClientController.showError(e.getLocalizedMessage());
            System.err.println("Error sending message: " + e.getLocalizedMessage());
            closeResources();
        }
    }

    public void receiveMsgFromServer(VBox vBox) {
        new Thread(() -> {
            while (socket.isConnected()) {
                try {
                    String messageFromServer = bufferedReader.readLine();
                    ClientController.receiveMessage(messageFromServer, vBox);
                } catch (IOException e) {
                    System.err.println("Error receiving message: " + e.getLocalizedMessage());
                    closeResources();
                    break;
                }
            }
        }).start();
    }


    private void closeResources() {
        try {
            if (bufferedWriter != null)
                bufferedWriter.close();
            if (bufferedReader != null)
                bufferedReader.close();
            if (socket != null)
                socket.close();

            System.out.println("Connection closed.");
        } catch (IOException e) {
            System.err.println(e.getLocalizedMessage());
        }
    }

    private void loadProperties() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            Properties props = new Properties();
            props.load(input);

            clientName = props.getProperty("client.name");
            host = props.getProperty("client.host");
            port = Integer.parseInt(props.getProperty("client.port"));

        } catch (IOException e) {
            System.err.println("Error reading property file: " + e.getMessage());
        }
    }
}
