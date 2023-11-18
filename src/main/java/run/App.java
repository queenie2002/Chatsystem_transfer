package run;

import controller.ReceiveMessage;
import controller.SendMessage;
import model.User;
import view.Beginning;
import view.Register;

import java.io.IOException;
import java.net.InetAddress;

public class App {


    public static void main(String[] args) throws IOException {


        User me = null;

        User user1 = new User("idsamenickname", "samenickname", "Y-Quynh", "Nguyen", "2002-03-25" , "pwd", true, InetAddress.getLocalHost());


        ReceiveMessage r = new ReceiveMessage ();
        r.start();
        SendMessage s = new SendMessage ();
        Beginning beginning = new Beginning(user1, r, s);


        User user2 = new User("idsamenickname", "samenickname", "Y-Quynh", "Nguyen", "2002-03-25" , "pwd", true, InetAddress.getLocalHost());
        User user3 = new User("idtest3", "test3", "Y-Quynh", "Nguyen", "2002-03-25" , "pwd", true, InetAddress.getLocalHost());


        /*
        r.start();
        s.sendBroadcastBeginning(user1);
        s.sendBroadcastBeginning(user2);
        s.sendBroadcastBeginning(user3);


        s.sendDisconnect(user1);
        */



    }
}
