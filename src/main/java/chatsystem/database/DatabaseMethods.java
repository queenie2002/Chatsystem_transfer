package chatsystem.database;

import chatsystem.controller.Controller;
import chatsystem.network.TCPMessage;
import chatsystem.contacts.User;
import chatsystem.MainClass;
import org.apache.logging.log4j.*;
import java.net.*;
import java.sql.*;
import java.util.ArrayList;

/*
could close resources
deletecontact
 */


public class DatabaseMethods {

    private static final Logger LOGGER = LogManager.getLogger(MainClass.class);

    private static Connection connection;



    /** We start the connection and create users' table */
    public static void startConnection(User me) throws SQLException {
        String ipAddress = me.getIpAddress().getHostAddress().replace(".","_"); //we replace . by _
        String DATABASE_URL = "jdbc:sqlite:my_database_"+ipAddress+".db";
        connection = DriverManager.getConnection(DATABASE_URL);
        LOGGER.info("Created database with my IP address " + ipAddress);

        createUsersTable();
        createMeTable();
    }

    /** Checks if table exists in database */
    private static boolean doesTableExist(String tableName) throws SQLException {
        String sql = "SELECT name FROM sqlite_master WHERE type='table' AND name=?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, tableName);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                // If the result set has at least one row, the table exists
                return resultSet.next();
            }

        } catch (SQLException e) {
            LOGGER.error("Couldn't check if table exists in database" + tableName);
            throw new SQLException();
        }
    }

    /** Checks if a user is already in the database */
    private static boolean doesUserExist(User user) throws SQLException {

        String nickname= user.getNickname();
        String sql = "SELECT COUNT(*) FROM users WHERE nickname = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, nickname);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                // If the result set has at least one row, the user exists
                return resultSet.next() && resultSet.getInt(1) > 0;
            }

        } catch (SQLException e) {
            LOGGER.error("Couldn't check if user exists in database" + user);
            throw new SQLException();
        }
    }

    public static boolean doesMeExist() throws SQLException {
        String sql = "SELECT * FROM me";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                // If the result set has at least one row, the user exists
                return resultSet.next();
            }

        } catch (SQLException e) {
            LOGGER.error("I am not in the database");
            throw new SQLException();
        }
    }



    /** Replaces the . in an IP Address into _ to name tables in database */
    private static String convertIPAddressForDatabase(String ipAddress) {
        return ipAddress.replace(".","_");
    }


    /** Creates the users table in database */
    private static void createUsersTable() throws SQLException {
        String usersTableQuery = "CREATE TABLE IF NOT EXISTS Users ("
                + "idUserDatabase INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "nickname TEXT , " //can make unique
                + "ipAddress TEXT NOT NULL, "
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
    private static void createSpecificMessagesTable(String ipAddress) throws SQLException {
        ipAddress = ipAddress.substring(1).replace(".","_");

        String specificMessagesTableQuery = "CREATE TABLE IF NOT EXISTS Messages_" + ipAddress + " ("
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
            throw new SQLException();
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




    /** Adds a user and creates an empty specific Messages table for that user. If user already added, it updates the user */
    public static void addUser(User user) throws SQLException {

        String addUserSQL;
        boolean userAddedAlready = doesUserExist(user);

        if (userAddedAlready) {
            addUserSQL = "UPDATE users SET nickname = ?, ipAddress = ?, firstName = ?, lastName = ?, birthday = ?, status = ?, password = ? WHERE nickname = ?";
        }
        else {
            addUserSQL = "INSERT INTO Users (nickname, ipAddress, firstName, lastName, birthday, status, password) VALUES (?, ?, ?, ?, ?, ?, ?)";
        }

        String lastInsertIdSQL = "SELECT last_insert_rowid()";

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

            LOGGER.info("User added to database " + user.getNickname());

        } catch (SQLException e) {
            LOGGER.error("User couldn't be added to database " + user.getNickname());
            throw new SQLException();
        }


        // If it's a new user, we set the idDatabase
        if (!userAddedAlready) {
            // Fetch the last inserted ID
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(lastInsertIdSQL)) {
                if (resultSet.next()) {
                    long idDatabase = resultSet.getLong(1);
                    user.setIdDatabase((int) idDatabase);
                }
            } catch (SQLException e) {
                LOGGER.error("Couldn't get the user ID in database");
                throw new SQLException();
            }
        }

        //If it's another user, we create a specific message table
        if (!user.getIpAddress().equals(MainClass.me.getIpAddress())) {
            createSpecificMessagesTable(String.valueOf(user.getIpAddress()));
        }

    }

    /** Adds a message to database */
    public static void addMessage(TCPMessage tcpMessage) throws SQLException {

        boolean table1Exists = doesTableExist("Messages_" + convertIPAddressForDatabase(tcpMessage.getFromUserIP()));
        boolean table2Exists = doesTableExist("Messages_" + convertIPAddressForDatabase(tcpMessage.getToUserIP()));

        String tableName;

        if (table1Exists||table2Exists) {
            if (table1Exists) {
                tableName = "Messages_" + convertIPAddressForDatabase(tcpMessage.getFromUserIP());
            } else {
                tableName = "Messages_" + convertIPAddressForDatabase(tcpMessage.getToUserIP());
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
        }
        else {
            LOGGER.error("Table messages doesn't exist in database");
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







    /** Get user from database */
    public static User getUser(String nickname) throws SQLException, UnknownHostException {

        User user = new User();

        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Users WHERE nickname = ?")) {
            preparedStatement.setString(1, nickname);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {

                    // User found
                    user.setIdDatabase(resultSet.getInt("idUserDatabase"));
                    user.setNickname(resultSet.getString("nickname"));
                    user.setIpAddress(InetAddress.getByName(resultSet.getString("ipAddress").substring(1)));
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
                    LOGGER.error("User not found " + nickname);
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Couldn't get user due to SQLException" + nickname);
            throw new SQLException();
        } catch (UnknownHostException e) {
            LOGGER.error("Couldn't get user due to UnknownHostException " + nickname);
            throw new UnknownHostException();
        }

        return user;
    }

    /** Load users list from database */
    public static void loadUserList() throws UnknownHostException, SQLException {

        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Users")) {

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {

                    String nickname = resultSet.getString("nickname");
                    String ipAddress = resultSet.getString("ipAddress");
                    boolean status = false;
                    if (resultSet.getInt("status") == 0) {
                        status = false;
                    } else if (resultSet.getInt("status") == 1) {
                        status = true;
                    }
                    Controller.changeStatus(nickname, InetAddress.getByName(ipAddress.substring(1)), status);

                }
            } catch (SQLException e) {
                LOGGER.error("Couldn't get users list due to SQLException");
                throw new SQLException();
            }
        }
    }


    /** Get messages from user from database */
    public static ArrayList<TCPMessage> getMessagesList(String nickname) throws SQLException, UnknownHostException {

        ArrayList<TCPMessage> myMessagesList = new ArrayList<TCPMessage>();

        User user = getUser(nickname);
        String ipAddress = convertIPAddressForDatabase(user.getIpAddress().getHostAddress());

        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM messages_" + ipAddress)) {

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    while (resultSet.next()) {

                        TCPMessage message = new TCPMessage();

                        // User found
                        message.setChatId(resultSet.getInt("chatID"));
                        message.setContent(resultSet.getString("content"));
                        message.setDate(resultSet.getString("date"));
                        message.setFromUserIP(resultSet.getString("fromUser"));
                        message.setToUserIP(resultSet.getString("toUser"));

                        myMessagesList.add(message);
                        TCPMessage.printTCPMessageList(myMessagesList);
                    }
                } else {
                    LOGGER.error("We don't have any message from  " + nickname);
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Couldn't get messages due to SQLException" + nickname);
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

}
