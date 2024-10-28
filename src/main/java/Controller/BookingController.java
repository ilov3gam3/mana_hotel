package Controller;


import Dao.*;
import Model.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;

public class BookingController {
    @WebServlet("/customer/book-room")
    public static class CustomerBookRoom extends HttpServlet{
        @Override
        protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            String room_id = req.getParameter("room_id");
            String from_date = req.getParameter("from_date");
            String to_date = req.getParameter("to_date");
            String room_type_id = req.getParameter("room_type_id");
            String customer_id = req.getSession().getAttribute("customer").toString();
            if (RoomDao.bookARoom(room_id, from_date, to_date, room_type_id, customer_id)){
                req.getSession().setAttribute("mess", "success|Đặt phòng thành công.");
            } else {
                req.getSession().setAttribute("mess", "error|Đặt phòng không thành công.");
            }
            resp.sendRedirect(req.getContextPath() + "/room-type?id=" + room_type_id + "&from_date=" + from_date + "&to_date=" + to_date);
        }
    }

    @WebServlet("/customer/booking")
    public static class CustomerViewBooking extends HttpServlet {
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            String customer_id = req.getSession().getAttribute("customer").toString();
            ArrayList<Booking> bookings = BookingDao.getBookingsOfCustomer(customer_id);
            req.setAttribute("bookings", bookings);
            req.getRequestDispatcher("/views/customer/booking.jsp").forward(req, resp);
        }
    }
    @WebServlet("/customer/cancel-booking")
    public static class CancelBooking extends HttpServlet{
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            String booking_id = req.getParameter("booking_id");
            String customer_id = req.getSession().getAttribute("customer").toString();
            if (BookingDao.cancelBooking(booking_id, customer_id)){
                req.getSession().setAttribute("mess", "success|Hủy thành công.");
            } else {
                req.getSession().setAttribute("mess", "error|Hủy không thành công.");
            }
            resp.sendRedirect(req.getContextPath() + "/customer/booking");
        }
    }

    @WebServlet("/hotel/api/hotel-get-statistics-data")
    public static class HotelGetAllBookings extends HttpServlet {
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            String hotel_id = req.getSession().getAttribute("hotel").toString();
            ArrayList<Booking> bookings = BookingDao.getAllBookingsOfHotel(hotel_id);
            ArrayList<Payment> payments = PaymentDao.getAllPaymentsOfAHotel(hotel_id);
            ArrayList<RoomType> roomTypes = RoomTypeDao.getAllRoomTypesOfAHotel(Integer.parseInt(hotel_id));
            ArrayList<Room> rooms = RoomDao.getAllRoomsOfAHotel(hotel_id);
            ArrayList<Review> reviews = ReviewDao.getAllReviewOfAHotel(hotel_id);
            JsonObject jsonObject = new JsonObject();
            Gson gson = new Gson();
            jsonObject.addProperty("bookings", gson.toJson(bookings));
            jsonObject.addProperty("payments", gson.toJson(payments));
            jsonObject.addProperty("roomTypes", gson.toJson(roomTypes));
            jsonObject.addProperty("rooms", gson.toJson(rooms));
            jsonObject.addProperty("reviews", gson.toJson(reviews));
            resp.setContentType("application/json");
            resp.getWriter().write(gson.toJson(jsonObject));
        }
    }
}
