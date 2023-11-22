package controller;

import model.ContactList;
import model.User;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static model.ContactList.getInstance;

public class ReceiveMessageRaph extends Thread {

    public static boolean running = true;

    public String[] extractInfoFromPattern(String inputString) {
        List<String> patternList = List.of(
                "POTENTIAL NICKNAME: nickname: (\\S+) ip address: (\\S+)",
                "DISCONNECT: id: (\\S+)",
                "CONNECT: id: (\\S+) nickname: (\\S+) ip address: (\\S+)",
                "NICKNAME EXISTS",
                "NETWORK_SCAN_REQUEST: id: (\\S+)",
                "CONNECT_RESPONSE: id: (\\S+) nickname: (\\S+) ip address: (\\S+)"
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
                    if (patternString.equals("POTENTIAL NICKNAME: nickname: (\\S+) ip address: (\\S+)")) {
                        // Extract information for the "POTENTIAL NICKNAME" pattern
                        String nickname = matcher2.group(1);
                        String ipAddress = matcher2.group(2);
                        res = new String[]{patternString, nickname, ipAddress};
                    } else if (patternString.equals("DISCONNECT: id: (\\S+)")) {
                        // Extract information for the "DISCONNECT" pattern
                        String id = matcher2.group(1);
                        res = new String[]{patternString, id};
                    } else if (patternString.equals("CONNECT: id: (\\S+) nickname: (\\S+) ip address: (\\S+)")) {
                        // Extract information for the "CONNECT" pattern
                        String id = matcher2.group(1);
                        String nickname = matcher2.group(2);
                        String ipAddress = matcher2.group(3);
                        res = new String[]{patternString, id, nickname, ipAddress};
                    } else if (patternString.equals("NICKNAME EXISTS")) {
                        res = new String[]{patternString};
                    } else if (patternString.equals("NETWORK_SCAN_REQUEST: id: (\\S+)")) {
                        // Extract information for the "NETWORK_SCAN_REQUEST" pattern
                        String id = matcher2.group(1);
                        res = new String[]{patternString, id};
                    } else if (patternString.equals("CONNECT_RESPONSE: id: (\\S+) nickname: (\\S+) ip address: (\\S+)")) {
                        // Extract information for the "CONNECT_RESPONSE" pattern
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
        try (DatagramSocket receivingSocket = new DatagramSocket(2000)) {
            byte[] buf = new byte[256];

            while (running) {
                DatagramPacket inPacket = new DatagramPacket(buf, buf.length);
                receivingSocket.receive(inPacket);
                String received = new String(inPacket.getData(), 0, inPacket.getLength());

                String[] res = extractInfoFromPattern(received);

                if (res != null) {
                    // Handle different types of messages
                    switch (res[0]) {
                        case "NETWORK_SCAN_REQUEST: id: (\\S+)":
                            handleNetworkScanRequest(res[1]);
                            break;
                        case "DISCONNECT: id: (\\S+)":
                            handleDisconnect(res[1]);
                            break;
                        case "CONNECT: id: (\\S+) nickname: (\\S+) ip address: (\\S+)":
                            handleConnect(res[1], res[2], res[3]);
                            break;
                        case "CONNECT_RESPONSE: id: (\\S+) nickname: (\\S+) ip address: (\\S+)":
                            handleConnectResponse(res[1], res[2], res[3]);
                            break;
                        case "NICKNAME EXISTS":
                            handleNicknameExists();
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

    private void handleNetworkScanRequest(String id) {
        // Respond with the user's IP and pseudo
        SendMessageRaph sender = new SendMessageRaph();
        ContactList instance = getInstance();
        User user = instance.getContact(id);
        if (user != null) {
            try {
                sender.sendConnectResponse(user);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleDisconnect(String
