package controller;

import controller.TCPReceiveMessage;

import java.io.IOException;

public class TCPTestServer {
    public static void main(String[] args) {
        int port = 12345; // Example port number
        try {
            TCPReceiveMessage server = new TCPReceiveMessage(port);
            System.out.println("Server listening on port " + port);
            server.listenForMessages();
        } catch (IOException e) {
            System.err.println("Server failed to start: " + e.getMessage());
        }
    }
}
