import java.util.*;

/*List of all users who we discovered at some point during a contact discovery.
In order to send a message to a given user, we first have to check if they are connected.
*/

public class ContactList{
    public ArrayList<User> contactList;
    public ArrayList<User> connectedList;

    // Constructor
    public ContactList() {
        contactList = new ArrayList<>();
        connectedList = new ArrayList<>();
    }

    //Methods
    public void addContact(User user) {
        contactList.add(user);
    }

    public ArrayList<User> getContactList(ArrayList<User> contactList) {
        return contactList;
    }
    public ArrayList<User> addConnectedUser(ArrayList<User> contactList) {
        for (User u : contactList) {
            u.getStatus();
        }
    }
    public ArrayList<User> getConnectedList(ArrayList<User> connectedList) {
        return connectedList;
    }

}