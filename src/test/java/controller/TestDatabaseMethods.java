package controller;
import org.junit.*;
import java.sql.*;

public class TestDatabaseMethods {

    private static final String TEST_DB_URL = "jdbc:sqlite:test_database.db";

    @BeforeClass
    public static void setUpClass() throws SQLException {
        // Initialize connection for tests
        DatabaseMethods.startConnection(TEST_DB_URL);
    }

    @AfterClass
    public static void tearDownClass() throws SQLException {
        // Close the connection after tests
        DatabaseMethods.closeConnection();
    }

    @Test
    public void testTableUsersCreation() throws SQLException {
        DatabaseMethods.tableUsers();
        // Add assertions or checks to verify the table creation
    }

    @Test
    public void testTableMessagesCreation() throws SQLException {
        String testTableName = "Test";
        DatabaseMethods.tableMessages(testTableName);
        // Add assertions or checks to verify the table creation
    }

    @Test
    public void testAddUser() throws SQLException {
        String ipAddress = "127.0.0.1";
        String name = "Test User";
        int status = 1;
        DatabaseMethods.addUser(ipAddress, name, status);
        // Add assertions or checks to verify user addition
    }

    @Test
    public void testAddMessage() throws SQLException {
        String tableName = "Test";
        String content = "Test message";
        String date = "2023-12-19";
        DatabaseMethods.addMessage(tableName, content, date);
        // Add assertions or checks to verify message addition
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("TestDatabaseMethods");
    }
}
