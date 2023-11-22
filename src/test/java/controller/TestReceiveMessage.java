package controller;

import model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestReceiveMessage {

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
    public void testReceiveMessage() throws IOException, InterruptedException {
        // Setup
        ReceiveMessage.running = true;
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> {
            ReceiveMessage receiveMessage = new ReceiveMessage();
            receiveMessage.start();
        });

        // Give some time for the thread to start
        Thread.sleep(1000);

        // Simulate receiving a message
        SendMessage sendMessage = new SendMessage();
        sendMessage.sendBroadcastBeginning(new User("TestUser", InetAddress.getLocalHost()));

        // Give some time for the message to be processed
        Thread.sleep(1000);

        // Assert
        assertTrue(outContent.toString().contains("Broadcast message sent."));

        // Cleanup
        ReceiveMessage.running = false;
        executorService.shutdown();
    }
}
