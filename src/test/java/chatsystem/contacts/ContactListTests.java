package chatsystem.contacts;

import chatsystem.database.DatabaseMethods;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.xml.crypto.Data;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;

public class ContactListTests {

    public ContactListTests() throws UnknownHostException {
    }

    public interface FallibleCode {

        void run() throws Exception;
    }

    private static void assertThrows(FallibleCode code) {
        try {
            code.run();
            throw new RuntimeException("code should have thrown an exception");
        } catch (Exception e) {
            //ok expected an exception
        }
    }

    ContactList contacts;
    @BeforeEach
    void setUp() throws SQLException, UnknownHostException {
        DatabaseMethods.deleteDatabase(alice.getIpAddress());
        DatabaseMethods.deleteDatabase(bob.getIpAddress());
        DatabaseMethods.startConnection(alice);
        DatabaseMethods.initializeDatabase();
        contacts = ContactList.getInstance();
        contacts.clearContactList();
    }



    private User alice = new User("alice","pwdAlice", true, InetAddress.getLoopbackAddress());
    private User bob = new User("bob", "pwdBob", true, InetAddress.getByName("192.127.2.1"));

    @Test
    void contactAdditionTest() throws SQLException, ContactAlreadyExists, UnknownHostException {

        assert !contacts.existsContact(alice.getIpAddress());
        contacts.addContact(alice);
        assert contacts.existsContact(alice.getIpAddress());
        assert !contacts.existsContact(bob.getIpAddress());

        assert !contacts.existsContact(bob.getIpAddress());
        contacts.addContact(bob);
        assert contacts.existsContact(bob.getIpAddress());
        assert contacts.existsContact(alice.getIpAddress());
    }

    @Test
    void contactDuplicationTest() throws SQLException, ContactAlreadyExists, UnknownHostException {

        contacts.addContact(alice);
        assert contacts.existsContact(alice.getIpAddress());

        assertThrows(() -> contacts.addContact(alice));
        assertThrows(() -> contacts.addContact(alice));

    }

    @Test
    void updateContactTest() throws SQLException, ContactAlreadyExists, ContactDoesntExist, UnknownHostException {

        assertThrows(() -> contacts.updateContact(alice));

        contacts.addContact(alice);
        assert contacts.existsContact(alice.getIpAddress());

        alice.setPassword("another one");
        contacts.updateContact(alice);
        assert contacts.existsContact(alice.getIpAddress());
        assert alice.getPassword().equals("another one");
        assert !alice.getPassword().equals("pwdAlice");
    }


    @Test
    void deleteContactTest() throws SQLException, ContactAlreadyExists, ContactDoesntExist, UnknownHostException {

        assert contacts.isEmptyContactList();
        assertThrows(() -> contacts.deleteContact(alice.getIpAddress()));

        contacts.addContact(alice);
        assert contacts.existsContact(alice.getIpAddress());
        assert !contacts.isEmptyContactList();

        contacts.deleteContact(alice.getIpAddress());
        assertThrows(() ->  contacts.existsContact(alice.getIpAddress()));

        assert contacts.isEmptyContactList();
    }



    @Test
    void getContactTest() throws SQLException, ContactAlreadyExists, ContactDoesntExist, UnknownHostException {

        assertThrows(() -> contacts.getContact(alice.getIpAddress()));

        contacts.addContact(alice);
        assert contacts.existsContact(alice.getIpAddress());

        User test = contacts.getContact(alice.getIpAddress());
        assert test.getPassword().equals("pwdAlice");
    }


    @Test
    void existsContactTest() throws SQLException, ContactAlreadyExists, UnknownHostException {

        assert !contacts.existsContact(alice.getIpAddress());

        contacts.addContact(alice);
        assert contacts.existsContact(alice.getIpAddress());

    }


    @Test
    void emptyContactListTest() throws SQLException, ContactAlreadyExists, UnknownHostException {

        assert contacts.isEmptyContactList();
        contacts.addContact(alice);
        assert contacts.existsContact(alice.getIpAddress());
        assert !contacts.isEmptyContactList();
    }




}
