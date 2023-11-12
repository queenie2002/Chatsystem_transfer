package model.test;

import model.*;
import java.util.ArrayList;

public class ChatTest {
    // Class attributes
    private int chatId;
    private int counter; // Number of messages in chat (0 to 5). If counter = 5, delete oldest message in chat history
    private int fromUserId; // User who requested the chat session
    private int toUserId; // User who accepted the request
    private boolean isActive; // Status indicating whether the chat is active or not

    // Constructor
    public ChatTest(int chatId, int counter, int fromUserId, int toUserId, boolean isActive) {
        this.chatId = chatId;
        this.counter = counter;
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.isActive = isActive;
    }

    // Getter and setter methods
    public int getChatId() {
        return chatId;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public int getFromUserId() {
        return fromUserId;
    }

    public int getToUserId() {
        return toUserId;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setStatus(boolean isActive) {
        this.isActive = isActive;
    }

    public static int createChatId(ArrayList<ChatTest> myChatHistory) {
        return (ChatHistory.getChatFromIndex(myChatHistory.size() - 1)).getChatId() + 1 ;
    }



}
