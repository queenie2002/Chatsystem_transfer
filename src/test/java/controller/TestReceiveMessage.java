package controller;

import controller.ReceiveMessage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TestReceiveMessage {

    @Test
    void extractInfoFromPattern() {
        ReceiveMessage receiveMessage = new ReceiveMessage();
        String[] result = receiveMessage.extractInfoFromPattern("IAMCONNECTED: id: testId nickname: testNickname ip address: 127.0.0.1");

        assertNotNull(result);
        assertEquals(4, result.length);
        assertEquals("IAMCONNECTED: id: (\\S+) nickname: (\\S+) ip address: (\\S+)", result[0]);
        assertEquals("testId", result[1]);
        assertEquals("testNickname", result[2]);
        assertEquals("127.0.0.1", result[3]);
    }

    @Test
    void run() {

    }
}