package controller;

import model.ContactList;
import model.User;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

import static run.MainClass.me;

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
    private static final int BROADCAST_PORT = 60000;

    private static int SENDER_PORT;


    public SendMessage(int socket) throws SocketException {
        SENDER_PORT = socket;
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
        send(message, InetAddress.getByName(BROADCAST_ADDRESS), BROADCAST_PORT);
        System.out.println("SENT: To choose nickname request.");
    }

    //we send a message saying we're connected, the receiver won't send anything back
    public static void sendIAmConnected (User user) throws IOException {
        String message = "IAMCONNECTED: id: " + user.getId() + " nickname: " + user.getNickname();
        send(message, InetAddress.getByName(BROADCAST_ADDRESS), BROADCAST_PORT);
        System.out.println("SENT: I am connected.");
    }

    //we send a message saying we're connected and asks if the other people are too, the receiver will send a iamconnected message back
    public static void sendIAmConnectedAreYou (User user) throws IOException {
        String message = "IAMCONNECTEDAREYOU: id: " + user.getId() + " nickname: " + user.getNickname();
        send(message, InetAddress.getByName(BROADCAST_ADDRESS), BROADCAST_PORT);
        System.out.println("SENT: I am connected, are you?.");
    }

    public static void sendDisconnect (User user) throws IOException {
        String message = "DISCONNECT: id: " + user.getId() + " nickname: " + user.getNickname();
        send(message, InetAddress.getByName(BROADCAST_ADDRESS), BROADCAST_PORT);
        System.out.println("SENT: Disconnect.");
    }

    public static void toDisconnect() throws IOException {
        //have to socket close for all contacts ??
        SendMessage.sendDisconnect(me);
        me.setStatus(false);
        System.out.println("am supposed to close app after--------------");
    }



    //TCP PART

    public static void sendAMessage(User user, String message) throws IOException {
        send(message, user.getIpAddress(), user.getMySocket());
        System.out.println("SENT: A Message.");
    }









    //generic send method, useful in other methods
    private static void send (String message, InetAddress receiverAddress, int receiverPort) throws IOException {
        byte[] buf = message.getBytes();
        DatagramPacket outPacket = new DatagramPacket(buf, buf.length, receiverAddress, receiverPort); //receiver port

        boolean found = false;
        while(!found) {
            try {
                DatagramSocket sendingSocket = new DatagramSocket(SENDER_PORT); //sending port

                found = true;

                ContactList instance = ContactList.getInstance();
                instance.updateMySocketOfUser(receiverAddress, SENDER_PORT);
                System.out.println("FOUND A RIGHT PORT " + SENDER_PORT);
                sendingSocket.setBroadcast(true);
                sendingSocket.send(outPacket);
                System.out.println();

                sendingSocket.close();
            } catch (Exception e) {

                System.out.println("error: couldn't assign a socket to send a message");
                e.printStackTrace();
                System.out.println("SENDER PORT " + SENDER_PORT);
                SENDER_PORT+=1;

            }
        }


    }
}

