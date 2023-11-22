package run;

import controller.*;
import model.*;
import view.*;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class MainClass {

    //we create an empty user to keep my information
    public static User me;

    static {
        try {
            me = new User("idqueenie", "queenie", "queen", "name", "2002-03-02" , "pwd", false, InetAddress.getLocalHost());
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }


    public static void main(String[] args) {


        try {
            ReceiveMessage receiveMessage = new ReceiveMessage(me);
            SendMessage sendMessage = new SendMessage();

            receiveMessage.start();
            SendMessage.sendToChooseNickname(me); //does change nickname and i am connected

            System.out.println();
            System.out.println();
            System.out.println("Thread is going to sleep for 5 seconds...");
            try {
                Thread.sleep(5000);
                System.out.println("Thread woke up after 5 seconds.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            SendMessage.sendIAmConnectedAreYou(me);



            System.out.println();
            System.out.println();
            System.out.println("Thread is going to sleep for 5 seconds...");
            try {
                Thread.sleep(5000);
                System.out.println("Thread woke up after 5 seconds.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            SendMessage.sendDisconnect(me);


            // Create an instance of the Beginning class
            //Beginning beginning = new Beginning(me, receiveMessage, sendMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
