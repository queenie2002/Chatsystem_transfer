package model;

/*
we only have the contact list of our user
so we only make one in the main?
 */

import java.util.ArrayList;

public class ContactList {
    // Class attribute
    private final static ContactList instance = new ContactList();
    private final ArrayList<User> myContactList;

    // Constructor
    ContactList() { //i'm adding a constructor qui est empty
        myContactList = new ArrayList<User>();
    }

    public static ContactList getInstance() {
        return instance;
    }

    // Methods
    public void addContact(User user) {
        myContactList.add(user);
    }

    public void removeContact(int idUser) { //on utilise idUser
        for (User aUser : myContactList) {
            if (aUser.getId() == idUser) {
                myContactList.remove(aUser);
            }
        }
    }

    public User getContact(int idUser) { //on utilise idUser
        for (User aUser : myContactList) {
            if (aUser.getId() == idUser) {
                return aUser;
            }
        }
        System.out.println("couldn't find the user with id: " + idUser);
        return null;  //dans ce cas c'est parce qu'on a pas trouvé idUser
    }

    public void changeContact(int idUser, User user) {
        for (User aUser : myContactList) {
            if (aUser.getId() == idUser) {
                return aUser;
            }
        }
        int index = myContactList.indexOf(elementToReplace);



    }
    public ArrayList<User> getContactList() {
        return myContactList;
    }

    public ArrayList<User> getConnectedContactsList() {
        ArrayList<User> connectedList = new ArrayList<>();
        for (User u : myContactList) {
            if (u.getStatus()) {
                connectedList.add(u);
            }
        }
        return connectedList;
    }
}
