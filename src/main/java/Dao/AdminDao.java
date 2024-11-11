package Dao;

import Model.Admin;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class AdminDao {
    public static Admin adminLogin(String username, String password) {
        try {
            Connection connection = DBContext.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("select * from admins where username = ?");
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                if (BCrypt.checkpw(password, resultSet.getString("password"))) {
                    return new Admin(
                            resultSet.getInt("id"),
                            resultSet.getString("username"),
                            resultSet.getString("password")
                    );
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean createAdmin(String name, String username, String password) {
        return DBContext.executeUpdate("insert into admins(name, username, password, avatar) values(?, ?, ?, 'assets/img/admin-avatar.jpg')", new String[]{name, username, BCrypt.hashpw(password, BCrypt.gensalt())});
    }

    public static ArrayList<Admin> getAllAdmins() {
        try {
            Connection connection = DBContext.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("select * from admins");
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<Admin> admins = new ArrayList<>();
            while (resultSet.next()) {
                admins.add(new Admin(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("username"),
                        resultSet.getString("avatar"),
                        resultSet.getTimestamp("created_at")
                ));
            }
            return admins;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Admin getAdminWithId(int id) {
        try {
            Connection connection = DBContext.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("select * from admins where id = ?");
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new Admin(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("username"),
                        resultSet.getString("avatar"),
                        resultSet.getTimestamp("created_at")
                );
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean updateAdmin(String id, String name, String username, String password) {
        if (password != null && !password.isEmpty()) {
            return DBContext.executeUpdate("update admins set name = ?, username = ?, password = ? where id = ?", new String[]{name, username, BCrypt.hashpw(password, BCrypt.gensalt()), id});
        } else {
            return DBContext.executeUpdate("update admins set name = ?, username = ? where id = ?", new String[]{name, username, id});
        }
    }
    public static boolean deleteAdmin(String admin_id){
        return DBContext.executeUpdate("delete from admins where id = ?", new String[]{admin_id});
    }
}
