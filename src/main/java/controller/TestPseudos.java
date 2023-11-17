package controller;
//import model.*;
import model.ContactList;
import model.User;

import java.util.*;
public class TestPseudos {

    public static void main(String[] args){
        User me = new User(1,"myPseudo","myFirstName","myLastName","myBday","myPword",true);
        User newUser = new User(2,"newPseudo","newFirstName","newLastName","myBday","myPword",true);
        ContactList instance = ContactList.getInstance();
        //instance.addContact(//Add contacts here);
    }

}
