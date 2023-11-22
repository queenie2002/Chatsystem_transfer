package controller;

import model.User;

import java.io.IOException;
import java.net.*;

/*implemented :
*
* using constants for the sender port, receiver port, and broadcast address for better readability and future modifications
DatagramSocket is created in the constructor and reused for each method to avoid redundancy
closeSocket method to properly close the socket
private send method to centralise common code
*
* */

/*
Currently no absolute checking of pseudo unicity because no database
-> during the broadcast, we scan the network : send ip to all connected users
upon receiving this the latter will in turn send their ip and pseudo back allowing us to construct the list of connected users
we can then choose a pseudo different to all other currently connected users
* */

public class SendMessage {

    private static final int SENDER_PORT = 1200;
    private static final int RECEIVER_PORT = 2000;
    private static final String BROADCAST_ADDRESS = "255.255.255.255";

    private final DatagramSocket sendingSocket;

    public SendMessage() throws SocketException {
        // Initialize the DatagramSocket for sending
        this.sendingSocket = new DatagramSocket(SENDER_PORT);
        this.sendingSocket.setBroadcast(true);
    }

    public void closeSocket() {
        if (sendingSocket != null && !sendingSocket.isClosed()) {
            sendingSocket.close();
        }
    }

    public void sendNetworkScanRequest(User user) throws IOException {
        InetAddress senderAddress = InetAddress.getByName(BROADCAST_ADDRESS);
        String message = "NETWORK_SCAN_REQUEST: id: " + user.getId();
        send(message, senderAddress);

        System.out.println("Network scan request sent.");
    }

    public void sendNetworkScanResponse(User user) throws IOException {
        InetAddress senderAddress = user.getIpAddress();
        String message = "NETWORK_SCAN_RESPONSE: id: " + user.getId() + " nickname: " + user.getNickname() + " ip address: " + user.getIpAddress().getHostAddress();
        send(message, senderAddress);

        System.out.println("Network scan response sent.");
    }

    private void send(String message, InetAddress receiverAddress) throws IOException {
        byte[] buf = message.getBytes();
        DatagramPacket outPacket = new DatagramPacket(buf, buf.length, receiverAddress, RECEIVER_PORT);
        sendingSocket.send(outPacket);
    }
}
