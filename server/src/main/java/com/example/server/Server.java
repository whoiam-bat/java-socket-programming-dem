package com.example.server;

import javafx.scene.layout.VBox;
import lombok.Getter;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    @Getter
    private String serverName;

    private ServerSocket serverSocket;

    private Socket socket;

    private BufferedReader bufferedReader;

    private BufferedWriter bufferedWriter;


    public Server(ServerSocket serverSocket) {
        try {
            this.serverSocket = serverSocket;
            this.socket = serverSocket.accept();
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            System.err.println("Error creating server.\n" + e.getLocalizedMessage());
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
        } catch (IOException e) {
            System.err.println(e.getLocalizedMessage());
        }
    }

}
