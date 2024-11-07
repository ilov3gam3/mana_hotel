package Dao;

import Model.Hotel;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class HotelDao {
    public static Hotel hotelLogin(String email, String password) {
        try {
            Connection connection = DBContext.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("select * from hotels where email = ?");
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                if (BCrypt.checkpw(password, resultSet.getString("password"))) {
                    return new Hotel(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("email"),
                            resultSet.getString("avatar"),
                            resultSet.getString("password"),
                            resultSet.getTimestamp("created_at"),
                            resultSet.getBoolean("is_verified"),
                            resultSet.getBoolean("is_blocked")
                    );
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<Hotel> getAllHotels() {
        try {
            Connection connection = DBContext.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("select * from hotels");
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<Hotel> hotels = new ArrayList<>();
            while (resultSet.next()) {
                hotels.add(new Hotel(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("avatar"),
                        resultSet.getString("address"),
                        resultSet.getString("gg_map_link"),
                        resultSet.getString("password"),
                        resultSet.getTimestamp("created_at"),
                        resultSet.getBoolean("is_verified"),
                        resultSet.getBoolean("is_blocked")
                ));
            }
            return hotels;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean createNewHotel(String name, String email, String avatar, String password, String address, String gg_map_link) {
        return DBContext.executeUpdate("insert into hotels(name, email, avatar, password, address, gg_map_link) values (?, ?, ?, ?, ?, ?)", new String[]{name, email, avatar, BCrypt.hashpw(password, BCrypt.gensalt()), address, gg_map_link});
    }

    public static Hotel getHotelWithId(int id) {
        try {
            Connection connection = DBContext.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("select * from hotels where id = ?");
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new Hotel(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("avatar"),
                        resultSet.getString("address"),
                        resultSet.getString("gg_map_link"),
                        resultSet.getString("password"),
                        resultSet.getTimestamp("created_at")
                );
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean updateHotelProfile(String name, String email, String id, String address, String gg_map_link) {
        return DBContext.executeUpdate("update hotels set name = ?, email = ?, address = ?, gg_map_link = ? where id = ?", new String[]{name, email, address, gg_map_link, id});
    }

    public static boolean updatePassword(String password, String id) {
        return DBContext.executeUpdate("update hotels set password = ? where id = ?", new String[]{BCrypt.hashpw(password, BCrypt.gensalt()), id});
    }

    public static boolean updateAvatar(String avatar, String id) {
        return DBContext.executeUpdate("update hotels set avatar = ? where id = ?", new String[]{avatar, id});
    }
    public static boolean changeVerifyHotel(String hotel_id){
        return DBContext.executeUpdate("update hotels set is_verified = ~is_verified where id = ?", new String[]{hotel_id});
    }
    public static boolean changeBlockHotel(String hotel_id){
        return DBContext.executeUpdate("update hotels set is_blocked = ~is_blocked where id = ?", new String[]{hotel_id});
    }
}
