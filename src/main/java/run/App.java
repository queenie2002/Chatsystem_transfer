package run;

import controller.ReceiveMessage;
import controller.SendMessage;
import model.User;

import java.io.IOException;

public class App {


    public static void main(String[] args) throws IOException {
        User user1 = new User(1, "Queen", "Y-Quynh", "Nguyen", "2002-03-25" , "pwd", true);

        ReceiveMessage r1 = new ReceiveMessage ();
        SendMessage s = new SendMessage ();
        r1.start();
        s.sendBroadcast(user1);


    }
}
