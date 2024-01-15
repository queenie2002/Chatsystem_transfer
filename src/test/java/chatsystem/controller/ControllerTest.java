package chatsystem.controller;

import chatsystem.contacts.ContactList;
import chatsystem.contacts.User;
import chatsystem.database.DatabaseMethods;
import chatsystem.network.UDPMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;

public class ControllerTest {

    ContactList contacts;
    private User alice = new User("alice", "testFirstName", "testLastName", "testBirthday", "testPassword", true, InetAddress.getLoopbackAddress());

    @BeforeEach
    void setUp() throws SQLException {
        DatabaseMethods.startConnection(alice);
        contacts = ContactList.getInstance();
        contacts.clear();
    }
    @Test
    void messageHandlingTest() throws UnknownHostException {
        ContactList instance = ContactList.getInstance();
        UDPMessage msg1 = new UDPMessage("alice", InetAddress.getByName("10.1.5.10"));
        UDPMessage msg2 = new UDPMessage("bob", InetAddress.getByName("10.1.5.11"));

        assert !instance.existsContactWithNickname("alice");
        Controller.handleContactDiscoveryMessage(msg1);
        assert instance.existsContactWithNickname("alice");

        assert !instance.existsContactWithNickname("bob");
        Controller.handleContactDiscoveryMessage(msg2);
        assert instance.existsContactWithNickname("bob");

        Controller.handleContactDiscoveryMessage(msg2);


    }
}
