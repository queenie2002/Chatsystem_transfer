
package chatsystem.network;//package chatsystem.network;//package controller;
//
////import model.*;
//
//import java.net.*;
//import java.io.*;
//
//public class TCPServer {
//
//    /*public interface MessageObserver {
//        void handleMessage(String msg) throws SQLException;
//    }*/
//        /*server.addObserver(new MessageObserver() {
//            public void handle(String msg) {
//                DatabaseMethods::addMessage(msg);
//            }
//        });
//        server.addObserver(new MessageObserver() {
//            public void handle(String msg) {
//                if(isCurrentUser())
//                    View.display(msg);
//            }
//        });
//        server.addObserver(this);*/
//
//    private ServerSocket serverSocket;
//
//    /*private static final List<MessageObserver> observers = new ArrayList<>();
//
//    // Methods to add and remove observers
//    public void addObserver(MessageObserver observer) {
//        observers.add(observer);
//    }
//
//    public void removeObserver(MessageObserver observer) {
//        observers.remove(observer);
//    }
//
//    // Notify all observers about the received message
//    private static void notifyObservers(String msg) throws SQLException {
//        for (MessageObserver observer : observers) {
//            observer.handleMessage(msg);
//        }
//    }*/
//
//    public void start(int port) throws IOException {
//        serverSocket = new ServerSocket(port);
//        System.out.println("TCP Server started on port " + port);
//
//        while (true) {
//            Socket clientSocket = serverSocket.accept();
//            System.out.println("New client connected: " + clientSocket.getInetAddress().getHostAddress());
//            new EchoClientHandler(clientSocket).start();
//        }
//    }
//
//    public void stop() throws IOException {
//        serverSocket.close();
//        System.out.println("Server stopped.");
//    }
//
//    private static class EchoClientHandler extends Thread {
//        private Socket clientSocket;
//        private PrintWriter out;
//        private BufferedReader in;
//
//        public EchoClientHandler(Socket socket) {
//            this.clientSocket = socket;
//        }
//
//        public void run() {
//            try {
//                out = new PrintWriter(clientSocket.getOutputStream(), true);
//                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//
//                String inputLine;
//                while ((inputLine = in.readLine()) != null) {
//                    System.out.println("Received from client: " + inputLine);
//                    //notifyObservers(inputLine); // Notify observers
//                    if (".".equals(inputLine)) {
//                        System.out.println("Disconnect signal received. Closing connection.");
//                        break;
//                    }
//                    send(inputLine); // Echo back the received message
//                }
//            } catch (IOException /*| SQLException*/ e) {
//                System.err.println("IOException in EchoClientHandler: " + e.getMessage());
//                e.printStackTrace();
//            } finally {
//                try {
//                    in.close();
//                    out.close();
//                    clientSocket.close();
//                } catch (IOException e) {
//                    System.err.println("IOException on closing client connection: " + e.getMessage());
//                    e.printStackTrace();
//                }
//            }
//        }
//
//        public void send(String message) {
//            out.println(message);
//            System.out.println("Sent to client: " + message);
//        }
//    }
//
//    public static void main(String[] args) {
//        int PORT = 8080; // Default port number
//
//        try {
//            TCPServer server = new TCPServer();
//            server.start(PORT);
//        } catch (IOException e) {
//            System.err.println("Error starting the server: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
//}

import java.net.*;
import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TCPServer {
    private static ServerSocket serverSocket;
    private List<MessageObserver> observers = new ArrayList<>();

    public interface MessageObserver {
        void handleMessage(TCPMessage msg) throws SQLException;
    }

    public void addObserver(MessageObserver observer) {
        observers.add(observer);
    }

    private void notifyObservers(TCPMessage message) {
        for (MessageObserver observer : observers) {
            try {
                observer.handleMessage(message);
            } catch (SQLException e) {
                e.printStackTrace();
                // Handle the SQLException
            }
        }
    }

    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        while (true) {
            new ClientHandler(serverSocket.accept(), this).start();
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
                        out.println(inputLine.getContent()); // Modify as needed
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                        // Handle invalid message format
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        /*public void stop() throws IOException {
            serverSocket.close();
        }*/
    }
}