package com.example.client;

import javafx.scene.layout.VBox;
import lombok.Getter;

import java.io.*;
import java.net.Socket;

public class Client {
    @Getter
    private String clientName;
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;


    public Client(Socket socket) {
        try {
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
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
        } catch (IOException e) {
            System.err.println(e.getLocalizedMessage());
        }
    }
}
