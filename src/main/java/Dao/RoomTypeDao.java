package Dao;

import Model.Image;
import Model.RoomType;
import Model.Utility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import static Dao.RoomDao.convert2array;

public class RoomTypeDao {
    public static Utility[] convert2Utility(String ids, String names) {
        String[] id_arr = ids.split(",");
        String[] name_arr = names.split(",");
        if (id_arr.length == name_arr.length) {
            Utility[] utilities = new Utility[id_arr.length];
            for (int i = 0; i < id_arr.length; i++) {
                utilities[i] = new Utility(Integer.parseInt(id_arr[i]), name_arr[i]);
            }
            return utilities;
        }
        return new Utility[0];
    }

    public static Image[] convert2Image(String ids, String urls) {
        String[] id_arr = ids.split(",");
        String[] url_arr = urls.split(",");
        if (id_arr.length == url_arr.length) {
            Image[] images = new Image[id_arr.length];
            for (int i = 0; i < id_arr.length; i++) {
                images[i] = new Image(Integer.parseInt(id_arr[i]), url_arr[i]);
            }
            return images;
        }
        return new Image[0];
    }

    public static ArrayList<RoomType> getAllRoomTypesOfAHotel(int id) {
        try {
            Connection connection = DBContext.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("WITH UtilityAggregates AS (\n" +
                    "    SELECT\n" +
                    "        rt.id AS room_type_id,\n" +
                    "        STRING_AGG(CAST(u.id AS NVARCHAR), ',') AS utility_ids,\n" +
                    "        STRING_AGG(u.name, ',') AS utility_names\n" +
                    "    FROM room_types rt\n" +
                    "    LEFT JOIN room_types_has_utilities rtu ON rt.id = rtu.room_type_id\n" +
                    "    LEFT JOIN utilities u ON rtu.utility_id = u.id\n" +
                    "    GROUP BY rt.id\n" +
                    "),\n" +
                    "ImageAggregates AS (\n" +
                    "    SELECT\n" +
                    "        rt.id AS room_type_id,\n" +
                    "        STRING_AGG(CAST(i.id AS NVARCHAR), ',') AS image_ids,\n" +
                    "        STRING_AGG(i.url, ',') AS image_urls\n" +
                    "    FROM room_types rt\n" +
                    "    LEFT JOIN room_type_has_images rti ON rt.id = rti.room_type_id\n" +
                    "    LEFT JOIN images i ON rti.image_id = i.id\n" +
                    "    GROUP BY rt.id\n" +
                    ")\n" +
                    "SELECT\n" +
                    "    rt.id AS room_type_id,\n" +
                    "    rt.name AS room_type_name,\n" +
                    "    rt.description AS room_type_description,\n" +
                    "    rt.beds,\n" +
                    "    rt.area,\n" +
                    "    rt.price,\n" +
                    "    rt.hidden,\n" +
                    "    ua.utility_ids,\n" +
                    "    ua.utility_names,\n" +
                    "    ia.image_ids,\n" +
                    "    ia.image_urls\n" +
                    "FROM room_types rt\n" +
                    "         LEFT JOIN UtilityAggregates ua ON rt.id = ua.room_type_id\n" +
                    "         LEFT JOIN ImageAggregates ia ON rt.id = ia.room_type_id\n" +
                    "where hotel_id = ?;");
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<RoomType> roomTypes = new ArrayList<>();
            while (resultSet.next()) {
                roomTypes.add(new RoomType(
                        resultSet.getInt("room_type_id"),
                        id,
                        resultSet.getString("room_type_name"),
                        resultSet.getString("room_type_description"),
                        resultSet.getInt("beds"),
                        resultSet.getFloat("area"),
                        resultSet.getInt("price"),
                        convert2Utility(resultSet.getString("utility_ids"), resultSet.getString("utility_names")),
                        convert2Image(resultSet.getString("image_ids"), resultSet.getString("image_urls")),
                        resultSet.getBoolean("hidden")
                ));
            }
            return roomTypes;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean updateRoomType(String name, String description, String beds, String area, String price, String hotel_id, String id, String images_to_delete, String[] utility_ids, ArrayList<Integer> images_ids) {
        try {
            DBContext.executeUpdate("update room_types set name = ?, description = ?, beds = ?, area = ?, price = ? where hotel_id = ? and id = ?", new String[]{name, description, beds, area, price, hotel_id, id});
            StringBuilder sql;
            if (!images_to_delete.isEmpty()) {
                String[] images_to_delete_arr = convert2array(images_to_delete);
                sql = new StringBuilder("delete from room_type_has_images where image_id in xxx and room_type_id = ? and room_type_id in (select id from room_types where hotel_id = ?);");
                StringBuilder replace = new StringBuilder("(");
                for (int i = 0; i < images_to_delete_arr.length; i++) {
                    if (i == images_to_delete_arr.length - 1) {
                        replace.append("?");
                    } else {
                        replace.append("?").append(", ");
                    }
                }
                replace.append(")");
                sql = new StringBuilder(sql.toString().replace("xxx", replace.toString()));
                String[] value2 = new String[]{id, hotel_id};
                DBContext.executeUpdate(sql.toString(), Stream.concat(Arrays.stream(images_to_delete_arr), Arrays.stream(value2)).toArray(String[]::new));
            }
            if (!images_ids.isEmpty()) {
                sql = new StringBuilder();
                String[] value = new String[images_ids.size() * 2];
                for (int i = 0; i < images_ids.size(); i++) {
                    sql.append("insert into room_type_has_images(room_type_id, image_id) values (?, ?);");
                    value[i * 2] = id;
                    value[i * 2 + 1] = String.valueOf(images_ids.get(i));
                }
                DBContext.executeUpdate(String.valueOf(sql), value);
            }
            sql = new StringBuilder("select STRING_AGG(utility_id, ',') as ids from room_types_has_utilities where room_type_id = ?;");
            PreparedStatement preparedStatement = DBContext.getConnection().prepareStatement(String.valueOf(sql));
            preparedStatement.setString(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String result = resultSet.getString("ids");
                String[] old_ids = convert2array(result);
                Set<String> oldSet = new HashSet<>(Arrays.asList(old_ids));
                Set<String> newSet = new HashSet<>(Arrays.asList(utility_ids));
                Set<String> addedSet = new HashSet<>(newSet);
                addedSet.removeAll(oldSet);
                Set<String> removedSet = new HashSet<>(oldSet);
                removedSet.removeAll(newSet);
                String[] added = addedSet.toArray(new String[0]);
                String[] removed = removedSet.toArray(new String[0]);
                if (added.length != 0) {
                    sql = new StringBuilder();
                    for (int i = 0; i < added.length; i++) {
                        sql.append("insert into room_types_has_utilities(utility_id, room_type_id) VALUES (?, ").append(id).append(");");
                    }
                    DBContext.executeUpdate(String.valueOf(sql), added);
                }
                if (removed.length != 0) {
                    sql = new StringBuilder();
                    for (int i = 0; i < removed.length; i++) {
                        sql.append("delete from room_types_has_utilities where utility_id = ? and room_type_id = " + id + ";");
                    }
                    DBContext.executeUpdate(String.valueOf(sql), removed);
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean addRoomType(String name, String description, String hotel_id, String beds, String area, String price, String[] utility_ids, ArrayList<Integer> images_ids) {
        try {
            int room_type_id = DBContext.insertGetLastId("insert into room_types(hotel_id, name, description, beds, area, price) values (?, ?, ?, ?, ?, ?);", new String[]{hotel_id, name, description, beds, area, price});
            StringBuilder sql = new StringBuilder();
            for (int i = 0; i < utility_ids.length; i++) {
                sql.append("insert into room_types_has_utilities(utility_id, room_type_id) VALUES (?, ").append(room_type_id).append(");");
            }
            DBContext.executeUpdate(String.valueOf(sql), utility_ids);
            sql = new StringBuilder();
            for (int i = 0; i < images_ids.size(); i++) {
                sql.append("insert into room_type_has_images(room_type_id, image_id) VALUES (").append(room_type_id).append(", ?);");
            }
            DBContext.executeUpdate(String.valueOf(sql), images_ids.stream().map(String::valueOf).toArray(String[]::new));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static ArrayList<RoomType> search(String query, String[] utilityIds, String priceFrom, String priceTo, String sort) {
        try {
            String sql = "WITH UtilityAggregates AS (SELECT rt.id                                   AS room_type_id,\n" +
                    "                                  STRING_AGG(CAST(u.id AS NVARCHAR), ',') AS utility_ids,\n" +
                    "                                  STRING_AGG(u.name, ',')                 AS utility_names\n" +
                    "                           FROM room_types rt\n" +
                    "                                    LEFT JOIN room_types_has_utilities rtu ON rt.id = rtu.room_type_id\n" +
                    "                                    LEFT JOIN utilities u ON rtu.utility_id = u.id\n" +
                    "                           GROUP BY rt.id),\n" +
                    "     ImageAggregates AS (SELECT rt.id                                   AS room_type_id,\n" +
                    "                                STRING_AGG(CAST(i.id AS NVARCHAR), ',') AS image_ids,\n" +
                    "                                STRING_AGG(i.url, ',')                  AS image_urls\n" +
                    "                         FROM room_types rt\n" +
                    "                                  LEFT JOIN room_type_has_images rti ON rt.id = rti.room_type_id\n" +
                    "                                  LEFT JOIN images i ON rti.image_id = i.id\n" +
                    "                         GROUP BY rt.id)\n" +
                    "SELECT rt.id          AS room_type_id,\n" +
                    "       rt.hotel_id    AS hotel_id,\n" +
                    "       hotels.name as hotel_name,\n" +
                    "       rt.name        AS room_type_name,\n" +
                    "       rt.description AS room_type_description,\n" +
                    "       rt.beds,\n" +
                    "       rt.area,\n" +
                    "       rt.price,\n" +
                    "       ua.utility_ids,\n" +
                    "       ua.utility_names,\n" +
                    "       ia.image_ids,\n" +
                    "       ia.image_urls,\n" +
                    "       (select count(bookings.id) from bookings inner join rooms on bookings.room_id = rooms.id where bookings.payment_id is not null and rooms.room_type_id = rt.id) as booked,\n" +
                    "       (select avg(reviews.rating) from reviews inner join bookings on reviews.booking_id = bookings.id inner join rooms on bookings.room_id = rooms.id where rooms.room_type_id = rt.id) as rating\n" +
                    "FROM room_types rt\n" +
                    "         LEFT JOIN UtilityAggregates ua ON rt.id = ua.room_type_id\n" +
                    "         LEFT JOIN ImageAggregates ia ON rt.id = ia.room_type_id\n" +
                    "         JOIN room_types_has_utilities rtu ON rt.id = rtu.room_type_id\n" +
                    "         Join hotels on rt.hotel_id = hotels.id\n" +
                    "where 1 = 1 and rt.hidden = 0";
            ArrayList<String> params = new ArrayList<>();
            if (query != null && !query.isEmpty()) {
                sql += " and (rt.name like ? or description like ?)";
                params.add("%" + query + "%");
                params.add("%" + query + "%");
            }
            if (priceFrom != null && !priceFrom.isEmpty()) {
                sql += " and price >= ?";
                params.add(priceFrom);
            }
            if (priceTo != null && !priceTo.isEmpty()) {
                sql += " and price <= ?";
                params.add(priceTo);
            }
            if (utilityIds != null && utilityIds.length > 0) {
                sql += " and rtu.utility_id in ";
                StringBuilder replace = new StringBuilder("(");
                for (int i = 0; i < utilityIds.length; i++) {
                    params.add(utilityIds[i]);
                    if (i == utilityIds.length - 1) {
                        replace.append("?");
                    } else {
                        replace.append("?, ");
                    }
                }
                replace.append(")");
                sql += replace;
            }
            sql += " group by rt.id, rt.hotel_id, rt.name, rt.description, rt.beds, rt.area, rt.price, ua.utility_ids, ua.utility_names, ia.image_ids, ia.image_urls, hotels.name";
            if (sort != null && !sort.isEmpty()) {
                switch (sort) {
                    case "1": {
                        sql += " order by rt.price ASC";
                        break;
                    }
                    case "2": {
                        sql += " order by rt.price DESC";
                        break;
                    }
                    case "3": {
                        sql += " ORDER BY rating ASC";
                        break;
                    }
                    case "4": {
                        sql += " ORDER BY rating DESC";
                        break;
                    }
                    case "5": {
                        sql += " ORDER BY booked ASC";
                        break;
                    }
                    case "6": {
                        sql += " ORDER BY booked DESC";
                        break;
                    }
                    default: {
                        break;
                    }
                }
            }
            PreparedStatement preparedStatement = DBContext.getConnection().prepareStatement(sql);
            for (int i = 0; i < params.size(); i++) {
                preparedStatement.setString(i + 1, params.get(i));
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<RoomType> roomTypes = new ArrayList<>();
            while (resultSet.next()) {
                roomTypes.add(new RoomType(
                        resultSet.getInt("room_type_id"),
                        resultSet.getInt("hotel_id"),
                        resultSet.getString("hotel_name"),
                        resultSet.getString("room_type_name"),
                        resultSet.getString("room_type_description"),
                        resultSet.getInt("beds"),
                        resultSet.getFloat("area"),
                        resultSet.getInt("price"),
                        convert2Utility(resultSet.getString("utility_ids"), resultSet.getString("utility_names")),
                        convert2Image(resultSet.getString("image_ids"), resultSet.getString("image_urls")),
                        resultSet.getString("booked"),
                        resultSet.getString("rating")
                ));
            }
            return roomTypes;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    public static RoomType getRoomTypeWithId(String id){
        try {
            Connection connection = DBContext.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("WITH UtilityAggregates AS (\n" +
                    "    SELECT\n" +
                    "        rt.id AS room_type_id,\n" +
                    "        STRING_AGG(CAST(u.id AS NVARCHAR), ',') AS utility_ids,\n" +
                    "        STRING_AGG(u.name, ',') AS utility_names\n" +
                    "    FROM room_types rt\n" +
                    "    LEFT JOIN room_types_has_utilities rtu ON rt.id = rtu.room_type_id\n" +
                    "    LEFT JOIN utilities u ON rtu.utility_id = u.id\n" +
                    "    GROUP BY rt.id\n" +
                    "),\n" +
                    "ImageAggregates AS (\n" +
                    "    SELECT\n" +
                    "        rt.id AS room_type_id,\n" +
                    "        STRING_AGG(CAST(i.id AS NVARCHAR), ',') AS image_ids,\n" +
                    "        STRING_AGG(i.url, ',') AS image_urls\n" +
                    "    FROM room_types rt\n" +
                    "    LEFT JOIN room_type_has_images rti ON rt.id = rti.room_type_id\n" +
                    "    LEFT JOIN images i ON rti.image_id = i.id\n" +
                    "    GROUP BY rt.id\n" +
                    ")\n" +
                    "SELECT\n" +
                    "    rt.id AS room_type_id,\n" +
                    "   rt.hotel_id AS hotel_id,\n" +
                    "    rt.name AS room_type_name,\n" +
                    "    rt.description AS room_type_description,\n" +
                    "    rt.beds,\n" +
                    "    rt.area,\n" +
                    "    rt.price,\n" +
                    "    ua.utility_ids,\n" +
                    "    ua.utility_names,\n" +
                    "    ia.image_ids,\n" +
                    "    ia.image_urls\n" +
                    "FROM room_types rt\n" +
                    "         LEFT JOIN UtilityAggregates ua ON rt.id = ua.room_type_id\n" +
                    "         LEFT JOIN ImageAggregates ia ON rt.id = ia.room_type_id\n" +
                    "where rt.id = ? and rt.hidden = 0;");
            preparedStatement.setString(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                 return new RoomType(
                        resultSet.getInt("room_type_id"),
                        resultSet.getInt("hotel_id"),
                        resultSet.getString("room_type_name"),
                        resultSet.getString("room_type_description"),
                        resultSet.getInt("beds"),
                        resultSet.getFloat("area"),
                        resultSet.getInt("price"),
                        convert2Utility(resultSet.getString("utility_ids"), resultSet.getString("utility_names")),
                        convert2Image(resultSet.getString("image_ids"), resultSet.getString("image_urls"))
                );
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static ArrayList<RoomType> selectTop3Rating(){
        try {
            String sql = "WITH UtilityAggregates AS (SELECT rt.id                                   AS room_type_id,\n" +
                    "                                  STRING_AGG(CAST(u.id AS NVARCHAR), ',') AS utility_ids,\n" +
                    "                                  STRING_AGG(u.name, ',')                 AS utility_names\n" +
                    "                           FROM room_types rt\n" +
                    "                                    LEFT JOIN room_types_has_utilities rtu ON rt.id = rtu.room_type_id\n" +
                    "                                    LEFT JOIN utilities u ON rtu.utility_id = u.id\n" +
                    "                           GROUP BY rt.id),\n" +
                    "     ImageAggregates AS (SELECT rt.id                                   AS room_type_id,\n" +
                    "                                STRING_AGG(CAST(i.id AS NVARCHAR), ',') AS image_ids,\n" +
                    "                                STRING_AGG(i.url, ',')                  AS image_urls\n" +
                    "                         FROM room_types rt\n" +
                    "                                  LEFT JOIN room_type_has_images rti ON rt.id = rti.room_type_id\n" +
                    "                                  LEFT JOIN images i ON rti.image_id = i.id\n" +
                    "                         GROUP BY rt.id)\n" +
                    "SELECT top 3 rt.id          AS room_type_id,\n" +
                    "       rt.hotel_id    AS hotel_id,\n" +
                    "       hotels.name as hotel_name,\n" +
                    "       rt.name        AS room_type_name,\n" +
                    "       rt.description AS room_type_description,\n" +
                    "       rt.beds,\n" +
                    "       rt.area,\n" +
                    "       rt.price,\n" +
                    "       ua.utility_ids,\n" +
                    "       ua.utility_names,\n" +
                    "       ia.image_ids,\n" +
                    "       ia.image_urls,\n" +
                    "       (select count(bookings.id) from bookings inner join rooms on bookings.room_id = rooms.id where bookings.payment_id is not null and rooms.room_type_id = rt.id) as booked,\n" +
                    "       (select avg(reviews.rating) from reviews inner join bookings on reviews.booking_id = bookings.id inner join rooms on bookings.room_id = rooms.id where rooms.room_type_id = rt.id) as rating\n" +
                    "FROM room_types rt\n" +
                    "         LEFT JOIN UtilityAggregates ua ON rt.id = ua.room_type_id\n" +
                    "         LEFT JOIN ImageAggregates ia ON rt.id = ia.room_type_id\n" +
                    "         JOIN room_types_has_utilities rtu ON rt.id = rtu.room_type_id\n" +
                    "         Join hotels on rt.hotel_id = hotels.id\n" +
                    "where 1 = 1 group by rt.id, rt.hotel_id, rt.name, rt.description, rt.beds, rt.area, rt.price, ua.utility_ids, ua.utility_names, ia.image_ids, ia.image_urls, hotels.name ORDER BY rating DESC;";
            PreparedStatement preparedStatement = DBContext.getConnection().prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<RoomType> roomTypes = new ArrayList<>();
            while (resultSet.next()) {
                roomTypes.add(new RoomType(
                        resultSet.getInt("room_type_id"),
                        resultSet.getInt("hotel_id"),
                        resultSet.getString("hotel_name"),
                        resultSet.getString("room_type_name"),
                        resultSet.getString("room_type_description"),
                        resultSet.getInt("beds"),
                        resultSet.getFloat("area"),
                        resultSet.getInt("price"),
                        convert2Utility(resultSet.getString("utility_ids"), resultSet.getString("utility_names")),
                        convert2Image(resultSet.getString("image_ids"), resultSet.getString("image_urls")),
                        resultSet.getString("booked"),
                        resultSet.getString("rating")
                ));
            }
            return roomTypes;
        } catch (Exception e){
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    public static ArrayList<RoomType> selectTop3Book(){
        try {
            String sql = "WITH UtilityAggregates AS (SELECT rt.id                                   AS room_type_id,\n" +
                    "                                  STRING_AGG(CAST(u.id AS NVARCHAR), ',') AS utility_ids,\n" +
                    "                                  STRING_AGG(u.name, ',')                 AS utility_names\n" +
                    "                           FROM room_types rt\n" +
                    "                                    LEFT JOIN room_types_has_utilities rtu ON rt.id = rtu.room_type_id\n" +
                    "                                    LEFT JOIN utilities u ON rtu.utility_id = u.id\n" +
                    "                           GROUP BY rt.id),\n" +
                    "     ImageAggregates AS (SELECT rt.id                                   AS room_type_id,\n" +
                    "                                STRING_AGG(CAST(i.id AS NVARCHAR), ',') AS image_ids,\n" +
                    "                                STRING_AGG(i.url, ',')                  AS image_urls\n" +
                    "                         FROM room_types rt\n" +
                    "                                  LEFT JOIN room_type_has_images rti ON rt.id = rti.room_type_id\n" +
                    "                                  LEFT JOIN images i ON rti.image_id = i.id\n" +
                    "                         GROUP BY rt.id)\n" +
                    "SELECT top 3 rt.id          AS room_type_id,\n" +
                    "       rt.hotel_id    AS hotel_id,\n" +
                    "       hotels.name as hotel_name,\n" +
                    "       rt.name        AS room_type_name,\n" +
                    "       rt.description AS room_type_description,\n" +
                    "       rt.beds,\n" +
                    "       rt.area,\n" +
                    "       rt.price,\n" +
                    "       ua.utility_ids,\n" +
                    "       ua.utility_names,\n" +
                    "       ia.image_ids,\n" +
                    "       ia.image_urls,\n" +
                    "       (select count(bookings.id) from bookings inner join rooms on bookings.room_id = rooms.id where bookings.payment_id is not null and rooms.room_type_id = rt.id) as booked,\n" +
                    "       (select avg(reviews.rating) from reviews inner join bookings on reviews.booking_id = bookings.id inner join rooms on bookings.room_id = rooms.id where rooms.room_type_id = rt.id) as rating\n" +
                    "FROM room_types rt\n" +
                    "         LEFT JOIN UtilityAggregates ua ON rt.id = ua.room_type_id\n" +
                    "         LEFT JOIN ImageAggregates ia ON rt.id = ia.room_type_id\n" +
                    "         JOIN room_types_has_utilities rtu ON rt.id = rtu.room_type_id\n" +
                    "         Join hotels on rt.hotel_id = hotels.id\n" +
                    "where 1 = 1 group by rt.id, rt.hotel_id, rt.name, rt.description, rt.beds, rt.area, rt.price, ua.utility_ids, ua.utility_names, ia.image_ids, ia.image_urls, hotels.name ORDER BY booked DESC;";
            PreparedStatement preparedStatement = DBContext.getConnection().prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<RoomType> roomTypes = new ArrayList<>();
            while (resultSet.next()) {
                roomTypes.add(new RoomType(
                        resultSet.getInt("room_type_id"),
                        resultSet.getInt("hotel_id"),
                        resultSet.getString("hotel_name"),
                        resultSet.getString("room_type_name"),
                        resultSet.getString("room_type_description"),
                        resultSet.getInt("beds"),
                        resultSet.getFloat("area"),
                        resultSet.getInt("price"),
                        convert2Utility(resultSet.getString("utility_ids"), resultSet.getString("utility_names")),
                        convert2Image(resultSet.getString("image_ids"), resultSet.getString("image_urls")),
                        resultSet.getString("booked"),
                        resultSet.getString("rating")
                ));
            }
            return roomTypes;
        } catch (Exception e){
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static boolean changeHidden(String room_type_id){
        return DBContext.executeUpdate("update room_types set hidden = ~hidden where id = ?", new String[]{room_type_id});
    }
}
