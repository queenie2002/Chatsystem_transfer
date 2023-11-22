package controller;

import model.ContactList;
import model.User;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static model.ContactList.getInstance;

public class ReceiveMessage {

    private static final int RECEIVER_PORT = 2000;

    public static boolean running = true;

    private List<User> connectedUsers = new ArrayList<>();

    public String[] extractInfoFromPattern(String inputString) {
        List<String> patternList = List.of(
                "NETWORK_SCAN_REQUEST: ip: (\\S+)",
                "NETWORK_SCAN_RESPONSE: id: (\\S+) nickname: (\\S+) ip address: (\\S+)",
                "NICKNAME_AND_IP: nickname: (\\S+) ip address: (\\S+)",
                "DISCONNECT: id: (\\S+)"
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
                    if (patternString.equals("NETWORK_SCAN_REQUEST: ip: (\\S+)")) {
                        String ipAddress = matcher2.group(1);
                        res = new String[]{patternString, ipAddress};
                    } else if (patternString.equals("NETWORK_SCAN_RESPONSE: id: (\\S+) nickname: (\\S+) ip address: (\\S+)")) {
                        String id = matcher2.group(1);
                        String nickname = matcher2.group(2);
                        String ipAddress = matcher2.group(3);
                        res = new String[]{patternString, id, nickname, ipAddress};
                    } else if (patternString.equals("NICKNAME_AND_IP: nickname: (\\S+) ip address: (\\S+)")) {
                        String nickname = matcher2.group(1);
                        String ipAddress = matcher2.group(2);
                        res = new String[]{patternString, nickname, ipAddress};
                    } else if (patternString.equals("DISCONNECT: id: (\\S+)")) {
                        String id = matcher2.group(1);
                        res = new String[]{patternString, id};
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
                        case "NETWORK_SCAN_REQUEST: ip: (\\S+)":
                            handleNetworkScanRequest(res[1], inPacket.getAddress());
                            break;
                        case "NETWORK_SCAN_RESPONSE: id: (\\S+) nickname: (\\S+) ip address: (\\S+)":
                            handleNetworkScanResponse(res[1], res[2], res[3]);
                            break;
                        case "NICKNAME_AND_IP: nickname: (\\S+) ip address: (\\S+)":
                            handleNicknameAndIP(res[1], res[2]);
                            break;
                        case "DISCONNECT: id: (\\S+)":
                            handleDisconnect(res[1]);
                            break;
                        default:
                            System.out.println("Unhandled message type");
                            break;
                    }
                }
            }

            if (!running) {
                System.out.println("Closing the app");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleNetworkScanRequest(String ipAddress, InetAddress requesterAddress) throws IOException {
        SendMessage sender = new SendMessage();
        User currentUser = new User("id" + "someUniqueID", "someUniqueNickname", "", "", "", "", true, InetAddress.getByName(ipAddress));
        sender.sendNetworkScanResponse(currentUser, requesterAddress);
    }

    private void handleNetworkScanResponse(String id, String nickname, String ipAddress) throws UnknownHostException {
        User user = new User(id, nickname, "", "", "", "", true, InetAddress.getByName(ipAddress));
        connectedUsers.add(user);
        System.out.println("User connected: " + nickname + " (" + ipAddress + ")");
    }

    private void handleNicknameAndIP(String nickname, String ipAddress) throws UnknownHostException {
        User user = new User("id" + nickname, nickname, "", "", "", "", true, InetAddress.getByName(ipAddress));
        connectedUsers.add(user);
        System.out.println("User connected: " + nickname + " (" + ipAddress + ")");
    }

    private void handleDisconnect(String id) {
        ContactList instance = getInstance();
        User disconnectedUser = instance.getContact(id);

        if (disconnectedUser != null) {
            disconnectedUser.setStatus(false);
            instance.changeContact(disconnectedUser);
            connectedUsers.remove(disconnectedUser);
            System.out.println("User disconnected: " + disconnectedUser.getNickname() + " (" + disconnectedUser.getIpAddress().getHostAddress() + ")");
        }
    }
}
