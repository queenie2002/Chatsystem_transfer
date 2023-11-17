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


    public String[] ExtractNumbersFromPattern(String pattern, String inputString) {

        // Use a regular expression to match numbers after "username" and "id"
        Pattern pattern = Pattern.compile("BROADCAST: id: (\\d+) username: (\\S+) ip address: (\\S+)");

        Matcher matcher = pattern.matcher(inputString);

        // Check if a match is found
        if (matcher.find()) {
            // Extract the matched numbers
            String id = matcher.group(1);
            String username = matcher.group(2);
            String ipAddress = matcher.group(3);

            String[] res = new String[3];
            res[0] = id;
            res[1] = username;
            res[2] = ipAddress;

            return res;

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

                System.out.println(Thread.currentThread().getName() + received);


                if ((received.substring(0,Math.min(received.length(), 11))).equals("BROADCAST: ")) {   //IF ITS A BROADCAST

                    //update le fait que la personne qui a envoyé ca est connectée
                    String[] res = ExtractNumbersFromPattern(received);

                    for (String s: res) {
                        System.out.println(s);
                    }


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
