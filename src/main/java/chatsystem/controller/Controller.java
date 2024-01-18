package chatsystem.controller;

import chatsystem.MainClass;
import static chatsystem.MainClass.me;
import chatsystem.contacts.*;
import chatsystem.database.DatabaseMethods;
import chatsystem.network.*;
import chatsystem.ui.*;


import org.apache.logging.log4j.*;

import javax.swing.*;
import javax.xml.crypto.Data;
import java.awt.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.regex.*;


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

    public static void handleIAmConnected(String nickname, InetAddress ipAddress) throws SQLException {
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

    public static void toDisconnect(JFrame frame) throws IOException {
        sendDisconnect(me);
        me.setStatus(false);

        System.out.println("am supposed to close app after--------------");
        frame.dispose();
        System.exit(0);
    }

    public static void loginFunction(String nicknameInput, String passwordInput, JFrame frame) throws UnknownHostException, SQLException {



        me = DatabaseMethods.getMe();
        //ContactList instance = ContactList.getInstance();

        if ((Objects.equals(me.getNickname(), nicknameInput)) && Objects.equals(me.getPassword(), passwordInput)) {

            me.setStatus(true);

            DatabaseMethods.loadUserList();

            try {
                Controller.sendIAmConnectedAreYou(me);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

            new HomeTab();
            frame.dispose();
        }
        else {
            new PopUpTab("Wrong Login information. Please try again");
        }
    }
    public static void registerUser(String nicknameInfo,String firstNameInfo, String lastNameInfo, String birthdayInfo, String passwordInfo,JFrame frame) throws SQLException {
        if (nicknameInfo.isEmpty()) {
            new PopUpTab("Nickname can't be empty. Please choose one.");
        } else if (passwordInfo.isEmpty()) {
            new PopUpTab("Password can't be empty. Please choose one.");
        } else {


            me = new User(nicknameInfo, firstNameInfo, lastNameInfo, birthdayInfo, passwordInfo, true, me.getIpAddress());


            ContactList instance = ContactList.getInstance();
            if (instance.existsContactWithNickname(nicknameInfo)) { //if someone already has nickname
                new PopUpTab("Nickname already taken. Please choose another one");
            } else { //if unique i go to next tab and tell people i am connected
                new HomeTab();
                try {
                    DatabaseMethods.addMe(MainClass.me);
                    Controller.sendIAmConnected(MainClass.me);

                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

                frame.dispose();
            }
        }
    }

    public static void canRegister(JFrame frame) throws SQLException, IOException {
        if (DatabaseMethods.doesMeExist()) {
            new PopUpTab("You already registered. Please Login.");
        } else {
            new Register();
            Controller.sendToChooseNickname();
            frame.dispose();
        }
    }


    public static void canLogin(JFrame frame) throws SQLException {
        if (!DatabaseMethods.doesMeExist()) {
            new PopUpTab("You haven't registered yet. Please Register.");
        } else {
            new Login();
            frame.dispose();
        }
    }



}

