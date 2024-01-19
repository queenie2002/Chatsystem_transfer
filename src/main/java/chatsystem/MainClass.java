package chatsystem;

import chatsystem.contacts.*;
import chatsystem.controller.Controller;
import chatsystem.database.DatabaseMethods;
import chatsystem.network.*;
import chatsystem.observers.MyObserver;
import chatsystem.ui.*;


import org.apache.logging.log4j.*;
import org.apache.logging.log4j.core.config.Configurator;
import java.io.IOException;
import java.net.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class MainClass {

    //LOGGER
    private static final Logger LOGGER = LogManager.getLogger(MainClass.class);



    public static final int BROADCAST_RECEIVER_PORT = 2000;
    public static final int TCP_SERVER_PORT = 6666;
    public static MyObserver controller = new Controller();
    public static User me;
    static {
        try {
            me = new User("[]", "[]", "[]", "[]", "[]", null, InetAddress.getByName("0.0.0.0"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }




    public static void main(String[] args) throws SQLException, UnknownHostException {

        Configurator.setRootLevel(Level.INFO);
        LOGGER.info("Starting ChatSystem application");

        MainClass mainclass = new MainClass();

        // Initialize and start UDP components
        mainclass.initializeUDPComponents();

        //Initialize Database and Contact List
        mainclass.initializeDatabaseAndContactList();

        // Initialize and start TCP server
        mainclass.initializeTCPServer();

        // Check online contacts
        mainclass.checkOnlineContacts();

    }

    private void initializeUDPComponents() {
        UDPReceiver udpReceiver;
        try {
            udpReceiver = new UDPReceiver();
            udpReceiver.addObserver(controller);
            udpReceiver.start();

            Beginning beginning = new Beginning();
            beginning.addObserver(controller);

        } catch (SocketException e) {
            LOGGER.error("Could not start UDP server: " + e.getMessage());
            System.exit(1);
        } catch (IOException e) {
            LOGGER.error("Error in application: " + e.getMessage());
            System.exit(1);
        }
    }



    private void initializeTCPServer() {
        TCPServer myServer = new TCPServer();
        myServer.addObserver(controller);

        try {
            myServer.start(TCP_SERVER_PORT);
        } catch (IOException e) {
            LOGGER.error("Failed to start TCP Server: " + e.getMessage());
            System.exit(1);
        }
    }


    private void initializeDatabaseAndContactList() throws SQLException {

        DatabaseMethods database = new DatabaseMethods();
        database.addObserver(controller);
        DatabaseMethods.startConnection(me);
        ContactList.getInstance().addObserver(controller);

    }

    private void checkOnlineContacts() {
        ArrayList<User> onlineUsers = ContactList.getInstance().getConnectedContactsList();

        if (onlineUsers.isEmpty()) {
            System.out.println("No contacts are online.");
        } else {
            System.out.println("Online Contacts:");
            for (User user : onlineUsers) {
                System.out.println("Nickname: " + user.getNickname() + ", IP Address: " + user.getIpAddress());
            }
        }
    }

}
