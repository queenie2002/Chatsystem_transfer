package chatsystem.controller;

import chatsystem.MainClass;
import chatsystem.contacts.ContactAlreadyExists;
import chatsystem.contacts.ContactList;
import chatsystem.contacts.User;
import chatsystem.network.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Controller {

    private static Logger LOGGER = LogManager.getLogger(Controller.class);



    public static void handleContactDiscoveryMessage(UDPMessage message) {
        ContactList instance = ContactList.getInstance();

        try {

            User aUser = new User(message.content(), "", "", "", "", true, message.originAddress());


            instance.addContact(aUser);
            LOGGER.info("New contact added to the list " + message.content());
        } catch (ContactAlreadyExists e) {
            LOGGER.error("Receive a contact already in the contact list " + message.content());
        }


    }

    public static void handle(UDPMessage message) {

        String[] res = extractInfoFromPattern(message.content());

        try {
            if (res != null) {
                switch (res[0]) {
                    case "TO_CHOOSE_NICKNAME:" -> handleToChooseNickname();
                    case "IAMCONNECTED: nickname: (\\S+)" -> handleIAmConnected(res[1], message.originAddress());
                    case "IAMCONNECTEDAREYOU: nickname: (\\S+)" ->
                            handleIAmConnectedAreYou(res[1], message.originAddress());
                    case "DISCONNECT: nickname: (\\S+)" -> handleDisconnect(res[1], message.originAddress());
                    default -> System.out.println("error: unhandled message type");
                }
            }
        } catch (IOException e) {
            LOGGER.error("Couldn't send back message for contact discovery.");
        }
    }








    public static String[] extractInfoFromPattern(String inputString) {
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
            LOGGER.error("String does not match any pattern in the list.");
        }
        return null;
    }


    public static void handleToChooseNickname() throws IOException {
        //when we receive the request, we respond by saying who we are
        UDPSender.sendIAmConnected(MainClass.me);
        LOGGER.info("RECEIVED to choose nickname request");
    }

    public static void handleIAmConnected(String nickname, InetAddress ipAddress){
        changeStatus(nickname, ipAddress, true);
        LOGGER.info("RECEIVED i am connected: " + nickname + " (" + ipAddress + ")");
    }

    public static void handleIAmConnectedAreYou(String nickname, InetAddress ipAddress) throws IOException{
        changeStatus(nickname, ipAddress, true);
        UDPSender.sendIAmConnected(MainClass.me);
        LOGGER.info("RECEIVED i am connected, are you?: " + nickname + " (" + ipAddress + ")");
    }

    public static void handleDisconnect(String nickname, InetAddress ipAddress) {
        changeStatus(nickname, ipAddress, false);
        LOGGER.info("RECEIVED i am disconnected: " + nickname + " (" + ipAddress + ")");
    }

    public static void changeStatus(String nickname, InetAddress ipAddress, Boolean status) {
        ContactList instance = ContactList.getInstance();
        try {
            if (!(nickname.equals("[]"))) {
                instance.addContact(new User(nickname, "", "", "", "", status, ipAddress));
            }
        } catch (ContactAlreadyExists e) {
            //if we know him, we change his status
            User user = instance.getContactWithNickname(nickname);
            user.setStatus(status);
            instance.updateContact(user);
        }
    }

}

