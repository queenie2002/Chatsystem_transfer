package chatsystem.network;

import chatsystem.MainClass;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.*;
import java.io.*;

public class TCPClient {

    //LOGGER
    private static final Logger LOGGER = LogManager.getLogger(TCPClient.class);


    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public void startConnection(String ip, int port) throws IOException {
        clientSocket = new Socket(ip, port);
        LOGGER.info("Start connection with ip: "+ip+" on port: "+port);
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    public String sendMessage(TCPMessage message) throws IOException {
        String serializedMessage = serializeMessage(message);
        out.println(serializedMessage);
        return in.readLine();
    }

    public String serializeMessage(TCPMessage message) {
        return message.getContent() + "," + message.getDate() + "," + message.getFromUserIP() + "," + message.getToUserIP();
    }

    public void stopConnection() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }
}