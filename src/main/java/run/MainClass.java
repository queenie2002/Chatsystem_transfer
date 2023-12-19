package run;

import com.sun.tools.javac.Main;
import controller.*;
import model.*;
import view.*;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

import static model.ContactList.getInstance;

public class MainClass {

    //we create an empty user to keep my information
    public static User me;

    static {
        try {
            me = new User("[]", "[]", "[]", "[]", "[]" , "[]", null, InetAddress.getByName("0.0.0.0"));
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }


    public static void main(String[] args) throws IOException {

        //Initialize----------------------------------------------------------------------
        ReceiveMessage receiveMessage = new ReceiveMessage();
        SendMessage sendMessage = new SendMessage();
        receiveMessage.start();
        ContactList instance = getInstance();
        //Scanner scanner = new Scanner(System.in);


        // Create an instance of the Beginning class
        Beginning beginning = new Beginning(receiveMessage, sendMessage);

    }


}
