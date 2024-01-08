package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPReceiveMessage {

    private final ServerSocket serverSocket;

    public TCPReceiveMessage(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    public void listenForMessages() {
        try {
            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
                    String receivedMessage = in.readLine();
                    // Process the received message
                    processMessage(receivedMessage);
                }
            }
        } catch (IOException e) {
            System.err.println("TCP Receive Error: " + e.getMessage());
        } finally {
            closeServerSocket();
        }
    }

    private void processMessage(String message) {
        // Implement message processing logic here
        System.out.println("Received: " + message);
    }

    private void closeServerSocket() {
        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                System.err.println("Error closing server socket: " + e.getMessage());
            }
        }
    }

    // Other methods as required
}
