package chatsystem.contacts;

import chatsystem.database.DatabaseMethods;
import org.junit.jupiter.api.Test;

import java.net.InetAddress;
import java.sql.SQLException;

public class ContactListTests {/*

    private User alice = new User("alice", "testFirstName", "testLastName", "testBirthday", "testPassword", true, InetAddress.getLoopbackAddress());
    private User bob = new User("bob", "testFirstName", "testLastName", "testBirthday", "testPassword", true, InetAddress.getLoopbackAddress());

    @Test
    void contactAdditionTest() throws SQLException, ContactAlreadyExists {

        DatabaseMethods.startConnection(alice);
        ContactList contacts = ContactList.getInstance();



        assert !contacts.existsContactWithNickname("alice");
        contacts.addContact(alice);
        assert contacts.existsContactWithNickname("alice");
        assert !contacts.existsContactWithNickname("bob");

        assert !contacts.existsContactWithNickname("bob");
        contacts.addContact(bob);
        assert contacts.existsContactWithNickname("bob");
        assert contacts.existsContactWithNickname("alice");
    }

    @Test
    void contactDuplicationTest() throws SQLException, ContactAlreadyExists {

        DatabaseMethods.startConnection(alice);
        ContactList contacts = ContactList.getInstance();


        contacts.addContact(alice);
        assert contacts.existsContactWithNickname("alice");

        try {
            contacts.addContact(alice);
            throw new RuntimeException("Expected ContactAlreadyExists exception");
        } catch (ContactAlreadyExists e ) {
            //expected outcome

        }
    }

*/
}
