package run;

import com.sun.tools.javac.Main;
import controller.*;
import model.*;
import view.*;

import java.io.IOException;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

import static model.ContactList.getInstance;

public class MainClass {

    //we create an empty user to keep my information
    public static User me;

    static {
        try {
            me = new User("[]", "[]", "[]", "[]" , "[]", null, InetAddress.getByName("0.0.0.0"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static void main(String[] args) throws IOException, SQLException {




        ReceiveMessage receiveMessage = new ReceiveMessage();
        SendMessage sendMessage = new SendMessage();
        receiveMessage.start();

        DatabaseMethods.startConnection(me);


        Beginning beginning = new Beginning(receiveMessage, sendMessage);



    }


}
