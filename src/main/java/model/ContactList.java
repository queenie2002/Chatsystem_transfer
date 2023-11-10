package model;
import java.util.ArrayList;

public class ContactList {
    // Class attribute
    private ArrayList<User> myContactList;

    // Constructor
    public ContactList(ArrayList<User> currentContactList) {
        this.myContactList = currentContactList;
    }

    // Methods
    public void addContact(User user) {
        myContactList.add(user);
    }

    public ArrayList<User> getContactList() {
        return myContactList;
    }

    public ArrayList<User> getConnectedContacts() {
        ArrayList<User> connectedList = new ArrayList<>();
        for (User u : myContactList) {
            if (u.getStatus()) {
                connectedList.add(u);
            }
        }
        return connectedList;
    }
}
