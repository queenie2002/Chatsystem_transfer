package run;

import controller.ReceiveMessage;
import controller.SendMessage;
import model.ContactList;
import model.User;
import view.Beginning;

import java.io.IOException;
import java.net.InetAddress;

import static model.ContactList.getInstance;

public class AppQueen {


    public static void main(String[] args) throws IOException {

        User me = new User("", "", "", "", "" , "", false, InetAddress.getLocalHost());
        ReceiveMessage r = new ReceiveMessage ();
        r.start();
        SendMessage s = new SendMessage ();


        //User me2 = new User("", "", "", "", "" , "", false, InetAddress.getLocalHost());
        User user1 = new User("idyq", "yq", "yq", "yq", "yq" , "yq", true, InetAddress.getLocalHost());
        ContactList instance = getInstance();
        instance.addContact(user1);

        Beginning beginning1 = new Beginning(me, r, s);



        //User user1 = new User("idyq", "yq", "yq", "yq", "yq" , "yq", true, InetAddress.getLocalHost());
        //User user2 = new User("idsamenickname", "samenickname", "Y-Quynh", "Nguyen", "2002-03-25" , "pwd", true, InetAddress.getLocalHost());
        //User user3 = new User("idtest3", "test3", "Y-Quynh", "Nguyen", "2002-03-25" , "pwd", true, InetAddress.getLocalHost());


        /*
        r.start();
        s.sendBroadcastBeginning(user1);
        s.sendBroadcastBeginning(user2);
        s.sendBroadcastBeginning(user3);


        s.sendDisconnect(user1);
        */



    }
}
