package chatsystem;

import chatsystem.contacts.User;
import chatsystem.database.DatabaseMethods;
import chatsystem.network.*;
import chatsystem.view.Beginning;
import org.apache.logging.log4j.*;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.Level;


import java.io.IOException;
import java.net.*;
import java.sql.SQLException;
public class MainClass {


    private static final Logger LOGGER = LogManager.getLogger(MainClass.class);

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
        LOGGER.info("Starting ChatSystem application");
        Configurator.setRootLevel(Level.INFO);

        UDPSender UDPSender = new UDPSender();

        try {
            UDPReceiver udpReceiver = new UDPReceiver();
            udpReceiver.start();

            udpReceiver.addObserver(new UDPReceiver.Observer() {
                @Override
                public void handle(UDPMessage received) {
                    //whatever you want to do
                }
            });

            Beginning beginning = new Beginning(udpReceiver, UDPSender);


        } catch (SocketException e) {
            System.err.println("Could not start UDP server: " + e.getMessage());
            System.exit(1);
        } catch (IOException e) {
            System.err.println("idk where this comes from, maybe the view ??" );
            System.exit(1);
        }

        DatabaseMethods.startConnection(me);




        /*TCPServer myServer = new TCPServer();

        TCPMessage chat = new TCPMessage("content","date","fromUserIP","toUserIP");
        myServer.addObserver(new TCPServer.MessageObserver() {
            @Override
            public void handleMessage(String msg) throws SQLException {
                DatabaseMethods.addMessage(chat);
            }
        });*/
    }


}
