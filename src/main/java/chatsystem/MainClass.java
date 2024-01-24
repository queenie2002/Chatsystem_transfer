package chatsystem;

import chatsystem.contacts.*;
import chatsystem.controller.Controller;
import chatsystem.database.DatabaseMethods;
import chatsystem.network.*;
import chatsystem.ui.*;


import org.apache.logging.log4j.*;
import org.apache.logging.log4j.core.config.Configurator;
import java.io.IOException;
import java.net.*;
import java.sql.SQLException;

public class MainClass {

    //LOGGER
    private static final Logger LOGGER = LogManager.getLogger(MainClass.class);


    //my informations, empty to begin with
    public static User me;
    static {
        try {
            me = new User("[]", "[]", null, InetAddress.getByName("0.0.0.0"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static final int BROADCAST_RECEIVER_PORT = 2000;
    public static final int TCP_SERVER_PORT = 6666;
    public static Controller controller = new Controller();





    public static void main(String[] args) {

        Configurator.setRootLevel(Level.INFO);
        LOGGER.info("Starting ChatSystem application");

        MainClass mainclass = new MainClass();

        // Initialize and start UDP components
        mainclass.initializeUDPComponents();

        //Initialize Database and Contact List
        mainclass.initializeDatabaseAndContactList();

        // Initialize and start TCP server
        mainclass.initializeTCPServer();

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


    private void initializeDatabaseAndContactList() {

        DatabaseMethods database = new DatabaseMethods();
        database.addObserver(controller);
        DatabaseMethods.startConnection(me);
        DatabaseMethods.initializeDatabase();
        ContactList.getInstance().addObserver(controller);

    }

}
