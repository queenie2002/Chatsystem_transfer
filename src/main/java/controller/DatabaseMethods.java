package controller;

import model.*;

import java.net.InetAddress;
import java.sql.*;

/*
could close resources
query
update

find length of a tale
delete table

deletecontact

removeuser
updateuser
print contact
get contact


 */


public class DatabaseMethods {

    private static final String DATABASE_URL = "jdbc:sqlite:my_database_";
    private static Connection connection;

    // Method to start the database connection
    public static void startConnection(User user) throws SQLException {
        String IPAddress = String.valueOf(user.getIpAddress());
        IPAddress = IPAddress.substring(1).replace(".","_"); //we take out the / a,d replace . by _
        connection = DriverManager.getConnection(DATABASE_URL+IPAddress+".db");
        createUsersTable();
    }

    // Method to create the Users table
    public static void createUsersTable() throws SQLException {
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

    // Method to add a user to the Users table and create an empty specific Messages table for that user. If user already added, it updates the user
    public static void addUser(User user) throws SQLException {

        String addUserSQL;
        Boolean userAddedAlready = doesUserExist(user);

        if (userAddedAlready) {
            addUserSQL = "UPDATE users SET nickname = ?, ipAddress = ?, firstName = ?, lastName = ?, birthday = ?, status = ?, password = ?, mySocket = ?, theirSocket = ? WHERE nickname = ?";
        }
        else {
            addUserSQL = "INSERT INTO Users (nickname, ipAddress, firstName, lastName, birthday, status, password, mySocket, theirSocket) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        }

        String lastInsertIdSQL = "SELECT last_insert_rowid()";

        try (PreparedStatement preparedStatement = connection.prepareStatement(addUserSQL)) {
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
        }


        if (!userAddedAlready) {
            // Fetch the last inserted ID
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(lastInsertIdSQL)) {
                if (resultSet.next()) {
                    long idDatabase = resultSet.getLong(1);

                    user.setIdDatabase((int) idDatabase);

                    createSpecificMessagesTable(idDatabase); // Create an empty Messages table for this user
                }
            }
        }
    }



    public static boolean doesUserExist(User user) {

        String nickname= user.getNickname();
        String sql = "SELECT COUNT(*) FROM users WHERE nickname = ?";


        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, nickname);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                // If the result set has at least one row, the user exists
                return resultSet.next() && resultSet.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();  // Handle or log the exception as needed
            return false;  // Assuming false if an exception occurs
        }
    }




    // Method to create a specific empty Messages table for a user
    private static void createSpecificMessagesTable(long idDatabase) throws SQLException {
        String specificMessagesTableQuery = "CREATE TABLE IF NOT EXISTS Messages_" + idDatabase + " ("
                + "chatID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "content TEXT NOT NULL, "
                + "date TEXT NOT NULL"
                + "fromUser INT NOT NULL"
                + "toUser INT NOT NULL"
                + ")";

        try (Statement statement = connection.createStatement()) {
            statement.execute(specificMessagesTableQuery);
        }
    }

    // Method to add a message to the specific Messages table of a user
    public static void addMessage(Chat chat) throws SQLException {
        String tableName = "Messages_" + idDatabase;
        String insertMessageSQL = "INSERT INTO " + tableName + " (content, date) VALUES (?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertMessageSQL)) {
            preparedStatement.setString(1, content);
            preparedStatement.setString(2, date);
            preparedStatement.executeUpdate();
        }
    }
}
