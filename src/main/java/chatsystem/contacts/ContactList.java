package chatsystem.contacts;


import chatsystem.MainClass;
import chatsystem.observers.MyObserver;

import org.apache.logging.log4j.*;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;
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
    private final static ContactList INSTANCE = new ContactList();
    private final ArrayList<User> myContactList;
    private ContactList() { //i'm adding a constructor qui est empty
        myContactList = new ArrayList<User>();
    }



    /** Returns an instance of Contact List */
    public synchronized static ContactList getInstance() {
        return INSTANCE;
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

    public synchronized boolean isEmptyContactList() {
        return myContactList.isEmpty();
    }




    /** Adds a Contact to Contact List, and database if they are already in the list, throws exception ContactAlreadyExists*/
    public synchronized void addContact(User user) throws ContactAlreadyExists, SQLException, UnknownHostException {
        InetAddress ipAddress = user.getIpAddress();
        if (existsContact(ipAddress)) {
            throw new ContactAlreadyExists(ipAddress.getHostAddress());
        }
        else {
            myContactList.add(user);
            LOGGER.info("Added user to myContactList " + ipAddress);
            for (MyObserver obs : observers) {
                obs.newContactAdded(user);
            }
        }
    }
    /** Updates a Contact in the Contact List and the database, throws exception ContactDoesntExist*/
    public synchronized void updateContact(User user) throws SQLException, ContactDoesntExist, UnknownHostException {
        int index=-1;
        for (User aUser : myContactList) {
            if (Objects.equals(aUser.getIpAddress(), user.getIpAddress())) {
                index = myContactList.indexOf(aUser);
            }
        }

        if (index != -1) {
            // Replace the element at the found index
            myContactList.set(index, user);
            LOGGER.info("Updated user in ContactList " + user.getIpAddress());
        } else {
            throw new ContactDoesntExist(user.getIpAddress().getHostAddress());
        }

        for (MyObserver obs : observers) {
            obs.newContactAdded(user);
        }
    }
    /** Deletes Contact with ipAddress*/
    public synchronized void deleteContact(InetAddress ipAddress) throws ContactDoesntExist {
        if (existsContact(ipAddress)) {
            User aUser = getContact(ipAddress);
            myContactList.remove(aUser);
            //we dont remove a user from database
            LOGGER.info("Deleted user in ContactList " + ipAddress);
        }
        else {
            throw new ContactDoesntExist(ipAddress.getHostAddress());
        }
    }





    /** Returns Contact with ipAddress */
    public synchronized User getContact(InetAddress ipAddress) throws ContactDoesntExist {
        for (User aUser : myContactList) {
            if (Objects.equals(aUser.getIpAddress(), ipAddress)) {
                return aUser;
            }
        }
        LOGGER.error("Couldn't find the user with ipAddress: " + ipAddress);
        throw new ContactDoesntExist(ipAddress.getHostAddress());
    }








    /** Returns true if there exists a Contact with ipAddress*/
    public synchronized Boolean existsContact(InetAddress ipAddress) {
        for (User aUser : myContactList) {
            if (Objects.equals(aUser.getIpAddress(), ipAddress)) {
                return true;
            }
        }
        return false;
    }

    /** Returns true if there exists a Contact with ipAddress*/
    public synchronized Boolean existsContactWithNickname(String nickname) {
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
        if (user.getIpAddress().equals(MainClass.me.getIpAddress())) {
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




