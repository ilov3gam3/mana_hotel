package Controller;

import Dao.BookingDao;
import Dao.HotelDao;
import Dao.PaymentDao;
import Dao.RoomTypeDao;
import Model.Booking;
import Model.Hotel;
import Model.Payment;
import Model.RoomType;
import Util.UploadImage;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.util.ArrayList;

public class HotelController {
    @WebServlet("/hotel/profile")
    public static class Profile extends HttpServlet{
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            req.getRequestDispatcher("/views/hotel/profile.jsp").forward(req, resp);
        }

        @Override
        protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            String name = req.getParameter("name");
            String email = req.getParameter("email");
            String address = req.getParameter("address");
            String gg_map_link = req.getParameter("gg_map_link");
            String hotel_id = req.getSession().getAttribute("hotel").toString();
            if (HotelDao.updateHotelProfile(name, email, hotel_id, address, gg_map_link)){
                req.getSession().setAttribute("mess", "success|Cập nhật thành công");
                resp.sendRedirect(req.getContextPath() + "/hotel/profile");
            } else {
                req.getSession().setAttribute("mess", "error|Cập nhật không thành công");
                resp.sendRedirect(req.getContextPath() + "/hotel/profile");
            }
        }
    }

    @WebServlet("/hotel/change-password")
    public static class ChangePassword extends HttpServlet{
        @Override
        protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            String old_pass = req.getParameter("old_pass");
            String new_pass = req.getParameter("new_pass");
            String re_new_pass = req.getParameter("re_new_pass");
            Hotel hotel = HotelDao.getHotelWithId(Integer.parseInt(req.getSession().getAttribute("hotel").toString()));
            assert hotel != null;
            if (BCrypt.checkpw(old_pass, hotel.password)){
                if (new_pass.equals(re_new_pass)){
                    if (HotelDao.updatePassword(new_pass, String.valueOf(hotel.id))){
                        req.getSession().setAttribute("mess", "success|Cập nhật thành công");
                        resp.sendRedirect(req.getContextPath() + "/hotel/profile");
                    } else {
                        req.getSession().setAttribute("mess", "error|Lỗi hệ thống");
                        resp.sendRedirect(req.getContextPath() + "/hotel/profile");
                    }
                } else {
                    req.getSession().setAttribute("mess", "warning|Mật khẩu không trùng khớp");
                    resp.sendRedirect(req.getContextPath() + "/hotel/profile");
                }
            } else {
                req.getSession().setAttribute("mess", "warning|Mật khẩu cũ không đúng");
                resp.sendRedirect(req.getContextPath() + "/hotel/profile");
            }
        }
    }
    @WebServlet("/hotel/update-avatar")
    @MultipartConfig(
            fileSizeThreshold = 1024 * 1024,
            maxFileSize = 1024 * 1024 * 50,
            maxRequestSize = 1024 * 1024 * 50
    )
    public static class UpdateAvatar extends HttpServlet{
        @Override
        protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            try {
                String newFileName = UploadImage.saveImage(req, "avatar");
                String hotel_id = req.getSession().getAttribute("hotel").toString();
                if (HotelDao.updateAvatar(newFileName, hotel_id)){
                    req.getSession().setAttribute("mess", "success|Cập nhật thành công");
                    resp.sendRedirect(req.getContextPath() + "/hotel/profile");
                }else {
                    req.getSession().setAttribute("mess", "error|Lỗi hệ thống");
                    resp.sendRedirect(req.getContextPath() + "/hotel/profile");
                }
            } catch (Exception e){
                e.printStackTrace();
                req.getSession().setAttribute("mess", "error|Lỗi hệ thống");
                resp.sendRedirect(req.getContextPath() + "/hotel/profile");
            }
        }
    }

    @WebServlet("/admin/block-hotel")
    public static class BlockHotel extends HttpServlet{
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            String hotel_id = req.getParameter("hotel_id");
            if (HotelDao.changeBlockHotel(hotel_id)){
                req.getSession().setAttribute("mess", "success|Cập nhật thành công");
                resp.sendRedirect(req.getContextPath() + "/admin/hotel-control");
            } else {
                req.getSession().setAttribute("mess", "error|Cập nhật không thành công");
                resp.sendRedirect(req.getContextPath() + "/admin/hotel-control");
            }
        }
    }
    @WebServlet("/admin/verrify-hotel")
    public static class VerifyHotel extends HttpServlet{
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            String hotel_id = req.getParameter("hotel_id");
            if (HotelDao.changeVerifyHotel(hotel_id)){
                req.getSession().setAttribute("mess", "success|Cập nhật thành công");
                resp.sendRedirect(req.getContextPath() + "/admin/hotel-control");
            } else {
                req.getSession().setAttribute("mess", "error|Cập nhật không thành công");
                resp.sendRedirect(req.getContextPath() + "/admin/hotel-control");
            }
        }
    }
    @WebServlet("/hotel/statistic")
    public static class Statistic extends HttpServlet {
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            req.getRequestDispatcher("/views/hotel/statistic.jsp").forward(req, resp);
        }
    }
    @WebServlet("/hotel/api/hotel-get-statistics-data")
    public static class HotelGetAllBookings extends HttpServlet {
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            String hotel_id = req.getSession().getAttribute("hotel").toString();
            ArrayList<Payment> payments = PaymentDao.getAllPaymentsOfAHotel(hotel_id);
            ArrayList<RoomType> roomTypes = RoomTypeDao.getAllRoomTypesOfAHotel(Integer.parseInt(hotel_id));
            ArrayList<Booking> bookings = BookingDao.getAllBookingsOfHotel(hotel_id);
            JsonObject jsonObject = new JsonObject();
            Gson gson = new Gson();
            jsonObject.addProperty("payments", gson.toJson(payments));
            jsonObject.addProperty("roomTypes", gson.toJson(roomTypes));
            jsonObject.addProperty("bookings", gson.toJson(bookings));
            resp.setContentType("application/json");
            resp.getWriter().write(gson.toJson(jsonObject));
        }
    }
}
