package run;

import controller.*;
import model.*;
import view.*;

import java.io.IOException;
import java.net.InetAddress;

public class AppQueen {

    //we create an empty user to keep my information
    public static User me = new User("", "", "", "", "" , "", false, InetAddress.getLocalHost());

    public static void main(String[] args) throws IOException {

        ReceiveMessage r = new ReceiveMessage ();
        r.start();
        SendMessage s = new SendMessage ();

        Beginning beginning = new Beginning(me, r, s);


        //User me2 = new User("", "", "", "", "" , "", false, InetAddress.getLocalHost());
        //User user1 = new User("idyq", "yq", "yq", "yq", "yq" , "yq", true, InetAddress.getLocalHost());
        //ContactList instance = getInstance();
        //instance.addContact(user1);





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
