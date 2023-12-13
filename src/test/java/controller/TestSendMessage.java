package controller;

import controller.SendMessage;
import model.User;
import org.junit.jupiter.api.Test;
import org.junit.platform.engine.support.descriptor.FileSystemSource;

import java.io.IOException;
import java.net.InetAddress;

import static org.junit.jupiter.api.Assertions.*;

// Test to verify that the methods in SendMessage can be called without errors
class TestSendMessage {

    @Test
    void sendToChooseNickname() {
        User user = new User("testId", "testNickname", "testFirstName", "testLastName", "testBirthday", "testPassword", true, InetAddress.getLoopbackAddress());

        assertDoesNotThrow(SendMessage::sendToChooseNickname);
        System.out.println("sendToChooseNickname success");

    }

    @Test
    void sendIAmConnected() {
        //User with test info.
        // The loopback address is IP address that is used to do tests locally without actually sending data
        User user = new User("testId", "testNickname", "testFirstName", "testLastName", "testBirthday", "testPassword", true, InetAddress.getLoopbackAddress());

        //makes sure that no exceptions are thrown
        assertDoesNotThrow(() -> SendMessage.sendIAmConnected(user));
        System.out.println("sendIAmConnected success");
    }

    @Test
    void sendIAmConnectedAreYou() {
        User user = new User("testId", "testNickname", "testFirstName", "testLastName", "testBirthday", "testPassword", true, InetAddress.getLoopbackAddress());

        assertDoesNotThrow(() -> SendMessage.sendIAmConnectedAreYou(user));
        System.out.println("sendIAmConnectedAreYou success");
    }

    @Test
    void sendDisconnect() {
        User user = new User("testId", "testNickname", "testFirstName", "testLastName", "testBirthday", "testPassword", true, InetAddress.getLoopbackAddress());

        assertDoesNotThrow(() -> SendMessage.sendDisconnect(user));
        System.out.println("sendDisconnect success");
    }
}


