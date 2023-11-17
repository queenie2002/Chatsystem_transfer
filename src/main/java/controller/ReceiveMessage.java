package controller;
/*
contact discovery ph2 receives broadcast and responds accordingly

si recoit message broadcast, renvoie un message disant qu’il est connecté du coup et update le fait que la personne qui a envoyé le message est connecté
 */

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.*;

import model.*;
import static model.ContactList.getInstance;


public class ReceiveMessage extends Thread {


    public String[] ExtractNumbersFromPattern(String inputString) {

        // List of patterns
        List<String> patternList = new ArrayList<>();
        patternList.add("BROADCAST: id: (\\d+) username: (\\S+) ip address: (\\S+)");
        patternList.add("(\\S+)");  // Matches one or more letters

        boolean matchesAnyPattern = false;
        for (String patternString : patternList) {
            Pattern pattern = Pattern.compile(patternString);
            Matcher matcher1 = pattern.matcher(inputString);
            Matcher matcher2 = pattern.matcher(inputString);

            if (matcher1.matches()) {
                matchesAnyPattern = true;
                System.out.println("String matches pattern: " + patternString);
                String[] res = new String[0];
                
                // Check if a match is found
                if (matcher2.find()) {
                    if (patternString.equals("BROADCAST: id: (\\d+) username: (\\S+) ip address: (\\S+)")) {
                        // Extract the matched numbers
                        String id = matcher2.group(1);
                        String username = matcher2.group(2);
                        String ipAddress = matcher2.group(3);

                        res = new String[4];
                        res[0] = patternString;
                        res[1] = id;
                        res[2] = username;
                        res[3] = ipAddress;
                    }
                    return res;
                }
                break;  // Exit the loop if a match is found
            }
        }

        if (!matchesAnyPattern) {
            System.out.println("String does not match any pattern in the list.");
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

                String[] res = ExtractNumbersFromPattern(received);

                if (res[0].equals("BROADCAST: id: (\\d+) username: (\\S+) ip address: (\\S+)")) {  //si c'est le broadcast de début

                    //update le fait que la personne qui a envoyé ca est connectée
                    int id = Integer.parseInt(res[1]);
                    String username = res[2];
                    String ipAddress = res[3];


                    User me = new User(1,"myPseudo","myFirstName","myLastName","myBday","myPword",true);
                    ContactList list = getInstance();
                    list.addContact(me);
                    User me2 = list.getContact(1);

                    System.out.println(Thread.currentThread().getName() + received);



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
