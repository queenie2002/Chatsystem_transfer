package controller;

import java.sql.*;

public class DatabaseMethods {

    public static String url = "jdbc:sqlite:my_database.db";

    public static void tableUsers() throws SQLException {
        try (Connection connection = DriverManager.getConnection(url);
             Statement statement = connection.createStatement()) {

            String usersTableQuery = "CREATE TABLE IF NOT EXISTS Users ("
                    + "ipAddress TEXT PRIMARY KEY AUTOINCREMENT, "
                    + "name TEXT NOT NULL, "
                    + "status INTEGER NOT NULL)";

            statement.execute(usersTableQuery);
            System.out.println("Table Users created");
        }
    }

    public static void tableMessages(String tableName) throws SQLException {

        try (Connection connection = DriverManager.getConnection(url);
             Statement statement = connection.createStatement()) {

            String messagesTableQuery = "CREATE TABLE IF NOT EXISTS Messages_" + tableName + " ("
                    + "messageID INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "content TEXT NOT NULL, "
                    + "date TEXT NOT NULL)";

            statement.execute(messagesTableQuery);
            System.out.println("Table Messages_" + tableName + " created");
        }
    }

    public static void addUser(String ipAddress, String name, int status) throws SQLException {
        String insertUserSQL = "INSERT INTO Users (ipAddress, name, status) VALUES (?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(insertUserSQL)) {
            preparedStatement.setString(1, ipAddress);
            preparedStatement.setString(2, name);
            preparedStatement.setInt(3, status);

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("User added successfully");
            } else {
                System.out.println("User could not be added");
            }
        }
    }

    public static void addMessage(String tableName, String content, String date) throws SQLException {

        String insertMessageSQL = "INSERT INTO Messages_" + tableName + " (content, date) VALUES (?, ?)";

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(insertMessageSQL)) {
            preparedStatement.setString(1, content);
            preparedStatement.setString(2, date);

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Message added successfully to table Messages_" + tableName);
            } else {
                System.out.println("Message could not be added to table Messages_" + tableName);
            }
        }
    }

}
