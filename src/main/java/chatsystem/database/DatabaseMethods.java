package chatsystem.database;

import chatsystem.network.TCPMessage;
import chatsystem.contacts.User;
import chatsystem.MainClass;
import org.apache.logging.log4j.*;
import java.net.*;
import java.sql.*;

/*
could close resources
query
update

find length of a tale
delete table

deletecontact

print contact
get contact
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
    }

    /** Checks if table exists in database */
    private static boolean doesTableExist(String tableName) {
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
    private static boolean doesUserExist(User user) {

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
            return false;
        }
    }

    /** Replaces the . in an IP Address into _ to name tables in database */
    private static String convertIPAddressForDatabase(String ipAddress) {
        return ipAddress.replace(".","_");
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




    /** Creates the users table in database */
    private static void createUsersTable() {
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
        }
    }

    /** Creates a specific empty messages table for a user with their IP Address*/
    private static void createSpecificMessagesTable(String ipAddress) {
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
        }
    }


    /** Adds a user and creates an empty specific Messages table for that user. If user already added, it updates the user */
    public static void addUser(User user) {

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
            //Test

            System.out.println("my address" +MainClass.me.getIpAddress().getHostAddress());
            //TCPMessage aTCPMessage = new TCPMessage("bonjour", "2002", "10.1.5.156", MainClass.me.getIpAddress().getHostAddress());
            //addMessage(aTCPMessage);
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
            }
        }

        //If it's another user, we create a specific message table
        if (!user.getIpAddress().equals(MainClass.me.getIpAddress())) {
            createSpecificMessagesTable(String.valueOf(user.getIpAddress()));


            //TEST
            System.out.println("we try to add a message");
            TCPMessage aTCPMessage = new TCPMessage("bonjour", "2002", "10.1.5.12", MainClass.me.getIpAddress().getHostAddress());
            addMessage(aTCPMessage);

        }
    }

    /** Adds a message to database */
    public static void addMessage(TCPMessage tcpMessage) {

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
            }
        }
        else {
            LOGGER.error("Table messages doesn't exist in database");
        }
    }

    /** Get user from database */
    private static User getUser(String nickname) {

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
        } catch (UnknownHostException e) {
            LOGGER.error("Couldn't get user due to UnknownHostException " + nickname);
        }

        return user;
    }
}
