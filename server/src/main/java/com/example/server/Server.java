package com.example.server;

import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.Setter;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

public class Server {

    @Getter
    @Setter
    private String serverName;

    @Getter
    @Setter
    private int port;

    private ServerSocket serverSocket;

    private Socket socket;

    private BufferedReader bufferedReader;

    private BufferedWriter bufferedWriter;


    public Server() {
        try {
            loadProperties();
            this.serverSocket = new ServerSocket(port);
            System.out.println("Server " + serverName + " is running on: " + port + " port.");

            this.socket = serverSocket.accept();
            System.out.println("Client connected.");

            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            System.err.println("Error creating server: " + e.getLocalizedMessage());
            closeResources();
        }
    }

    public void sendMsgToClient(String messageToClient) {
        try {
            bufferedWriter.write(messageToClient);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            System.err.println("Error sending message: " + e.getLocalizedMessage());
            closeResources();
        }
    }

    public void receiveMsgFromClient(VBox vBox) {
        new Thread(() -> {
            while (socket.isConnected()) {
                try {
                    String messageFromClient = bufferedReader.readLine();
                    ServerController.receiveMessage(messageFromClient, vBox);
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

            System.out.println("Client connection closed.");
        } catch (IOException e) {
            System.err.println(e.getLocalizedMessage());
        }
    }

    private void loadProperties() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            Properties props = new Properties();
            props.load(input);

            serverName = props.getProperty("server.name");
            port = Integer.parseInt(props.getProperty("server.port"));

        } catch (IOException e) {
            System.err.println("Error reading property file: " + e.getMessage());
        }
    }
}
