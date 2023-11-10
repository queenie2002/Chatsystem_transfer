package controller;
/*
contact discovery ph2 receives broadcast and responds accordingly

si recoit message broadcast, renvoie un message disant qu’il est connecté du coup et update le fait que la personne qui a envoyé le message est connecté


 */

import model.*;
import java.nio.ByteBuffer; /*pour convert int into byte*/
import java.io.*;
import java.net.*;



public class ReceiveMessage extends Thread {

    public void receiveBroadcast(User me) throws IOException {

        //we set the address
        InetAddress senderAddress = InetAddress.getByName("255.255.255.255");
        int senderPort = 2000; /*le port sur lequel il devra recevoir*/

        //create datagram socket
        //avec le try, le socket est relâché automatiquement à la fin, donc pas besoin du close
        try (DatagramSocket socket = new DatagramSocket(1000)) {  /*who I am, port duquel j'envoie*/

            //we enable broadcasting
            socket.setBroadcast(true);

            //create message
            byte[] buf = ByteBuffer.allocate(4).putInt(me.getId()).array(); /*the message I send, says who I am and carries info of where I am so they can send it back  */
            /*returns the array of 4 int*/ /*can make it bigger, made it 4 bytes long for 1 int for id*/ //le message est en byte du coup, à convertir

            DatagramPacket outPacket = new DatagramPacket(buf, buf.length, senderAddress, senderPort);
            socket.send(outPacket);

            System.out.println("Broadcast message sent.");
        }
    }




}
