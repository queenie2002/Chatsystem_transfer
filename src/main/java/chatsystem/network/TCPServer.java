
package chatsystem.network;

import chatsystem.observers.MyObserver;

import java.net.*;
import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TCPServer {


    //OBSERVER
    private List<MyObserver> observers = new ArrayList<>();
    public void addObserver(MyObserver observer) {
        observers.add(observer);
    }
    private void notifyObservers(TCPMessage message) {
        for (MyObserver observer : observers) {
            try {
                observer.handleTCPMessage(message);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }



    private static ServerSocket serverSocket;
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
                        out.println(inputLine.getContent());
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                try{
                    in.close();
                    out.close();
                    clientSocket.close();
                } catch (IOException ex){
                    ex.printStackTrace();
                }
            }
        }

        /*public void stop() throws IOException {
            serverSocket.close();
        }*/
    }
}