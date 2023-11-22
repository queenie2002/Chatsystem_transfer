package controller;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import model.ContactList;
import model.User;

import static model.ContactList.getInstance;

public class ReceiveMessageRaph extends Thread {

    public static boolean running = true; // boolean to keep receive turning

    public String[] extractInfoFromPattern(String inputString) {

        // List of patterns for better readability
        List<String> patternList = List.of(
                "POTENTIAL NICKNAME: nickname: (\\S+) ip address: (\\S+)", // broadcast at the beginning, unique pseudo
                "DISCONNECT: id: (\\S+)", // disconnecting
                "CONNECT: id: (\\S+) nickname: (\\S+) ip address: (\\S+)", // connecting
                "NICKNAME EXISTS" // nickname exists
        );

        boolean matchesAnyPattern = false;
        for (String patternString : patternList) {
            Pattern pattern = Pattern.compile(patternString);
            Matcher matcher1 = pattern.matcher(inputString);
            Matcher matcher2 = pattern.matcher(inputString);

            if (matcher1.matches()) {
                matchesAnyPattern = true;
                String[] res;

                // Check if a match is found
                if (matcher2.find()) {
                    if (patternString.equals("POTENTIAL NICKNAME: nickname: (\\S+) ip address: (\\S+)")) {
                        String nickname = matcher2.group(1);
                        String ipAddress = matcher2.group(2);

                        res = new String[]{patternString, nickname, ipAddress};
                    } else if (patternString.equals("DISCONNECT: id: (\\S+)")) {
                        // Extract the matched numbers
                        String id = matcher2.group(1);

                        res = new String[]{patternString, id};
                    } else if (patternString.equals("CONNECT: id: (\\S+) nickname: (\\S+) ip address: (\\S+)")) {
                        // Extract the matched numbers
                        String id = matcher2.group(1);
                        String nickname = matcher2.group(2);
                        String ipAddress = matcher2.group(3);

                        res = new String[]{patternString, id, nickname, ipAddress};
                    } else if (patternString.equals("NICKNAME EXISTS")) {
                        res = new String[]{patternString};
                    } else {
                        res = new String[0];
                    }
                    return res;
                }
                break; // Exit the loop if a match is found
            }
        }

        if (!matchesAnyPattern) {
            System.out.println("error: string does not match any pattern in the list.");
        }
        return null;
    }

    public void run() {

        // this receives messages
        try (DatagramSocket receivingSocket = new DatagramSocket(2000)) {
            byte[] buf = new byte[256];

            while (running) {
                DatagramPacket inPacket = new DatagramPacket(buf, buf.length);
                receivingSocket.receive(inPacket);
                String received = new String(inPacket.getData(), 0, inPacket.getLength());

                // extracts info from packet
                String[] res = extractInfoFromPattern(received);

                // si c'est le broadcast du début, adds un contact with ip and nickname and updates his status to connected
                // have to deal with unique pseudo here
                if (res[0].equals("POTENTIAL NICKNAME: nickname: (\\S+) ip address: (\\S+)")) {
                    ContactList instance = getInstance();

                    String nickname = res[1];
                    String ipAddress = res[2];

                    if (instance.existsContactWithNickname(nickname)) {
                        // if we already know that person, we update their status to connected and send message saying there already exists someone
                        User userSender = instance.getContactWithNickname(nickname);
                        userSender.setStatus(true);
                        instance.changeContact(userSender);
                    } else {
                        // have to check if their nickname is unique
                        if (instance.existsContactWithNickname(nickname)) {
                            // have to send a message saying there already exists someone A MODIFIER ON RENTRE PAS LA CA CEST EN HAUT
                            SendMessage s1 = new SendMessage();
                            User userSender = new User("id" + nickname, nickname, "", "", "", "", true, InetAddress.getByName(ipAddress.substring(11)));
                            s1.sendNicknameExists(userSender);
                        } else {
                            // we add them to our list (mais en vrai non)
                            User userSender = new User("id" + nickname, nickname, "", "", "", "", true, InetAddress.getByName(ipAddress.substring(11)));
                            instance.addContact(userSender);
                        }
                    }
                }

                // si c'est message disconnect, updates user to disconnected
                if (res[0].equals("DISCONNECT: id: (\\S+)")) {
                    String id = res[1];
                    ContactList instance = getInstance();
                    User userSender = instance.getContact(id);
                    userSender.setStatus(false);
                    instance.changeContact(userSender);
                }

                // si c'est message connect, updates user to connected if we already know them and else we create a new user
                if (res[0].equals("CONNECT: id: (\\S+) nickname: (\\S+) ip address: (\\S+)")) {
                    String id = res[1];
                    String nickname = res[2];
                    String ipAddress = res[3];
                    ContactList instance = getInstance();

                    if (instance.existsContact(id)) {
                        // we already know them so we update status ------------A TESTER CETTE WHOLE FONCTION
                        User userSender = instance.getContact(id);
                        userSender.setStatus(true);
                        instance.changeContact(userSender);
                    } else {
                        // we add them to our list
                        User userSender = new User("id" + nickname, nickname, "", "", "", "", true, InetAddress.getByName(ipAddress.substring(11)));
                        instance.addContact(userSender);
                    }
                }

                if (res[0].equals("NICKNAME EXISTS")) {
                    System.out.println("nickname exists already");
                    User.uniqueNickname = 1; // handled dans register
                }
            }

            if (!running) {
                System.out.println("closing the app");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
