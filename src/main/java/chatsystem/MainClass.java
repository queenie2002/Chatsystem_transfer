
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
import chatsystem.controller.Controller;
import chatsystem.database.DatabaseMethods;
import chatsystem.network.*;
import chatsystem.observers.MyObserver;
import chatsystem.ui.*;
import org.apache.logging.log4j.*;
import org.apache.logging.log4j.core.config.Configurator;

import javax.swing.*;
import java.io.IOException;
import java.net.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class MainClass {

    private static final Logger LOGGER = LogManager.getLogger(MainClass.class);

    public static final int BROADCAST_RECEIVER_PORT = 2000;
    public static final int TCP_SERVER_PORT = 6666;
    //private static ChatWindow chatWindow;

    public static User me;
    static {
        try {
            me = new User("[]", "[]", "[]", "[]", "[]", null, InetAddress.getByName("0.0.0.0"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static MyObserver controller = new Controller();



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
        myServer.addObserver(new TCPServer.MessageObserver() {
            @Override
            public void handleMessage(TCPMessage msg) throws SQLException {
                System.out.println("Received message: " + msg.getContent());
                DatabaseMethods.addMessage(msg);

                // Determine the chat window based on the message
                String userKey = msg.getFromUserIP(); // Or any other identifier you use
                ChatWindow2 chatWindow = MainWindow.getChatWindowForUser(userKey);
                if (chatWindow != null) {
                    SwingUtilities.invokeLater(() -> chatWindow.displayReceivedMessage(msg));
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


    private void initializeDatabaseAndContactList() throws SQLException {

        // Start connection with the database
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
