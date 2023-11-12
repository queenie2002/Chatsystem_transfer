package controller;

import java.io.IOException;
import controller.*;

public class SendMessageTest {

    public static void main(String[] args) throws IOException, InterruptedException {
        ReceiveMessage r1 = new ReceiveMessage ();
        SendMessage s = new SendMessage ();
        r1.start();
        s.sendBroadcast(123);
        s.sendBroadcast(456);
        Thread.sleep(10000);
        s.sendBroadcast(789);
    }
}
