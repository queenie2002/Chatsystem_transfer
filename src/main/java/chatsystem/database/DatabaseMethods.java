package chatsystem.database;

import chatsystem.network.TCPMessage;
import chatsystem.contacts.User;
import chatsystem.MainClass;

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



    private static String DATABASE_URL;
    private static Connection connection;

    // We start the connection and create users' table
    public static void startConnection(User user) throws SQLException {
        //start connection and create database for user
        String ipAddress = user.getIpAddress().getHostAddress().replace(".","_"); //we replace . by _
        DATABASE_URL = "jdbc:sqlite:my_database_"+ipAddress+".db";
        connection = DriverManager.getConnection(DATABASE_URL);

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

    /*
    public static void dropTables() throws SQLException {

        //to get list of tables
        DatabaseMetaData metaData = connection.getMetaData();
        // Specify the types of database objects to include in the result set (in this case, tables)
        String[] types = {"TABLE"};
        // Get the result set containing table information
        ResultSet resultSet = metaData.getTables(null, null, "%", types);

        // Iterate through the result set and print table names
        while (resultSet.next()) {
            String tableName = resultSet.getString("TABLE_NAME");

            try (Statement statement = connection.createStatement()) {

                // Execute the DROP TABLE statement
                String dropTableSql = "DROP TABLE " + tableName;
                statement.executeUpdate(dropTableSql);
                System.out.println();
                System.out.println();
                System.out.println();
                System.out.println("Table '" + tableName + "' dropped successfully.");
                System.out.println();
                System.out.println();


            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }
*/




    // Method to create the Users table
    public static void createUsersTable() throws SQLException {
        String usersTableQuery = "CREATE TABLE IF NOT EXISTS Users ("
                + "idUserDatabase INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "nickname TEXT , " //can make unique
                + "ipAddress TEXT NOT NULL, "
                + "firstName TEXT, "
                + "lastName TEXT, "
                + "birthday TEXT, "
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


        System.out.println("my birthday " + user.getBirthday());

        try (PreparedStatement preparedStatement = connection.prepareStatement(addUserSQL)) {
            preparedStatement.setString(1, user.getNickname());
            preparedStatement.setString(2, String.valueOf(user.getIpAddress()));
            preparedStatement.setString(3, user.getFirstName());
            preparedStatement.setString(4, user.getLastName());
            preparedStatement.setString(5, user.getBirthday());

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

        if (!user.getIpAddress().equals(MainClass.me.getIpAddress())) {
            System.out.println("ip address for specific table" + user.getIpAddress());
            createSpecificMessagesTable(String.valueOf(user.getIpAddress()));

            //Test

            System.out.println("my address" +MainClass.me.getIpAddress().getHostAddress());
            TCPMessage aTCPMessage = new TCPMessage("bonjour", "2002", "10.1.5.156", MainClass.me.getIpAddress().getHostAddress());
            addMessage(aTCPMessage);
        }



        if (!userAddedAlready) {

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

        System.out.println( "Messages_" + ipAddress) ;
        String specificMessagesTableQuery = "CREATE TABLE IF NOT EXISTS Messages_" + ipAddress + " ("
                + "chatID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "content TEXT NOT NULL, "
                + "date TEXT NOT NULL, "
                + "fromUser TEXT NOT NULL, "
                + "toUser TEXT NOT NULL"
                + ")";

        try (Statement statement = connection.createStatement()) {
            statement.execute(specificMessagesTableQuery);
        }
        catch (SQLException e) {
            System.out.println("DATABASE METHODS: narrive pas a creer table");
            /*throw new RuntimeException("narrive pas a creer table", e);*/

        }
    }

    // Method to add a message to the specific Messages table of a user
    public static void addMessage(TCPMessage TCPMessage) throws SQLException {

        System.out.println("Checking if table exists: Messages_" + TCPMessage.getFromUserIP().replace(".","_"));
        System.out.println("Checking if table exists: Messages_" + TCPMessage.getToUserIP().replace(".","_"));


        boolean table1Exists = doesTableExist("Messages_" + TCPMessage.getFromUserIP().replace(".","_"));
        boolean table2Exists = doesTableExist("Messages_" + TCPMessage.getToUserIP().replace(".","_"));

        System.out.println("table1: " + table1Exists);
        System.out.println("table2: " + table2Exists);

        String tableName;

        if (table1Exists||table2Exists) {
            if (table1Exists) {
                tableName = "Messages_" + TCPMessage.getFromUserIP();
            } else {
                tableName = "Messages_" + TCPMessage.getToUserIP();
            }

            String insertMessageSQL = "INSERT INTO " + tableName + " (content, date, fromUser, toUser) VALUES (?, ?, ?, ?)";

            System.out.println("content" + TCPMessage.getContent());
            System.out.println("date" + TCPMessage.getDate());
            System.out.println("fromuser" + "10_2_2_2"/*TCPMessage.getFromUserIP()*/);
            System.out.println("touser" + "10_2_2_2"/*TCPMessage.getToUserIP()*/);
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertMessageSQL)) {

                preparedStatement.setString(1, TCPMessage.getContent());

                preparedStatement.setString(2, TCPMessage.getDate());

                preparedStatement.setString(3, TCPMessage.getFromUserIP());

                preparedStatement.setString(4, TCPMessage.getToUserIP());
                preparedStatement.executeUpdate();
            }
            catch (SQLException e){
                e.printStackTrace();
            }
        }
        else {
            System.out.println("DATABASE METHODS: Table doesn't exist in AddMessage");
            /*throw new RuntimeException("Table doesn't exist in AddMessage");*/
        }
    }

}
