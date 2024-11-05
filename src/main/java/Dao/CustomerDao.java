package Dao;

import Model.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class CustomerDao {

    public static boolean checkEmail(String email) {
        try {
            Connection connection = DBContext.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("select * from customers where email = ?");
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean checkEmailExcept(String new_email, String customer_id) {
        try {
            Connection connection = DBContext.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("select * from customers where email = ? and id != ?");
            preparedStatement.setString(1, new_email);
            preparedStatement.setString(2, customer_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean checkPhoneExcept(String new_phone, String customer_id) {
        try {
            Connection connection = DBContext.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("select * from customers where phone = ? and id != ?");
            preparedStatement.setString(1, new_phone);
            preparedStatement.setString(2, customer_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return false;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean checkPhone(String phone) {
        try {
            Connection connection = DBContext.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("select * from customers where phone = ?");
            preparedStatement.setString(1, phone);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return false;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Customer getCustomerWithEmail(String email) {
        try {
            Connection connection = DBContext.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("select * from customers where email = ?");
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new Customer(
                        resultSet.getInt("id"),
                        resultSet.getString("email"),
                        resultSet.getString("password"),
                        resultSet.getBoolean("is_verified"),
                        resultSet.getBoolean("is_blocked")
                );
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean verifyCustomerWithToken(String token) {
        return DBContext.executeUpdate("update customers set is_verified = 'true', token = null where token = ?", new String[]{token});
    }

    public static boolean addNewCustomer(String name, String email, String phone, String dob, String avatar, String hash_password, String is_verified, String token) {
        return DBContext.executeUpdate("insert into customers(name, email, phone, dob, avatar, password, is_verified, token) values (?, ?, ?, ?, ?, ?, ?, ?)", new String[]{name, email, phone, dob, avatar, hash_password, is_verified, token});
    }

    public static boolean addNewCustomer(String name, String email, String avatar, String is_verified) {
        return DBContext.executeUpdate("insert into customers(name, email, avatar, is_verified) values (?, ?, ?, ?)", new String[]{name, email, avatar, is_verified});
    }

    public static Customer getCustomerWithId(int id) {
        try {
            Connection connection = DBContext.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("select * from customers where id = ?");
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new Customer(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("phone"),
                        resultSet.getDate("dob"),
                        resultSet.getString("avatar"),
                        resultSet.getString("password"),
                        resultSet.getBoolean("is_verified"),
                        resultSet.getString("dob")
                );
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean updatePassword(String id, String password) {
        return DBContext.executeUpdate("update customers set password = ? where id = ?", new String[]{password, id});
    }

    public static boolean updateAvatar(String id, String avatar) {
        return DBContext.executeUpdate("update customers set avatar = ? where id = ?", new String[]{avatar, id});
    }

    public static boolean updateProfile(String name, String email, String phone, String dob, String id) {
        return DBContext.executeUpdate("update customers set name = ?, email = ?, phone = ?, dob = ? where id = ?", new String[]{name, email, phone, dob, id});
    }
    public static boolean updateTokenForgotPassword(String token, String email){
        return DBContext.executeUpdate("update customers set token = ? where email = ?", new String[]{token, email});
    }
    public static boolean resetPassword(String password, String token){
        return DBContext.executeUpdate("update customers set password = ?, token = null where token = ?", new String[]{password, token});
    }
    public static ArrayList<Customer> getAllCustomers(){
        try {
            Connection connection = DBContext.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("select * from customers");
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<Customer> customers = new ArrayList<>();
            while (resultSet.next()){
                customers.add(new Customer(
                    resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getString("email"),
                    resultSet.getString("phone"),
                    resultSet.getDate("dob"),
                    resultSet.getString("avatar"),
                    resultSet.getString("password"),
                    resultSet.getBoolean("is_verified"),
                    resultSet.getString("token"),
                    resultSet.getTimestamp("created_at"),
                    resultSet.getBoolean("is_blocked")
                ));
            }
            return customers;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    public static boolean blockCustomer(String customer_id){
        return DBContext.executeUpdate("update customers set is_blocked = ~is_blocked where id = ?", new String[]{customer_id});
    }
}
