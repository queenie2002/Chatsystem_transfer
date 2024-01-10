package controller;

import model.*;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.*;
import java.util.Enumeration;

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
    public static void startConnection(User user) throws SQLException, UnknownHostException, SocketException {
        //start connection and create database for user



        Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
        if(e.hasMoreElements())
        {
            NetworkInterface n = (NetworkInterface) e.nextElement();
            Enumeration<InetAddress> ee = n.getInetAddresses();
            if (ee.hasMoreElements())
            {
                InetAddress i = (InetAddress) ee.nextElement();
                user.setIpAddress(InetAddress.getByName(i.getHostAddress()));
            }
        }


        String ipAddress = String.valueOf(user.getIpAddress());
        ipAddress = ipAddress.substring(1).replace(".","_"); //we take out the / and replace . by _
        System.out.println(DATABASE_URL+ipAddress+".db");
        connection = DriverManager.getConnection(DATABASE_URL+ipAddress+".db");
        createUsersTable();
    }


    public static boolean doesTableExist(String tableName) {
        String sql = "SELECT name FROM sqlite_master WHERE type='table' AND name=?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, tableName);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                // If the result set has at least one row, the table exists
                return resultSet.next();
            }

        } catch (SQLException e) {
            e.printStackTrace();  // Handle or log the exception as needed
            return false;  // Assuming false if an exception occurs
        }
    }


    // Method to create the Users table
    public static void createUsersTable() throws SQLException {
        String usersTableQuery = "CREATE TABLE IF NOT EXISTS Users ("
                + "idUserDatabase INTEGER PRIMARY KEY AUTOINCREMENT, "
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

            createSpecificMessagesTable(String.valueOf(user.getIpAddress()));


            // Fetch the last inserted ID
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(lastInsertIdSQL)) {
                if (resultSet.next()) {
                    long idDatabase = resultSet.getLong(1);

                    user.setIdDatabase((int) idDatabase);
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
    private static void createSpecificMessagesTable(String ipAddress) throws SQLException {

        ipAddress = ipAddress.substring(1).replace(".","_");
        String specificMessagesTableQuery = "CREATE TABLE IF NOT EXISTS Messages_" + ipAddress + " ("
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

        boolean table1Exists = doesTableExist("Messages_" + chat.getFromUserIP().substring(1).replace(".","_"));
        boolean table2Exists = doesTableExist("Messages_" + chat.getToUserIP().substring(1).replace(".","_"));

        String tableName = "";

        if (table1Exists||table2Exists) {
            if (table1Exists) {
                tableName = "Messages_" + chat.getFromUserIP();
            } else {
                tableName = "Messages_" + chat.getToUserIP();
            }

            String insertMessageSQL = "INSERT INTO " + tableName + " (content, date) VALUES (?, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(insertMessageSQL)) {
                preparedStatement.setString(1, chat.getContent());
                preparedStatement.setString(2, chat.getDate());
                preparedStatement.executeUpdate();
            }
        }
        else {
            throw new RuntimeException("Table doesn't exist in AddMessage");
        }
    }




    //HAVE TO DO THIS
/*
    public static void findIdDatabaseFromIP(InetAddress ipAddress) throws SQLException {
        String sql = "SELECT idUserDatabase FROM users WHERE ipAddress = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, String.valueOf(ipAddress));

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                // If the result set has at least one row, get the ID
                if (resultSet.next()) {
                    return resultSet.getInt("idUserDatabase");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();  // Handle or log the exception as needed
        }

        return -1;  // Return -1 if the IP address is not found or an error occurs
    }
*/

}
