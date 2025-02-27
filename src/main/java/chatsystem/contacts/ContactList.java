package chatsystem.contacts;


import chatsystem.MainClass;
import chatsystem.observers.MyObserver;

import org.apache.logging.log4j.*;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Objects;

/** Our Contact List, we get it through an Instance */
public class ContactList {


    //LOGGER
    private static final Logger LOGGER = LogManager.getLogger(ContactList.class);


    //OBSERVERS
    ArrayList<MyObserver> observers = new ArrayList<>();
    public synchronized void addObserver(MyObserver obs) {
        this.observers.add(obs);
    }





    //CONTACT LISTS and INSTANCE
    private final ArrayList<User> myContactList;
    private ContactList() { //i'm adding a constructor qui est empty
        myContactList = new ArrayList<User>();
    }
    private final static ContactList INSTANCE = new ContactList();




    /** Returns an instance of Contact List */
    public synchronized static ContactList getInstance() {
        return INSTANCE;
    }
    /** Return a Contact List of Connected Users */
    public synchronized ArrayList<User> getConnectedContactsList() {
        ArrayList<User> connectedList = new ArrayList<>();
        for (User u : myContactList) {
            //if their status is true = connected
            if (u.getStatus()) {
                //we add them to the list
                connectedList.add(u);
            }
        }
        return connectedList;
    }
    /** Clears our Contact List */
    public synchronized void clearContactList() {
        myContactList.clear();
        LOGGER.trace("Cleared myContactList ");
    }
    /** Returns true if the ContactList is empty*/
    public synchronized boolean isEmptyContactList() {
        return myContactList.isEmpty();
    }




    /** Adds a Contact to Contact List and database. If they are already in the list, throws exception ContactAlreadyExists */
    public synchronized void addContact(User user) throws ContactAlreadyExists {
        //we check if we already have that user in our list with their ip address, that is unique
        InetAddress ipAddress = user.getIpAddress();
        if (existsContact(ipAddress)) {
            //if we already have them, we throw an exception
            throw new ContactAlreadyExists(ipAddress.getHostAddress());
        }
        else {
            //else we add them to our Contact List
            myContactList.add(user);
            LOGGER.trace("Added user to myContactList " + ipAddress);
            //we notify the observers and call the method contactAddedOrUpdated
            for (MyObserver obs : observers) {
                obs.contactAddedOrUpdated(user);
            }
        }
    }
    /** Updates a Contact in the Contact List and database. */
    public synchronized void updateContact(User user) {
        int index=-1;
        //we look for the user in our Contact List with his ipAddress and get his index
        for (User aUser : myContactList) {
            if (Objects.equals(aUser.getIpAddress(), user.getIpAddress())) {
                index = myContactList.indexOf(aUser);
            }
        }

        //if we find him
        if (index != -1) {
            // we replace the user at the index we found
            myContactList.set(index, user);
            LOGGER.trace("Updated user in ContactList " + user.getIpAddress());
        } else {
            //that shouldn't happen, it's handled already before
            throw new RuntimeException("Couldn't find user to update " + user.getNickname());
        }

        //we notify all observers that a contact has been added/updated
        for (MyObserver obs : observers) {
            obs.contactAddedOrUpdated(user);
        }
    }
    /** Deletes Contact with given ipAddress but only from Contact List and not from database (for tests)*/
    public synchronized void deleteContact(InetAddress ipAddress) throws ContactDoesntExist {
        //we check if we have that contact in our Contact List
        if (existsContact(ipAddress)) {
            User aUser = getContact(ipAddress);
            //we delete them from our Contact List
            myContactList.remove(aUser);
            LOGGER.trace("Deleted user in ContactList " + ipAddress);
        }
        else {
            LOGGER.error("Couldn't find user to delete " + ipAddress.getHostAddress());
            throw new ContactDoesntExist(ipAddress.getHostAddress());
        }
    }





    /** Returns Contact with given ipAddress */
    public synchronized User getContact(InetAddress ipAddress) {
        //we look for user with ip address and return them
        for (User aUser : myContactList) {
            if (Objects.equals(aUser.getIpAddress(), ipAddress)) {
                return aUser;
            }
        }
        //that shouldn't happen, it's handled already before
        LOGGER.error("Couldn't get the user with ipAddress: " + ipAddress);
        throw new RuntimeException(ipAddress.getHostAddress());
    }








    /** Returns true if there exists a Contact with given ipAddress*/
    public synchronized Boolean existsContact(InetAddress ipAddress) {
        for (User aUser : myContactList) {
            if (Objects.equals(aUser.getIpAddress(), ipAddress)) {
                return true;
            }
        }
        return false;
    }

    /** Returns true if there exists a Contact with given nickname*/
    public synchronized Boolean existsContactWithNickname(String nickname) {
        for (User aUser : myContactList) {
            if (Objects.equals(aUser.getNickname(), nickname)) {
                return true;
            }
        }
        return false;
    }




    /** Prints a Contact (for tests) */
    public synchronized void printContact(User user) {
        //if it's me, we print all our information
        if (user.getIpAddress().equals(MainClass.me.getIpAddress())) {
            System.out.println(" nickname: "+user.getNickname()+" password: "+user.getPassword()+" status: "+user.getStatus()+" ip address: "+user.getIpAddress());
        }
        //if it's a contact, we don't print the password
        else {
            System.out.println(" nickname: "+user.getNickname()+" status: "+user.getStatus()+" ip address: "+user.getIpAddress());
        }
    }
    /** Prints Contact List (for tests)*/
    public synchronized void printContactList() {
        for (User user : myContactList) {
            System.out.println("Printing A Contact List: ");
            printContact(user);
        }
    }
}
