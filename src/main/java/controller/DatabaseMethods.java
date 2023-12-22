package controller;

import java.sql.*;

public class DatabaseMethods {

    private static final String DATABASE_URL = "jdbc:sqlite:my_database.db";

    // Method to start the database connection
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DATABASE_URL);
    }

    // Method to create the Users table
    public static void createUsersTable(Connection connection) throws SQLException {
        String usersTableQuery = "CREATE TABLE IF NOT EXISTS Users ("
                + "userID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "ipAddress TEXT NOT NULL, "
                + "name TEXT NOT NULL, "
                + "status INTEGER NOT NULL)";

        try (Statement statement = connection.createStatement()) {
            statement.execute(usersTableQuery);
        }
    }

    // Method to add a user to the Users table and create an empty specific Messages table for that user
    public static void addUser(Connection connection, String ipAddress, String name, int status) throws SQLException {
        String insertUserSQL = "INSERT INTO Users (ipAddress, name, status) VALUES (?, ?, ?)";
        String lastInsertIdSQL = "SELECT last_insert_rowid()";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertUserSQL)) {
            preparedStatement.setString(1, ipAddress);
            preparedStatement.setString(2, name);
            preparedStatement.setInt(3, status);
            preparedStatement.executeUpdate();

            // Fetch the last inserted ID
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(lastInsertIdSQL)) {
                if (resultSet.next()) {
                    long userID = resultSet.getLong(1);
                    createSpecificMessagesTable(connection, userID); // Create an empty Messages table for this user
                }
            }
        }
    }

    // Method to create a specific empty Messages table for a user
    private static void createSpecificMessagesTable(Connection connection, long userID) throws SQLException {
        String specificMessagesTableQuery = "CREATE TABLE IF NOT EXISTS Messages_" + userID + " ("
                + "messageID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "content TEXT NOT NULL, "
                + "date TEXT NOT NULL)";

        try (Statement statement = connection.createStatement()) {
            statement.execute(specificMessagesTableQuery);
        }
    }

    // Method to add a message to the specific Messages table of a user
    public static void addMessage(Connection connection, long userID, String content, String date) throws SQLException {
        String tableName = "Messages_" + userID;
        String insertMessageSQL = "INSERT INTO " + tableName + " (content, date) VALUES (?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertMessageSQL)) {
            preparedStatement.setString(1, content);
            preparedStatement.setString(2, date);
            preparedStatement.executeUpdate();
        }
    }
}
