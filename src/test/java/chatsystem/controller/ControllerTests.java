package chatsystem.controller;

import chatsystem.MainClass;
import chatsystem.contacts.*;
import chatsystem.database.DatabaseMethods;
import chatsystem.network.UDPMessage;
import chatsystem.network.UDPReceiver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.SQLException;

public class ControllerTests {

    Controller controller = new Controller();

    ContactList contacts;
    private User alice = new User("alice", "firstAlice", "lastAlice", "birthdayAlice", "pwdAlice", true,InetAddress.getByName("10.1.5.10"));
    private User bob = new User("bob", "firstBob", "lastBob", "birthdayBob", "pwdBob", true, InetAddress.getByName("10.1.5.11"));

    public ControllerTests() throws UnknownHostException {
    }


    private static void assertThrows(ContactListTests.FallibleCode code) {
        try {
            code.run();
            throw new RuntimeException("code should have thrown an exception");
        } catch (Exception e) {
            //ok expected an exception
        }
    }
    @BeforeEach
    void setUp() throws SQLException {
        DatabaseMethods.startConnection(alice);
        DatabaseMethods.initializeDatabase();
        contacts = ContactList.getInstance();
        contacts.clearContactList();
    }





    // ---------------------------RECEIVE UDP MESSAGES-------------------------//
    @Test
    void toChooseNicknameMessageHandlingTest() throws UnknownHostException {
        String message1 = "TO_CHOOSE_NICKNAME:";
        String message2 = "FAILURE:";

        //IP ADDRESS SHOULD BE SOMETHING DIFFERENT FROM MY IP ADDRESS, because UDPReceiver doesn't handle messages sent from own computer
        UDPMessage udpMessage1 = new UDPMessage(message1, InetAddress.getByName("10.1.5.10"));
        UDPMessage udpMessage2 = new UDPMessage(message2, InetAddress.getByName("10.1.5.11"));

        assert contacts.isEmptyContactList();
        controller.handle(udpMessage1);
        assert contacts.isEmptyContactList();
        assertThrows(() -> controller.handle(udpMessage2));
        assert contacts.isEmptyContactList();
    }

    @Test
    void iAmConnectedMessageHandlingTest() throws UnknownHostException, ContactDoesntExist {
        String message1 = "IAMCONNECTED: nickname: " + alice.getNickname();
        String message2 = "IAMCONNECTED: nickname: " + bob.getNickname();

        //IP ADDRESS SHOULD BE SOMETHING DIFFERENT FROM MY IP ADDRESS
        UDPMessage udpMessage1 = new UDPMessage(message1, InetAddress.getByName("10.1.5.10"));
        UDPMessage udpMessage2 = new UDPMessage(message2, InetAddress.getByName("10.1.5.11"));

        assert contacts.isEmptyContactList();
        assert !contacts.existsContact(alice.getIpAddress());
        controller.handle(udpMessage1);
        assert contacts.existsContact(alice.getIpAddress());
        assert contacts.getContact(alice.getIpAddress()).getStatus();
        assert !contacts.existsContact(bob.getIpAddress());


        controller.handle(udpMessage2);
        assert contacts.existsContact(bob.getIpAddress());
        assert contacts.getContact(bob.getIpAddress()).getStatus();
        assert contacts.getContact(alice.getIpAddress()).getStatus();
        assert contacts.existsContact(alice.getIpAddress());

        controller.handle(udpMessage2);
        assert contacts.existsContact(bob.getIpAddress());
        assert contacts.existsContact(alice.getIpAddress());
        assert contacts.getContact(bob.getIpAddress()).getStatus();
        assert contacts.getContact(alice.getIpAddress()).getStatus();

    }

    @Test
    void iAmConnectedAreYouMessageHandlingTest() throws UnknownHostException, ContactDoesntExist {
        String message1 = "IAMCONNECTEDAREYOU: nickname: " + alice.getNickname();
        String message2 = "IAMCONNECTEDAREYOU: nickname: " + bob.getNickname();

        //IP ADDRESS SHOULD BE SOMETHING DIFFERENT FROM MY IP ADDRESS
        UDPMessage udpMessage1 = new UDPMessage(message1, InetAddress.getByName("10.1.5.10"));
        UDPMessage udpMessage2 = new UDPMessage(message2, InetAddress.getByName("10.1.5.11"));

        assert contacts.isEmptyContactList();
        assert !contacts.existsContact(alice.getIpAddress());
        controller.handle(udpMessage1);
        assert contacts.existsContact(alice.getIpAddress());
        assert contacts.getContact(alice.getIpAddress()).getStatus();
        assert !contacts.existsContact(bob.getIpAddress());


        controller.handle(udpMessage2);
        assert contacts.existsContact(bob.getIpAddress());
        assert contacts.getContact(bob.getIpAddress()).getStatus();
        assert contacts.getContact(alice.getIpAddress()).getStatus();
        assert contacts.existsContact(alice.getIpAddress());

        controller.handle(udpMessage2);
        assert contacts.existsContact(bob.getIpAddress());
        assert contacts.existsContact(alice.getIpAddress());
        assert contacts.getContact(bob.getIpAddress()).getStatus();
        assert contacts.getContact(alice.getIpAddress()).getStatus();
        assert !contacts.isEmptyContactList();

    }

    @Test
    void disconnectMessageHandlingTest() throws UnknownHostException, ContactDoesntExist, SQLException, ContactAlreadyExists {
        String message1 = "DISCONNECT: nickname: " + alice.getNickname();
        String message2 = "DISCONNECT: nickname: " + bob.getNickname();

        //IP ADDRESS SHOULD BE SOMETHING DIFFERENT FROM MY IP ADDRESS
        UDPMessage udpMessage1 = new UDPMessage(message1, InetAddress.getByName("10.1.5.10"));
        UDPMessage udpMessage2 = new UDPMessage(message2, InetAddress.getByName("10.1.5.11"));

        assert contacts.isEmptyContactList();
        assert !contacts.existsContact(alice.getIpAddress());

        contacts.addContact(alice);
        assert contacts.getContact(alice.getIpAddress()).getStatus();
        assert contacts.existsContact(alice.getIpAddress());

        controller.handle(udpMessage1);
        assert contacts.existsContact(alice.getIpAddress());
        assert !contacts.getContact(alice.getIpAddress()).getStatus();
        assert !contacts.existsContact(bob.getIpAddress());


        controller.handle(udpMessage2);
        assert contacts.existsContact(bob.getIpAddress());
        assert !contacts.getContact(bob.getIpAddress()).getStatus();
        assert !contacts.getContact(alice.getIpAddress()).getStatus();
        assert contacts.existsContact(alice.getIpAddress());

        controller.handle(udpMessage2);
        assert contacts.existsContact(bob.getIpAddress());
        assert contacts.existsContact(alice.getIpAddress());
        assert !contacts.getContact(bob.getIpAddress()).getStatus();
        assert !contacts.getContact(alice.getIpAddress()).getStatus();
        assert !contacts.isEmptyContactList();

    }





    // ---------------------------VIEW-------------------------//

    @Test
    void toCloseAppTest() throws SocketException {

        JFrame frame = new JFrame();
        assertThrows( () -> controller.toCloseApp(frame));

        UDPReceiver udpReceiver = new UDPReceiver();
        udpReceiver.start();
        assert udpReceiver.isAlive();
        controller.toCloseApp(frame);
        assert !udpReceiver.isAlive();

    }

    @Test
    void toDisconnectTest() throws IOException {

        JFrame frame = new JFrame();
        assertThrows( () -> controller.toCloseApp(frame));
        assert alice.getStatus();

        UDPReceiver udpReceiver = new UDPReceiver();
        udpReceiver.start();
        assert udpReceiver.isAlive();
        controller.toDisconnect(frame, alice);
        assert !udpReceiver.isAlive();
        assert !alice.getStatus();

    }



}
