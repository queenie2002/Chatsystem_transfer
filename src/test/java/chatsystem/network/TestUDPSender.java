package chatsystem.network;

import chatsystem.network.UDPSender;
import chatsystem.model.User;
import org.junit.jupiter.api.Test;

import java.net.InetAddress;
import java.net.SocketException;

import static org.junit.jupiter.api.Assertions.*;

// Test to verify that the methods in UDPSender can be called without errors
class TestUDPSender {

    @Test
    void sendToChooseNickname() throws SocketException {
        User user = new User("testNickname", "testFirstName", "testLastName", "testBirthday", "testPassword", true, InetAddress.getLoopbackAddress());

        assertDoesNotThrow(UDPSender::sendToChooseNickname);
        System.out.println("sendToChooseNickname success");

    }

    @Test
    void sendIAmConnected() throws SocketException {
        //User with test info.
        // The loopback address is IP address that is used to do tests locally without actually sending data
        User user = new User("testNickname", "testFirstName", "testLastName", "testBirthday", "testPassword", true, InetAddress.getLoopbackAddress());
        //makes sure that no exceptions are thrown
        assertDoesNotThrow(() -> UDPSender.sendIAmConnected(user));
        System.out.println("sendIAmConnected success");
    }

    @Test
    void sendIAmConnectedAreYou() throws SocketException {
        User user = new User("testNickname", "testFirstName", "testLastName", "testBirthday", "testPassword", true, InetAddress.getLoopbackAddress());

        assertDoesNotThrow(() -> UDPSender.sendIAmConnectedAreYou(user));
        System.out.println("sendIAmConnectedAreYou success");
    }

    @Test
    void sendDisconnect() throws SocketException {
        User user = new User("testNickname", "testFirstName", "testLastName", "testBirthday", "testPassword", true, InetAddress.getLoopbackAddress());

        assertDoesNotThrow(() -> UDPSender.sendDisconnect(user));
        System.out.println("sendDisconnect success");
    }
}


