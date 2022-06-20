package utils;

import model.Flat;

import java.sql.*;
import java.util.Properties;

public class DatabaseManager {
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


    public static Integer insertFlat(Flat flat, int userId) throws SQLException {

        String INSERT_FLAT = "INSERT INTO flats (id, user_id, flat_name, coor_x, coor_y, init_date, area, number_of_rooms, furnish, flat_view, transport, house_name, house_age, house_floor) " +
                "Values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                "RETURNING id";
        PreparedStatement preparedStatement = connection.prepareStatement(INSERT_FLAT);

        connection.setAutoCommit(false);

        if (flat.getHouse() != null) {

            preparedStatement.setString(12, flat.getHouse().getName());
            preparedStatement.setLong(13, flat.getHouse().getYear());
            preparedStatement.setDouble(14, flat.getHouse().getNumberOfFlatsOnFloor());
        } else {
            preparedStatement.setNull(12, Types.CHAR);
            preparedStatement.setNull(13, Types.BIGINT);
            preparedStatement.setNull(14, Types.DOUBLE);
        }

        preparedStatement.setInt(1, flat.getId());
        preparedStatement.setInt(2, userId);
        preparedStatement.setString(3, flat.getName());
        preparedStatement.setFloat(4, flat.getCoordinates().getX());
        preparedStatement.setDouble(5, flat.getCoordinates().getY());
        preparedStatement.setTimestamp(6, Timestamp.from(flat.getCreationDate().toInstant()));
        preparedStatement.setDouble(7, flat.getArea());
        preparedStatement.setInt(8, flat.getNumberOfRooms());
        preparedStatement.setInt(9, flat.getFurnish().ordinal());
        preparedStatement.setInt(10, flat.getView().ordinal());
        preparedStatement.setInt(11, flat.getTransport().ordinal());

        preparedStatement.execute();

        ResultSet last_updated_ticket = preparedStatement.getResultSet();
        last_updated_ticket.next();
        return last_updated_ticket.getInt(1);

    }

}
