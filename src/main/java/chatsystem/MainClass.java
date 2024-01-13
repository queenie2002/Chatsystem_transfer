package chatsystem;

import chatsystem.controller.DatabaseMethods;
import chatsystem.network.*;
import chatsystem.model.User;
import chatsystem.view.Beginning;

import java.io.IOException;
import java.net.*;
import java.sql.SQLException;

public class MainClass {

    //we create an empty user to keep my information
    public static User me;

    static {
        try {
            me = new User("[]", "[]", "[]", "[]" , "[]", null, InetAddress.getByName("0.0.0.0"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static final int BROADCAST_RECEIVER_PORT = 2000;

    public static void main(String[] args) throws SQLException {

        UDPSender UDPSender = new UDPSender();

        try {
            UDPReceiver UDPReceiver = new UDPReceiver();
            UDPReceiver.start();

            Beginning beginning = new Beginning(UDPReceiver, UDPSender);

        } catch (SocketException e) {
            System.err.println("Could not start UDP server: " + e.getMessage());
            System.exit(1);
        } catch (IOException e) {
            System.err.println("idk where this comes from, maybe the view ??" );
            System.exit(1);
        }

        DatabaseMethods.startConnection(me);




        /*tcpServer myServer = new tcpServer();

        Chat chat = new Chat("content","date","fromUserIP","toUserIP");
        myServer.addObserver(new tcpServer.MessageObserver() {
            @Override
            public void handleMessage(String msg) throws SQLException {
                DatabaseMethods.addMessage(chat);
            }
        });*/
    }


}
