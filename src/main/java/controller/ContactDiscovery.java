package controller;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
public class ContactDiscovery extends Thread {
    public static void sendBroadcast() throws IOException{

        DatagramSocket socket = new DatagramSocket(2369);  /*who I am */  /*UPDATE QUI JE SUIS*/
        byte[] buf = new byte[256];

        for (int i=0; i<name.length(); i++) {
            buf[i] = (byte) name.charAt(i); /*name.getBytes() qu'on peut directement mettre au niveau de la création de buf*/
        }

        InetAddress senderAddress = InetAddress.getByName("127.0.0.1") ;
        int senderPort = 2005;
        DatagramPacket outPacket = new DatagramPacket(buf, buf.length, senderAddress, senderPort);
        socket.send(outPacket);

        socket.close();
    }


}
