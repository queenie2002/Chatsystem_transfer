package run;

import controller.ReceiveMessage;
import controller.SendMessage;
import model.User;
import view.Beginning;

import java.io.IOException;
import java.net.InetAddress;

public class MainClass {
    public static void main(String[] args) {
        try {
            User me = new User("idUserTest","nicknameUserTest","firstNameUserTest","lastNameUserTest","birthdayUserTest","passwordUserTest",false, InetAddress.getLocalHost());
            ReceiveMessage receiveMessage = new ReceiveMessage(me);
            SendMessage sendMessage = new SendMessage();

            // Create an instance of the Beginning class
            Beginning beginning = new Beginning(me, receiveMessage, sendMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
