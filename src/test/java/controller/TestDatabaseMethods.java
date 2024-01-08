package controller;

import org.junit.jupiter.api.*;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

class TestDatabaseMethods {

    private static final String TEST_DATABASE_URL = "jdbc:sqlite:my_test_database.db";
    private Connection connection;

    @BeforeEach
    void setUp() throws SQLException {
        // Establish a new connection to the test database for each test
        connection = DriverManager.getConnection(TEST_DATABASE_URL);
        DatabaseMethods.createUsersTable(); // Ensure Users table is created in the test database
    }

    @AfterEach
    void tearDown() throws SQLException {
        // Close the connection after each test
        if (connection != null) {
            connection.close();
        }
    }

    @Test
    void testTableUsersCreation() throws SQLException {
        assertDoesNotThrow(DatabaseMethods::createUsersTable);
    }

    /*
    @Test
    void testAddUserAndCreateMessagesTable() throws SQLException {
        DatabaseMethods.createUsersTable(connection);
        assertDoesNotThrow(() -> DatabaseMethods.addUser(connection, "127.0.0.1", "TestUser", 1));
        assertTrue(checkIfTableExists(connection, "Messages_1"), "Messages table for user 1 should exist");
    }

    @Test
    void testAddMessage() throws SQLException {
        DatabaseMethods.createUsersTable(connection);
        DatabaseMethods.addUser(connection, "127.0.0.1", "TestUser", 1);
        assertDoesNotThrow(() -> DatabaseMethods.addMessage(connection, 1, "Test Message", "2023-01-01"));
    }
    */


    // Helper method to check if a table exists in the database
    private boolean checkIfTableExists(Connection connection, String tableName) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='" + tableName + "'");
            return rs.next();
        }
    }
}
