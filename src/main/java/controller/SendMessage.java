package controller;

import model.User;
import static run.MainClass.me;
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

    private static final String BROADCAST_ADDRESS = "255.255.255.255";
    private static final int BROADCAST_RECEIVER_PORT = 2000;

    private static int BROADCAST_SENDING_PORT = 1500;
    private static DatagramSocket sendingSocket;


    public SendMessage () {
        boolean found = false;

        while (!found) {
            try {
                sendingSocket = new DatagramSocket(BROADCAST_SENDING_PORT); //sending port
                sendingSocket.setBroadcast(true);
                found = true;
            } catch (Exception e) {
                BROADCAST_SENDING_PORT +=1;
                System.out.println("error: couldn't assign a socket to send a message");
                e.printStackTrace();
            }
        }
    }




    public void closeSocket(DatagramSocket sendingSocket) {
        if (sendingSocket != null && !sendingSocket.isClosed()) {
            sendingSocket.close();
        }
    }


    //we ask all connected users for their information to be able to choose a nickname(broadcast)
    public static void sendToChooseNickname () throws IOException {
        //user sends his ip
        String message = "TO_CHOOSE_NICKNAME:";
        send(message, InetAddress.getByName(BROADCAST_ADDRESS));
        System.out.println("SENT: To choose nickname request.");
    }

    //we send a message saying we're connected, the receiver won't send anything back
    public static void sendIAmConnected (User user) throws IOException {
        String message = "IAMCONNECTED: nickname: " + user.getNickname();
        send(message, InetAddress.getByName(BROADCAST_ADDRESS));
        System.out.println("SENT: I am connected.");
    }

    //we send a message saying we're connected and asks if the other people are too, the receiver will send a iamconnected message back
    public static void sendIAmConnectedAreYou (User user) throws IOException {
        String message = "IAMCONNECTEDAREYOU: nickname: " + user.getNickname();
        send(message, InetAddress.getByName(BROADCAST_ADDRESS));
        System.out.println("SENT: I am connected, are you?.");
    }

    public static void sendDisconnect (User user) throws IOException {
        String message = "DISCONNECT: nickname: " + user.getNickname();
        send(message, InetAddress.getByName(BROADCAST_ADDRESS));
        System.out.println("SENT: Disconnect.");
    }

    public static void toDisconnect() throws IOException {
        //have to socket close for all contacts ??
        SendMessage.sendDisconnect(me);
        me.setStatus(false);
        System.out.println("am supposed to close app after--------------");
    }


    //generic send method, useful in other methods
    private static void send (String message, InetAddress receiverAddress) throws IOException {
        byte[] buf = message.getBytes();
        DatagramPacket outPacket = new DatagramPacket(buf, buf.length, receiverAddress, BROADCAST_RECEIVER_PORT); //receiver port

        try {
            sendingSocket.send(outPacket);
            System.out.println();
        } catch (Exception e) {
            System.out.println("error: couldn't assign a socket to send a message");
            e.printStackTrace();
        }
    }
}