package controller;

/*

contact discovery extends a thread,
on met tout le monde à déconnectés d'abord  ---------------------A FAIRE
j'envoie broadcast asking if people are there en udp
je reçois messages:
if doesn't exist: crée un user for each one
if exists: update to connected
au bout de 5s, on arrete l'autre thread----------------FAIT?

donc faut lancer l'autre thread à partir de send broadcast

on envoie toujours au port 2000 (c'est le port serveur)
 */



import controller.*;
import model.*;
import java.io.*;
import java.net.*;



public class SendMessage{

    //we're sending a message to everyone asking if i can use a certain nickname
    public void sendBroadcastBeginning(User user) throws IOException {

        //we set the address
        InetAddress senderAddress = InetAddress.getByName("255.255.255.255");
        int senderPort = 2000; /*le port sur lequel il devra recevoir*/

        //create datagram socket
        //avec le try, le socket est relâché automatiquement à la fin, donc pas besoin du close
        try (DatagramSocket sendingSocket = new DatagramSocket(1200)) {  /*who I am, port duquel j'envoie*/

            //we enable broadcasting
            sendingSocket.setBroadcast(true);

            //create message broadcast, on met broadcast au début comme ça on sait que c'est un msg broadcast


            //HAVE TO BE CAREFUL QUE PAS PLUS LONG QUE 256 BYTES
            String message = "POTENTIAL NICKNAME: nickname: " + user.getNickname() + " ip address: " + user.getIpAddress();
            byte[] buf = message.getBytes();

           /*the message I send, says who I am and carries info of where I am so they can send it back  */
           //le message est en byte du coup, à convertir

            DatagramPacket outPacket = new DatagramPacket(buf, buf.length, senderAddress, senderPort);
            sendingSocket.send(outPacket);

            System.out.println("Broadcast message sent.");
        }
    }



    public void sendDisconnect(User user) throws IOException {

        //we set the address
        InetAddress senderAddress = InetAddress.getByName("255.255.255.255");
        int senderPort = 2000; /*le port sur lequel il devra recevoir*/

        //create datagram socket
        //avec le try, le socket est relâché automatiquement à la fin, donc pas besoin du close
        try (DatagramSocket sendingSocket = new DatagramSocket(1200)) {  /*who I am, port duquel j'envoie*/

            //we enable broadcasting
            sendingSocket.setBroadcast(true);

            //HAVE TO BE CAREFUL QUE PAS PLUS LONG QUE 256 BYTES
            String message = "DISCONNECT: id: " + user.getId();
            byte[] buf = message.getBytes();


            /*the message I send, says who I am and carries info of where I am so they can send it back  */
            DatagramPacket outPacket = new DatagramPacket(buf, buf.length, senderAddress, senderPort);
            sendingSocket.send(outPacket);

            System.out.println("Disconnect message sent.");
        }
    }

    public void sendConnect(User user) throws IOException {

        //we set the address
        InetAddress senderAddress = InetAddress.getByName("255.255.255.255");
        int senderPort = 2000; /*le port sur lequel il devra recevoir*/

        //create datagram socket
        //avec le try, le socket est relâché automatiquement à la fin, donc pas besoin du close
        try (DatagramSocket sendingSocket = new DatagramSocket(1200)) {  /*who I am, port duquel j'envoie*/

            //we enable broadcasting
            sendingSocket.setBroadcast(true);

            //HAVE TO BE CAREFUL QUE PAS PLUS LONG QUE 256 BYTES
            String message = "CONNECT: id: " + user.getId() + " nickname: " + user.getNickname() + " ip address: " + user.getIpAddress();
            byte[] buf = message.getBytes();

            /*the message I send, says who I am and carries info of where I am so they can send it back  */
            DatagramPacket outPacket = new DatagramPacket(buf, buf.length, senderAddress, senderPort);
            sendingSocket.send(outPacket);

            System.out.println("Connect message sent.");
        }
    }





    public void sendNicknameExists(User user) throws IOException {

        //we set the address
        InetAddress senderAddress = InetAddress.getByName(user.getIpAddress().getHostAddress());
        int senderPort = 2000; /*le port sur lequel il devra recevoir*/

        //create datagram socket
        //avec le try, le socket est relâché automatiquement à la fin, donc pas besoin du close
        try (DatagramSocket sendingSocket = new DatagramSocket(1200)) {  /*who I am, port duquel j'envoie*/

            //HAVE TO BE CAREFUL QUE PAS PLUS LONG QUE 256 BYTES
            String message = "NICKNAME EXISTS";
            byte[] buf = message.getBytes();

            /*the message I send, says who I am and carries info of where I am so they can send it back  */
            DatagramPacket outPacket = new DatagramPacket(buf, buf.length, senderAddress, senderPort);
            sendingSocket.send(outPacket);

            System.out.println("Nickname Exists message sent.");
        }
    }

    public void sendNicknameUnique(User user) throws IOException {

        // Set the address to the user's IP address
        InetAddress senderAddress = InetAddress.getByName(user.getIpAddress().getHostAddress());
        int senderPort = 2000;

        try (DatagramSocket sendingSocket = new DatagramSocket(1200)) {

            // Create the unique nickname message
            String message = "NICKNAME UNIQUE";
            byte[] buf = message.getBytes();

            // Create and send the DatagramPacket
            DatagramPacket outPacket = new DatagramPacket(buf, buf.length, senderAddress, senderPort);
            sendingSocket.send(outPacket);

            System.out.println("Nickname Unique message sent.");
        }
    }


}
