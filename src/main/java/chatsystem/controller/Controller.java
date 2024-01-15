package chatsystem.controller;

import chatsystem.contacts.ContactAlreadyExists;
import chatsystem.contacts.ContactList;
import chatsystem.contacts.User;
import chatsystem.network.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.sql.SQLException;

public class Controller {

    private static Logger LOGGER = LogManager.getLogger(Controller.class);
    public static void handleContactDiscoveryMessage(UDPMessage message) {
        ContactList instance = ContactList.getInstance();
        try {

            User aUser = new User(message.content(), "", "", "", "", true, message.originAddress());


            instance.addContact(aUser);
            LOGGER.info("New contact added to the list " + message.content());
        } catch (ContactAlreadyExists e) {
            LOGGER.error("Receive a contact already in the contact list " + message.content());
        } catch (SQLException e) {
            throw new RuntimeException("User could not be added to database");
        }


    }

}

