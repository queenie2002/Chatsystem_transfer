package run;

import controller.ReceiveMessage;
import controller.SendMessage;
import model.User;
import view.Beginning;

import java.io.IOException;
import java.net.InetAddress;

public class AppQueen {


    public static void main(String[] args) throws IOException {

        User me = new User("", "", "", "", "" , "", false, InetAddress.getByName("0.0.0.0"));
        User me2 = new User("", "", "", "", "" , "", false, InetAddress.getByName("0.0.0.0"));

        ReceiveMessage r = new ReceiveMessage ();
        r.start();
        SendMessage s = new SendMessage ();
        Beginning beginning1 = new Beginning(me, r, s);
        Beginning beginning2 = new Beginning(me2, r, s);

        User user1 = new User("iduser1", "user1", "Y-Quynh", "Nguyen", "2002-03-25" , "pwd", true, InetAddress.getLocalHost());
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
