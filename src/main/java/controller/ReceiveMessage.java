package controller;

import model.ContactList;
import model.User;
import run.MainClass;

import java.io.IOException;
import java.net.*;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;

import static model.ContactList.getInstance;

public class ReceiveMessage extends Thread {

    private static int RECEIVER_PORT;
    private ContactList instance = getInstance();

    public final ArrayList<String> myIPAddresses;
    public ReceiveMessage(int socket) throws SocketException { //i'm adding a constructor qui est empty
        myIPAddresses = new ArrayList<String>();
        makeMyIPAddresses();
        RECEIVER_PORT = socket;
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
            }
        }
    }


    public String[] extractInfoFromPattern(String inputString) {
        List<String> patternList = List.of(
                "TO_CHOOSE_NICKNAME:",
                "IAMCONNECTED: id: (\\S+) nickname: (\\S+)",
                "IAMCONNECTEDAREYOU: id: (\\S+) nickname: (\\S+)",
                "DISCONNECT: id: (\\S+) nickname: (\\S+)"
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
                    } else if (patternString.equals("IAMCONNECTED: id: (\\S+) nickname: (\\S+)")) {
                        String id = matcher2.group(1);
                        String nickname = matcher2.group(2);
                        res = new String[]{patternString, id, nickname};
                    } else if (patternString.equals("IAMCONNECTEDAREYOU: id: (\\S+) nickname: (\\S+)")) {
                        String id = matcher2.group(1);
                        String nickname = matcher2.group(2);
                        res = new String[]{patternString, id, nickname};
                    } else if (patternString.equals("DISCONNECT: id: (\\S+) nickname: (\\S+)")) {
                        String id = matcher2.group(1);
                        String nickname = matcher2.group(2);
                        res = new String[]{patternString, id, nickname};
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

    public void run() {
        try (DatagramSocket receivingSocket = new DatagramSocket(RECEIVER_PORT)) {
            byte[] buf = new byte[256];

            while (true) {
                DatagramPacket inPacket = new DatagramPacket(buf, buf.length);
                try {
                    receivingSocket.receive(inPacket);
                }
                catch(Exception e){
                    e.printStackTrace();
                    return;
                }


                String received = new String(inPacket.getData(), 0, inPacket.getLength());

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
                            case "IAMCONNECTED: id: (\\S+) nickname: (\\S+)" -> handleIAmConnected(res[1], res[2], inPacket.getAddress());
                            case "IAMCONNECTEDAREYOU: id: (\\S+) nickname: (\\S+)" ->
                                    handleIAmConnectedAreYou(res[1], res[2], inPacket.getAddress());
                            case "DISCONNECT: id: (\\S+) nickname: (\\S+)" -> handleDisconnect(res[1], res[2], inPacket.getAddress());
                            default -> System.out.println("error: unhandled message type");
                        }
                        System.out.println();
                    }
                }



            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("error: we closed the socket ??");
        }
    }

    public void handleToChooseNickname() throws IOException {
        //when we receive the request, we respond by saying who we are
        SendMessage.sendIAmConnected(MainClass.me);
        System.out.println("RECEIVED to choose nickname request");
    }

    public void handleIAmConnected(String id, String nickname, InetAddress ipAddress) throws UnknownHostException, SocketException {
        changeStatus(id, nickname, ipAddress, true);
        System.out.println("RECEIVED i am connected: " + nickname + " (" + ipAddress + ")");
        System.out.println();
        System.out.println("Printing Contact List ");
        instance.printContactList();

    }

    public void handleIAmConnectedAreYou(String id, String nickname, InetAddress ipAddress) throws IOException {
        changeStatus(id, nickname, ipAddress, true);
        SendMessage.sendIAmConnected(MainClass.me);
        System.out.println("RECEIVED i am connected, are you?: " + nickname + " (" + ipAddress + ")");
    }

    public void handleDisconnect(String id, String nickname, InetAddress ipAddress) throws UnknownHostException, SocketException {
        changeStatus(id, nickname, ipAddress, false);
        System.out.println("RECEIVED i am disconnected: " + nickname + " (" + ipAddress + ")");
    }

    public void changeStatus(String id, String nickname, InetAddress ipAddress, Boolean status) throws UnknownHostException, SocketException {
        if (instance.existsContact(id)) { //if we know him, we change his status
            User user = instance.getContact(id);
            user.setStatus(status);
            instance.changeContact(user);
            System.out.println("contact already exists");
        }
        else { //else we add him
            if (!((id.equals("[]")) && (nickname.equals("[]")))) {
                instance.addContact(new User(id, nickname, "", "", "", "", status, ipAddress));
            }
        }
    }


}
