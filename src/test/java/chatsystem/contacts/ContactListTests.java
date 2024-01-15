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
        contacts.clear();
    }



    private User alice = new User("alice", "testFirstName", "testLastName", "testBirthday", "testPassword", true, InetAddress.getLoopbackAddress());
    private User bob = new User("bob", "testFirstName", "testLastName", "testBirthday", "testPassword", true, InetAddress.getLoopbackAddress());

    @Test
    void contactAdditionTest() throws SQLException, ContactAlreadyExists {

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

        contacts.addContact(alice);
        assert contacts.existsContactWithNickname("alice");

        assertThrows(() -> contacts.addContact(alice));
        assertThrows(() -> contacts.addContact(alice));

    }


}
