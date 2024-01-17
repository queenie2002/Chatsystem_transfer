package chatsystem.network;

import java.io.IOException;
import java.net.*;

/** UDP client that sends messages from a random port */
public class UDPSender {
    private static DatagramSocket sendingSocket;

    //A FAIRE
    public static void closeSocket(DatagramSocket sendingSocket) {
        if (sendingSocket != null && !sendingSocket.isClosed()) {
            sendingSocket.close();
        }
    }

    /**Sends a UDP message to given address and port**/
    public static void send (String message, InetAddress receiverAddress, int port) throws IOException {
        sendingSocket = new DatagramSocket(); //sending port
        sendingSocket.setBroadcast(true);
        byte[] buf = message.getBytes();
        DatagramPacket outPacket = new DatagramPacket(buf, buf.length, receiverAddress, port); //receiver port
        sendingSocket.send(outPacket);
        sendingSocket.close();
    }
}