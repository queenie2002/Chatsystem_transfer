package controller;

import java.io.IOException;
import java.net.InetAddress;

import controller.*;
import model.User;

public class SendMessageTest {

    public static void main(String[] args) throws IOException, InterruptedException {

        User user1 = new User(1, "test1", "Y-Quynh", "Nguyen", "2002-03-25" , "pwd", true, InetAddress.getLocalHost());
        User user2 = new User(2, "test2", "Y-Quynh", "Nguyen", "2002-03-25" , "pwd", true, InetAddress.getLocalHost());
        User user3 = new User(3, "test3", "Y-Quynh", "Nguyen", "2002-03-25" , "pwd", true, InetAddress.getLocalHost());


        ReceiveMessage r1 = new ReceiveMessage ();
        SendMessage s = new SendMessage ();
        r1.start();
        s.sendBroadcast(user1);
        s.sendBroadcast(user2);
        s.sendBroadcast(user3);
    }
}
