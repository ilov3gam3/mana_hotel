package Dao;

import Model.Booking;
import Model.BookingStatus;
import Model.Review;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class BookingDao {
    public static ArrayList<Booking> getBookingsOfCustomer(String customer_id) {
        try {
            String sql = "select bookings.*,\n" +
                    "       room_types.price   as temp_price,\n" +
                    "       room_types.name    as room_type_name,\n" +
                    "       hotels.name        as hotel_name,\n" +
                    "       room_types.id      as room_type_id,\n" +
                    "       hotels.id          as hotel_id,\n" +
                    "       reviews.id         as review_id,\n" +
                    "       booking_id,\n" +
                    "       rating,\n" +
                    "       comment,\n" +
                    "       reviews.created_at as review_created_at\n" +
                    "from bookings\n" +
                    "         inner join rooms on bookings.room_id = rooms.id\n" +
                    "         inner join room_types on rooms.room_type_id = room_types.id\n" +
                    "         inner join hotels on room_types.hotel_id = hotels.id\n" +
                    "         left join reviews on bookings.id = reviews.booking_id\n" +
                    "where bookings.customer_id = ?;";
            PreparedStatement preparedStatement = DBContext.getConnection().prepareStatement(sql);
            preparedStatement.setString(1, customer_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<Booking> bookings = new ArrayList<>();
            while (resultSet.next()) {
                bookings.add(new Booking(
                        resultSet.getInt("id"),
                        resultSet.getInt("customer_id"),
                        resultSet.getInt("room_id"),
                        resultSet.getInt("payment_id"),
                        resultSet.getTimestamp("check_in_date"),
                        resultSet.getTimestamp("check_out_date"),
                        resultSet.getInt("price"),
                        BookingStatus.valueOf(resultSet.getString("status")),
                        resultSet.getTimestamp("created_at"),
                        resultSet.getTimestamp("updated_at"),
                        resultSet.getString("room_type_name"),
                        resultSet.getString("room_type_id"),
                        resultSet.getString("hotel_name"),
                        resultSet.getString("hotel_id"),
                        resultSet.getInt("temp_price"),
                        new Review(
                                resultSet.getInt("review_id"),
                                resultSet.getInt("customer_id"),
                                resultSet.getInt("booking_id"),
                                resultSet.getInt("rating"),
                                resultSet.getString("comment"),
                                resultSet.getTimestamp("review_created_at")
                        )
                ));
            }
            return bookings;
        }catch (Exception e){
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    public static ArrayList<Booking> getBookingWithId(String[] booking_ids){
        try {
            String sql = "select bookings.*, room_types.price as temp_price, room_types.name as room_type_name, hotels.name as hotel_name, room_types.id as room_type_id, hotels.id as hotel_id from bookings inner join rooms on bookings.room_id = rooms.id inner join room_types on rooms.room_type_id = room_types.id inner join hotels on room_types.hotel_id = hotels.id where bookings.id in ";
            StringBuilder sql_ids = new StringBuilder("(");
            for (int i = 0; i < booking_ids.length; i++) {
                if (i == booking_ids.length - 1){
                    sql_ids.append("?");
                } else {
                    sql_ids.append("?").append(",");
                }
            }
            sql_ids.append(")");
            sql += sql_ids;
            PreparedStatement preparedStatement = DBContext.getConnection().prepareStatement(sql);
            for (int i = 0; i < booking_ids.length; i++) {
                preparedStatement.setString(i+1, booking_ids[i]);
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<Booking> bookings = new ArrayList<>();
            while (resultSet.next()){
                bookings.add(new Booking(
                        resultSet.getInt("id"),
                        resultSet.getInt("customer_id"),
                        resultSet.getInt("room_id"),
                        resultSet.getInt("payment_id"),
                        resultSet.getTimestamp("check_in_date"),
                        resultSet.getTimestamp("check_out_date"),
                        resultSet.getInt("price"),
                        BookingStatus.valueOf(resultSet.getString("status")),
                        resultSet.getTimestamp("created_at"),
                        resultSet.getTimestamp("updated_at"),
                        resultSet.getString("room_type_name"),
                        resultSet.getString("hotel_name"),
                        resultSet.getString("room_type_id"),
                        resultSet.getString("hotel_id"),
                        resultSet.getInt("temp_price")
                ));
            }
            return bookings;
        }catch (Exception e){
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    public static boolean cancelBooking(String booking_id, String customer_id){
        return DBContext.executeUpdate("update bookings set status = 'CANCELED' where id = ? and customer_id = ? and status = ?;", new String[]{booking_id, customer_id, BookingStatus.NOT_PAID.text});
    }
    public static ArrayList<Booking> getAllBookingsOfHotel(String hotel_id){
        try {
            String sql = "select bookings.*,\n" +
                    "       room_types.price   as temp_price,\n" +
                    "       room_types.name    as room_type_name,\n" +
                    "       hotels.name        as hotel_name,\n" +
                    "       room_types.id      as room_type_id,\n" +
                    "       hotels.id          as hotel_id,\n" +
                    "       reviews.id         as review_id,\n" +
                    "       booking_id,\n" +
                    "       rating,\n" +
                    "       comment,\n" +
                    "       reviews.created_at as review_created_at\n" +
                    "from bookings\n" +
                    "         inner join rooms on bookings.room_id = rooms.id\n" +
                    "         inner join room_types on rooms.room_type_id = room_types.id\n" +
                    "         inner join hotels on room_types.hotel_id = hotels.id\n" +
                    "         left join reviews on bookings.id = reviews.booking_id\n" +
                    "where rooms.hotel_id = ?;";
            PreparedStatement preparedStatement = DBContext.getConnection().prepareStatement(sql);
            preparedStatement.setString(1, hotel_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<Booking> bookings = new ArrayList<>();
            while (resultSet.next()) {
                bookings.add(new Booking(
                        resultSet.getInt("id"),
                        resultSet.getInt("customer_id"),
                        resultSet.getInt("room_id"),
                        resultSet.getInt("payment_id"),
                        resultSet.getTimestamp("check_in_date"),
                        resultSet.getTimestamp("check_out_date"),
                        resultSet.getInt("price"),
                        BookingStatus.valueOf(resultSet.getString("status")),
                        resultSet.getTimestamp("created_at"),
                        resultSet.getTimestamp("updated_at"),
                        resultSet.getString("room_type_name"),
                        resultSet.getString("room_type_id"),
                        resultSet.getString("hotel_name"),
                        resultSet.getString("hotel_id"),
                        resultSet.getInt("temp_price"),
                        new Review(
                                resultSet.getInt("review_id"),
                                resultSet.getInt("customer_id"),
                                resultSet.getInt("booking_id"),
                                resultSet.getInt("rating"),
                                resultSet.getString("comment"),
                                resultSet.getTimestamp("review_created_at")
                        )
                ));
            }
            return bookings;
        }catch (Exception e){
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
