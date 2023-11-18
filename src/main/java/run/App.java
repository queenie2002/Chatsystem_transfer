package run;

import controller.ReceiveMessage;
import controller.SendMessage;
import model.User;
import view.Register;

import java.io.IOException;
import java.net.InetAddress;

public class App {


    public static void main(String[] args) throws IOException {


        User me = null;
        Register register = new Register(me);


        User user1 = new User(1, "samenickname", "Y-Quynh", "Nguyen", "2002-03-25" , "pwd", true, InetAddress.getLocalHost());
        User user2 = new User(2, "samenickname", "Y-Quynh", "Nguyen", "2002-03-25" , "pwd", true, InetAddress.getLocalHost());
        User user3 = new User(3, "test3", "Y-Quynh", "Nguyen", "2002-03-25" , "pwd", true, InetAddress.getLocalHost());


        ReceiveMessage r1 = new ReceiveMessage ();
        SendMessage s = new SendMessage ();
        r1.start();
        s.sendBroadcastBeginning(user1);
        s.sendBroadcastBeginning(user2);
        s.sendBroadcastBeginning(user3);


        s.sendDisconnect(user1);



    }
}
