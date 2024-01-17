package chatsystem.network;

import chatsystem.MainClass;
import org.apache.logging.log4j.*;

import java.io.IOException;
import java.net.*;
import java.util.*;

/** UDP server that (once started) listens indefinitely on a given port*/
public class UDPReceiver extends Thread {

    private static final Logger LOGGER = LogManager.getLogger(MainClass.class);

    /** Interface that observers of the UDP server must implement*/
    public interface Observer {
        /** Method that is called each time a message is received*/
        void handle(UDPMessage received);
    }
    private final List<Observer> observers = new ArrayList<>();
    /** Add a new observer to the class, for which the handle method will be called for each incoming message */
    public void addObserver (Observer obs) {
        synchronized (this.observers) {
            this.observers.add(obs);
        }
    }





    private final DatagramSocket receivingSocket;
    public final ArrayList<String> myIPAddresses;
    public UDPReceiver() throws SocketException {
        receivingSocket = new DatagramSocket(MainClass.BROADCAST_RECEIVER_PORT);
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







    public void run() {
        while (true) {
            try {

                byte[] buf = new byte[1024];
                DatagramPacket inPacket = new DatagramPacket(buf, buf.length);

                receivingSocket.receive(inPacket);

                String received = new String(inPacket.getData(), 0, inPacket.getLength());
                UDPMessage message = new UDPMessage(received, inPacket.getAddress());

                LOGGER.trace("Received on port " + MainClass.BROADCAST_RECEIVER_PORT + ": " + message.content() + " from " + message.originAddress());




                //check if i sent this
                boolean didISendThis = false;

                for (String myIPAddress : myIPAddresses) {
                    if (message.originAddress().getHostAddress().equals(String.valueOf(myIPAddress))) {
                        didISendThis = true;
                        break;
                    }
                }

                if (!didISendThis) {
                    synchronized (this.observers) {
                        for (Observer obs : this.observers) {
                            obs.handle(message);
                        }
                    }
                }
            } catch (IOException e) {
                LOGGER.error("Receive error: " + e.getMessage());
            }
        }
    }




}