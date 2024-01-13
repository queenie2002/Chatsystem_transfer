package controller;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import chatsystem.model.*;
import java.sql.*;
import java.io.IOException;

import chatsystem.network.UDPReceiver;
import org.junit.jupiter.api.Test;



import java.net.*;



class TestUDPReceiver {


    @Test
    void extractInfoFromPattern() throws SocketException {
        UDPReceiver receiveMessage = new UDPReceiver();
        String[] result = receiveMessage.extractInfoFromPattern("IAMCONNECTED: nickname: testNickname");

        assertNotNull(result);
        assertEquals(3, result.length);
        assertEquals("IAMCONNECTED: nickname: (\\S+)", result[0]);
        assertEquals("testNickname", result[1]);
    }


    @Test
    void handleIAmConnected() throws UnknownHostException, SocketException, SQLException {

        UDPReceiver receiveMessage = new UDPReceiver();
        User user = new User("testNickname", "testFirstName", "testLastName", "testBirthday", "testPassword", true, InetAddress.getLoopbackAddress());

        // Simulate receiving an "I am connected" message
        receiveMessage.handleIAmConnected("testNickname", InetAddress.getByName("127.0.0.1"));

        // Verify the changes in the contact list
        assertTrue(ContactList.getInstance().existsContactWithNickname("testId"));
        assertEquals(1, ContactList.getInstance().getContactList().size());
        User receivedUser = ContactList.getInstance().getContactWithNickname("testId");
        assertEquals("testNickname", receivedUser.getNickname());
        assertEquals("127.0.0.1", receivedUser.getIpAddress().getHostAddress());
        assertTrue(receivedUser.getStatus());

        System.out.println("handleIAmConnected success");
    }



    @Test
    void handleIAmConnectedAreYou() throws IOException, SQLException {
        UDPReceiver receiveMessage = new UDPReceiver();
        User user = new User("testNickname", "testFirstName", "testLastName", "testBirthday", "testPassword", true, InetAddress.getLoopbackAddress());

        // Simulate receiving an "I am connected, are you?" message
        receiveMessage.handleIAmConnectedAreYou("testNickname", InetAddress.getByName("127.0.0.1"));

        // Verify the changes in the contact list
        assertTrue(ContactList.getInstance().existsContactWithNickname("testNickname"));
        assertEquals(1, ContactList.getInstance().getContactList().size());
        User receivedUser = ContactList.getInstance().getContactWithNickname("testNickname");
        assertEquals("testNickname", receivedUser.getNickname());
        assertEquals("127.0.0.1", receivedUser.getIpAddress().getHostAddress());
        assertTrue(receivedUser.getStatus());

        System.out.println("handleIAmConnectedAreYou success");
    }

    @Test
    void handleDisconnect() throws UnknownHostException, SocketException, SQLException {
        UDPReceiver receiveMessage = new UDPReceiver();
        User user = new User("testNickname", "testFirstName", "testLastName", "testBirthday", "testPassword", true, InetAddress.getLoopbackAddress());

        // Add a user to the contact list before disconnecting
        ContactList.getInstance().addContact(new User( "testNickname", "", "", "", "", true, InetAddress.getLoopbackAddress()));

        // Simulate receiving a "disconnect" message
        receiveMessage.handleDisconnect("testNickname", InetAddress.getByName("127.0.0.1"));

        // Verify the changes in the contact list
        assertTrue(ContactList.getInstance().existsContactWithNickname("testNickname"));
        assertEquals(1, ContactList.getInstance().getContactList().size());
        User disconnectedUser = ContactList.getInstance().getContactWithNickname("testNickname");
        assertEquals("testNickname", disconnectedUser.getNickname());
        assertEquals("127.0.0.1", disconnectedUser.getIpAddress().getHostAddress());
        assertFalse(disconnectedUser.getStatus());
    }



    @Test
    void run() {

    }


}