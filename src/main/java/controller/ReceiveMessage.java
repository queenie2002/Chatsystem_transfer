package controller;
/*
contact discovery ph2 receives broadcast and responds accordingly

si recoit message broadcast, renvoie un message disant qu’il est connecté du coup et update le fait que la personne qui a envoyé le message est connecté
 */

import model.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReceiveMessage extends Thread {


    public int[] ExtractNumbersFromPattern(String inputString) {

        // Use a regular expression to match numbers after "username" and "id"
        Pattern pattern = Pattern.compile("BROADCAST: id: (\\d+) username: (\\d+)");

        Matcher matcher = pattern.matcher(inputString);

        // Check if a match is found
        if (matcher.find()) {
            // Extract the matched numbers
            String idNumber = matcher.group(1);
            String usernameNumber = matcher.group(2);

            // Convert the string numbers to actual numbers (int or long)
            try {
                int[] res = new int[2];

                res[0] = Integer.parseInt(usernameNumber);
                res[1]  = Integer.parseInt(idNumber);

                System.out.println("Username Number: " + res[0] );
                System.out.println("ID Number: " + res[1] );

                return res;
            } catch (NumberFormatException e) {
                System.out.println("Unable to convert the matched string to a number.");
            }
        } else {
            System.out.println("No match found for the pattern.");

        }
        return null;
    }




    /* are we making a function to receive all messages and then making a if case it's a broadcast*/
    public void run() {
        //this receives messages
        try {DatagramSocket receivingSocket = new DatagramSocket(2000);
            boolean running = true;
            byte[] buf = new byte[256];

            while (running) {
                DatagramPacket inPacket  = new DatagramPacket(buf, buf.length);
                receivingSocket.receive(inPacket);
                String received = new String(inPacket.getData(), 0, inPacket.getLength());

                System.out.println(/*Thread.currentThread().getName() +*/ received);

                int[] test = ExtractNumbersFromPattern(received);

                if ((received.substring(0, Math.min(received.length(), 12))).equals("BROADCAST: ")) {   //IF ITS A BROADCAST

                    //update le fait que la personne qui a envoyé ca est connectée
                    System.out.println("on est rentré ici");



                }


                if (received.equals("end")) {
                    System.out.println("DONE");
                    running = false;
                }

            }
            receivingSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
