package Util;

import Dao.BookingDao;
import Dao.CustomerDao;
import Model.Booking;
import Model.Customer;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Properties;
public class Mail {
    public static boolean send(String mail_to,String subject, String html){
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true"); // if authentication is required
        properties.put("mail.smtp.starttls.enable", "true"); // if using TLS
        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(Config.email_address, Config.email_password);
            }
        });
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(Config.email_address, Config.app_name));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(mail_to));
            message.setSubject(subject);
            message.setContent(html, "text/html; charset=UTF-8");
            Transport.send(message);
            return true;
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static void sendMailBookingSuccess(String customer_id, String contextPath, ArrayList<Integer> booking_ids){
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true"); // if authentication is required
        properties.put("mail.smtp.starttls.enable", "true"); // if using TLS
        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(Config.email_address, Config.email_password);
            }
        });
        Customer customer = CustomerDao.getCustomerWithId(Integer.parseInt(customer_id));
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(Config.email_address, Config.app_name));
            assert customer != null;
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(customer.email));
            message.setSubject("Đặt phòng thành công.");
            StringBuilder url = new StringBuilder(Config.app_url + contextPath + "/customer/booking?");
            for (int i = 0; i < booking_ids.size(); i++) {
                url.append("booking_id=").append(booking_ids.get(i).toString());
            }
            String html = "Bạn đã đặt phòng thành công, vui lòng nhấn vào <a href='url'>đây</a> để thanh toán.".replace("url", url.toString());
            message.setContent(html, "text/html; charset=UTF-8");
            Transport.send(message);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void sendMailReview(String booking_id, String contextPath){
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true"); // if authentication is required
        properties.put("mail.smtp.starttls.enable", "true"); // if using TLS
        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(Config.email_address, Config.email_password);
            }
        });

        Booking booking = BookingDao.getBookingWithId(new String[]{booking_id}).get(0);
        Customer customer = CustomerDao.getCustomerWithId(booking.customer_id);
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(Config.email_address, Config.app_name));
            assert customer != null;
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(customer.email));
            message.setSubject("Bạn đã checkout.");
            String url = Config.app_url + contextPath + "/customer/booking?review_booking_id=" + booking_id;
            String html = "Bạn đã checkout thành công, cảm ơn đã sử dụng dịch cụ của chúng tôi.Vui lòng nhấn vào <a href='url'>đây</a> để đánh giá dịch vụ..".replace("url", url);
            message.setContent(html, "text/html; charset=UTF-8");
            Transport.send(message);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
