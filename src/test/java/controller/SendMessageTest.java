package controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import controller.*;
import model.User;

public class SendMessageTest {

    public static void main(String[] args) throws IOException, InterruptedException {

        String birthdayString = "1990-05-15";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate birthday = LocalDate.parse(birthdayString, formatter);

        User user1 = new User(1, "Queen", "Y-Quynh", "Nguyen", birthday , "pwd", true);


        ReceiveMessage r1 = new ReceiveMessage ();
        SendMessage s = new SendMessage ();
        r1.start();
        s.sendBroadcast(user1);
    }
}
