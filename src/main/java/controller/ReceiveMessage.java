package controller;

import model.ContactList;
import model.User;
import run.AppQueen;
import run.MainClass;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static model.ContactList.getInstance;

public class ReceiveMessage extends Thread {

    private static final int RECEIVER_PORT = 2000;

    public static boolean running = true;

    private ContactList instance = getInstance();

    private User me;

    public ReceiveMessage(User me) {this.me = me;}

    public String[] extractInfoFromPattern(String inputString) {
        List<String> patternList = List.of(
                "TO_CHOOSE_NICKNAME: ip: (\\S+)",
                "CONNECT: id: (\\S+) nickname: (\\S+) ip address: (\\S+)",
                "DISCONNECT: id: (\\S+) nickname: (\\S+) ip address: (\\S+)"
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
                    if (patternString.equals("TO_CHOOSE_NICKNAME: ip: (\\S+)")) {
                        String ipAddress = matcher2.group(1);
                        res = new String[]{patternString, ipAddress};
                    } else if (patternString.equals("CONNECT: id: (\\S+) nickname: (\\S+) ip address: (\\S+)")) {
                        String id = matcher2.group(1);
                        String nickname = matcher2.group(2);
                        String ipAddress = matcher2.group(3);
                        res = new String[]{patternString, id, nickname, ipAddress};
                    } else if (patternString.equals("DISCONNECT: id: (\\S+) nickname: (\\S+) ip address: (\\S+)")) {
                        String id = matcher2.group(1);
                        String nickname = matcher2.group(2);
                        String ipAddress = matcher2.group(3);
                        res = new String[]{patternString, id, nickname, ipAddress};
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

            while (running) {
                DatagramPacket inPacket = new DatagramPacket(buf, buf.length);
                receivingSocket.receive(inPacket);
                String received = new String(inPacket.getData(), 0, inPacket.getLength());

                String[] res = extractInfoFromPattern(received);

                if (res != null) {
                    switch (res[0]) {
                        case "TO_CHOOSE_NICKNAME: ip: (\\S+)" ->
                                handleToChooseNickname(res[1], inPacket.getAddress());
                        case "CONNECT: id: (\\S+) nickname: (\\S+) ip address: (\\S+)" ->
                                handleConnect(res[1], res[2], res[3]);
                        case "DISCONNECT: id: (\\S+) nickname: (\\S+) ip address: (\\S+)" ->
                                handleDisconnect(res[1], res[2], res[3]);
                        default -> System.out.println("error: unhandled message type");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("error: we closed the socket ??");
        }
    }

    private void handleToChooseNickname(String ipAddress, InetAddress requesterAddress) throws IOException {
        //when we receive the request, we respond by saying who we are
        //User currentUser = new User("id" + "someUniqueID", "someUniqueNickname", "", "", "", "", true, InetAddress.getByName(ipAddress));
        SendMessage.sendConnect(MainClass.me);
        System.out.println("Received to choose nickname request");
    }

    private void handleConnect(String id, String nickname, String ipAddress) throws UnknownHostException {
        if (instance.existsContact(id)) { //if we know him, we put him to connected
            User connectedUser = instance.getContact(id);
            connectedUser.setStatus(true);
            instance.changeContact(connectedUser);
            System.out.println("User connected: " + connectedUser.getNickname() + " (" + connectedUser.getIpAddress().getHostAddress() + ")");
        }
        else { //else we add him
            User connectedUser = new User(id, nickname, "", "", "", "", true, InetAddress.getByName(ipAddress));
            instance.addContact(connectedUser);
            System.out.println("User connected: " + connectedUser.getNickname() + " (" + connectedUser.getIpAddress().getHostAddress() + ")");

        }
    }

    private void handleDisconnect(String id, String nickname, String ipAddress) throws UnknownHostException {
        if (instance.existsContact(id)) { //if we know him, we put him to disconnected
            User disconnectedUser = instance.getContact(id);
            disconnectedUser.setStatus(false);
            instance.changeContact(disconnectedUser);
            System.out.println("User disconnected: " + disconnectedUser.getNickname() + " (" + disconnectedUser.getIpAddress().getHostAddress() + ")");
        }
        else { //else we add him
            instance.addContact(new User(id, nickname, "", "", "", "", true, InetAddress.getByName(ipAddress)));
        }
    }

}
