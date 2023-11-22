/*package controller;
import model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.SocketException;

import static org.junit.Assert.assertEquals;

public class TestSendMessage {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    public void testSendBroadcastBeginning() throws IOException {
        SendMessageRaph sendMessageRaph = new SendMessageRaph();
        User user = new User("TestUser", "192.168.0.1");
        sendMessageRaph.sendBroadcastBeginning(user);

        // Verify the output contains the expected message
        String expectedOutput = "Broadcast message sent.";
        assertEquals(expectedOutput, outContent.toString().trim());

        // Clean up
        sendMessageRaph.closeSocket();
    }

    @Test
    public void testSendDisconnect() throws IOException {
        SendMessageRaph sendMessageRaph = new SendMessageRaph();
        User user = new User("TestUser", "192.168.0.1");
        sendMessageRaph.sendDisconnect(user);

        // Verify the output contains the expected message
        String expectedOutput = "Disconnect message sent.";
        assertEquals(expectedOutput, outContent.toString().trim());

        // Clean up
        sendMessageRaph.closeSocket();
    }

    @Test
    public void testSendConnect() throws IOException {
        SendMessageRaph sendMessageRaph = new SendMessageRaph();
        User user = new User("TestUser", "192.168.0.1");
        sendMessageRaph.sendConnect(user);

        // Verify the output contains the expected message
        String expectedOutput = "Connect message sent.";
        assertEquals(expectedOutput, outContent.toString().trim());

        // Clean up
        sendMessageRaph.closeSocket();
    }

    @Test
    public void testSendNicknameExists() throws IOException {
        SendMessageRaph sendMessageRaph = new SendMessageRaph();
        User user = new User("TestUser", "192.168.0.1");
        sendMessageRaph.sendNicknameExists(user);

        // Verify the output contains the expected message
        String expectedOutput = "Nickname Exists message sent.";
        assertEquals(expectedOutput, outContent.toString().trim());

        // Clean up
        sendMessageRaph.closeSocket();
    }

    @Test
    public void testSendNicknameUnique() throws IOException {
        SendMessageRaph sendMessageRaph = new SendMessageRaph();
        User user = new User("TestUser", "192.168.0.1");
        sendMessageRaph.sendNicknameUnique(user);

        // Verify the output contains the expected message
        String expectedOutput = "Nickname Unique message sent.";
        assertEquals(expectedOutput, outContent.toString().trim());

        // Clean up
        sendMessageRaph.closeSocket();
    }
}
*/