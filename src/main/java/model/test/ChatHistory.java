package model.test;


import model.*;


import java.util.ArrayList;

public class ChatHistory {

    // Class attribute
    private ArrayList<Chat> myChatHistory;

    // Constructor
    public ChatHistory() { //i'm adding a constructor qui est empty
        this.myChatHistory = new ArrayList<Chat>();
    }

    // Methods
    public void addChat(Chat chat) {
        myChatHistory.add(chat);
    }

    public void removeChat(int idChat) { //on utilise idChat
        for (Chat aChat : myChatHistory) {
            if (aChat.getChatId() == idChat) {
                myChatHistory.remove(aChat);
            }
        }
    }

    public Chat getChat(int idChat) { //on utilise idUser
        for (Chat aChat : myChatHistory) {
            if (aChat.getChatId() == idChat) {
                return aChat;
            }
        }
        System.out.println("couldn't find the chat with id: " + idChat);
        return null;  //dans ce cas c'est parce qu'on a pas trouvé idChat
    }

    public ArrayList<Chat> getChatHistory() {
        return myChatHistory;
    }

}

