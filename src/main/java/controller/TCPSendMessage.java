package controller;

import java.io.*;
import java.net.Socket;

public class TCPSendMessage {

    public static void sendTCPMessage(String ipAddress, int port, String message) {
        try (Socket socket = new Socket(ipAddress, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
            out.println(message);
        } catch (IOException e) {
            System.err.println("TCP Send Error: " + e.getMessage());
        }
    }

    // Add other methods as required, e.g., to handle different types of messages or perform additional processing.
}
