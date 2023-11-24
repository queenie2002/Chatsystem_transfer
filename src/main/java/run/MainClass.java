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
            me = new User("[]", "[]", "[]", "[]", "[]" , "[]", null, InetAddress.getLocalHost());
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }


    public static void main(String[] args) throws IOException {


        System.out.print(InetAddress.getLocalHost());

        ReceiveMessage receiveMessage = new ReceiveMessage();
        SendMessage sendMessage = new SendMessage();
        receiveMessage.start();
        ContactList instance = getInstance();

        SendMessage.sendIAmConnected(me);


        //startContactDiscovery
        //SendMessage.sendToChooseNickname(me);
        // Create a Scanner object to read input
        Scanner scanner = new Scanner(System.in);

        boolean isUnique = false;
        while(!isUnique) {

            System.out.print("Enter Nickname: ");
            String nicknameInput = scanner.nextLine();

            if (instance.existsContactWithNickname(nicknameInput)) { //if someone already has nickname
                PopUpTab popup1 = new PopUpTab("choose another nickname");
                return;
            } else {
                isUnique = true;
                scanner.close();
                try {
                    me.setNickname(nicknameInput);
                    me.setId("id"+nicknameInput);
                    SendMessage.sendIAmConnected(MainClass.me);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }

        }

        System.out.println();
        System.out.print("Printing Contact List ");
        instance.printContactList();

        System.out.print(InetAddress.getLocalHost());



        // Create an instance of the Beginning class
        //Beginning beginning = new Beginning(receiveMessage, sendMessage);

    }


}
