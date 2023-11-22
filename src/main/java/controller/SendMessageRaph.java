package controller;

import model.User;

import java.io.IOException;
import java.net.*;

/*differences implemented
*
* using constants for the sender port, receiver port, and broadcast address.
DatagramSocket is created in the constructor and reused for each method to eliminate redundancy.
closeSocket method to properly close the socket when it's no longer needed.
private send method to centralise common code.
*
* */

public class SendMessageRaph {

    private static final int SENDER_PORT = 1200;
    private static final int RECEIVER_PORT = 2000;
    private static final String BROADCAST_ADDRESS = "255.255.255.255";

    private DatagramSocket sendingSocket;

    public SendMessageRaph() throws SocketException {
        // Initialise the DatagramSocket for sending
        this.sendingSocket = new DatagramSocket(SENDER_PORT);
        this.sendingSocket.setBroadcast(true);
    }

    public void closeSocket() {
        if (sendingSocket != null && !sendingSocket.isClosed()) {
            sendingSocket.close();
        }
    }

    public void sendBroadcastBeginning(User user) throws IOException {
        InetAddress senderAddress = InetAddress.getByName(BROADCAST_ADDRESS);

        // Create message broadcast
        String message = "POTENTIAL NICKNAME: nickname: " + user.getNickname() + " ip address: " + user.getIpAddress();
        send(message, senderAddress, RECEIVER_PORT);

        System.out.println("Broadcast message sent.");
    }

    public void sendDisconnect(User user) throws IOException {
        InetAddress senderAddress = InetAddress.getByName(BROADCAST_ADDRESS);
        String message = "DISCONNECT: id: " + user.getId();
        send(message, senderAddress, RECEIVER_PORT);

        System.out.println("Disconnect message sent.");
    }

    public void sendConnect(User user) throws IOException {
        InetAddress senderAddress = InetAddress.getByName(BROADCAST_ADDRESS);
        String message = "CONNECT: id: " + user.getId() + " nickname: " + user.getNickname() + " ip address: " + user.getIpAddress();
        send(message, senderAddress, RECEIVER_PORT);

        System.out.println("Connect message sent.");
    }

    public void sendNicknameExists(User user) throws IOException {
        InetAddress senderAddress = InetAddress.getByName(user.getIpAddress().getHostAddress());
        String message = "NICKNAME EXISTS";
        send(message, senderAddress, RECEIVER_PORT);

        System.out.println("Nickname Exists message sent.");
    }

    public void sendNicknameUnique(User user) throws IOException {
        InetAddress senderAddress = InetAddress.getByName(user.getIpAddress().getHostAddress());
        String message = "NICKNAME UNIQUE";
        send(message, senderAddress, RECEIVER_PORT);

        System.out.println("Nickname Unique message sent.");
    }

    private void send(String message, InetAddress receiverAddress, int receiverPort) throws IOException {
        byte[] buf = message.getBytes();
        DatagramPacket outPacket = new DatagramPacket(buf, buf.length, receiverAddress, receiverPort);
        sendingSocket.send(outPacket);
    }
}
