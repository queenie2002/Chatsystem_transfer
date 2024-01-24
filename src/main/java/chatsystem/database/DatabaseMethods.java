package chatsystem.database;

import chatsystem.network.TCPMessage;
import chatsystem.contacts.User;
import chatsystem.MainClass;
import chatsystem.observers.MyObserver;

import org.apache.logging.log4j.*;
import java.io.File;
import java.net.*;
import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Methods to use database, we have to start the connection and initialize the database first*/
public class DatabaseMethods {

    //LOGGER
    private static final Logger LOGGER = LogManager.getLogger(DatabaseMethods.class);


    //OUR CONNECTION
    private static Connection connection;


    //OBSERVERS
    static ArrayList<MyObserver> observers = new ArrayList<>();

    public void addObserver(MyObserver obs) {
        observers.add(obs);
    }


    //HANDLES CONNECTION

    /**
     * We start the connection, create our database name my_database_+ our ip address
     */
    public static void startConnection(User me) {
        //we change the dots into underscore to not cause a problem with sql
        String ipAddress = changeDotsIntoUnderscore(me.getIpAddress());
        String DATABASE_URL = "jdbc:sqlite:my_database_" + ipAddress + ".db";

        try {
            //we try to get connection
            connection = DriverManager.getConnection(DATABASE_URL);
        } catch (SQLException e) {
            //if can't, we cannot do anything, so we throw runtime exception
            LOGGER.error("Couldn't start the connection for database: SQLException");
            throw new RuntimeException(e);
        }
        LOGGER.info("Created database with my IP address " + ipAddress);
    }

    /**
     * We close the connection
     */
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            LOGGER.error("Couldn't close the connection of database: SQLException");
            throw new RuntimeException(e);
        }
        LOGGER.trace("Closed the database.");
    }

    /**
     * We create users' and me table
     */
    public static void initializeDatabase() {
        createUsersTable();
        createMeTable();
    }

    /**
     * To delete database of user with ipAddress from repo chatsystem-rees-nguyen
     */
    public static void deleteDatabase(InetAddress ipAddress) {
        //we get the path to database file
        String filePath = "./my_database_" + changeDotsIntoUnderscore(extractFormattedIP(ipAddress)) + ".db";
        File fileToDelete = new File(filePath);

        //if the file exists
        if (fileToDelete.exists()) {
            //we delete it, returns a boolean but we ignore it
            boolean done = fileToDelete.delete();
            if (done) {
                LOGGER.info("Deleted my_database_" + changeDotsIntoUnderscore(extractFormattedIP(ipAddress)) + ".db");
            } else {
                LOGGER.error("Failed to delete my_database_" + changeDotsIntoUnderscore(extractFormattedIP(ipAddress)) + ".db");
            }
        }
    }

    /**
     * Returns the number of tables in database for tests
     */
    public static int numberTablesInDatabase() {
        String sql = "SELECT count(*) FROM sqlite_master WHERE type='table'";
        int tableCount = 0;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            if (resultSet.next()) {
                tableCount = resultSet.getInt(1);
            }

        } catch (SQLException e) {
            LOGGER.error("Couldn't get the number of tables in the database: SQLException");
        }
        return tableCount;
    }


    /**
     * Replaces the . in an IP Address into _ to name tables in database
     */
    private static String changeDotsIntoUnderscore(InetAddress ipAddress) {
        return ipAddress.getHostAddress().replace(".", "_");
    }

    /**
     * Extracts the ipAddress from InetAddress
     */
    private static InetAddress extractFormattedIP(InetAddress input) {
        //we're looking for an ipAddress and don't care if there is other information before
        String regex = ".*?(\\d+\\.\\d+\\.\\d+\\.\\d+)";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input.getHostAddress());

        while (matcher.find()) {
            try {
                //if we find a match, we return the ipAddress
                return InetAddress.getByName(matcher.group(1));
            } catch (UnknownHostException e) {
                LOGGER.error("Couldn't extract IP address: UnknownHostException");
                //we try to return input, hoping it might work
                return input;
            }
        }
        //if we didn't find a match, we throw and error
        LOGGER.error("Couldn't extract IP address: no match found.");
        return input;
    }


    //CHECKS IF SOMETHING EXISTS

    /**
     * Checks if table exists in database
     */
    public static boolean doesTableExist(String tableName) {
        //we're looking for the table named tableName
        String sql = "SELECT name FROM sqlite_master WHERE type='table' AND name=?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, tableName);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                // if there is something in result set, the table exists
                return resultSet.next();
            }

        } catch (SQLException e) {
            LOGGER.error("Couldn't check if table exists in database" + tableName + ": SQLException");
            throw new RuntimeException(e);
        }
    }

    /**
     * Checks if a user with given ip address is already in the database
     */
    public static boolean doesUserExist(InetAddress ipAddress) {
        //we get the ipAddress of user
        String ipAddressString = extractFormattedIP(ipAddress).getHostAddress();

        //to look for a user with the same ipAddress to see if we already know them
        String sql = "SELECT COUNT(*) FROM users WHERE ipAddress = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, ipAddressString);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                // if there is something in result set, the user exists
                return resultSet.next() && resultSet.getInt(1) > 0;
            }

        } catch (SQLException e) {
            LOGGER.error("Couldn't check if user exists in database: SQLException");
            throw new RuntimeException(e);
        }
    }

    /**
     * Checks if I am already in the database
     */
    public static boolean doesMeExist() {
        String sql = "SELECT * FROM me";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql); ResultSet resultSet = preparedStatement.executeQuery()) {
            // if there is something in result set, then I exist
            return resultSet.next();
        } catch (SQLException e) {
            LOGGER.error("I am not in the database: SQLException");
            throw new RuntimeException(e);
        }
    }


    //CREATE TABLES

    /**
     * Creates the users table in database
     */
    private static void createUsersTable() {
        String usersTableQuery = "CREATE TABLE IF NOT EXISTS Users ("
                + "nickname TEXT, " //can make unique
                + "ipAddress TEXT NOT NULL PRIMARY KEY, "
                + "status INT  NOT NULL,"
                + "password TEXT"
                + ")";

        try (Statement statement = connection.createStatement()) {
            statement.execute(usersTableQuery);
            LOGGER.info("Table users created");
        } catch (SQLException e) {
            LOGGER.error("Users Table couldn't be created in database: SQLException");
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates a specific empty messages table for a user with their IP Address
     */
    private static void createSpecificMessagesTable(InetAddress ipAddress) {
        String ipAddressString = changeDotsIntoUnderscore(extractFormattedIP(ipAddress));

        String specificMessagesTableQuery = "CREATE TABLE IF NOT EXISTS Messages_" + ipAddressString + " ("
                + "chatID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "content TEXT NOT NULL, "
                + "date TEXT NOT NULL, "
                + "fromUser TEXT NOT NULL, "
                + "toUser TEXT NOT NULL"
                + ")";

        try (Statement statement = connection.createStatement()) {
            statement.execute(specificMessagesTableQuery);
            LOGGER.info("Table messages created for " + ipAddress);
        } catch (SQLException e) {
            LOGGER.error("Table messages couldn't be created in database: SQLException");
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates the ME table in database
     */
    private static void createMeTable() {
        String usersTableQuery = "CREATE TABLE IF NOT EXISTS Me ("
                + "nickname TEXT , " //can make unique
                + "ipAddress TEXT NOT NULL, "
                + "password TEXT"
                + ")";

        try (Statement statement = connection.createStatement()) {
            statement.execute(usersTableQuery);
            LOGGER.info("Table Me created");
        } catch (SQLException e) {
            LOGGER.error("Users Me couldn't be created in database: SQLException");
            throw new RuntimeException(e);
        }
    }


    //ADDS SOMETHING TO TABLE

    /**
     * Adds a user and creates an empty specific Messages table for that user. If user already added, it updates the user
     */
    public static void addUser(User user) {

        String addUserSQL;

        //we check if the user is already in the list
        boolean userAddedAlready = doesUserExist(user.getIpAddress());

        //we do the appropriate sql
        if (userAddedAlready) {
            addUserSQL = "UPDATE users SET nickname = ?, ipAddress = ?, status = ?, password = ? WHERE ipAddress = ?";
        } else {
            addUserSQL = "INSERT INTO Users (nickname, ipAddress, status, password) VALUES ( ?, ?, ?, ?)";
        }

        try (PreparedStatement preparedStatement = connection.prepareStatement(addUserSQL)) {
            preparedStatement.setString(1, user.getNickname());
            preparedStatement.setString(2, user.getIpAddress().getHostAddress());
            if (user.getStatus()) {
                preparedStatement.setInt(3, 1); //true is 1
            } else {
                preparedStatement.setInt(3, 0); //false is 0
            }
            preparedStatement.setString(4, user.getPassword());
            if (userAddedAlready) {
                preparedStatement.setString(5, user.getIpAddress().getHostAddress());
            }
            preparedStatement.executeUpdate();

            LOGGER.trace("User added/updated to database " + user.getIpAddress());

        } catch (SQLException e) {
            LOGGER.error("User couldn't be added/updated to database " + user.getIpAddress() + ": SQLException");
            //if they couldn't be added, we let it go
        }


        //If it's another user, we create a specific message table
        if (!user.getIpAddress().equals(MainClass.me.getIpAddress())) {
            createSpecificMessagesTable(user.getIpAddress());
        }

    }

    /**
     * Adds a message to database
     */
    public static void addMessage(TCPMessage tcpMessage) {
        //we add TCPMessage I sent and received with one user to the messages table

        //we don't know if I sent it or received it, so we check both possible tables
        try {
            String tableName1 = "Messages_" + changeDotsIntoUnderscore(InetAddress.getByName(tcpMessage.getFromUserIP()));
            String tableName2 = "Messages_" + changeDotsIntoUnderscore(InetAddress.getByName(tcpMessage.getToUserIP()));


            boolean table1Exists = doesTableExist(tableName1);
            boolean table2Exists = doesTableExist(tableName2);

            String finalTableName;

            //we add the message to the right table
            if (table1Exists || table2Exists) {
                if (table1Exists) {
                    finalTableName = tableName1;
                } else {
                    finalTableName = tableName2;
                }

                String insertMessageSQL = "INSERT INTO " + finalTableName + " (content, date, fromUser, toUser) VALUES (?, ?, ?, ?)";

                try (PreparedStatement preparedStatement = connection.prepareStatement(insertMessageSQL)) {

                    preparedStatement.setString(1, tcpMessage.getContent());
                    preparedStatement.setString(2, tcpMessage.getTimestamp().toString());
                    preparedStatement.setString(3, tcpMessage.getFromUserIP());
                    preparedStatement.setString(4, tcpMessage.getToUserIP());
                    preparedStatement.executeUpdate();

                    LOGGER.trace("Message added to database");
                } catch (SQLException e) {
                    LOGGER.error("Couldn't add message to database: SQLException");
                    //if we couldn't add message, we let it go
                }
            } else {
                LOGGER.error("Table messages doesn't exist in database, couldn't add message.");
                throw new RuntimeException();
            }
        } catch (UnknownHostException e) {
            LOGGER.error("Couldn't retrieve ipAddress to addMessage: UnknownHostException");
            throw new RuntimeException(e);
        }
    }

    /**
     * Adds Me to database if I register or updates me if I change nickname
     */
    public static void addMe(User user) {

        String addMeSQL;
        boolean meAddedAlready = doesMeExist();

        if (meAddedAlready) {
            addMeSQL = "UPDATE me SET nickname = ?, ipAddress = ?, password = ? WHERE ipAddress = ?";
        } else {
            addMeSQL = "INSERT INTO Me (nickname, ipAddress, password) VALUES ( ?, ?, ?)";
        }

        try (PreparedStatement preparedStatement = connection.prepareStatement(addMeSQL)) {
            preparedStatement.setString(1, user.getNickname());
            preparedStatement.setString(2, user.getIpAddress().getHostAddress());
            preparedStatement.setString(3, user.getPassword());
            if (meAddedAlready) {
                preparedStatement.setString(4, user.getIpAddress().getHostAddress());

            }

            preparedStatement.executeUpdate();

            LOGGER.trace("Me added/updated to database " + user.getIpAddress());

        } catch (SQLException e) {
            LOGGER.error("Couldn't add me to database " + user.getIpAddress() + ": SQLException");
            throw new RuntimeException(e);
        }
    }


    //GET SOMETHING FROM TABLE

    /**
     * Get user from database
     */
    public static User getUser(InetAddress ipAddress) {

        User user = new User();

        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Users WHERE ipAddress = ?")) {
            preparedStatement.setString(1, ipAddress.getHostAddress());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {

                    user.setNickname(resultSet.getString("nickname"));
                    user.setIpAddress(extractFormattedIP(InetAddress.getByName(resultSet.getString("ipAddress"))));
                    if (resultSet.getInt("status") == 0) {
                        user.setStatus(false);
                    } else if (resultSet.getInt("status") == 1) {
                        user.setStatus(true);
                    } else {
                        LOGGER.error("Status should be 0 or 1 but was " + resultSet.getInt("status"));
                    }
                    user.setPassword(resultSet.getString("password"));

                } else {
                    LOGGER.error("User not found " + ipAddress);
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Couldn't get user " + ipAddress + ": SQLException");
            throw new RuntimeException();
        } catch (UnknownHostException e) {
            LOGGER.error("Couldn't get user " + ipAddress + ": UnknownHostException");
            throw new RuntimeException();
        }
        return user;
    }

    /**
     * Get messages from user from database
     */
    public static ArrayList<TCPMessage> getMessagesList(InetAddress ipAddress) {

        ArrayList<TCPMessage> myMessagesList = new ArrayList<TCPMessage>();

        String ipAddressString = changeDotsIntoUnderscore(extractFormattedIP(ipAddress));

        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Messages_" + ipAddressString)) {

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {

                    TCPMessage message = new TCPMessage();

                    message.setChatId(resultSet.getInt("chatID"));
                    message.setContent(resultSet.getString("content"));
                    message.setTimestamp(Instant.parse(resultSet.getString("date")));
                    message.setFromUserIP(resultSet.getString("fromUser"));
                    message.setToUserIP(resultSet.getString("toUser"));

                    myMessagesList.add(message);
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Couldn't get messages " + ipAddress + ": SQLException");
            throw new RuntimeException();
        }

        return myMessagesList;
    }

    /**
     * Get me from database
     */
    public static User getMe() {

        User user = new User();

        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Me")) {

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {

                    user.setNickname(resultSet.getString("nickname"));
                    user.setIpAddress(InetAddress.getByName(resultSet.getString("ipAddress")));
                    user.setPassword(resultSet.getString("password"));

                } else {
                    LOGGER.error("Couldn't find me in database: result set is empty");
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Couldn't get me: SQLException");
            throw new RuntimeException();
        } catch (UnknownHostException e) {
            LOGGER.error("Couldn't get me: UnknownHostException");
            throw new RuntimeException();
        }

        return user;
    }


    /**
     * Load users list from database into Contact List
     */
    public static void loadUserList() throws SQLException, UnknownHostException {

        //we get all users
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Users")) {

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {

                    //make a new user that takes this information
                    String nickname = resultSet.getString("nickname");
                    String ipAddress = resultSet.getString("ipAddress");
                    boolean status = resultSet.getInt("status") == 1;
                    User user = new User(nickname, "", status, extractFormattedIP(InetAddress.getByName(ipAddress)));

                    //and notify observers so they can update
                    for (MyObserver obs : observers) {
                        obs.contactAddedOrUpdated(user);
                    }
                }
            } catch (SQLException e) {
                LOGGER.error("Couldn't get users list: SQLException");
                throw new RuntimeException(e);
            }
        }
    }


}