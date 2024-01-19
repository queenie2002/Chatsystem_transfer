package chatsystem.database;

import chatsystem.network.TCPMessage;
import chatsystem.contacts.User;
import chatsystem.MainClass;
import chatsystem.observers.MyObserver;
import org.apache.logging.log4j.*;

import java.io.File;
import java.net.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Methods to use database */
public class DatabaseMethods {

    //LOGGER
    private static final Logger LOGGER = LogManager.getLogger(DatabaseMethods.class);


    //OUR CONNECTION
    private static Connection connection;


    //OBSERVERS
    static List<MyObserver> observers = new ArrayList<>();
    public void addObserver(MyObserver obs) {
        observers.add(obs);
    }




    //HANDLES CONNECTION

    /** We start the connection, create our database and create users' and me table */
    public static void startConnection(User me) throws SQLException {
        String ipAddress = changeDotsIntoUnderscore(me.getIpAddress());
        String DATABASE_URL = "jdbc:sqlite:my_database_"+ipAddress+".db";
        connection = DriverManager.getConnection(DATABASE_URL);
        LOGGER.info("Created database with my IP address " + ipAddress);
    }

    /** We close the connection */
    public static void closeConnection()  {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            LOGGER.error("Couldn't close the database.");
            throw new RuntimeException(e);
        }
        LOGGER.info("Closed the database.");

    }

    /** We start the connection, create our database and create users' and me table */
    public static void initializeDatabase() throws SQLException {

        createUsersTable();
        createMeTable();
    }

    /** To delete database from class in tests or in packages contacts/controller/database/network/observers/ui returns true if deleted the file */
    public static void deleteDatabase(InetAddress ipAddress) throws UnknownHostException {

        String filePath = "./my_database_"+changeDotsIntoUnderscore(extractFormattedIP(ipAddress))+".db";
        File fileToDelete = new File(filePath);

        if (fileToDelete.exists()) {
            fileToDelete.delete();
        }
    }

    public static int numberTablesInDatabase() {
        String sql = "SELECT count(*) FROM sqlite_master WHERE type='table'";
        int tableCount = 0;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            if (resultSet.next()) {
                tableCount = resultSet.getInt(1);
            }

        } catch (SQLException e) {
            LOGGER.error("Couldn't get the count of tables in the database");
        }
        return tableCount;
    }






    /** Replaces the . in an IP Address into _ to name tables in database */
    private static String changeDotsIntoUnderscore(InetAddress ipAddress) {
        return ipAddress.getHostAddress().replace(".","_");
    }

    private static InetAddress extractFormattedIP(InetAddress input) throws UnknownHostException {
        String regex = ".*?(\\d+\\.\\d+\\.\\d+\\.\\d+)";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input.getHostAddress());


        while (matcher.find()) {
            return InetAddress.getByName(matcher.group(1));
        }
        throw new RuntimeException("Coudln't extract IP address");
    }




    //CHECKS IF SOMETHING EXISTS

    /** Checks if table exists in database */
    public static boolean doesTableExist(String tableName) {
        String sql = "SELECT name FROM sqlite_master WHERE type='table' AND name=?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, tableName);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                // If the result set has at least one row, the table exists
                return resultSet.next();
            }

        } catch (SQLException e) {
            LOGGER.error("Couldn't check if table exists in database" + tableName);
            return false;
        }
    }

    /** Checks if a user is already in the database */
    public static boolean doesUserExist(User user) throws SQLException, UnknownHostException {

        String ipAddress= extractFormattedIP(user.getIpAddress()).getHostAddress();
        String sql = "SELECT COUNT(*) FROM users WHERE ipAddress = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, ipAddress);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                // If the result set has at least one row, the user exists
                return resultSet.next() && resultSet.getInt(1) > 0;
            }

        } catch (SQLException e) {

            LOGGER.error("Couldn't check if user exists in database");
            throw new SQLException();
        }
    }

    /** Checks if I am already in the database */
    public static boolean doesMeExist()  {
        String sql = "SELECT * FROM me";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                // If the result set has at least one row, the user exists
                return resultSet.next();
            }

        } catch (SQLException e) {
            LOGGER.error("I am not in the database");
            return false;
        }
    }







    //CREATE TABLES

    /** Creates the users table in database */
    private static void createUsersTable() throws SQLException {
        String usersTableQuery = "CREATE TABLE IF NOT EXISTS Users ("
                + "nickname TEXT, " //can make unique
                + "ipAddress TEXT NOT NULL PRIMARY KEY, "
                + "firstName TEXT, "
                + "lastName TEXT, "
                + "birthday TEXT, "
                + "status INT  NOT NULL,"
                + "password TEXT"
                + ")";

        try (Statement statement = connection.createStatement()) {
            statement.execute(usersTableQuery);
            LOGGER.info("Table users created");
        } catch (SQLException e) {
            LOGGER.error("Users Table couldn't be created in database");
            throw new SQLException();
        }
    }

    /** Creates a specific empty messages table for a user with their IP Address*/
    private static void createSpecificMessagesTable(InetAddress ipAddress) throws SQLException, UnknownHostException {
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
        }
        catch (SQLException e) {
            LOGGER.error("Table messages couldn't be created in database");
            e.printStackTrace();
            throw new SQLException(e);
        }
    }

    /** Creates the ME table in database */
    private static void createMeTable() throws SQLException {
        String usersTableQuery = "CREATE TABLE IF NOT EXISTS Me ("
                + "nickname TEXT , " //can make unique
                + "ipAddress TEXT NOT NULL, "
                + "firstName TEXT, "
                + "lastName TEXT, "
                + "birthday TEXT, "
                + "password TEXT"
                + ")";

        try (Statement statement = connection.createStatement()) {
            statement.execute(usersTableQuery);
            LOGGER.info("Table Me created");
        } catch (SQLException e) {
            LOGGER.error("Users Me couldn't be created in database");
            throw new SQLException();
        }
    }






    //ADDS SOMETHING TO TABLE

    /** Adds a user and creates an empty specific Messages table for that user. If user already added, it updates the user */
    public static void addUser(User user) throws SQLException, UnknownHostException {

        String addUserSQL;
        boolean userAddedAlready = doesUserExist(user);

        if (userAddedAlready) {
            addUserSQL = "UPDATE users SET nickname = ?, ipAddress = ?, firstName = ?, lastName = ?, birthday = ?, status = ?, password = ? WHERE nickname = ?";
            LOGGER.info("User updated to database " + user.getIpAddress());
        }
        else {
            addUserSQL = "INSERT INTO Users (nickname, ipAddress, firstName, lastName, birthday, status, password) VALUES (?, ?, ?, ?, ?, ?, ?)";
            LOGGER.info("User added to database " + user.getIpAddress());
        }

        try (PreparedStatement preparedStatement = connection.prepareStatement(addUserSQL)) {
            preparedStatement.setString(1, user.getNickname());
            preparedStatement.setString(2, String.valueOf(user.getIpAddress()));
            preparedStatement.setString(3, user.getFirstName());
            preparedStatement.setString(4, user.getLastName());
            preparedStatement.setString(5, user.getBirthday());
            if (user.getStatus()) {
                preparedStatement.setInt(6, 1); //true is 1
            } else {
                preparedStatement.setInt(6, 0); //false is 0
            }
            preparedStatement.setString(7, user.getPassword());
            preparedStatement.executeUpdate();

            LOGGER.info("User added/updated to database " + user.getIpAddress());

        } catch (SQLException e) {
            LOGGER.error("User couldn't be added to database " + user.getIpAddress());
            throw new SQLException();
        }


        //If it's another user, we create a specific message table
        if (!user.getIpAddress().equals(MainClass.me.getIpAddress())) {
            createSpecificMessagesTable(user.getIpAddress());
        }

    }

    /** Adds a message to database */
    public static boolean addMessage(TCPMessage tcpMessage) throws SQLException, UnknownHostException {

        boolean table1Exists = doesTableExist("Messages_" + changeDotsIntoUnderscore(InetAddress.getByName(tcpMessage.getFromUserIP())));
        boolean table2Exists = doesTableExist("Messages_" + changeDotsIntoUnderscore(InetAddress.getByName(tcpMessage.getToUserIP())));

        String tableName;

        if (table1Exists||table2Exists) {
            if (table1Exists) {
                tableName = "Messages_" + changeDotsIntoUnderscore(InetAddress.getByName(tcpMessage.getFromUserIP()));
            } else {
                tableName = "Messages_" + changeDotsIntoUnderscore(InetAddress.getByName(tcpMessage.getToUserIP()));
            }

            String insertMessageSQL = "INSERT INTO " + tableName + " (content, date, fromUser, toUser) VALUES (?, ?, ?, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(insertMessageSQL)) {

                preparedStatement.setString(1, tcpMessage.getContent());
                preparedStatement.setString(2, tcpMessage.getDate());
                preparedStatement.setString(3, tcpMessage.getFromUserIP());
                preparedStatement.setString(4, tcpMessage.getToUserIP());
                preparedStatement.executeUpdate();

                LOGGER.info("Message added to database");
            }
            catch (SQLException e){
                LOGGER.error("Couldn't add message to database");
                throw new SQLException();
            }
            return true;
        }
        else {
            LOGGER.error("Table messages doesn't exist in database");
            return false;
        }
    }

    /** Adds Me to database if I register*/
    public static void addMe(User user) throws SQLException {

        String insertMessageSQL = "INSERT INTO Me (nickname, ipAddress, firstName, lastName, birthday, password) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertMessageSQL)) {

            preparedStatement.setString(1, user.getNickname());
            preparedStatement.setString(2, user.getIpAddress().getHostAddress());
            preparedStatement.setString(3, user.getFirstName());
            preparedStatement.setString(4, user.getLastName());
            preparedStatement.setString(5, user.getBirthday());
            preparedStatement.setString(6, user.getPassword());
            preparedStatement.executeUpdate();

            LOGGER.info("Me added to database");
        }
        catch (SQLException e){
            LOGGER.error("Couldn't add Me to database");
            throw new SQLException();
        }
    }







    //GET SOMETHING FROM TABLE

    /** Get user from database */
    public static User getUser(InetAddress ipAddress) throws SQLException, UnknownHostException {

        User user = new User();

        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Users WHERE ipAddress = ?")) {
            preparedStatement.setString(1, ipAddress.getHostAddress());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {

                    // User found
                    user.setNickname(resultSet.getString("nickname"));
                    user.setIpAddress(extractFormattedIP(InetAddress.getByName(resultSet.getString("ipAddress"))));
                    user.setFirstName(resultSet.getString("firstName"));
                    user.setLastName(resultSet.getString("lastName"));
                    user.setBirthday(resultSet.getString("birthday"));
                    if (resultSet.getInt("status")==0) {
                        user.setStatus(false);
                    } else if  (resultSet.getInt("status")==1){
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
            LOGGER.error("Couldn't get user due to SQLException" + ipAddress);
            throw new SQLException();
        } catch (UnknownHostException e) {
            LOGGER.error("Couldn't get user due to UnknownHostException " + ipAddress);
            throw new UnknownHostException();
        }

        return user;
    }

    /** Get messages from user from database */
    public static ArrayList<TCPMessage> getMessagesList(InetAddress ipAddress) throws SQLException, UnknownHostException {

        ArrayList<TCPMessage> myMessagesList = new ArrayList<TCPMessage>();

        String ipAddressString = changeDotsIntoUnderscore(extractFormattedIP(ipAddress));

        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Messages_" + ipAddressString)) {

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {

                    TCPMessage message = new TCPMessage();

                    // User found
                    message.setChatId(resultSet.getInt("chatID"));
                    message.setContent(resultSet.getString("content"));
                    message.setDate(resultSet.getString("date"));
                    message.setFromUserIP(resultSet.getString("fromUser"));
                    message.setToUserIP(resultSet.getString("toUser"));

                    myMessagesList.add(message);
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Couldn't get messages due to SQLException" + ipAddress);
            throw new SQLException();
        }

        return myMessagesList;
    }

    /** Get me from database */
    public static User getMe() throws UnknownHostException, SQLException {

        User user = new User();

        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Me")) {

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {

                    // User found
                    user.setNickname(resultSet.getString("nickname"));
                    user.setIpAddress(InetAddress.getByName(resultSet.getString("ipAddress")));
                    user.setFirstName(resultSet.getString("firstName"));
                    user.setLastName(resultSet.getString("lastName"));
                    user.setBirthday(resultSet.getString("birthday"));
                    user.setPassword(resultSet.getString("password"));

                } else {
                    LOGGER.error("Couldn't find me");
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Couldn't get me due to SQLException");
            throw new SQLException();
        }
        catch (UnknownHostException e) {
            LOGGER.error("Couldn't get me due to UnknownHostException");
            throw new UnknownHostException();
        }

        return user;
    }





    /** Load users list from database into Contact List */
    public static void loadUserList() throws UnknownHostException, SQLException {

        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Users")) {

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {

                    String nickname = resultSet.getString("nickname");
                    String ipAddress = resultSet.getString("ipAddress");
                    boolean status = resultSet.getInt("status") == 1;
                    User user = new User(nickname, "", "", "", "", status, extractFormattedIP(InetAddress.getByName(ipAddress)));

                    for (MyObserver obs : observers) {
                        obs.newContactAdded(user);
                    }
                }
            } catch (SQLException e) {
                LOGGER.error("Couldn't get users list due to SQLException");
                throw new SQLException();
            }
        }
    }







}
