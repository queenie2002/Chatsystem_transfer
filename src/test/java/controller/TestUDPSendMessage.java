package controller;

import chatsystem.controller.UDPSendMessage;
import chatsystem.model.User;
import org.junit.jupiter.api.Test;

import java.net.InetAddress;
import java.net.SocketException;

import static org.junit.jupiter.api.Assertions.*;

// Test to verify that the methods in UDPSendMessage can be called without errors
class TestUDPSendMessage {

    @Test
    void sendToChooseNickname() throws SocketException {
        User user = new User("testNickname", "testFirstName", "testLastName", "testBirthday", "testPassword", true, InetAddress.getLoopbackAddress());

        assertDoesNotThrow(UDPSendMessage::sendToChooseNickname);
        System.out.println("sendToChooseNickname success");

    }

    @Test
    void sendIAmConnected() throws SocketException {
        //User with test info.
        // The loopback address is IP address that is used to do tests locally without actually sending data
        User user = new User("testNickname", "testFirstName", "testLastName", "testBirthday", "testPassword", true, InetAddress.getLoopbackAddress());

        //makes sure that no exceptions are thrown
        assertDoesNotThrow(() -> UDPSendMessage.sendIAmConnected(user));
        System.out.println("sendIAmConnected success");
    }

    @Test
    void sendIAmConnectedAreYou() throws SocketException {
        User user = new User("testNickname", "testFirstName", "testLastName", "testBirthday", "testPassword", true, InetAddress.getLoopbackAddress());

        assertDoesNotThrow(() -> UDPSendMessage.sendIAmConnectedAreYou(user));
        System.out.println("sendIAmConnectedAreYou success");
    }

    @Test
    void sendDisconnect() throws SocketException {
        User user = new User("testNickname", "testFirstName", "testLastName", "testBirthday", "testPassword", true, InetAddress.getLoopbackAddress());

        assertDoesNotThrow(() -> UDPSendMessage.sendDisconnect(user));
        System.out.println("sendDisconnect success");
    }
}


