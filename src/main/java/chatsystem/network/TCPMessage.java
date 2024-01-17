package chatsystem.network;

import java.util.ArrayList;

public class TCPMessage {
    // Class attributes
    private int chatId;
    private String content;
    private String date;
    private String fromUserIP; // User who requested the chat session
    private String toUserIP; // User who accepted the request
    /*private boolean isActive; // Status indicating whether the chat is active or not*/

    // Constructor
    public TCPMessage(String content, String date, String fromUserIP, String toUserIP/*, boolean isActive*/) {

        this.content = content;
        this.date = date;
        this.fromUserIP = fromUserIP;
        this.toUserIP = toUserIP;
        /*this.isActive = isActive;*/
    }

    public TCPMessage() {
    }

    // Getter and setter methods
    public int getChatId() {
        return chatId;
    }
    public String getDate() {
        return date;
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


    public void setChatId(int chatId) {
        this.chatId = chatId;
    }
    public void setDate(String date) {
        this.date = date;
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

        return new TCPMessage(parts[0], parts[1], parts[2], parts[3]);
    }

    public static void printTCPMessage(TCPMessage message) {
        System.out.println("ChatID: "+message.getChatId()+" content: "+message.getContent()+" date: "+message.getDate()+" fromUserIP: "+message.getFromUserIP()+" toUserIP: "+message.getToUserIP());
    }

    public static void printTCPMessageList(ArrayList<TCPMessage> messagesList) {

        System.out.println("Printing list of messages :\n");
        for (TCPMessage message : messagesList) {
            printTCPMessage(message);
        }
    }

}
