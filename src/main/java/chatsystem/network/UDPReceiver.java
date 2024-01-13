package chatsystem.network;

import chatsystem.MainClass;
import chatsystem.contacts.ContactAlreadyExists;
import chatsystem.contacts.ContactList;
import chatsystem.model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.*;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.*;

/** UDP server that (once started) listens indefinitely on a given port*/
public class UDPReceiver extends Thread {

    private static final Logger LOGGER = LogManager.getLogger(MainClass.class);

    /** Interface that observers of the UDP server must implement*/
    public interface Observer {
        /** Method that is called each time a message is received*/
        void handle(UDPMessage received);
    }

    private final DatagramSocket receivingSocket;
    private final List<Observer> observers = new ArrayList<>();

    private ContactList instance = ContactList.getInstance();

    public final ArrayList<String> myIPAddresses;
    public UDPReceiver() throws SocketException {
        receivingSocket = new DatagramSocket(MainClass.BROADCAST_RECEIVER_PORT);
        myIPAddresses = new ArrayList<String>();
        makeMyIPAddresses();
    }

    public void makeMyIPAddresses() throws SocketException {
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


    public String[] extractInfoFromPattern(String inputString) {
        List<String> patternList = List.of(
                "TO_CHOOSE_NICKNAME:",
                "IAMCONNECTED: nickname: (\\S+)",
                "IAMCONNECTEDAREYOU: nickname: (\\S+)",
                "DISCONNECT: nickname: (\\S+)"
        );


        boolean matchesAnyPattern = false;
        for (String patternString : patternList) {
            Pattern pattern = Pattern.compile(patternString);
            Matcher matcher1 = pattern.matcher(inputString);
            Matcher matcher2 = pattern.matcher(inputString);

            if (matcher1.matches()) {
                matchesAnyPattern = true;
                String[] res;

                if (matcher2.find()) {
                    if (patternString.equals("TO_CHOOSE_NICKNAME:")) {
                        res = new String[]{patternString};
                    } else if (patternString.equals("IAMCONNECTED: nickname: (\\S+)")) {
                        String nickname = matcher2.group(1);
                        res = new String[]{patternString, nickname};
                    } else if (patternString.equals("IAMCONNECTEDAREYOU: nickname: (\\S+)")) {
                        String nickname = matcher2.group(1);
                        res = new String[]{patternString, nickname};
                    } else if (patternString.equals("DISCONNECT: nickname: (\\S+)")) {
                        String nickname = matcher2.group(1);
                        res = new String[]{patternString, nickname};
                    } else {
                        res = new String[0];
                    }
                    return res;
                }
                break;
            }
        }

        if (!matchesAnyPattern) {
            System.out.println("error: string does not match any pattern in the list.");
        }
        return null;
    }

    /** Add a new observer to the class, for which the handle method will be called for each incoming message */
    public void addObserver (Observer obs) {
        synchronized (this.observers) {
            this.observers.add(obs);
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

                synchronized (this.observers) {
                    for (Observer obs : this.observers) {
                        obs.handle(message);
                    }
                }

                String senderIpAddress = inPacket.getAddress().getHostAddress();

                //check if i sent this
                boolean didISendThis = false;

                for (String myIPAddress : myIPAddresses) {
                    if (senderIpAddress.equals(String.valueOf(myIPAddress))) {
                        didISendThis = true;
                        break;
                    }
                }

                if (!didISendThis) {
                    String[] res = extractInfoFromPattern(received);

                    if (res != null) {
                        switch (res[0]) {
                            case "TO_CHOOSE_NICKNAME:" -> handleToChooseNickname();
                            case "IAMCONNECTED: nickname: (\\S+)" -> handleIAmConnected(res[1], inPacket.getAddress());
                            case "IAMCONNECTEDAREYOU: nickname: (\\S+)" ->
                                    handleIAmConnectedAreYou(res[1], inPacket.getAddress());
                            case "DISCONNECT: nickname: (\\S+)" -> handleDisconnect(res[1], inPacket.getAddress());
                            default -> System.out.println("error: unhandled message type");
                        }
                        System.out.println();
                    }
                }
            } catch (IOException e) {
                System.err.println("Receive error: " + e.getMessage());
            } catch (SQLException e){
                throw new RuntimeException("SQL Exception");
            }


        }
    }

    public void handleToChooseNickname() throws IOException {
        //when we receive the request, we respond by saying who we are
        UDPSender.sendIAmConnected(MainClass.me);
        System.out.println("RECEIVED to choose nickname request");
    }

    public void handleIAmConnected(String nickname, InetAddress ipAddress) throws UnknownHostException, SQLException {
        changeStatus(nickname, ipAddress, true);
        System.out.println("RECEIVED i am connected: " + nickname + " (" + ipAddress + ")");
    }

    public void handleIAmConnectedAreYou(String nickname, InetAddress ipAddress) throws IOException, SQLException {
        changeStatus(nickname, ipAddress, true);
        UDPSender.sendIAmConnected(MainClass.me);
        System.out.println("RECEIVED i am connected, are you?: " + nickname + " (" + ipAddress + ")");
    }

    public void handleDisconnect(String nickname, InetAddress ipAddress) throws UnknownHostException, SQLException {
        changeStatus(nickname, ipAddress, false);
        System.out.println("RECEIVED i am disconnected: " + nickname + " (" + ipAddress + ")");
    }

    public void changeStatus(String nickname, InetAddress ipAddress, Boolean status) throws UnknownHostException, SQLException {
        try {
            if (!(nickname.equals("[]"))) {
                instance.addContact(new User(nickname, "", "", "", "", status, ipAddress));
            }
        } catch (ContactAlreadyExists e) {
            //if we know him, we change his status
            User user = instance.getContactWithNickname(nickname);
            user.setStatus(status);
            instance.changeContact(user);
            System.out.println("contact already exists");

        }
    }


}