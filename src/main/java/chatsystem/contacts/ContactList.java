package chatsystem.contacts;


import chatsystem.MainClass;
import chatsystem.observers.MyObserver;

import org.apache.logging.log4j.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

/** Our Contact List, we get it through an Instance */
public class ContactList {


    //LOGGER
    private static final Logger LOGGER = LogManager.getLogger(MainClass.class);


    //OBSERVERS
    ArrayList<MyObserver> observers = new ArrayList<>();
    public synchronized void addObserver(MyObserver obs) {
        this.observers.add(obs);
    }





    //CONTACT LISTS and INSTANCE
    private final static ContactList INSTANCE = new ContactList();
    private final ArrayList<User> myContactList;
    private ContactList() { //i'm adding a constructor qui est empty
        myContactList = new ArrayList<User>();
    }



    /** Returns an instance of Contact List */
    public synchronized static ContactList getInstance() {
        return INSTANCE;
    }
    /** Returns Contact List */
    public synchronized ArrayList<User> getContactList() {
        return myContactList;
    }
    /** Return a Contact List of Connected Users */
    public synchronized ArrayList<User> getConnectedContactsList() {
        ArrayList<User> connectedList = new ArrayList<>();
        for (User u : myContactList) {
            if (u.getStatus()) {
                connectedList.add(u);
            }
        }
        return connectedList;
    }
    /** Clears our Contact List */
    public synchronized void clearContactList() {
        myContactList.clear();
        LOGGER.info("Cleared myContactList ");
    }






    /** Adds a Contact to Contact List, and database if they are already in the list, throws exception ContactAlreadyExists*/
    public synchronized void addContact(User user) throws ContactAlreadyExists, SQLException {
        String nickname = user.getNickname();
        if (existsContact(nickname)) {
            throw new ContactAlreadyExists(nickname);
        }
        else {
            myContactList.add(user);
            LOGGER.info("Added user to myContactList " + nickname);
            for (MyObserver obs : observers) {
                obs.newContactAdded(user);
            }
        }
    }
    /** Updates a Contact in the Contact List and the database*/
    public synchronized void updateContact(User user) throws SQLException {
        int index=-1;
        for (User aUser : myContactList) {
            if (Objects.equals(aUser.getNickname(), user.getNickname())) {
                index = myContactList.indexOf(aUser);
            }
        }

        if (index != -1) {
            // Replace the element at the found index
            myContactList.set(index, user);
            LOGGER.info("Updated user in ContactList " + user.getNickname());
        } else {
            LOGGER.error("Couldn't find user in ContactList " + user.getNickname());
        }

        for (MyObserver obs : observers) {
            obs.newContactAdded(user);
        }
    }
    /** Deletes Contact with nickname*/
    public synchronized void deleteContact(String nickname) {
        if (existsContact(nickname)) {
            User aUser = getContact(nickname);
            myContactList.remove(aUser);
            //we dont remove a user from database
            LOGGER.info("Deleted user in ContactList " + nickname);
        }
        else {
            LOGGER.error("Couldn't find user to delete in ContactList " + nickname);
        }
    }





    /** Returns Contact with nickname */
    public synchronized User getContact(String nickname) {
        for (User aUser : myContactList) {
            if (Objects.equals(aUser.getNickname(), nickname)) {
                return aUser;
            }
        }
        LOGGER.error("Couldn't find the user with nickname: " + nickname);
        return null;
    }




    /** Returns true if there exists a Contact with nickname*/
    public synchronized Boolean existsContact(String nickname) {
        for (User aUser : myContactList) {
            if (Objects.equals(aUser.getNickname(), nickname)) {
                return true;
            }
        }
        return false;
    }




    /** Prints a Contact */
    public synchronized void printContact(User user) {
        //if it's me
        if (user.getNickname().equals(MainClass.me.getNickname())) {
            System.out.println(" nickname: "+user.getNickname()+" firstname: "+user.getFirstName()+" lastname: "+user.getLastName()+" birthday: "+user.getBirthday()+/*" password: "+user.getPassword()+*/" status: "+user.getStatus()+" ip address: "+user.getIpAddress());
        }
        //if it's someone else
        else {
            System.out.println(" nickname: "+user.getNickname()+" status: "+user.getStatus()+" ip address: "+user.getIpAddress());
        }
    }
    /** Prints Contact List */
    public synchronized void printContactList() {
        for (User user : myContactList) {
            System.out.println("Printing A Contact List: ");
            printContact(user);
        }
    }
}




