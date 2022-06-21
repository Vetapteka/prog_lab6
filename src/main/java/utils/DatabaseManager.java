package utils;

import model.*;

import java.sql.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Set;

public class DatabaseManager {
    private static final Properties properties = PropertiesManager.getProperties();
    private static final String url = properties.getProperty("urlLocal");
    private static final String username = properties.getProperty("username");
    private static final String password = properties.getProperty("password");
    private static Connection connection;


    //    TODO исправить тут русский текст

    public static void connectionToDataBase() throws SQLException {
        connection = DriverManager.getConnection(url, username, password);
    }

    // TODO добить везде причины исключений где это будет вызываться
    // TODO если уже существует сделать что-то PSQLException

    public static void registerUser(User user) throws SQLException {
        String ADD_USER = "INSERT INTO users (login, password) VALUES (?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(ADD_USER);
        preparedStatement.setString(1, user.getLogin());
        preparedStatement.setString(2, user.getPassword());
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    public static Integer findUser(User user) throws SQLException {
        String FIND_USER = "SELECT id FROM users WHERE login = ? AND password = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(FIND_USER);
        preparedStatement.setString(1, user.getLogin());
        preparedStatement.setString(2, user.getPassword());
        ResultSet resultSet = preparedStatement.executeQuery();
        Integer id = null;
        if (resultSet.next()) {
            id = resultSet.getObject(1, Integer.class);
        }
        return id;
    }


    public static Integer insertFlat(Flat flat, User user) throws SQLException {
        int userId = findUser(user);

        String INSERT_FLAT = "INSERT INTO flats (id, user_id, flat_name, coor_x, coor_y," +
                " init_date, area, number_of_rooms, furnish, flat_view, transport, house_name, " +
                "house_age, house_floor) " +
                "Values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                "RETURNING id";
        PreparedStatement preparedStatement = connection.prepareStatement(INSERT_FLAT);

        connection.setAutoCommit(false);

        if (flat.getHouse() != null) {

            preparedStatement.setString(12, flat.getHouse().getName());
            preparedStatement.setLong(13, flat.getHouse().getYear());
            preparedStatement.setLong(14, flat.getHouse().getNumberOfFlatsOnFloor());
        } else {
            preparedStatement.setNull(12, Types.CHAR);
            preparedStatement.setNull(13, Types.BIGINT);
            preparedStatement.setNull(14, Types.BIGINT);
        }

        preparedStatement.setInt(1, flat.getId()); //id
        preparedStatement.setInt(2, userId); //user_id
        preparedStatement.setString(3, flat.getName()); //flat_name
        preparedStatement.setLong(4, flat.getCoordinates().getX()); //coor_x
        preparedStatement.setFloat(5, flat.getCoordinates().getY()); //coor_y
        preparedStatement.setTimestamp(6, Timestamp.from(flat.getCreationDate().toInstant())); //init_date
        preparedStatement.setLong(7, flat.getArea()); //area
        preparedStatement.setInt(8, flat.getNumberOfRooms()); //number_of_rooms
        preparedStatement.setInt(9, flat.getFurnish().ordinal()); //furnish
        preparedStatement.setInt(10, flat.getView().ordinal()); //flat_view
        preparedStatement.setInt(11, flat.getTransport().ordinal()); //transport

        preparedStatement.execute();

        connection.setAutoCommit(true);

        ResultSet last_updated_ticket = preparedStatement.getResultSet();
        last_updated_ticket.next();
        int id = last_updated_ticket.getInt("id");
        preparedStatement.close();
        return id;
    }


    public static Hashtable<Integer, Flat> getCollection() throws SQLException {
        String GET_FLATS = "SELECT * FROM flats ";
        PreparedStatement preparedStatement = connection.prepareStatement(GET_FLATS);
        ResultSet resultSet = preparedStatement.executeQuery();

        Hashtable<Integer, Flat> flats = new Hashtable<>();

        while (resultSet.next()) {
            flats.put(resultSet.getInt("id"), getFlat(resultSet));
        }
        return flats;
    }


    private static Flat getFlat(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String name = resultSet.getString("flat_name");
        Coordinates coordinates = new Coordinates((long) resultSet.getDouble("coor_x"), resultSet.getFloat("coor_y"));
        ZonedDateTime creationDate = resultSet.getTimestamp("init_date").toLocalDateTime().atZone(ZoneId.of("+03:00"));
        Long area = resultSet.getLong("area");
        int numberOfRooms = resultSet.getInt("number_of_rooms");
        Furnish furnish = Furnish.values()[resultSet.getInt("furnish")];
        View view = View.values()[resultSet.getInt("flat_view")];
        Transport transport = Transport.values()[resultSet.getInt("transport")];
        House house = null;

        if (resultSet.getObject("house_name", String.class) != null) {
            house = new House(resultSet.getString("house_name"), resultSet.getLong("house_age"), resultSet.getLong("house_floor"));
        }

        return new Flat(id, name, coordinates, creationDate, area, numberOfRooms, furnish, view, transport, house);

    }

    public static Set<Integer> getUserFlatsId(User user) throws SQLException {
        String SELECT_USER_FLATS = "SELECT id FROM flats WHERE user_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_FLATS);
        int aa = findUser(user);
        preparedStatement.setInt(1, findUser(user));
        ResultSet resultSet = preparedStatement.executeQuery();
        Set<Integer> flatsId = new HashSet<>();
        while (resultSet.next()) {
            flatsId.add(resultSet.getObject(1, Integer.class));
        }
        return flatsId;
    }

    public static void deleteUserFlats(User user) throws SQLException {
        String DELETE = "DELETE FROM flats WHERE user_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(DELETE);
        connection.setAutoCommit(false);
        preparedStatement.setInt(1, findUser(user));
        preparedStatement.executeUpdate();
        preparedStatement.close();
        connection.commit();
    }

    public static void deleteFlat(User user, int id) throws SQLException {

        String DELETE = "DELETE FROM flats WHERE user_id = ? AND id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(DELETE);
        connection.setAutoCommit(false);
        preparedStatement.setInt(1, findUser(user));
        preparedStatement.setInt(2, id);
        preparedStatement.executeUpdate();
        preparedStatement.close();
        connection.commit();
    }

}
