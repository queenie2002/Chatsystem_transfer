package controller;
//import model.*;
import model.ContactList;
import model.User;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
public class TestPseudos {

    public static void main(String[] args) throws UnknownHostException {
        User me = new User(1,"myPseudo","myFirstName","myLastName","myBday","myPword",true, InetAddress.getLocalHost());
        User newUser = new User(2,"newPseudo","newFirstName","newLastName","myBday","myPword",true, InetAddress.getLocalHost());
        ContactList instance = ContactList.getInstance();
        //instance.addContact(//Add contacts here);
    }

}
