package controller;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import chatsystem.controller.*;
import chatsystem.model.*;
import java.sql.*;
import java.io.IOException;

import chatsystem.network.UDPReceiver;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;


import java.net.*;



class TestUDPReceiver {

    private UDPReceiver UDPReceiveMessage = new UDPReceiver();;
    private User user = new User("testNickname", "testFirstName", "testLastName", "testBirthday", "testPassword", true, InetAddress.getLoopbackAddress());

    TestUDPReceiver() throws SocketException, SQLException {
        UDPReceiveMessage.start();
        DatabaseMethods.startConnection(user);
    }

    @Test
    void extractInfoFromPattern() throws SocketException {

        String[] result = UDPReceiveMessage.extractInfoFromPattern("IAMCONNECTED: nickname: testNickname");

        assertNotNull(result);
        assertEquals(2, result.length);
        assertEquals("IAMCONNECTED: nickname: (\\S+)", result[0]);
        assertEquals("testNickname", result[1]);
    }


    @Test
    void handleIAmConnected() throws UnknownHostException, SocketException, SQLException {
        // Simulate receiving an "I am connected" message
        UDPReceiveMessage.handleIAmConnected("testNickname", InetAddress.getByName("127.0.0.1"));

        // Verify the changes in the contact list
        assertTrue(ContactList.getInstance().existsContactWithNickname("testNickname"));
        assertEquals(1, ContactList.getInstance().getContactList().size());
        User receivedUser = ContactList.getInstance().getContactWithNickname("testNickname");
        assertEquals("testNickname", receivedUser.getNickname());
        assertEquals("127.0.0.1", receivedUser.getIpAddress().getHostAddress());
        assertTrue(receivedUser.getStatus());

        System.out.println("handleIAmConnected success");
    }



    @Test
    void handleIAmConnectedAreYou() throws IOException, SQLException {
        // Simulate receiving an "I am connected, are you?" message
        UDPReceiveMessage.handleIAmConnectedAreYou("testNickname", InetAddress.getByName("127.0.0.1"));

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
    void handleDisconnect() throws UnknownHostException, SQLException {
        // Add a user to the contact list before disconnecting
        ContactList.getInstance().addContact(user);

        // Simulate receiving a "disconnect" message
        UDPReceiveMessage.handleDisconnect("testNickname", InetAddress.getByName("127.0.0.1"));

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