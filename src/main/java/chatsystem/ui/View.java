package chatsystem.ui;

import chatsystem.contacts.ContactList;
import chatsystem.contacts.User;

public class View implements ContactList.Observer {

    public static void initialize() {
        System.out.println("Welcome to the ChatSystem program");
        View view = new View();
        ContactList.getInstance().addObserver(view);
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