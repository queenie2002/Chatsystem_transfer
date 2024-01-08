package model;

/*
we only have the contact list of our user
so we only make one in the main?
 */

import controller.DatabaseMethods;

import java.sql.*;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Objects;

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
    public int lengthContactList(){ return myContactList.size(); }

    public void printContactList() {
        for (int i = 0; i < myContactList.size(); i++) {
            printContact(myContactList.get(i));
        }
    }

    public void addContact(User user) throws SQLException {
        myContactList.add(user);
        DatabaseMethods.addUser(user);
    }

    //should be able to remove contact ?


    public User getContactWithNickname(String nickname) { //on utilise nickname
        for (User aUser : myContactList) {
            if (Objects.equals(aUser.getNickname(), nickname)) {
                return aUser;
            }
        }
        System.out.println("couldn't find the user with nickname: " + nickname);
        return null;  //dans ce cas c'est parce qu'on a pas trouvé nickname
    }

    public User getContactWithIpAddress(InetAddress ipAddress) { //on utilise ipAddress
        for (User aUser : myContactList) {
            if (Objects.equals(aUser.getIpAddress(), ipAddress)) {
                return aUser;
            }
        }
        System.out.println("couldn't find the user with ipaddress: " + ipAddress);
        return null;  //dans ce cas c'est parce qu'on a pas trouvé ipAddress
    }

    public void changeContact(User user) {
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


    public Boolean existsContactWithIpAddress(InetAddress ipAddress) {
        for (User aUser : myContactList) {
            if (Objects.equals(String.valueOf(aUser.getIpAddress()), ipAddress.toString())) {
                return true;
            }
        }
        return false;
    }


    public Boolean existsContactWithNickname(String nickname) {
        for (User aUser : myContactList) {
            if (Objects.equals(aUser.getNickname(), nickname)) {
                return true;
            }
        }
        return false;
    }

    public void deleteContactWithNickname(String nickname) {
        if (existsContactWithNickname(nickname)) {
            User aUser = getContactWithNickname(nickname);
            myContactList.remove(aUser);
        }
        else {
            System.out.println("this contact doesn't exist so couldn't delete it");
        }

    }

    //changes "my socket" in user with ipAddress
    public void updateMySocketOfUser(InetAddress ipAddress, int newSocket) {
        if (existsContactWithIpAddress(ipAddress)) {
            User aUser = getContactWithIpAddress(ipAddress);
            aUser.setMySocket(newSocket);
        }
        else {
            System.out.println("this contact doesn't exist so couldn't update their socket ?");
        }

    }


    public void printContact(User user) {
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
