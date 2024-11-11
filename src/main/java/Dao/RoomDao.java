package Dao;

import Model.BookingStatus;
import Model.Room;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;

public class RoomDao {
    public static ArrayList<String> generateRange(String x, String y) {
        int start = Integer.parseInt(x);
        int end = Integer.parseInt(y);
        ArrayList<String> range = new ArrayList<>();
        for (int i = start; i <= end; i++) {
            range.add(String.valueOf(i));
        }
        return range;
    }

    public static boolean addRoom(String room_type_id, String from, String to, String hotel_id) {
        if (to == null) {
            DBContext.executeUpdate("insert into rooms(hotel_id, number, room_type_id, is_available) VALUES (?, ?, ?, ?);", new String[]{hotel_id, from, room_type_id, "true"});
        } else {
            if (to.isEmpty()){
                DBContext.executeUpdate("insert into rooms(hotel_id, number, room_type_id, is_available) VALUES (?, ?, ?, ?);", new String[]{hotel_id, from, room_type_id, "true"});
            } else {
                ArrayList<String> range = generateRange(from, to);
                for (int i = 0; i < range.size(); i++) {
                    DBContext.executeUpdate("insert into rooms(hotel_id, number, room_type_id, is_available) VALUES (?, ?, ?, ?);", new String[]{hotel_id, range.get(i), room_type_id, "true"});
                }
            }
        }
        return true;
    }

    public static String[] convert2array(String input) {
        if (input == null || input.isEmpty()) {
            return new String[0];
        }
        String[] result = input.split(",");
        for (int i = 0; i < result.length; i++) {
            result[i] = result[i].trim();
        }
        return result;
    }

    public static ArrayList<Room> getAllRoomsOfAHotel(String hotel_id) {
        try {
            String sql = "select rooms.*, room_types.name as room_type_name from rooms inner join room_types on rooms.room_type_id = room_types.id where rooms.hotel_id = ?";
            PreparedStatement preparedStatement = DBContext.getConnection().prepareStatement(sql);
            preparedStatement.setString(1, hotel_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<Room> rooms = new ArrayList<>();
            while (resultSet.next()) {
                rooms.add(new Room(
                        resultSet.getInt("id"),
                        resultSet.getInt("hotel_id"),
                        resultSet.getString("number"),
                        resultSet.getInt("room_type_id"),
                        resultSet.getString("room_type_name"),
                        resultSet.getBoolean("is_available")
                ));
            }
            return rooms;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static boolean updateRoom(String room_id, String room_type_id, String number, String hotel_id) {
        return DBContext.executeUpdate("update rooms set number = ?, room_type_id = ? where id = ? and hotel_id = ?;", new String[]{number, room_type_id, room_id, hotel_id});
    }
    public static ArrayList<Room> getAvailableRoom(String from_date, String to_date, String room_type_id) {
        try {
            String sql = "select rooms.* from rooms inner join room_types on rooms.room_type_id = room_types.id where room_type_id = ? and hidden = 0 and rooms.id not in (select bookings.room_id from bookings where payment_id is not null and check_in_date < ? and check_out_date > ?);";
            PreparedStatement preparedStatement = DBContext.getConnection().prepareStatement(sql);
            preparedStatement.setString(1, room_type_id);
            preparedStatement.setString(2, to_date);
            preparedStatement.setString(3, from_date);
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<Room> rooms = new ArrayList<>();
            while (resultSet.next()) {
                rooms.add(new Room(
                        resultSet.getInt("id"),
                        resultSet.getInt("hotel_id"),
                        resultSet.getString("number"),
                        resultSet.getInt("room_type_id"),
                        resultSet.getBoolean("is_available")
                ));
            }
            return rooms;
        } catch (Exception e){
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    public static boolean isAllTrue(boolean[] boolArray) {
        for (boolean value : boolArray) {
            if (!value) {
                return false;
            }
        }
        return true;
    }
    public static boolean bookARoom(String[] room_ids, String from_date, String to_date, String room_type_id, String customer_id){
        try {
            ArrayList<Room> availableRooms = getAvailableRoom(from_date, to_date, room_type_id);
            boolean[] check_arr = new boolean[room_ids.length];
            for (int i = 0; i < room_ids.length; i++) {
                for (int j = 0; j < availableRooms.size(); j++) {
                    if (Integer.parseInt(room_ids[i]) == availableRooms.get(j).id){
                        check_arr[i] = true;
                        break;
                    }
                }
            }

            if (isAllTrue(check_arr)){
                StringBuilder sql = new StringBuilder();
                String[] params = new String[room_ids.length * 5];
                for (int i = 0; i < room_ids.length; i++) {
                    sql.append("insert into bookings(customer_id, room_id, payment_id, check_in_date, check_out_date, price, status) VALUES (?, ?, null, ?, ?,null, ?);");
                    params[i*5] = customer_id;
                    params[i*5+1] = room_ids[i];
                    params[i*5+2] = from_date;
                    params[i*5+3] = to_date;
                    params[i*5+4] = BookingStatus.NOT_PAID.text;
                }
                return DBContext.executeUpdate(sql.toString(), params);
            }
            return false;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
