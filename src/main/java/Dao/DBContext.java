package Dao;

import Util.Config;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

public class DBContext {
    public static Connection getConnection(){
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String serverName = Config.db_server;
            String port = Config.db_port;
            String databaseName = Config.db_name;
            String username = Config.db_username;
            String password = Config.db_password;
            String url = "jdbc:sqlserver://" + serverName + ":" + port + ";databaseName=" + databaseName + ";trustServerCertificate=true;";
            return DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static boolean executeUpdate(String sql, String[] fields) {// insert update delete
        Connection connection = getConnection();
        try {
            assert connection != null;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < fields.length; i++) {
                preparedStatement.setString(i + 1, fields[i]);
            }
            int row = preparedStatement.executeUpdate();
            connection.close();
            return row > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static int insertGetLastId(String sql, String[] fields){
        Connection connection = getConnection();
        try {
            assert connection != null;
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            for (int i = 0; i < fields.length; i++) {
                preparedStatement.setString(i + 1, fields[i]);
            }
            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()){
                return (int) generatedKeys.getLong(1);
            } else {
                return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
    public static ArrayList<Integer> insertGetLastIds(String sql, String[] fields){
        Connection connection = getConnection();
        ArrayList<Integer> ids = new ArrayList<>();
        try {
            assert connection != null;

            for (String field : fields) {
                PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setString(1, field);
                preparedStatement.executeUpdate();
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    ids.add((int) generatedKeys.getLong(1));
                }
                preparedStatement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ids;
    }
    public static boolean isDateInFuture(String date) {
        try {
            LocalDate givenDate = LocalDate.parse(date);
            LocalDate today = LocalDate.now();
            return givenDate.isAfter(today);
        } catch (DateTimeParseException e) {
            e.printStackTrace();
            return false;
        }
    }
}
