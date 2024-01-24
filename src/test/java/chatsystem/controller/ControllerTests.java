package chatsystem.controller;

import chatsystem.contacts.*;
import chatsystem.database.DatabaseMethods;
import chatsystem.network.TCPMessage;
import chatsystem.network.UDPMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ControllerTests {

    Controller controller = new Controller();

    ContactList contacts;
    private User alice = new User("alice", "pwdAlice", true, InetAddress.getByName("10.1.5.10"));
    private User bob = new User("bob", "pwdBob", true, InetAddress.getByName("10.1.5.11"));

    public ControllerTests() throws UnknownHostException {
    }

    /**
     * Passes the test if the code does throw an error
     */
    private static void assertThrows(ContactListTests.FallibleCode code) {
        try {
            code.run();
            throw new RuntimeException("code should have thrown an exception");
        } catch (Exception e) {
            //ok expected an exception
        }
    }

    //before each test, we reset the contact list, database and alice and bob in case they were changed
    @BeforeEach
    void setUp() throws UnknownHostException {
        DatabaseMethods.deleteDatabase(alice.getIpAddress());
        DatabaseMethods.startConnection(alice);
        DatabaseMethods.initializeDatabase();
        contacts = ContactList.getInstance();
        contacts.clearContactList();
        contacts.addObserver(controller);
        alice = new User("alice", "pwdAlice", true, InetAddress.getByName("10.1.5.10"));
        bob = new User("bob", "pwdBob", true, InetAddress.getByName("10.1.5.11"));
    }


    // ---------------------------RECEIVE UDP MESSAGES-------------------------//

    //we test handle and tochoosenicknamemessagehandle and extractinfofrompattern
    @Test
    void toChooseNicknameMessageHandlingTest() throws UnknownHostException {
        //we're trying to choose a nickname so people who receive this message should only send back that they're connected
        String message1 = "TO_CHOOSE_NICKNAME:";
        String message2 = "FAILURE:";

        //IP ADDRESS SHOULD BE SOMETHING DIFFERENT FROM MY IP ADDRESS, because UDPReceiver doesn't handle messages sent from our own computer
        UDPMessage udpMessage1 = new UDPMessage(message1, InetAddress.getByName("10.1.5.10"));
        UDPMessage udpMessage2 = new UDPMessage(message2, InetAddress.getByName("10.1.5.11"));

        //at first the contact list is empty
        assert contacts.isEmptyContactList();

        //we receive the message and handle it
        controller.handle(udpMessage1);

        //nothing should happen and the list should stay empty
        assert contacts.isEmptyContactList();

        //if we try to handle a message that we don't know, nothing should happen either, we just ignore the message
        controller.handle(udpMessage2);
        assert contacts.isEmptyContactList();
    }

    //we are testing iamconnected message handling test, change status and extractinfofrompattern
    @Test
    void iAmConnectedMessageHandlingTest() throws UnknownHostException {
        String message1 = "IAMCONNECTED: nickname: " + alice.getNickname();
        String message2 = "IAMCONNECTED: nickname: " + bob.getNickname();

        //IP ADDRESS SHOULD BE SOMETHING DIFFERENT FROM MY IP ADDRESS
        UDPMessage udpMessage1 = new UDPMessage(message1, InetAddress.getByName("10.1.5.10"));
        UDPMessage udpMessage2 = new UDPMessage(message2, InetAddress.getByName("10.1.5.11"));

        //at first, everything should be empty
        assert contacts.isEmptyContactList();
        assert !contacts.existsContact(alice.getIpAddress());
        assert !contacts.existsContact(bob.getIpAddress());


        //we receive alice's message and add alice to our contact list and database
        controller.handle(udpMessage1);
        assertTrue(contacts.existsContact(alice.getIpAddress()));
        assert contacts.getContact(alice.getIpAddress()).getStatus();
        assert contacts.getContact(alice.getIpAddress()).getNickname().equals(alice.getNickname());
        assert DatabaseMethods.doesUserExist(alice.getIpAddress());
        assert !contacts.existsContact(bob.getIpAddress());
        assert !DatabaseMethods.doesUserExist(bob.getIpAddress());

        //we receive bob's message and add him to our contact list and database
        controller.handle(udpMessage2);
        assert contacts.existsContact(bob.getIpAddress());
        assert contacts.getContact(bob.getIpAddress()).getStatus();
        assert contacts.getContact(bob.getIpAddress()).getNickname().equals(bob.getNickname());
        assert DatabaseMethods.doesUserExist(bob.getIpAddress());
        assert contacts.getContact(alice.getIpAddress()).getStatus();
        assert contacts.existsContact(alice.getIpAddress());


        //if we receive bob's message another time, we update him in our contact list and database
        controller.handle(udpMessage2);
        assert contacts.existsContact(bob.getIpAddress());
        assert contacts.existsContact(alice.getIpAddress());
        assert contacts.getContact(bob.getIpAddress()).getStatus();
        assert contacts.getContact(alice.getIpAddress()).getStatus();

    }

    //we are testing iamconnectedareyou message handling test, change status and extractinfofrompattern
    @Test
    void iAmConnectedAreYouMessageHandlingTest() throws UnknownHostException {
        String message1 = "IAMCONNECTEDAREYOU: nickname: " + alice.getNickname();
        String message2 = "IAMCONNECTEDAREYOU: nickname: " + bob.getNickname();
        String message3 = "IAMCONNECTEDAREYOU: nickname: bobChanged";


        //IP ADDRESS SHOULD BE SOMETHING DIFFERENT FROM MY IP ADDRESS
        UDPMessage udpMessage1 = new UDPMessage(message1, InetAddress.getByName("10.1.5.10"));
        UDPMessage udpMessage2 = new UDPMessage(message2, InetAddress.getByName("10.1.5.11"));
        UDPMessage udpMessage3 = new UDPMessage(message3, InetAddress.getByName("10.1.5.11"));


        //at first, everything should be empty
        assert contacts.isEmptyContactList();
        assert !contacts.existsContact(alice.getIpAddress());
        assert !contacts.existsContact(bob.getIpAddress());


        //we receive alice's message and add alice to our contact list and database
        controller.handle(udpMessage1);
        assertTrue(contacts.existsContact(alice.getIpAddress()));
        assert contacts.getContact(alice.getIpAddress()).getStatus();
        assert contacts.getContact(alice.getIpAddress()).getNickname().equals(alice.getNickname());
        assert DatabaseMethods.doesUserExist(alice.getIpAddress());
        assert !contacts.existsContact(bob.getIpAddress());
        assert !DatabaseMethods.doesUserExist(bob.getIpAddress());

        //we receive bob's message and add him to our contact list and database
        controller.handle(udpMessage2);
        assert contacts.existsContact(bob.getIpAddress());
        assert contacts.getContact(bob.getIpAddress()).getStatus();
        assert contacts.getContact(bob.getIpAddress()).getNickname().equals(bob.getNickname());
        assert DatabaseMethods.doesUserExist(bob.getIpAddress());
        assert contacts.getContact(alice.getIpAddress()).getStatus();
        assert contacts.existsContact(alice.getIpAddress());


        //if we receive another message from bob (his ipaddress), we update him in our contact list and database with a different nickname
        controller.handle(udpMessage3);
        assert contacts.existsContact(bob.getIpAddress());
        assert contacts.existsContact(alice.getIpAddress());
        assert contacts.getContact(bob.getIpAddress()).getStatus();
        assert contacts.getContact(bob.getIpAddress()).getNickname().equals("bobChanged");
        assert contacts.getContact(alice.getIpAddress()).getStatus();

    }

    //we are testing disconnected message handling test, change status and extractinfofrompattern
    @Test
    void disconnectMessageHandlingTest() throws UnknownHostException, ContactAlreadyExists {
        String message1 = "DISCONNECT: nickname: " + alice.getNickname();
        String message2 = "DISCONNECT: nickname: " + bob.getNickname();
        String message3 = "DISCONNECT: nickname: bobChanged";


        //IP ADDRESS SHOULD BE SOMETHING DIFFERENT FROM MY IP ADDRESS
        UDPMessage udpMessage1 = new UDPMessage(message1, InetAddress.getByName("10.1.5.10"));
        UDPMessage udpMessage2 = new UDPMessage(message2, InetAddress.getByName("10.1.5.11"));
        UDPMessage udpMessage3 = new UDPMessage(message3, InetAddress.getByName("10.1.5.11"));

        assert contacts.isEmptyContactList();
        assert !contacts.existsContact(alice.getIpAddress());
        assert !contacts.existsContact(bob.getIpAddress());

        contacts.addContact(alice);
        assert contacts.getContact(alice.getIpAddress()).getStatus();
        assert contacts.existsContact(alice.getIpAddress());
        assert DatabaseMethods.doesUserExist(alice.getIpAddress());


        controller.handle(udpMessage1);
        assert contacts.existsContact(alice.getIpAddress());
        assert !contacts.getContact(alice.getIpAddress()).getStatus();
        assert !DatabaseMethods.getUser(alice.getIpAddress()).getStatus();
        assert !contacts.existsContact(bob.getIpAddress());

        //if bob isn't in the list yet and we set him to disconnected immediately it should work
        controller.handle(udpMessage2);
        assert contacts.existsContact(bob.getIpAddress());
        assert !contacts.getContact(bob.getIpAddress()).getStatus();
        assert !contacts.getContact(alice.getIpAddress()).getStatus();
        assert !DatabaseMethods.getUser(bob.getIpAddress()).getStatus();
        assert contacts.existsContact(alice.getIpAddress());

        //if we receive another message from bob with a different nickname it should work and update his nickname
        controller.handle(udpMessage3);
        assert contacts.existsContact(bob.getIpAddress());
        assert contacts.existsContact(alice.getIpAddress());
        assert !contacts.getContact(bob.getIpAddress()).getStatus();
        assert !contacts.getContact(alice.getIpAddress()).getStatus();
        assert contacts.getContact(bob.getIpAddress()).getNickname().equals("bobChanged");
        assert !contacts.isEmptyContactList();

    }


    // ---------------------------RECEIVE TCP MESSAGES-------------------------//


    //we are testing TCP messages handling test
    @Test
    void handleTCPMessagesTest() throws ContactAlreadyExists {
        String message1 = "a random message1";
        String message2 = "a random message2";
        String message3 = "a random message3";


        //IP ADDRESS SHOULD BE SOMETHING DIFFERENT FROM MY IP ADDRESS
        TCPMessage tcpMessage1 = new TCPMessage(message1, Instant.now(), "10.1.5.10", "10.1.5.12");
        TCPMessage tcpMessage2 = new TCPMessage(message2, Instant.now(), "10.1.5.12", "10.1.5.11");
        TCPMessage tcpMessage3 = new TCPMessage(message3, Instant.now(), "10.1.5.11", "10.1.5.12");


        contacts.addContact(alice);
        assertThrows(() -> DatabaseMethods.getMessagesList(alice.getIpAddress()));

        //we add message to database if i sent it
        controller.handleTCPMessage(tcpMessage1);
        assert DatabaseMethods.getMessagesList(alice.getIpAddress()).get(0).getContent().equals(message1);

        contacts.addContact(bob);
        assertThrows(() -> DatabaseMethods.getMessagesList(bob.getIpAddress()));

        //we add message to database if i receive it
        controller.handleTCPMessage(tcpMessage2);
        assert DatabaseMethods.getMessagesList(bob.getIpAddress()).get(0).getContent().equals(message2);
        assertThrows(() -> DatabaseMethods.getMessagesList(bob.getIpAddress()).get(1));

        // if i receive several messages, we add message to database
        controller.handleTCPMessage(tcpMessage3);
        assert DatabaseMethods.getMessagesList(bob.getIpAddress()).get(1).getContent().equals(message3);
        assertThrows(() -> DatabaseMethods.getMessagesList(bob.getIpAddress()).get(2));
    }


    //we are testing contactAddedOrUpdated
    @Test
    void contactAddedOrUpdatedTest() {

        assert !DatabaseMethods.doesUserExist(alice.getIpAddress());
        assert !DatabaseMethods.doesUserExist(bob.getIpAddress());

        //wee add alice to database
        controller.contactAddedOrUpdated(alice);
        assert DatabaseMethods.doesUserExist(alice.getIpAddress());
        assert !DatabaseMethods.doesUserExist(bob.getIpAddress());

        //we add bob to database
        controller.contactAddedOrUpdated(bob);
        assert DatabaseMethods.doesUserExist(alice.getIpAddress());
        assert DatabaseMethods.doesUserExist(bob.getIpAddress());
        assertEquals(DatabaseMethods.getUser(bob.getIpAddress()).getNickname(), "bob");
        assert !DatabaseMethods.getUser(bob.getIpAddress()).getNickname().equals("bobChanged");

        //we add bob to database but with a changed nickname, it updates
        bob.setNickname("bobChanged");
        controller.contactAddedOrUpdated(bob);
        assert DatabaseMethods.doesUserExist(alice.getIpAddress());
        assert DatabaseMethods.doesUserExist(bob.getIpAddress());
        assert DatabaseMethods.getUser(bob.getIpAddress()).getNickname().equals("bobChanged");
        assert !DatabaseMethods.getUser(bob.getIpAddress()).getNickname().equals("bob");

    }
}