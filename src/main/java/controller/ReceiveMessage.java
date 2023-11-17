package controller;
/*
contact discovery ph2 receives broadcast and responds accordingly

si recoit message broadcast, renvoie un message disant qu’il est connecté du coup et update le fait que la personne qui a envoyé le message est connecté
 */

import model.*;

import java.io.*;
import java.net.*;


public class ReceiveMessage extends Thread {


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

                System.out.println(Thread.currentThread().getName() + " received: " + received + "<");



                if ((received.substring(0, Math.min(received.length(), 10))).equals("broadcast ")) {   //IF ITS A BROADCAST

                    //update le fait que la personne qui a envoyé ca est connectée
                    int id;
                    User user = ContactList.getContact(id));




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
