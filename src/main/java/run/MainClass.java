package run;

import com.sun.tools.javac.Main;
import controller.*;
import model.*;
import view.*;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Scanner;

import static model.ContactList.getInstance;

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


    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");

        ReceiveMessage receiveMessage = new ReceiveMessage();
        SendMessage sendMessage = new SendMessage();
        receiveMessage.start();

        /*
        ContactList instance = getInstance();
        Scanner scanner = new Scanner(System.in);


        //New User or Not ?


        boolean notCorrectAnswer = true;

        while (notCorrectAnswer) {

            System.out.print("Login or Register ? ");
            String areYouNew = scanner.nextLine();

            if (areYouNew.equalsIgnoreCase("login")) {
                notCorrectAnswer = false;
            } else if (areYouNew.equalsIgnoreCase("register")) {
                notCorrectAnswer = false;
            } else {
                System.out.print("answer with 'login' or 'register'");
            }
        }

        //Start Contact Discovery---------------------------------------------------------
        SendMessage.sendToChooseNickname();


        boolean isUnique = false;
        while(!isUnique) {

            System.out.print("Enter Nickname: ");
            String nicknameInput = scanner.nextLine();

            if (instance.existsContactWithNickname(nicknameInput)) { //if someone already has nickname
                System.out.println("Nickname already in use. Please choose another one.");
                PopUpTab popup1 = new PopUpTab("choose another nickname");
                System.out.println();
            } else {
                isUnique = true;

                try {
                    me.setNickname(nicknameInput);
                    me.setId("id"+nicknameInput);
                    SendMessage.sendIAmConnected(MainClass.me);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }

        }

        scanner.close();
        */


        Beginning beginning = new Beginning(receiveMessage, sendMessage);



    }


}
