package run;

import controller.*;
import model.*;
import view.*;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class AppQueen {

    //we create an empty user to keep my information
    public static User me;

    static {
        try {
            me = new User("", "", "", "", "" , "", false, InetAddress.getLocalHost());
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    public static ReceiveMessage r = new ReceiveMessage ();

    public static SendMessage s;

    static {
        try {
            s = new SendMessage ();
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }

    public AppQueen() throws SocketException {
    }

    public static void main(String[] args) throws IOException {

        r.start();
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
