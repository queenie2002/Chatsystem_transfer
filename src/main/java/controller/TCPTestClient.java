package controller;

public class TCPTestClient {
    public static void main(String[] args) {
        String serverIP = "localhost"; // or the actual server IP
        int serverPort = 12345; // Same as the server port
        String message = "Hello from the client!";

        TCPSendMessage.sendTCPMessage(serverIP, serverPort, message);
    }
}

