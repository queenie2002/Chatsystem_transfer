package chatsystem.network;

import chatsystem.MainClass;
import chatsystem.observers.MyObserver;

import org.apache.logging.log4j.*;
import java.io.IOException;
import java.net.*;
import java.util.*;

/** UDP server that (once started) listens indefinitely on a given port*/
public class UDPReceiver extends Thread {


    //LOGGER
    private static final Logger LOGGER = LogManager.getLogger(UDPReceiver.class);


    //OBSERVERS
    private final List<MyObserver> observers = new ArrayList<>();
    /** Add a new observer to the class, for which the handle method will be called for each incoming message */
    public void addObserver (MyObserver obs) {
        synchronized (this.observers) {
            this.observers.add(obs);
        }
    }




    //OUR DATAGRAM SOCKET TO RECEIVE MESSAGES
    private static final DatagramSocket receivingSocket;

    static {
        try {
            receivingSocket = new DatagramSocket(MainClass.BROADCAST_RECEIVER_PORT);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }




    //MY IP ADDRESSES
    private final ArrayList<String> myIPAddresses;
    public UDPReceiver() throws SocketException {
        myIPAddresses = new ArrayList<String>();
        makeMyIPAddresses();
    }
    private void makeMyIPAddresses() throws SocketException {
        Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
        while(e.hasMoreElements())
        {
            NetworkInterface n = (NetworkInterface) e.nextElement();
            Enumeration<InetAddress> ee = n.getInetAddresses();
            while (ee.hasMoreElements())
            {
                InetAddress i = (InetAddress) ee.nextElement();
                myIPAddresses.add(i.getHostAddress());
                if (!i.getHostAddress().contains(":") && !i.getHostAddress().equals("127.0.0.1")) {
                    MainClass.me.setIpAddress(i);
                }
            }
        }
    }
    private static boolean isRunning = true;




    /** To stop UDP server*/
    public static void stopServer() {
        isRunning = false;
        receivingSocket.close();
        LOGGER.trace("Closed UDP server.");
    }



    public void run() {
        while (isRunning) {
            try {

                byte[] buf = new byte[1024];
                DatagramPacket inPacket = new DatagramPacket(buf, buf.length);

                //waits for a packet to come
                receivingSocket.receive(inPacket);

                //extracts info and makes a UDPMessage
                String received = new String(inPacket.getData(), 0, inPacket.getLength());
                UDPMessage message = new UDPMessage(received, inPacket.getAddress());

                LOGGER.trace("Received on port " + MainClass.BROADCAST_RECEIVER_PORT + ": " + message.content() + " from " + message.originAddress());




                //check if i sent this, if I did, i ignore the message
                boolean didISendThis = false;

                for (String myIPAddress : myIPAddresses) {
                    if (message.originAddress().getHostAddress().equals(String.valueOf(myIPAddress))) {
                        didISendThis = true;
                        break;
                    }
                }

                //if i didn't, i handle it
                if (!didISendThis) {
                    synchronized (this.observers) {
                        for (MyObserver obs : this.observers) {
                            obs.handle(message);
                        }
                    }
                }
            } catch (IOException e) {
                if (isRunning) {
                    LOGGER.error("Receive error: " + e.getMessage());
                }
            }
        }
    }




}