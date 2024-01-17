
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
import chatsystem.ui.*;
import com.sun.tools.javac.Main;
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
    private static ChatWindow chatWindow; // Reference to ChatWindow

    public static User me;
    static {
        try {
            me = new User("[]", "[]", "[]", "[]", "[]", null, InetAddress.getByName("0.0.0.0"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private Controller controller = new Controller();



    public static void main(String[] args) throws SQLException {

        Configurator.setRootLevel(Level.INFO);
        LOGGER.info("Starting ChatSystem application");

        // Initialize View
        View.initialize();

        MainClass mainclass = new MainClass();

        // Initialize and start UDP components
        mainclass.initializeUDPComponents();

        //Initialize Database and Contact List
        mainclass.initializeDatabaseAndContactList();

        // Initialize and start TCP server
        mainclass.initializeTCPServer();



        // Check online contacts
        mainclass.checkOnlineContacts();



        // Additional logic, if any, goes here
    }

    private void initializeUDPComponents() {
        UDPSender udpSender = new UDPSender();
        UDPReceiver udpReceiver;
        try {
            udpReceiver = new UDPReceiver();


            udpReceiver.addObserver(Controller::handle);



            udpReceiver.start();
            new Beginning(udpReceiver, udpSender);
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
            public void handleMessage(String msg) throws SQLException {
                TCPMessage chat = new TCPMessage(msg,"date","ip1","ip2");
                DatabaseMethods.addMessage(chat);
                if (chatWindow != null) {
                    SwingUtilities.invokeLater(() -> chatWindow.displayMessage(chat));
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


        ContactList.getInstance().addObserver(new ContactList.Observer() {
            @Override
            public void newContactAdded(User user) throws SQLException {
                DatabaseMethods.addUser(user);
            }

            @Override
            public void nicknameChanged(User newUser, String previousNickname) {

            }
        });

    }

    public void checkOnlineContacts() {
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
