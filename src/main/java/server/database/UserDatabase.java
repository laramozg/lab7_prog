package server.database;

import utility.UserData;
import utility.exceptions.DatabaseElementError;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.Objects;
import java.util.Random;

public class UserDatabase {
    private static UserDatabase instance = null;
    private Connection connection;
    private static final String DB_URL = "jdbc:postgresql://localhost:63333/studs";
    private static final String USER = "s336758";
    private static final String PASS = "oqv620";
    private static final String INSERT_VALUE = "INSERT INTO " +
            "users(login,password,salt)" +
            "VALUES(?,?,?)";
    private static final String SELECT_VALUE = "SELECT COUNT(*) FROM users WHERE login=?";
    private static final String SELECT_PASSWORD = "SELECT * FROM users WHERE login=?";

    private UserDatabase() throws SQLException {
        this.connection = DriverManager.getConnection(DB_URL, USER, PASS);
    }

    public static UserDatabase getInstance() {
        if (instance == null) {
            try {
                instance = new UserDatabase();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return instance;
    }

    public void createTableIfNotExist() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS users(" +
                "login VARCHAR(50) PRIMARY KEY," +
                "password VARCHAR(100)," +
                "salt VARCHAR(10)" +
                ")";
        connection.createStatement().execute(sql);
    }

    public void insertElement(UserData userData) throws SQLException {
        if ((Objects.equals(userData.getLogin().trim(), "") || (Objects.equals(userData.getPassword().trim(), "")))){
            throw new DatabaseElementError("Не может быть null");
        }
        if (checkUserData(userData)) {
            throw new DatabaseElementError("Данный логин занят!");
        }
        PreparedStatement statement = connection.prepareStatement(INSERT_VALUE);
        MessageDigest messageDigest;
        String salt = generateRandomString();
        try {
            messageDigest = MessageDigest.getInstance("SHA-384");
            messageDigest.update((userData.getPassword()+salt).getBytes());

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        statement.setString(1, userData.getLogin());
        statement.setString(2, toHexBytes(messageDigest.digest()));
        statement.setString(3, salt);
        statement.executeUpdate();
    }

    private boolean checkUserData(UserData userData) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(SELECT_VALUE);
        statement.setString(1, userData.getLogin());
        if (Objects.equals(userData.getLogin(), "")){
            throw new DatabaseElementError("Данный логин недопустим!");
        }
        ResultSet result = statement.executeQuery();
        result.next();
        return result.getInt(1) != 0;

    }


    public void checkUserPassword(UserData userData) throws SQLException {
        try {
            PreparedStatement statement = connection.prepareStatement(SELECT_PASSWORD);
            MessageDigest messageDigest;
            statement.setString(1, userData.getLogin());
            ResultSet result = statement.executeQuery();
            result.next();
            String salt = result.getString(3);
            try {
                messageDigest = MessageDigest.getInstance("SHA-384");
                messageDigest.update((userData.getPassword() + salt).getBytes());
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }

            if (!toHexBytes(messageDigest.digest()).equals(result.getString(2))) {
                throw new DatabaseElementError("Пароль неверен!");
            }
        }catch (SQLException e){
            throw new DatabaseElementError("Логин неверен!");
        }
    }

    private String generateRandomString() {
        byte[] array = new byte[10]; // length is bounded by 7
        new Random().nextBytes(array);
        return new String(array, StandardCharsets.UTF_8);
    }

    private String toHexBytes(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte aByte : bytes) {
            result.append(String.format("%02x", aByte));
        }
        return result.toString();
    }


}
