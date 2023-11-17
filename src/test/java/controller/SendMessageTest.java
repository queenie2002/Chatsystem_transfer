package controller;

import java.io.IOException;

import controller.*;
import model.User;

public class SendMessageTest {

    public static void main(String[] args) throws IOException, InterruptedException {

        User user1 = new User(1, "Queen", "Y-Quynh", "Nguyen", "2002-03-25" , "pwd", true);


        ReceiveMessage r1 = new ReceiveMessage ();
        SendMessage s = new SendMessage ();
        r1.start();
        s.sendBroadcast(user1);
    }
}
