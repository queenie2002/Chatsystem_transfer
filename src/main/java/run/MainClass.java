package run;

import com.sun.tools.javac.Main;
import controller.*;
import model.*;
import view.*;

import java.io.IOException;
import java.net.InetAddress;

public class MainClass {

    //we create an empty user to keep my information
    public static User me;

    static {
        try {
            me = new User("[]", "[]", "[]", "[]", "[]" , "[]", null, InetAddress.getByName("0.0.0.0"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static void main(String[] args) throws IOException {

        //Initialize----------------------------------------------------------------------
        ReceiveMessage receiveMessage = me.getReceiveMessage();
        SendMessage sendMessage = me.getSendMessage();

        Beginning beginning = new Beginning(receiveMessage, sendMessage);

    }


}
