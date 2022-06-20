package utils;

import java.sql.*;
import java.util.Properties;

public class DatabaseHandler {
    private static final Properties properties = PropertiesManager.getProperties();
    private static final String url = properties.getProperty("urlLocal");
    private static final String username = properties.getProperty("username");
    private static final String password = properties.getProperty("password");
    private static Connection connection;


    //    TODO исправить тут русский текст

    public static void connectionToDataBase() {
        try {
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("подключение установлено");
        } catch (SQLException e) {
            System.out.println("ошибка при подключении к базе");
            e.printStackTrace();
        }
    }

    // TODO добить везде причины исключений где это будет вызываться
    // TODO если уже существует сделать что-то PSQLException

    public static void registerUser(String login, String password) throws SQLException {
        String ADD_USER = "INSERT INTO users (login, password) VALUES (?, ?)";
        PreparedStatement addStatement = connection.prepareStatement(ADD_USER);
        addStatement.setString(1, login);
        addStatement.setString(2, password);
        addStatement.executeUpdate();
        addStatement.close();
    }

    public static Integer findUser(String login, String password) throws SQLException {
        String FIND_USER = "SELECT id FROM users WHERE login = ? AND password = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(FIND_USER);
        preparedStatement.setString(1, login);
        preparedStatement.setString(2, password);
        ResultSet resultSet = preparedStatement.executeQuery();
        Integer id = null;
        if (resultSet.next()) {
            id = resultSet.getObject(1, Integer.class);
        }
        return id;
    }

}
