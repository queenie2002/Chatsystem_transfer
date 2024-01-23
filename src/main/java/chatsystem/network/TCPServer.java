package chatsystem.network;

import chatsystem.observers.MyObserver;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class TCPServer {

    //OBSERVER
    private List<MyObserver> observers = new ArrayList<>();
    public void addObserver(MyObserver observer) {
        observers.add(observer);
    }
    private void notifyObservers(TCPMessage message) {
        for (MyObserver observer : observers) {
            observer.handleTCPMessage(message);
        }
    }

    private static ServerSocket serverSocket;
    private List<ClientHandler> clientHandlers = new CopyOnWriteArrayList<>();

    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        while (true) {
            ClientHandler clientHandler = new ClientHandler(serverSocket.accept(), this);
            clientHandlers.add(clientHandler);
            clientHandler.start();
        }
    }

    public void stopServer() throws IOException {
        for (ClientHandler clientHandler : clientHandlers) {
            clientHandler.stopClientConnection();
        }
        clientHandlers.clear();

        if (serverSocket != null && !serverSocket.isClosed()) {
            serverSocket.close();
        }
    }

    private static class ClientHandler extends Thread {
        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;
        private TCPServer server;

        public ClientHandler(Socket socket, TCPServer server) {
            this.clientSocket = socket;
            this.server = server;
        }

        public void run() {
            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                String line;
                while ((line = in.readLine()) != null) {
                    try {
                        TCPMessage inputLine = TCPMessage.deserialize(line);
                        server.notifyObservers(inputLine);
                        out.println(inputLine.getContent());
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                stopClientConnection();
                server.clientHandlers.remove(this);
            }
        }

        public void stopClientConnection() {
            try {
                if (out != null) out.close();
                if (in != null) in.close();
                if (clientSocket != null) clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
