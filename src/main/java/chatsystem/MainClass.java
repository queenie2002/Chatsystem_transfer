//package chatsystem;
//
//import chatsystem.contacts.User;
//import chatsystem.database.DatabaseMethods;
//import chatsystem.network.*;
//import chatsystem.view.Beginning;
//import org.apache.logging.log4j.*;
//import org.apache.logging.log4j.core.config.Configurator;
//import org.apache.logging.log4j.Level;
//
//
//import java.io.IOException;
//import java.net.*;
//import java.sql.SQLException;
//public class MainClass {
//
//
//    private static final Logger LOGGER = LogManager.getLogger(MainClass.class);
//
//    //we create an empty user to keep my information
//    public static User me;
//
//    static {
//        try {
//            me = new User("[]", "[]", "[]", "[]" , "[]", null, InetAddress.getByName("0.0.0.0"));
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public static final int BROADCAST_RECEIVER_PORT = 2000;
//
//
//    public static void main(String[] args) throws SQLException {
//        LOGGER.info("Starting ChatSystem application");
//        Configurator.setRootLevel(Level.INFO);
//
//        UDPSender UDPSender = new UDPSender();
//
//        try {
//            UDPReceiver udpReceiver = new UDPReceiver();
//            udpReceiver.start();
//
//            udpReceiver.addObserver(new UDPReceiver.Observer() {
//                @Override
//                public void handle(UDPMessage received) {
//                    //whatever you want to do
//                }
//            });
//
//            Beginning beginning = new Beginning(udpReceiver, UDPSender);
//
//
//        } catch (SocketException e) {
//            System.err.println("Could not start UDP server: " + e.getMessage());
//            System.exit(1);
//        } catch (IOException e) {
//            System.err.println("idk where this comes from, maybe the view ??" );
//            System.exit(1);
//        }
//
//        DatabaseMethods.startConnection(me);
//
//
//
//
//        /*TCPServer myServer = new TCPServer();
//
//        TCPMessage chat = new TCPMessage("content","date","fromUserIP","toUserIP");
//        myServer.addObserver(new TCPServer.MessageObserver() {
//            @Override
//            public void handleMessage(String msg) throws SQLException {
//                DatabaseMethods.addMessage(chat);
//            }
//        });*/
//    }
//
//
//}

package chatsystem;

import chatsystem.contacts.*;
import chatsystem.database.DatabaseMethods;
import chatsystem.network.*;
import chatsystem.view.Beginning;
import chatsystem.view.ChatWindow;
import org.apache.logging.log4j.*;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.Level;

import javax.swing.*;
import java.io.IOException;
import java.net.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class MainClass {

    private static final Logger LOGGER = LogManager.getLogger(MainClass.class);
    public static User me;
    public static final int BROADCAST_RECEIVER_PORT = 2000;
    public static final int TCP_SERVER_PORT = 6666;
    private static ChatWindow chatWindow; // Reference to ChatWindow

    static {
        try {
            me = new User("[]", "[]", "[]", "[]", "[]", null, InetAddress.getByName("0.0.0.0"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws SQLException {
        LOGGER.info("Starting ChatSystem application");
        Configurator.setRootLevel(Level.INFO);

        // Initialize and start UDP components
        initializeUDPComponents();

        // Initialize and start TCP server
        initializeTCPServer();

        // Start connection with the database
        DatabaseMethods.startConnection(me);

        // Check online contacts
        checkOnlineContacts();

        // Create and set the chat window
        chatWindow = new ChatWindow();
        // Additional logic, if any, goes here
    }

    private static void initializeUDPComponents() {
        UDPSender udpSender = new UDPSender();
        UDPReceiver udpReceiver;
        try {
            udpReceiver = new UDPReceiver();
            udpReceiver.start();
            udpReceiver.addObserver(new UDPReceiver.Observer() {
                @Override
                public void handle(UDPMessage received) {
                    // Handle received UDP messages
                }
            });

            new Beginning(udpReceiver, udpSender);
        } catch (SocketException e) {
            LOGGER.error("Could not start UDP server: " + e.getMessage());
            System.exit(1);
        } catch (IOException e) {
            LOGGER.error("Error in application: " + e.getMessage());
            System.exit(1);
        }
    }

    private static void initializeTCPServer() {
        TCPServer myServer = new TCPServer();
        myServer.addObserver(new TCPServer.MessageObserver() {
            @Override
            public void handleMessage(String msg) throws SQLException {
                TCPMessage chat = TCPMessage.deserialize(msg);
                DatabaseMethods.addMessage(chat);
                if (chatWindow != null) {
                    SwingUtilities.invokeLater(() -> chatWindow.displayNewMessage(chat));
                }
            }
        });

        try {
            myServer.start(TCP_SERVER_PORT);
        } catch (IOException e) {
            LOGGER.error("Failed to start TCP Server: " + e.getMessage());
            System.exit(1);
        }
    }

    public static void checkOnlineContacts() {
        ArrayList<User> onlineUsers = ContactList.getInstance().getConnectedContactsList();

        if (onlineUsers.isEmpty()) {
            System.out.println("No contacts are online.");
        } else {
            System.out.println("Online Contacts:");
            for (User user : onlineUsers) {
                System.out.println("Nickname: " + user.getNickname() + ", IP Address: " + user.getIpAddress());
                // Additional logic if needed
            }
        }
    }

    // MainClass other methods...
}
