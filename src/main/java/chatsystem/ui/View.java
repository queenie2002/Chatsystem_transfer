package chatsystem.ui;

import chatsystem.contacts.ContactList;
import chatsystem.contacts.User;
import chatsystem.observers.MyObserver;

public class View implements MyObserver {

    public static void initialize() {
        System.out.println("Welcome to the ChatSystem program");
        View view = new View();
        ContactList.getInstance().addObserver(view);
    }

    @Override
    public void showOnlineContacts() {

    }

    @Override
    public void showAllContacts() {

    }

    @Override
    public void newContactAdded(User user) {
        /*
        System.out.println("[VIEW] New contact: " + user.getNickname());
        displayContactList();
     */
    }


    @Override
    public void nicknameChanged(User newUser, String previousNickname) {
        System.out.println("[VIEW] Contact changed: " + newUser.getNickname()+ " (was previously: " + previousNickname + ")");
    }


}