package chatsystem.network;/*package chatsystem.network;//package controller;

import java.io.*;
import java.net.*;

public class TCPClient {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    // Method to start the client
    public void startConnection(String ip, int port) throws IOException {
        clientSocket = new Socket(ip, port);
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    // Method to send a message to the server
    public String sendMessage(String msg) throws IOException {
        out.println(msg);
        String resp = in.readLine();
        return resp;
    }

    // Method to close the connection
    public void stopConnection() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }

    public static void main(String[] args) {
        TCPClient client = new TCPClient();
        try {
            client.startConnection(args[0], 8080); //ip adress taken as an arg
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String inputLine;
            System.out.println("Type a message to send to the server (type '.' to disconnect):");

            while (!(inputLine = reader.readLine()).equals(".")) {
                String response = client.sendMessage(inputLine);
                System.out.println("Server response: " + response);
            }

            client.stopConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
*/

import java.net.*;
import java.io.*;

public class TCPClient {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public void startConnection(String ip, int port) throws IOException {
        clientSocket = new Socket(ip, port);
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    public String sendMessage(String msg) throws IOException {
        out.println(msg);
        return in.readLine();
    }

    public void stopConnection() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }
}