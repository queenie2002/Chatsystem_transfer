import java.io.*;
import java.net.*;

public class tcpClient {
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
        tcpClient client = new tcpClient();
        try {
            client.startConnection("127.0.0.1", 8080);
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
