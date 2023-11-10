package controller;

/*

contact discovery ph1 sends broadcast to see who is currently connected

contact discovery extends a thread,
on met tout le monde à déconnectés d'abord
j'envoie broadcast asking if people are there en udp
je reçois messages:
if doesn't exist: crée un user for each one
if exists: update to connected
au bout de 5s, on arrete l'autre thread

donc faut lancer l'autre thread à partir de send broadcast

on envoie toujours au port 2000 (c'est le port serveur)
 */
import model.*;
import java.nio.ByteBuffer; /*pour convert int into byte*/
import java.io.*;
import java.net.*;
public class SendMessage extends Thread {
    public void sendBroadcast(User me) throws IOException {

        //we set the address
        InetAddress senderAddress = InetAddress.getByName("255.255.255.255");
        int senderPort = 2000; /*le port sur lequel il devra recevoir*/

        //create datagram socket
        //avec le try, le socket est relâché automatiquement à la fin, donc pas besoin du close
        try (DatagramSocket socket = new DatagramSocket(1000)) {  /*who I am, port duquel j'envoie*/

            //we enable broadcasting
            socket.setBroadcast(true);

            //create message broadcast, on met broadcast au début comme ça on sait que c'est un msg broadcast
            byte[] buf = new byte[256];
            String message = "broadcast" + me.getId() ;
            for (int i=0; i<256; i++) { /*on écrit un message à envoyer*/
                if (i<=message.length()) {
                    buf[i] = (byte) message.charAt(i);
                }
            }

                    ByteBuffer.allocate(4).putInt(me.getId()).array(); /*the message I send, says who I am and carries info of where I am so they can send it back  */
            /*returns the array of 4 int*/ /*can make it bigger, made it 4 bytes long for 1 int for id*/ //le message est en byte du coup, à convertir

            DatagramPacket outPacket = new DatagramPacket(buf, buf.length, senderAddress, senderPort);
            socket.send(outPacket);

            System.out.println("Broadcast message sent.");
        }
    }
}
