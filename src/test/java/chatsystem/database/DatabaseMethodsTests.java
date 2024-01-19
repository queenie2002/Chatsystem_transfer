package chatsystem.database;


import chatsystem.contacts.ContactList;
import chatsystem.contacts.User;
import chatsystem.network.TCPMessage;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import javax.xml.crypto.Data;
import java.io.File;
import java.net.InetAddress;
import java.sql.SQLException;
import java.util.ArrayList;




import java.net.UnknownHostException;


class DatabaseMethodsTests {


    private User alice = new User("alice", "firstAlice", "lastAlice", "birthdayAlice", "pwdAlice", true,InetAddress.getLoopbackAddress());
    ;
    private User bob = new User("bob", "firstBob", "lastBob", "birthdayBob", "pwdBob", true,  InetAddress.getByName("192.168.1.2"));


    ContactList contacts;


    DatabaseMethodsTests() throws UnknownHostException {
    }

    @BeforeEach
    void setUp() throws SQLException, UnknownHostException {
        DatabaseMethods.deleteDatabase(alice.getIpAddress().getHostAddress());
        DatabaseMethods.deleteDatabase(bob.getIpAddress().getHostAddress());
        DatabaseMethods.startConnection(alice);
        contacts = ContactList.getInstance();
        contacts.clearContactList();

        alice = new User("alice", "firstAlice", "lastAlice", "birthdayAlice", "pwdAlice", true,InetAddress.getLoopbackAddress());
        bob = new User("bob", "firstBob", "lastBob", "birthdayBob", "pwdBob", true, InetAddress.getByName("192.168.1.2"));

    }



    /**start/stop connection, does table/user/i exist, addme */

    @Test
    void testAddUser() throws SQLException, UnknownHostException {

        assert !DatabaseMethods.doesUserExist(alice);
        DatabaseMethods.addUser(alice);
        assert DatabaseMethods.doesUserExist(alice);

        assert alice.getBirthday().equals(DatabaseMethods.getUser("alice").getBirthday());
        alice.setBirthday("another birthday");

        DatabaseMethods.addUser(alice);
        assert DatabaseMethods.doesUserExist(alice);
        assert !alice.getBirthday().equals(DatabaseMethods.getUser("alice").getBirthday());

    }

    @Test
    void testAddMessage() throws SQLException, UnknownHostException {

        TCPMessage testMessage = new TCPMessage("Hello, test message!", "2022-01-01", "127.0.0.1","192.168.1.2");


        assert !DatabaseMethods.doesTableExist("Messages_192_168_1_2");
        DatabaseMethods.addUser(bob);
        assert DatabaseMethods.doesTableExist("Messages_192_168_1_2");

        ArrayList<TCPMessage> messagesOfAlice = DatabaseMethods.getMessagesList("bob");
        assert messagesOfAlice.isEmpty();
        DatabaseMethods.addMessage(testMessage);
        messagesOfAlice = DatabaseMethods.getMessagesList("bob");
        TCPMessage message = messagesOfAlice.get(1);
        TCPMessage.printTCPMessage(message);
    }

    @Test
    void testGetUser() {
        try {
            User retrievedUser = DatabaseMethods.getUser("alice");
            assertNotNull(retrievedUser);
            assertEquals("alice", retrievedUser.getNickname());
            // Add more assertions based on the user details
        } catch (SQLException | UnknownHostException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    void testGetMessagesList() {
        try {
            ArrayList<TCPMessage> messagesList = DatabaseMethods.getMessagesList("alice");
            assertNotNull(messagesList);
            // Add more assertions based on the retrieved messages
        } catch (SQLException | UnknownHostException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    void testGetMe() {
        try {
            User me = DatabaseMethods.getMe();
            assertNotNull(me);
            // Add assertions based on the retrieved "Me" details
        } catch (SQLException | UnknownHostException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    void testLoadUserList() {
        try {
            DatabaseMethods.loadUserList();
            // Add assertions based on the loaded user list
        } catch (SQLException | UnknownHostException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }







}
