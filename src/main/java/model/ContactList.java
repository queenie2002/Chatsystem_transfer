package model;

/*
we only have the contact list of our user
so we only make one in the main?

 */


import java.util.ArrayList;


public class ContactList {
    // Class attribute
    private ArrayList<User> myContactList;

    // Constructor
    public ContactList() { //i'm adding a constructor qui est empty
        this.myContactList = new ArrayList<User>();
    }

    public ContactList(ArrayList<User> currentContactList) { //C'EST BIZARRE CA, when we first create it we make it empty
        this.myContactList = currentContactList;
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
