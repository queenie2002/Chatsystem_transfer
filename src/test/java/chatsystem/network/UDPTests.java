package chatsystem.network;

import chatsystem.model.User;
import chatsystem.network.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UDPTests {

    private final int TEST_PORT = 2000;
    private User testUser = new User("testNickname", "testFirstName", "testLastName", "testBirthday", "testPassword", true, InetAddress.getLoopbackAddress());

    @Test
    void sendReceiveTest() throws Exception {
        //messages qu'on envoie
        List<String> testMessages = Arrays.asList("TO_CHOOSE_NICKNAME:", "IAMCONNECTED: nickname: testNickname", "multiline\n string", "éàç");

        List<String> receivedMessages = new ArrayList<>();
        UDPReceiver UDPReceiveMessage = new UDPReceiver();
        UDPReceiveMessage.addObserver(message -> {
                receivedMessages.add(message.content());
        });
        UDPReceiveMessage.start();

        for (String msg: testMessages) {
            UDPSender.send(msg, InetAddress.getLoopbackAddress(), TEST_PORT);
        }

        /*
        UDPSender.sendToChooseNickname();
        UDPSender.sendIAmConnected(user);
         */

        Thread.sleep(100);
        System.out.println(receivedMessages);
        assertEquals(testMessages.size(), receivedMessages.size());
        assertEquals(testMessages, receivedMessages);
    }


}
