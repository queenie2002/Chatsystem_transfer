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

    public void changeContact(User user) {
        int index=-1;
        for (User aUser : myContactList) {
            if (aUser.getId() == user.getId()) {
                index = myContactList.indexOf(aUser);
            }
        }

        if (index != -1) {
            // Replace the element at the found index
            myContactList.set(index, user);
        } else {
            System.out.println("error: user not found in the contactlist (dans ContactList)");
        }
    }

    public Boolean existsContact(int idUser) {
        for (User aUser : myContactList) {
            if (aUser.getId() == idUser) {
                return true;
            }
        }
        return false;
    }

    public Boolean existsContactWithNickname(String nickname) {
        for (User aUser : myContactList) {
            if (aUser.getNickname() == nickname) {
                return true;
            }
        }
        return false;
    }


    public void printContact(User user) {
        Boolean found = false;
        for (User aUser : myContactList) {
            if (aUser.getId() == user.getId()) {
                System.out.println("id: "+user.getId()+" nickname: "+user.getNickname()+" firstname: "+user.getFirstName()+" lastname: "+user.getLastName()+" birthday: "+user.getBirthday()+" password: "+user.getPassword()+" status: "+user.getStatus()+" ip address: "+user.getIpAddress());
                found = true;
            }

        }
        if (!found) {
            System.out.println("couldn't find the user with id: " + user.getId());
        }

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
