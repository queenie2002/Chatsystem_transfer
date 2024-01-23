package chatsystem.network;

import java.util.ArrayList;
import java.time.Instant;

public class TCPMessage {

    private int chatId;
    private String content;
    private Instant timestamp;
    private String fromUserIP; // User who requested the chat session
    private String toUserIP; // User who accepted the request





    // CONSTRUCTORS
    public TCPMessage(String content, Instant timestamp, String fromUserIP, String toUserIP) {

        this.content = content;
        this.timestamp = timestamp;
        this.fromUserIP = fromUserIP;
        this.toUserIP = toUserIP;
    }

    public TCPMessage() {
    }








    // GETTERS
    public int getChatId() {
        return chatId;
    }
    public Instant getTimestamp() {
        return timestamp;
    }
    public String getContent() {
        return content;
    }

    public String getFromUserIP() {
        return fromUserIP;
    }

    public String getToUserIP() {
        return toUserIP;
    }




    // SETTERS

    public void setChatId(int chatId) {
        this.chatId = chatId;
    }
    public void setDate(Instant timestamp) {
        this.timestamp = timestamp;
    }
    public void setContent (String content) {
        this.content = content;
    }
    public void setFromUserIP(String fromUserIP) {
        this.fromUserIP = fromUserIP;
    }
    public void setToUserIP (String toUserIP) {
        this.toUserIP = toUserIP;
    }






    public static TCPMessage deserialize(String serializedData) {
        String[] parts = serializedData.split(",");

        if (parts.length != 4) {
            throw new IllegalArgumentException("Serialized data is invalid: " + serializedData);
        }

        Instant timestamp = Instant.parse(parts[1]); // Convert the string back to Instant
        return new TCPMessage(parts[0], timestamp, parts[2], parts[3]);
    }


    /** Prints a TCP Message*/
    public static void printTCPMessage(TCPMessage message) {
        System.out.println("ChatID: " + message.getChatId() + " content: " + message.getContent()
                + " date: " + message.getTimestamp().toString() + " fromUserIP: " + message.getFromUserIP()
                + " toUserIP: " + message.getToUserIP());
    }

    /** Prints a TCPMessage List*/
    public static void printTCPMessageList(ArrayList<TCPMessage> messagesList) {

        System.out.println("Printing list of messages :\n");
        for (TCPMessage message : messagesList) {
            printTCPMessage(message);
        }
    }

}
