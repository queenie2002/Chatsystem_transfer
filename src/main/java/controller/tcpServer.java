import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class tcpServer {

    interface MessageObserver {
        void handleMessage(String message);
    }
    private ServerSocket serverSocket;

    private static List<MessageObserver> observers = new ArrayList<>();

    // Methods to add and remove observers
    public void addObserver(MessageObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(MessageObserver observer) {
        observers.remove(observer);
    }

    // Notify all observers about the received message
    private static void notifyObservers(String message) {
        for (MessageObserver observer : observers) {
            observer.handleMessage(message);
        }
    }

    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("TCP Server started on port " + port);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("New client connected: " + clientSocket.getInetAddress().getHostAddress());
            new EchoClientHandler(clientSocket).start();
        }
    }

    public void stop() throws IOException {
        serverSocket.close();
        System.out.println("Server stopped.");
    }

    private static class EchoClientHandler extends Thread {
        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;

        public EchoClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    System.out.println("Received from client: " + inputLine);
                    notifyObservers(inputLine); // Notify observers
                    if (".".equals(inputLine)) {
                        System.out.println("Disconnect signal received. Closing connection.");
                        break;
                    }
                    send(inputLine); // Echo back the received message
                }
            } catch (IOException e) {
                System.err.println("IOException in EchoClientHandler: " + e.getMessage());
                e.printStackTrace();
            } finally {
                try {
                    in.close();
                    out.close();
                    clientSocket.close();
                } catch (IOException e) {
                    System.err.println("IOException on closing client connection: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }

        public void send(String message) {
            out.println(message);
            System.out.println("Sent to client: " + message);
        }
    }

    public static void main(String[] args) {
        int port = 8080; // Default port number

        try {
            tcpServer server = new tcpServer();
            server.start(port);
        } catch (IOException e) {
            System.err.println("Error starting the server: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
