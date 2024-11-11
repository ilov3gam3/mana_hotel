package Controller;

import Dao.*;
import Model.*;
import Util.UploadImage;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

public class RoomTypeController {
    @WebServlet("/hotel/manage-room-type")
    @MultipartConfig(
            fileSizeThreshold = 1024 * 1024,
            maxFileSize = 1024 * 1024 * 50,
            maxRequestSize = 1024 * 1024 * 50
    )
    public static class HotelViewRoomType extends HttpServlet{
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            ArrayList<RoomType> roomTypes = RoomTypeDao.getAllRoomTypesOfAHotel((Integer) req.getSession().getAttribute("hotel"));
            req.setAttribute("roomTypes", roomTypes);
            ArrayList<Utility> utilities = UtilityDao.getAllUtilities();
            req.setAttribute("utilities", utilities);
            req.getRequestDispatcher("/views/hotel/room-type.jsp").forward(req, resp);
        }

        @Override
        protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
            String hotel_id = req.getSession().getAttribute("hotel").toString();
            String name = req.getParameter("name");
            String description = req.getParameter("description");
            String beds = req.getParameter("beds");
            String area = req.getParameter("area");
            String price = req.getParameter("price");
            String[] utility_ids = req.getParameterValues("utility_ids");
            ArrayList<String> fileNames = UploadImage.multipleFileUpload(req, "images");
            ArrayList<Integer> images_ids = DBContext.insertGetLastIds("insert into images(url) values (?);", fileNames.toArray(new String[0]));
            if (RoomTypeDao.addRoomType(name, description, hotel_id, beds, area, price, utility_ids, images_ids)){
                req.getSession().setAttribute("mess", "success|Thêm mới thành công.");
            } else {
                req.getSession().setAttribute("mess", "error|Lỗi hệ thống");
            }
            resp.sendRedirect(req.getContextPath() + "/hotel/manage-room-type");
        }
    }
    @WebServlet("/hotel/update-room-type")
    @MultipartConfig(
            fileSizeThreshold = 1024 * 1024,
            maxFileSize = 1024 * 1024 * 50,
            maxRequestSize = 1024 * 1024 * 50
    )
    public static class UpdateRoomType extends HttpServlet{
        @Override
        protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
            String hotel_id = req.getSession().getAttribute("hotel").toString();
            String id = req.getParameter("id");
            String name = req.getParameter("name");
            String description = req.getParameter("description");
            String beds = req.getParameter("beds");
            String area = req.getParameter("area");
            String price = req.getParameter("price");
            String images_to_delete = req.getParameter("images_to_delete");
            String[] utility_ids = req.getParameterValues("utility_ids");
            ArrayList<String> fileNames = UploadImage.multipleFileUpload(req, "images");
            ArrayList<Integer> images_ids = DBContext.insertGetLastIds("insert into images(url) values (?);", fileNames.toArray(new String[0]));
            if (RoomTypeDao.updateRoomType(name, description, beds, area, price, hotel_id, id, images_to_delete, utility_ids, images_ids)){
                req.getSession().setAttribute("mess", "success|Cập nhật thành công.");
            } else {
                req.getSession().setAttribute("mess", "error|Lỗi hệ thống");
            }
            resp.sendRedirect(req.getContextPath() + "/hotel/manage-room-type");
        }
    }
    @WebServlet("/room-type")
    public static class ViewRoomType extends HttpServlet{
        public static boolean isDateInFuture(String date) {
            try {
                LocalDate givenDate = LocalDate.parse(date);
                LocalDate today = LocalDate.now();
                return !givenDate.isBefore(today);
            } catch (DateTimeParseException e) {
                e.printStackTrace();
                return false;
            }
        }
        public static boolean isFromDateBeforeToDate(String from_date, String to_date) {
            try {
                LocalDate firstDate = LocalDate.parse(from_date);
                LocalDate secondDate = LocalDate.parse(to_date);
                return firstDate.isBefore(secondDate);
            }catch (DateTimeParseException e) {
                e.printStackTrace();
                return false;
            }
        }
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            String id = req.getParameter("id");
            RoomType roomType = RoomTypeDao.getRoomTypeWithId(id);
            if (roomType == null){
                req.getSession().setAttribute("mess", "error|Phòng này không tồn tại.");
                resp.sendRedirect(req.getContextPath() + "/search");
            } else {
                req.setAttribute("roomType", roomType);
                Hotel hotel = HotelDao.getHotelWithId(roomType.hotel_id);
                req.setAttribute("hotel", hotel);
                if (req.getParameter("from_date") != null && req.getParameter("to_date") != null) {
                    String from_date = req.getParameter("from_date");
                    if (isDateInFuture(from_date)) {
                        String to_date = req.getParameter("to_date");
                        if (isFromDateBeforeToDate(from_date, to_date)) {
                            ArrayList<Room> rooms = RoomDao.getAvailableRoom(from_date, to_date, id);
                            req.setAttribute("rooms", rooms);
                        } else {
                            req.setAttribute("warning", "Ngày bắt đầu phải trước ngày kết thúc");
                        }
                    } else {
                        req.setAttribute("warning", "Ngày bắt đầu không được ở trong quá khứ");
                    }
                }
                ArrayList<Review> reviews = ReviewDao.getAllReviewsOfARoomType(id);
                req.setAttribute("reviews", reviews);
                req.getRequestDispatcher("/views/public/room-type.jsp").forward(req, resp);
            }
        }
    }
    @WebServlet("/hotel/change-room-type-hidden")
    public static class ChangeRoomTypeHidden extends HttpServlet{
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            String room_type_id = req.getParameter("room_type_id");
            if (RoomTypeDao.changeHidden(room_type_id)){
                req.getSession().setAttribute("mess", "success|Cập nhật thành công.");
            } else {
                req.getSession().setAttribute("mess", "error|Lỗi hệ thống");
            }
            resp.sendRedirect(req.getContextPath() + "/hotel/manage-room-type");
        }
    }
}
