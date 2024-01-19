package chatsystem.database;


import chatsystem.contacts.ContactList;
import chatsystem.contacts.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class DatabaseMethodsTests {

    private User alice = new User("alice", "firstAlice", "lastAlice", "birthdayAlice", "pwdAlice", true,InetAddress.getByName("10.1.5.10"));
    private User bob = new User("bob", "firstBob", "lastBob", "birthdayBob", "pwdBob", true, InetAddress.getByName("10.1.5.11"));

    ContactList contacts;

    DatabaseMethodsTests() throws UnknownHostException {
    }

    @BeforeEach
    void setUp() throws SQLException {
        DatabaseMethods.startConnection(alice);
        contacts = ContactList.getInstance();
        contacts.clearContactList();
    }


    @Test
    void testTableUsersCreation() throws SQLException {
        assert !DatabaseMethods.doesTableExist("users");
        DatabaseMethods.createUsersTable();
        assert DatabaseMethods.doesTableExist("users");
    }

    /*
    @Test
    void testAddUserAndCreateMessagesTable() throws SQLException {
        User user = new User("testNickname", "testFirstName", "testLastName", "testBirthday", "testPassword", true, InetAddress.getLoopbackAddress());
        assertDoesNotThrow(() -> DatabaseMethods.addUser(user));
        assert checkIfTableExists("Messages_1"),("Messages table for user 1 should exist";
    }

    @Test
    void testAddMessage() throws SQLException {
        User user = new User("testNickname", "testFirstName", "testLastName", "testBirthday", "testPassword", true, InetAddress.getLoopbackAddress());
        DatabaseMethods.addUser(user);
        assertDoesNotThrow(() -> DatabaseMethods.addMessage(1, "Test Message", "2023-01-01"));
    }



    // Helper method to check if a table exists in the database
    private boolean checkIfTableExists(String tableName) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='" + tableName + "'");
            return rs.next();
        }
    }
    */

}
