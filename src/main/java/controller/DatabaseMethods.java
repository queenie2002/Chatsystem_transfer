package controller;

import model.*;

import java.net.InetAddress;
import java.sql.*;

public class DatabaseMethods {

    private static final String DATABASE_URL = "jdbc:sqlite:my_database_";

    // Method to start the database connection
    public static Connection getConnection(User user) throws SQLException {
        String IPAddress = String.valueOf(user.getIpAddress());
        IPAddress = IPAddress.substring(1).replace(".","_"); //we take out the / a,d replace . by _
        return DriverManager.getConnection(DATABASE_URL+IPAddress+".db");
    }

    // Method to create the Users table
    public static void createUsersTable(Connection connection) throws SQLException {
        String usersTableQuery = "CREATE TABLE IF NOT EXISTS Users ("
                + "userID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "nickname TEXT , " //can make unique
                + "ipAddress TEXT NOT NULL, "
                + "firstName TEXT, "
                + "lastName TEXT, "
                + "birthday INTEGER, "
                + "status INTEGER  NOT NULL,"
                + "password TEXT,"
                + "mySocket INTEGER,"
                + "theirSocket INTEGER"
                + ")";

        try (Statement statement = connection.createStatement()) {
            statement.execute(usersTableQuery);
        }
    }

    // Method to add a user to the Users table and create an empty specific Messages table for that user
    public static void addUser(Connection connection, User user) throws SQLException {
        String insertUserSQL = "INSERT INTO Users (nickname, ipAddress, firstName, lastName, birthday, status, password, mySocket, theirSocket) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String lastInsertIdSQL = "SELECT last_insert_rowid()";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertUserSQL)) {
            preparedStatement.setString(1, user.getNickname());
            preparedStatement.setString(2, String.valueOf(user.getIpAddress()));
            preparedStatement.setString(3, user.getFirstName());
            preparedStatement.setString(4, user.getLastName());
            preparedStatement.setInt(5, Integer.parseInt(user.getBirthday()));

            if (user.getStatus()) {
                preparedStatement.setInt(6, 1);
            } else {
                preparedStatement.setInt(6, 0);
            }
            preparedStatement.setString(7, user.getPassword());
            preparedStatement.setInt(8, user.getMySocket());
            preparedStatement.setInt(9, user.getTheirSocket());
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
