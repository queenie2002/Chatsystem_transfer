package controller;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.*;
import model.*;
import static model.ContactList.getInstance;


public class ReceiveMessage extends Thread {


    public String[] ExtractInfoFromPattern(String inputString) {

        // List of patterns
        List<String> patternList = new ArrayList<>();
        patternList.add("BROADCAST: id: (\\d+) nickname: (\\S+) ip address: (\\S+)");  //broadcast at beginning, unique pseudo
        patternList.add("DISCONNECT: id: (\\d+)");  //disconnecting
        patternList.add("NICKNAME EXISTS");  //nickname exists



        boolean matchesAnyPattern = false;
        for (String patternString : patternList) {
            Pattern pattern = Pattern.compile(patternString);
            Matcher matcher1 = pattern.matcher(inputString);
            Matcher matcher2 = pattern.matcher(inputString);

            if (matcher1.matches()) {
                matchesAnyPattern = true;
                String[] res = new String[0];
                
                // Check if a match is found
                if (matcher2.find()) {
                    if (patternString.equals("BROADCAST: id: (\\d+) nickname: (\\S+) ip address: (\\S+)")) {
                        // Extract the matched numbers
                        String id = matcher2.group(1);
                        String nickname = matcher2.group(2);
                        String ipAddress = matcher2.group(3);

                        res = new String[4];
                        res[0] = patternString;
                        res[1] = id;
                        res[2] = nickname;
                        res[3] = ipAddress;
                    } else if (patternString.equals("DISCONNECT: id: (\\d+)")){
                        // Extract the matched numbers
                        String id = matcher2.group(1);

                        res = new String[2];
                        res[0] = patternString;
                        res[1] = id;
                    }
                    else if (patternString.equals("NICKNAME EXISTS")){
                        // Extract the matched numbers
                        String id = matcher2.group(1);

                        res = new String[1];
                        res[0] = patternString;
                    }
                    return res;
                }
                break;  // Exit the loop if a match is found
            }
        }

        if (!matchesAnyPattern) {
            System.out.println("error: string does not match any pattern in the list.");
        }
        return null;
    }




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

                //extracts info from packet
                String[] res = ExtractInfoFromPattern(received);

                //si c'est le broadcast du début, adds un contact with ip and nickname and updates his status to connected
                //have to deal with unique pseudo here
                if (res[0].equals("BROADCAST: id: (\\d+) nickname: (\\S+) ip address: (\\S+)")) {
                    ContactList instance = getInstance();

                    int id = Integer.parseInt(res[1]);
                    String nickname = res[2];
                    String ipAddress = res[3];

                    if (instance.existsContact(id)) { //if we already know that person, we put their status to true
                        User userSender = instance.getContact(id);
                        userSender.setStatus(true);
                        instance.changeContact(userSender);
                    }
                    else {//we create a new user and have to check if their nickname is unique
                        if (instance.existsContactWithNickname(nickname)) {
                            //have to send a message saying there already exists someone --------------------------------------------HERE HOW DO I DO
                        }
                        else {//we add them to our list
                            User userSender = new User(id, nickname, "", "", "", "", true, InetAddress.getByName(ipAddress.substring(11)));
                            instance.addContact(userSender);
                        }
                    }
                }

                //si c'est message disconnect, updates user to disconnected

                if (res[0].equals("DISCONNECT: id: (\\d+)")) {

                    int id = Integer.parseInt(res[1]);

                    ContactList instance = getInstance();
                    User userSender = instance.getContact(id);
                    userSender.setStatus(false);
                    instance.changeContact(userSender);
                }

                if (res[0].equals("NICKNAME EXISTS")) {
                    //HAVE TO CHANGE NICKNAME
                }


                //PAS LA MAIS CEST ICI QUON POURRA DISCONNECT I GUESS
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
