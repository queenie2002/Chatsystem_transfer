package model.test;

import java.util.ArrayList;

public class ChatTest {
    // Class attributes
    private int chatId;
    private int counter; // Number of messages in chat (0 to 5). If counter = 5, delete oldest message in chat history
    private int fromUserId; // User who requested the chat session
    private int toUserId; // User who accepted the request
    private boolean isActive; // Status indicating whether the chat is active or not

    private static ArrayList<ChatTest> chatSessions = new ArrayList<>(); // All chat sessions (to allow us to access message history)

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

    private static int createChatId() {
        return chatSessions.size() + 1;
    }

    public static ChatTest startNewChatSession(int fromUserId, int toUserId) {//creates new chatSession and sets it to active so we can begin chatting right away
        int newChatId = createChatId();
        ChatTest newChat = new ChatTest(newChatId, 0, fromUserId, toUserId, true); // Set initial status to active to allow chatting
        chatSessions.add(newChat);
        return newChat;
    }

    public static void endConversation(ChatTest chat) {//ends current conversation but keeps the chat to allow history access
        chat.setStatus(false); // Set the status to inactive
    }

    //need method to allow conversation between 2 users who already chatted in the past
}
