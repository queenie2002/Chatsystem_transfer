package controller;

import controller.ReceiveMessage;
import model.ContactList;
import model.User;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.SQLException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TestReceiveMessage {


    @Test
    void extractInfoFromPattern() throws SocketException {
        ReceiveMessage receiveMessage = new ReceiveMessage();
        String[] result = receiveMessage.extractInfoFromPattern("IAMCONNECTED: id: testId nickname: testNickname");

        assertNotNull(result);
        assertEquals(3, result.length);
        assertEquals("IAMCONNECTED: id: (\\S+) nickname: (\\S+)", result[0]);
        assertEquals("testId", result[1]);
        assertEquals("testNickname", result[2]);
    }


    @Test
    void handleIAmConnected() throws UnknownHostException, SocketException, SQLException {
        ReceiveMessage receiveMessage = new ReceiveMessage();
        User user = new User("testId", "testNickname", "testFirstName", "testLastName", "testBirthday", "testPassword", true, InetAddress.getLoopbackAddress());

        // Simulate receiving an "I am connected" message
        receiveMessage.handleIAmConnected("testId", "testNickname", InetAddress.getByName("127.0.0.1"));

        // Verify the changes in the contact list
        assertTrue(ContactList.getInstance().existsContact("testId"));
        assertEquals(1, ContactList.getInstance().getContactList().size());
        User receivedUser = ContactList.getInstance().getContact("testId");
        assertEquals("testId", receivedUser.getId());
        assertEquals("testNickname", receivedUser.getNickname());
        assertEquals("127.0.0.1", receivedUser.getIpAddress().getHostAddress());
        assertTrue(receivedUser.getStatus());

        System.out.println("handleIAmConnected success");
    }

    @Test
    void handleIAmConnectedAreYou() throws IOException, SQLException {
        ReceiveMessage receiveMessage = new ReceiveMessage();
        User user = new User("testId", "testNickname", "testFirstName", "testLastName", "testBirthday", "testPassword", true, InetAddress.getLoopbackAddress());

        // Simulate receiving an "I am connected, are you?" message
        receiveMessage.handleIAmConnectedAreYou("testId", "testNickname", InetAddress.getByName("127.0.0.1"));

        // Verify the changes in the contact list
        assertTrue(ContactList.getInstance().existsContact("testId"));
        assertEquals(1, ContactList.getInstance().getContactList().size());
        User receivedUser = ContactList.getInstance().getContact("testId");
        assertEquals("testId", receivedUser.getId());
        assertEquals("testNickname", receivedUser.getNickname());
        assertEquals("127.0.0.1", receivedUser.getIpAddress().getHostAddress());
        assertTrue(receivedUser.getStatus());

        System.out.println("handleIAmConnectedAreYou success");
    }
    /*
    @Test
    void handleDisconnect() throws UnknownHostException {
        ReceiveMessage receiveMessage = new ReceiveMessage();
        User user = new User("testId", "testNickname", "testFirstName", "testLastName", "testBirthday", "testPassword", true, InetAddress.getLoopbackAddress());

        // Add a user to the contact list before disconnecting
        ContactList.getInstance().addContact(new User("testId", "testNickname", "", "", "", "", true, InetAddress.getLoopbackAddress()));

        // Simulate receiving a "disconnect" message
        receiveMessage.handleDisconnect("testId", "testNickname", "127.0.0.1");

        // Verify the changes in the contact list
        assertTrue(ContactList.getInstance().existsContact("testId"));
        assertEquals(1, ContactList.getInstance().getContactList().size());
        User disconnectedUser = ContactList.getInstance().getContact("testId");
        assertEquals("testId", disconnectedUser.getId());
        assertEquals("testNickname", disconnectedUser.getNickname());
        assertEquals("127.0.0.1", disconnectedUser.getIpAddress().getHostAddress());
        assertFalse(disconnectedUser.getStatus());
    }
    */


    @Test
    void run() {

    }
}