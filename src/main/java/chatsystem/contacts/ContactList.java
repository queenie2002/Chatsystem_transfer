package chatsystem.contacts;

/** Contact List */

import chatsystem.database.DatabaseMethods;

import java.sql.*;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ContactList {

    public interface Observer {
        void newContactAdded(User user) throws SQLException;
        void nicknameChanged(User newUser, String previousNickname);
    }



    private final static ContactList INSTANCE = new ContactList();
    private final ArrayList<User> myContactList;
    List<Observer> observers = new ArrayList<>();





    private ContactList() { //i'm adding a constructor qui est empty
        myContactList = new ArrayList<User>();
    }





    public synchronized void addObserver(Observer obs) {
        this.observers.add(obs);
    }


    public synchronized static ContactList getInstance() {
        return INSTANCE;
    }








    public synchronized void printContactList() {
        for (int i = 0; i < myContactList.size(); i++) {
            printContact(myContactList.get(i));
        }
    }

    public synchronized void addContact(User user) throws SQLException, ContactAlreadyExists {
        String username = user.getNickname();
        if (existsContactWithNickname(username)) {
            throw new ContactAlreadyExists(username);
        }
        else {
            myContactList.add(user);

            for (Observer obs : observers) {
                obs.newContactAdded(user);
            }

        }
    }

    public synchronized User getContactWithNickname(String nickname) {
        for (User aUser : myContactList) {
            if (Objects.equals(aUser.getNickname(), nickname)) {
                return aUser;
            }
        }
        System.out.println("couldn't find the user with nickname: " + nickname);
        return null;  //dans ce cas c'est parce qu'on a pas trouvé nickname
    }

    public synchronized User getContactWithIpAddress(InetAddress ipAddress) { //on utilise ipAddress
        for (User aUser : myContactList) {
            if (Objects.equals(aUser.getIpAddress(), ipAddress)) {
                return aUser;
            }
        }
        System.out.println("couldn't find the user with ipaddress: " + ipAddress);
        return null;  //dans ce cas c'est parce qu'on a pas trouvé ipAddress
    }

    public synchronized void changeContact(User user) {
        int index=-1;
        for (User aUser : myContactList) {
            if (Objects.equals(aUser.getNickname(), user.getNickname())) {
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


    public synchronized Boolean existsContactWithIpAddress(InetAddress ipAddress) {
        for (User aUser : myContactList) {
            if (Objects.equals(String.valueOf(aUser.getIpAddress()), ipAddress.toString())) {
                return true;
            }
        }
        return false;
    }


    public synchronized Boolean existsContactWithNickname(String nickname) {
        for (User aUser : myContactList) {
            if (Objects.equals(aUser.getNickname(), nickname)) {
                return true;
            }
        }
        return false;
    }

    public synchronized void deleteContactWithNickname(String nickname) {
        if (existsContactWithNickname(nickname)) {
            User aUser = getContactWithNickname(nickname);
            myContactList.remove(aUser);
        }
        else {
            System.out.println("this contact doesn't exist so couldn't delete it");
        }

    }

    //changes "my socket" in user with ipAddress
    public synchronized void updateMySocketOfUser(InetAddress ipAddress, int newSocket) {
        if (existsContactWithIpAddress(ipAddress)) {
            User aUser = getContactWithIpAddress(ipAddress);
            aUser.setMySocket(newSocket);
        }
        else {
            System.out.println("this contact doesn't exist so couldn't update their socket ?");
        }

    }


    public synchronized void printContact(User user) {
        Boolean found = false;
        for (User aUser : myContactList) {
            if (Objects.equals(aUser.getNickname(), user.getNickname())) {
                System.out.println(" nickname: "+user.getNickname()+" firstname: "+user.getFirstName()+" lastname: "+user.getLastName()+" birthday: "+user.getBirthday()+/*" password: "+user.getPassword()+*/" status: "+user.getStatus()+" ip address: "+user.getIpAddress() + " my socket " + user.getMySocket() + " their socket " + user.getTheirSocket());
                found = true;
            }

        }
        if (!found) {
            System.out.println("couldn't find the user with nickname: " + user.getNickname());
        }

    }
    public synchronized ArrayList<User> getContactList() {
        return myContactList;
    }

    public synchronized ArrayList<User> getConnectedContactsList() {
        ArrayList<User> connectedList = new ArrayList<>();
        for (User u : myContactList) {
            if (u.getStatus()) {
                connectedList.add(u);
            }
        }
        return connectedList;
    }

    public synchronized void clear() {
        myContactList.clear();
    }
}
