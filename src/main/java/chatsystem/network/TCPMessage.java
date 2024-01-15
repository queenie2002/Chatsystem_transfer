package chatsystem.network;

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

    /*public boolean isActive() {
        return isActive;
    }

    public void setStatus(boolean isActive) {
        this.isActive = isActive;
    }*/

    public void setDate(String date) {
        this.date = date;
    }
    public void setContent (String content) {
        this.content = content;
    }
    /*
    public static int createChatId(ArrayList<TCPMessage> myChatHistory) {
        return (ChatHistory.getChatFromIndex(myChatHistory.size() - 1)).getChatId() + 1 ;
    }
*/


    public static TCPMessage deserialize(String serializedData) {
        String[] parts = serializedData.split(",");

        if (parts.length != 4) {
            throw new IllegalArgumentException("Serialized data is invalid: " + serializedData);
        }

        return new TCPMessage(parts[0], parts[1], parts[2], parts[3]);
    }


}
