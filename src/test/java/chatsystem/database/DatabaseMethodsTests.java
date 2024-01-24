package chatsystem.database;


import chatsystem.contacts.ContactList;
import chatsystem.contacts.User;
import chatsystem.network.TCPMessage;

import org.junit.jupiter.api.*;

import java.net.InetAddress;
import java.time.Instant;
import java.util.ArrayList;




import java.net.UnknownHostException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class DatabaseMethodsTests {


    private User alice = new User("alice","pwdAlice", true,InetAddress.getLoopbackAddress());
    ;
    private User bob = new User("bob", "pwdBob", true,  InetAddress.getByName("192.168.1.2"));


    ContactList contacts;


    DatabaseMethodsTests() throws UnknownHostException {
    }

    @BeforeEach
    void setUp() throws UnknownHostException {
        DatabaseMethods.deleteDatabase(alice.getIpAddress());
        DatabaseMethods.deleteDatabase(bob.getIpAddress());
        DatabaseMethods.startConnection(alice);
        contacts = ContactList.getInstance();
        contacts.clearContactList();
        assertEquals(0, DatabaseMethods.numberTablesInDatabase());
        DatabaseMethods.initializeDatabase();
        assertEquals(2, DatabaseMethods.numberTablesInDatabase());


        alice = new User("alice",  "pwdAlice", true,InetAddress.getLoopbackAddress());
        bob = new User("bob", "pwdBob", true, InetAddress.getByName("192.168.1.2"));

    }



    /**start/stop connection, does table/user/i exist, addme */

    @Test
    void testAddUser()  {

        System.out.println("dans test add user");
        assert !DatabaseMethods.doesUserExist(alice.getIpAddress());
        DatabaseMethods.addUser(alice);
        assertTrue(DatabaseMethods.doesUserExist(alice.getIpAddress()));

        assert alice.getPassword().equals(DatabaseMethods.getUser(alice.getIpAddress()).getPassword());
        alice.setPassword("another password");

        DatabaseMethods.addUser(alice);
        assert DatabaseMethods.doesUserExist(alice.getIpAddress());
    }

    @Test
    void testAddMessage() {

        TCPMessage testMessage = new TCPMessage("Hello, test message!", Instant.now(), "192.168.1.2", "127.0.0.1");

        assert !DatabaseMethods.doesTableExist("Messages_192_168_1_2");
        DatabaseMethods.addUser(bob);
        assert DatabaseMethods.doesTableExist("Messages_192_168_1_2");

        ArrayList<TCPMessage> messagesOfAlice = DatabaseMethods.getMessagesList(bob.getIpAddress());
        assert messagesOfAlice.isEmpty();
        DatabaseMethods.addMessage(testMessage);
        messagesOfAlice = DatabaseMethods.getMessagesList(bob.getIpAddress());
        assert !messagesOfAlice.isEmpty();

    }

}
