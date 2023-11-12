package model.test;


import model.*;


import java.util.ArrayList;

import static model.test.ChatTest.createChatId;

public class ChatHistory {

    // Class attribute
    private static ArrayList<ChatTest> myChatHistory;

    // Constructor
    public ChatHistory() { //i'm adding a constructor qui est empty
        myChatHistory = new ArrayList<ChatTest>();
    }

    // Methods
    public void addChat(ChatTest chat) {
        myChatHistory.add(chat);
    }

    public static void removeChatFromId(int idChat) { //on utilise idChat
        for (ChatTest aChat : myChatHistory) {
            if (aChat.getChatId() == idChat) {
                myChatHistory.remove(aChat);
            }
        }
    }

    public static void removeChatFromIndex(int index) { //on utilise Index, used static to solve problem
        myChatHistory.remove(index);
    }

    public static ChatTest getChatFromId(int idChat) { //on utilise idUser
        for (ChatTest aChat : myChatHistory) {
            if (aChat.getChatId() == idChat) {
                return aChat;
            }
        }
        System.out.println("couldn't find the chat with id: " + idChat);
        return null;  //dans ce cas c'est parce qu'on a pas trouvé idChat
    }

    public static ChatTest getChatFromIndex(int index) { //on utilise Index, used static to solve problem
        return myChatHistory.get(index);
    }


    public ArrayList<ChatTest> getChatHistory() {
        return myChatHistory;
    }

    public void startNewChatSession(int fromUserId, int toUserId) {//creates new chatSession and sets it to active so we can begin chatting right away
        int newChatId = createChatId(myChatHistory);
        ChatTest newChat = new ChatTest(newChatId, 0, fromUserId, toUserId, true); // Set initial status to active to allow chatting
        myChatHistory.add(newChat);
    }

    public static void endConversation(int idChat) {//ends current conversation but keeps the chat to allow history access
        ChatTest chat = getChatFromId(idChat);
        chat.setStatus(false); // Set the status to inactive
        myChatHistory.add(myChatHistory.indexOf(chat), chat);
    }

    public static void continueConversation(int idChat) {//set status to active
        ChatTest chat = getChatFromId(idChat);
        chat.setStatus(true); // Set the status to active
        myChatHistory.add(myChatHistory.indexOf(chat), chat);
    }

}

