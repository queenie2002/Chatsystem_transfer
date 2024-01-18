package chatsystem.contacts;

import chatsystem.database.DatabaseMethods;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.InetAddress;
import java.sql.SQLException;

public class ContactListTests {

    interface FallibleCode {

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
    void setUp() throws SQLException {
        DatabaseMethods.startConnection(alice);
        contacts = ContactList.getInstance();
        contacts.clearContactList();
    }



    private User alice = new User("alice", "firstAlice", "lastAlice", "birthdayAlice", "pwdAlice", true, InetAddress.getLoopbackAddress());
    private User bob = new User("bob", "firstBob", "lastBob", "birthdayBob", "pwdBob", true, InetAddress.getLoopbackAddress());

    @Test
    void contactAdditionTest() throws SQLException, ContactAlreadyExists {

        assert !contacts.existsContact("alice");
        contacts.addContact(alice);
        assert contacts.existsContact("alice");
        assert !contacts.existsContact("bob");

        assert !contacts.existsContact("bob");
        contacts.addContact(bob);
        assert contacts.existsContact("bob");
        assert contacts.existsContact("alice");
    }

    @Test
    void contactDuplicationTest() throws SQLException, ContactAlreadyExists {

        contacts.addContact(alice);
        assert contacts.existsContact("alice");

        assertThrows(() -> contacts.addContact(alice));
        assertThrows(() -> contacts.addContact(alice));

    }

    @Test
    void updateContactTest() throws SQLException, ContactAlreadyExists {

        assertThrows(() -> contacts.updateContact(alice));

        contacts.addContact(alice);
        assert contacts.existsContact("alice");

        alice.setPassword("another one");
        contacts.updateContact(alice);
        assert contacts.existsContact("alice");
        assert alice.getPassword().equals("another one");
        assert !alice.getPassword().equals("pwdAlice");
    }


    @Test
    void deleteContactTest() throws SQLException, ContactAlreadyExists {

        assertThrows(() -> contacts.deleteContact("alice"));

        contacts.addContact(alice);
        assert contacts.existsContact("alice");

        contacts.deleteContact("alice");
        assertThrows(() ->  contacts.existsContact("alice"));
    }



    @Test
    void getContactTest() throws SQLException, ContactAlreadyExists {

        assertThrows(() -> contacts.getContact("alice"));

        contacts.addContact(alice);
        assert contacts.existsContact("alice");

        User test = contacts.getContact("alice");
        assert test.getFirstName().equals("firstAlice");
    }


    @Test
    void existsContactTest() throws SQLException, ContactAlreadyExists {

        assert !contacts.existsContact("alice");

        contacts.addContact(alice);
        assert contacts.existsContact("alice");

    }



}
