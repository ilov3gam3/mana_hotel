package Dao;

import Model.BookingStatus;
import Model.Payment;
import Model.TransactionStatus;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class PaymentDao {
    public static boolean handleVNPayResult(String amount, String paid_at, String vnp_TransactionStatus, String vnp_TransactionNo, String vnp_BankTranNo, String vnp_CardType, String vnp_BankCode, String vnp_OrderInfo, String vnp_TxnRef, String customer_id){
        try {
            if (!checkPaymentExist(amount, vnp_TxnRef, vnp_OrderInfo)){
                String sql = "insert into payments(customer_id, amount, txnRef, orderInfo, bankCode, transactionNo, transactionStatus, cardType, bankTranNo, paid_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
                int payment_id = DBContext.insertGetLastId(sql, new String[]{customer_id, amount, vnp_TxnRef, vnp_OrderInfo, vnp_BankCode, vnp_TransactionNo, vnp_TransactionStatus, vnp_CardType, vnp_BankTranNo, paid_at});
                String[] booking_ids = vnp_OrderInfo.split("\\|")[1].split(",");
                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter sqlDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String formattedDateTime = now.format(sqlDateTimeFormatter);
                sql = "update bookings set payment_id = ?, price = ?, updated_at = ?, status = ? where id in ";
                StringBuilder sql_ids = new StringBuilder("(");
                for (int i = 0; i < booking_ids.length; i++) {
                    if (i == booking_ids.length - 1){
                        sql_ids.append(booking_ids[i]);
                    } else {
                        sql_ids.append(booking_ids[i]).append(",");
                    }
                }
                sql_ids.append(")");
                sql += sql_ids;
                return DBContext.executeUpdate(sql, new String[]{String.valueOf(payment_id), amount, formattedDateTime, BookingStatus.PAID.text});
            } else {
                return false;
            }

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    public static boolean checkPaymentExist(String amount, String vnp_TxnRef, String vnp_OrderInfo){
        try {
            String sql = "select * from payments where amount = ? and txnRef = ? and orderInfo = ?;";
            PreparedStatement preparedStatement = DBContext.getConnection().prepareStatement(sql);
            preparedStatement.setString(1, amount);
            preparedStatement.setString(2, vnp_TxnRef);
            preparedStatement.setString(3, vnp_OrderInfo);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    public static ArrayList<Payment> getPaymentsOfCustomer(String customer_id){
        try {
            String sql = "select * from payments where customer_id = ?;";
            PreparedStatement preparedStatement = DBContext.getConnection().prepareStatement(sql);
            preparedStatement.setString(1, customer_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<Payment> payments = new ArrayList<>();
            while (resultSet.next()) {
                payments.add(new Payment(
                        resultSet.getInt("id"),
                        resultSet.getInt("customer_id"),
                        resultSet.getLong("amount"),
                        resultSet.getString("txnRef"),
                        resultSet.getString("orderInfo"),
                        resultSet.getString("bankCode"),
                        resultSet.getString("transactionNo"),
                        TransactionStatus.fromCode(resultSet.getString("transactionStatus")),
                        resultSet.getString("cardType"),
                        resultSet.getString("bankTranNo"),
                        resultSet.getTimestamp("created_at"),
                        resultSet.getTimestamp("paid_at")
                ));
            }
            return payments;
        }catch (Exception e){
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    public static ArrayList<Payment> getAllPaymentsOfAHotel(String hotel_id){
        try {
            String sql = "select payments.*, rooms.id as room_id, room_type_id from payments inner join bookings on payments.id = bookings.payment_id inner join rooms on bookings.room_id = rooms.id where hotel_id = ?";
            PreparedStatement preparedStatement = DBContext.getConnection().prepareStatement(sql);
            preparedStatement.setString(1, hotel_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<Payment> payments = new ArrayList<>();
            while (resultSet.next()) {
                payments.add(new Payment(
                        resultSet.getInt("id"),
                        resultSet.getInt("customer_id"),
                        resultSet.getLong("amount"),
                        resultSet.getString("txnRef"),
                        resultSet.getString("orderInfo"),
                        resultSet.getString("bankCode"),
                        resultSet.getString("transactionNo"),
                        TransactionStatus.fromCode(resultSet.getString("transactionStatus")),
                        resultSet.getString("cardType"),
                        resultSet.getString("bankTranNo"),
                        resultSet.getTimestamp("created_at"),
                        resultSet.getTimestamp("paid_at"),
                        resultSet.getInt("room_id"),
                        resultSet.getInt("room_type_id")
                ));
            }
            return payments;
        } catch (Exception e){
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
