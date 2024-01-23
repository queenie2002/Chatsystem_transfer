package chatsystem.controller;

import chatsystem.MainClass;
import static chatsystem.MainClass.me;
import chatsystem.contacts.*;
import chatsystem.database.DatabaseMethods;
import chatsystem.network.*;
import chatsystem.observers.MyObserver;
import chatsystem.ui.*;


import org.apache.logging.log4j.*;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.*;
import java.sql.SQLException;
import java.util.*;
import java.util.List;
import java.util.regex.*;

/** Implements MyObserver
 * Sends different UDP messages
 * Handles UDP messages sent
 * Handles a contact being added or updated
 * Disconnects and closes app
 * Checks if we login and register
 * Handles login and register
 * */
public class Controller implements MyObserver {




    //LOGGER
    private static Logger LOGGER = LogManager.getLogger(Controller.class);


    //BROADCAST_ADDRESS
    private static final String BROADCAST_ADDRESS = "255.255.255.255";





    // ---------------------------SEND UDP MESSAGES-------------------------//


    /** Sends To Choose Nickname broadcast
     * Used when we want to choose our nickname, we get the list of connected users in return so we know what nicknames not to choose
     * */
    private static void sendToChooseNickname () throws IOException {
        String message = "TO_CHOOSE_NICKNAME:";
        UDPSender.send(message, InetAddress.getByName(BROADCAST_ADDRESS), MainClass.BROADCAST_RECEIVER_PORT);
        LOGGER.info("SENT: To choose nickname request.");
    }

    /** Sends I Am Connected broadcast
     * Used so the people connected can update their Contact List and know I am connected
     * Or when I changed my nickname, so they can update their Contact List
     * */
    private static void sendIAmConnected (User user) throws IOException {
        String message = "IAMCONNECTED: nickname: " + user.getNickname();
        UDPSender.send(message, InetAddress.getByName(BROADCAST_ADDRESS), MainClass.BROADCAST_RECEIVER_PORT);
        LOGGER.info("SENT: I am connected." + user.getNickname());
    }

    /** Sends I Am Connected, Are You ? broadcast
     * Used so the people connected can update their Contact List and know I am connected
     * And they send me back an I Am Connected message so I know they are connected
     * */
    private static void sendIAmConnectedAreYou (User user) throws IOException {
        String message = "IAMCONNECTEDAREYOU: nickname: " + user.getNickname();
        UDPSender.send(message, InetAddress.getByName(BROADCAST_ADDRESS), MainClass.BROADCAST_RECEIVER_PORT);
        LOGGER.info("SENT: I am connected, are you?.");
    }

    /** Sends Disconnected broadcast
     * Used so the people connected can update their Contact List and know I am disconnected
     * */
    private static void sendDisconnect (User user) throws IOException {
        String message = "DISCONNECT: nickname: " + user.getNickname();
        UDPSender.send(message, InetAddress.getByName(BROADCAST_ADDRESS), MainClass.BROADCAST_RECEIVER_PORT);
        LOGGER.info("SENT: Disconnect.");
    }





    // ---------------------------RECEIVE UDP MESSAGES-------------------------//

    /** Extract information from a pattern */
    private static String[] extractInfoFromPattern(String inputString) {
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
            throw new RuntimeException("String does not match any pattern in the list.");
        }
        return null;
    }


    /** Gets a UDP message, checks what kind of message it is and applies the correct method */
    @Override
    public void handle(UDPMessage message) {

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
            LOGGER.error("Couldn't send back message back for contact discovery.");
        }
    }

    /** Adds a Contact or updates Contact with given status*/
    private static void changeStatus(String nickname, InetAddress ipAddress, Boolean status) throws SQLException {
        ContactList instance = ContactList.getInstance();
        try {
            if (!(nickname.equals("[]"))) {
                instance.addContact(new User(nickname, "", "", "", "", status, ipAddress));
            }
        } catch (ContactAlreadyExists e) {
            try {
                //if we know him, we change his status
                User user = instance.getContact(ipAddress);
                user.setStatus(status);
                user.setNickname(nickname);
                instance.updateContact(user);
            } catch (UnknownHostException ex) {
                throw new RuntimeException(ex);
            }
        }
    }


    /** Handles a To Choose Nickname Message
     * Sends back a I Am Connected message
     * */
    private static void handleToChooseNickname() throws IOException {
        //when we receive the request, we respond by saying who we are
        sendIAmConnected(MainClass.me);
        LOGGER.info("RECEIVED to choose nickname request");
    }

    /** Handles a I Am Connected Message
     * Adds person to our Contact List with status connected or updates status and nickname of the person who sent message to connected
     * */
    private static void handleIAmConnected(String nickname, InetAddress ipAddress) throws SQLException {
        changeStatus(nickname, ipAddress, true);
        LOGGER.info("RECEIVED i am connected: " + nickname + " (" + ipAddress + ")");
    }

    /** Handles a I Am Connected Are You Message
     * Adds person to our Contact List with status connected or updates status of the person who sent message to connected
     * Sends back I Am Connected message
     * */
    private static void handleIAmConnectedAreYou(String nickname, InetAddress ipAddress) throws IOException, SQLException {
        changeStatus(nickname, ipAddress, true);
        sendIAmConnected(MainClass.me);
        LOGGER.info("RECEIVED i am connected, are you?: " + nickname + " (" + ipAddress + ")");
    }

    /** Handles a Disconnect Message
     * Adds person to our Contact List with status disconnected or updates status of the person who sent message to disconnected
     * */
    private static void handleDisconnect(String nickname, InetAddress ipAddress) throws SQLException {
        changeStatus(nickname, ipAddress, false);
        LOGGER.info("RECEIVED i am disconnected: " + nickname + " (" + ipAddress + ")");
    }





    // ---------------------------RECEIVE TCP MESSAGES-------------------------//

    /** Handles TCP Messages
     * Adds message to database
     * ??? OTHER THINGS----------------------------------------
     * */
    @Override
    public void handleTCPMessage(TCPMessage msg)  {
        LOGGER.info("Received message: " + msg.getContent());
        DatabaseMethods.addMessage(msg);

        String userKey = msg.getFromUserIP();
        ChatWindow2 chatWindow = getChatWindowForUser(userKey);
        if (chatWindow != null) {
            SwingUtilities.invokeLater(() -> chatWindow.displayReceivedMessage(msg));
        }
    }






    // ---------------------------DATABASE-------------------------//

    /** Adds a user to database*/
    @Override
    public void contactAddedOrUpdated(User user) {DatabaseMethods.addUser(user);}

    /** Updates MainClass.me, me in database and warns others to update me */
    public void changingNickname(String nickname) throws IOException {
        MainClass.me.setNickname(nickname);
        sendIAmConnected(me);
        DatabaseMethods.addMe(MainClass.me);
        LOGGER.info("Changing my nickname to " + nickname);
    }





    // ---------------------------VIEW-------------------------//

    //GENERAL
    /** Closes the app
     * Closes the database connection
     * Closes UDP server
     * Closes the given frame
     * */
    @Override
    public void toCloseApp(JFrame frame) {
        DatabaseMethods.closeConnection();
        UDPReceiver.stopServer();
        LOGGER.info("Closed app");
        frame.dispose();
        //System.exit(0);
    }

    /** Disconnects us and closes the app
     * Sets my status to false
     * Warns other that I am disconnected
     * */
    @Override
    public void toDisconnect(JFrame frame, User me) throws IOException {
        sendDisconnect(me);
        me.setStatus(false);
        //close tcp sessions
        toCloseApp(frame);
    }





    //BEGINNING TAB
    /** Checks if we can register
     * If we already have an account on the computer, opens a pop-up telling us we have to login
     * Else, sends a To Choose Nickname message to get list of connected users and opens Register tab.
     * */
    @Override
    public void canRegister(JFrame frame) throws IOException {
        if (DatabaseMethods.doesMeExist()) {
            new PopUpTab("You already registered. Please Login.");
        } else {
            Register register = new Register();
            register.addObserver(MainClass.controller);
            Controller.sendToChooseNickname();
            frame.dispose();
        }
    }

    /** Checks if we can login
     * If we haven't registered yet on the computer, opens a pop-up telling us we have to register
     * Else, opens Login tab.
     * */
    @Override
    public void canLogin(JFrame frame) {
        if (!DatabaseMethods.doesMeExist()) {
            new PopUpTab("You haven't registered yet. Please Register.");
        } else {
            Login login = new Login();
            login.addObserver(MainClass.controller);
            frame.dispose();
        }
    }





    //LOGIN TAB
    /** Login
     * Checks login information
     * If it's me:
     * Loads Contact List from database and sends I Am Connected Are You ? message to update our Contact List
     * Opens Hometab
     * Else:
     * Opens a pop-up saying we have to wwrong login information
     * */
    public void loginFunction(String nicknameInput, String passwordInput, JFrame frame) throws UnknownHostException, SQLException {

        me = DatabaseMethods.getMe();

        if ((Objects.equals(me.getNickname(), nicknameInput)) && Objects.equals(me.getPassword(), passwordInput)) {
            LOGGER.info("Successfully logged in.");

            me.setStatus(true);

            DatabaseMethods.loadUserList();
            try {
                Controller.sendIAmConnectedAreYou(me);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

            LOGGER.info("STARTING HOME TAB FROM LOGIN");
            SwingUtilities.invokeLater(() -> {
                HomeTab homeTab = new HomeTab();
                homeTab.setVisible(true);



            });
            frame.dispose();
        }
        else {
            new PopUpTab("Wrong Login information. Please try again");
        }
    }

    //REGISTER TAB
    /** Register
     * If the user didn't input a nickname or password : tells them to choose one.
     * If the nickname has already been taken : tells the user to choose another one
     * Else:
     * We keep our information in the database in table Me
     * Send a I Am Connected message so the people connected can add us to their database
     * Go to HomeTab
     * */
    public void registerFunction(String nicknameInfo, String firstNameInfo, String lastNameInfo, String birthdayInfo, String passwordInfo, JFrame frame) {
        if (nicknameInfo.isEmpty()) {
            new PopUpTab("Nickname can't be empty. Please choose one.");
        } else if (passwordInfo.isEmpty()) {
            new PopUpTab("Password can't be empty. Please choose one.");
        } else if (nicknameInfo.contains(" ")) {
            new PopUpTab("Nicknames can't have blank spaces. Please choose another one.");
        } else {

            me = new User(nicknameInfo, firstNameInfo, lastNameInfo, birthdayInfo, passwordInfo, true, me.getIpAddress());
            ContactList instance = ContactList.getInstance();


            if (instance.existsContactWithNickname(nicknameInfo)) { //if someone already has nickname
                new PopUpTab("Nickname already taken. Please choose another one");
            } else { //if unique i go to next tab and tell people i am connected
                LOGGER.info("Successfully registered in.");
                SwingUtilities.invokeLater(() -> {
                    HomeTab homeTab = new HomeTab();
                    homeTab.setVisible(true);
                });
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


}

