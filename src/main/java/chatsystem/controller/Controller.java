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
import java.sql.SQLException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static chatsystem.MainClass.me;

public class Controller {

    private static Logger LOGGER = LogManager.getLogger(MainClass.class);
    private static final String BROADCAST_ADDRESS = "255.255.255.255";

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
        } catch (IOException | SQLException e) {
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
        sendIAmConnected(MainClass.me);
        LOGGER.info("RECEIVED to choose nickname request");
    }

    public static void handleIAmConnected(String nickname, InetAddress ipAddress){
        changeStatus(nickname, ipAddress, true);
        LOGGER.info("RECEIVED i am connected: " + nickname + " (" + ipAddress + ")");
    }

    public static void handleIAmConnectedAreYou(String nickname, InetAddress ipAddress) throws IOException, SQLException {
        changeStatus(nickname, ipAddress, true);
        sendIAmConnected(MainClass.me);
        LOGGER.info("RECEIVED i am connected, are you?: " + nickname + " (" + ipAddress + ")");
    }

    public static void handleDisconnect(String nickname, InetAddress ipAddress) throws SQLException {
        changeStatus(nickname, ipAddress, false);
        LOGGER.info("RECEIVED i am disconnected: " + nickname + " (" + ipAddress + ")");
    }

    public static void changeStatus(String nickname, InetAddress ipAddress, Boolean status) throws SQLException {
        ContactList instance = ContactList.getInstance();
        try {
            if (!(nickname.equals("[]"))) {
                instance.addContact(new User(nickname, "", "", "", "", status, ipAddress));
            }
        } catch (ContactAlreadyExists | SQLException e) {
            //if we know him, we change his status
            User user = instance.getContactWithNickname(nickname);
            user.setStatus(status);
            instance.updateContact(user);
        }
    }



    /**we ask all connected users for their information to be able to choose a nickname(broadcast)*/
    public static void sendToChooseNickname () throws IOException {
        //user sends his ip
        String message = "TO_CHOOSE_NICKNAME:";
        UDPSender.send(message, InetAddress.getByName(BROADCAST_ADDRESS), MainClass.BROADCAST_RECEIVER_PORT);
        LOGGER.info("SENT: To choose nickname request.");
    }

    /** we send a message saying we're connected, the receiver won't send anything back */
    public static void sendIAmConnected (User user) throws IOException {
        String message = "IAMCONNECTED: nickname: " + user.getNickname();
        UDPSender.send(message, InetAddress.getByName(BROADCAST_ADDRESS), MainClass.BROADCAST_RECEIVER_PORT);
        LOGGER.info("SENT: I am connected.");
    }

    /** we send a message saying we're connected and asks if the other people are too, the receiver will send a iamconnected message back */
    public static void sendIAmConnectedAreYou (User user) throws IOException {
        String message = "IAMCONNECTEDAREYOU: nickname: " + user.getNickname();
        UDPSender.send(message, InetAddress.getByName(BROADCAST_ADDRESS), MainClass.BROADCAST_RECEIVER_PORT);
        LOGGER.info("SENT: I am connected, are you?.");
    }

    public static void sendDisconnect (User user) throws IOException {
        String message = "DISCONNECT: nickname: " + user.getNickname();
        UDPSender.send(message, InetAddress.getByName(BROADCAST_ADDRESS), MainClass.BROADCAST_RECEIVER_PORT);
        LOGGER.info("SENT: Disconnect.");
    }

    public static void toDisconnect() throws IOException {
        //have to socket close for all contacts ??
        sendDisconnect(me);
        me.setStatus(false);
        System.out.println("am supposed to close app after--------------");
    }





}

