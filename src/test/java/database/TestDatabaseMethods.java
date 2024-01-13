package database;

class TestDatabaseMethods {
/*
    private static final String TEST_DATABASE_URL = "jdbc:sqlite:my_test_database.db";
    private Connection connection;

    @BeforeEach
    void setUp() throws SQLException {
        // Establish a new connection to the test database for each test
        User user = new User("testNickname", "testFirstName", "testLastName", "testBirthday", "testPassword", true, InetAddress.getLoopbackAddress());
        DatabaseMethods.startConnection(user);

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


    @Test
    void testAddUserAndCreateMessagesTable() throws SQLException {
        User user = new User("testNickname", "testFirstName", "testLastName", "testBirthday", "testPassword", true, InetAddress.getLoopbackAddress());
        assertDoesNotThrow(() -> DatabaseMethods.addUser(user));
        assertTrue(checkIfTableExists("Messages_1"), "Messages table for user 1 should exist");
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
