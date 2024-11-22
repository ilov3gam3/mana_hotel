package Controller;

import Dao.DBContext;
import Dao.RoomDao;
import Dao.RoomTypeDao;
import Model.Room;
import Model.RoomType;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;

public class RoomController {
    @WebServlet("/hotel/manage-room")
    public static class ManageRoom extends HttpServlet {
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            ArrayList<RoomType> roomTypes = RoomTypeDao.getAllRoomTypesOfAHotel((Integer) req.getSession().getAttribute("hotel"));
            ArrayList<Room> rooms = RoomDao.getAllRoomsOfAHotel(req.getSession().getAttribute("hotel").toString());
            req.setAttribute("roomTypes", roomTypes);
            req.setAttribute("rooms", rooms);
            req.getRequestDispatcher("/views/hotel/room.jsp").forward(req, resp);
        }
    }

    @WebServlet("/hotel/add-room")
    @MultipartConfig(
            fileSizeThreshold = 1024 * 1024,
            maxFileSize = 1024 * 1024 * 50,
            maxRequestSize = 1024 * 1024 * 50
    )
    public static class AddRoom extends HttpServlet {

        @Override
        protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            String room_type_id = req.getParameter("room_type_id");
            String from = req.getParameter("from");
            String to = req.getParameter("to");
            String hotel_id = req.getSession().getAttribute("hotel").toString();
            if (RoomDao.addRoom(room_type_id, from, to, hotel_id)){
                req.getSession().setAttribute("mess", "success|Thêm phòng thành công.");
            } else {
                req.getSession().setAttribute("mess", "error|Lỗi hệ thống.");
            }
            resp.sendRedirect(req.getContextPath() + "/hotel/manage-room");
        }
    }
    @WebServlet("/hotel/change-availability")
    public static class ChangeAvailability extends HttpServlet{
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            String id = req.getParameter("id");
            String hotel_id = req.getSession().getAttribute("hotel").toString();
            if (DBContext.executeUpdate("update rooms set is_available = ~ is_available where id = ? and hotel_id = ?;", new String[]{id, hotel_id})){
                req.getSession().setAttribute("mess", "success|Cập nhật thành công.");
                resp.sendRedirect(req.getContextPath() + "/hotel/manage-room");
            } else {
                req.getSession().setAttribute("mess", "error|Lỗi hệ thống.");
                resp.sendRedirect(req.getContextPath() + "/hotel/manage-room");
            }
        }
    }
    @WebServlet("/hotel/update-room")
    public static class HotelUpdateRoom extends HttpServlet{
        @Override
        protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            try {
                String room_id = req.getParameter("id");
                String room_type_id = req.getParameter("room_type_id");
                String number = req.getParameter("number");
                String hotel_id = req.getSession().getAttribute("hotel").toString();
                RoomDao.updateRoom(room_id, room_type_id, number, hotel_id);
            } catch (Exception e){
                e.printStackTrace();
                req.getSession().setAttribute("mess", "error|Lỗi hệ thống.");
                resp.sendRedirect(req.getContextPath() + "/hotel/manage-room");
            }
            req.getSession().setAttribute("mess", "success|Cập nhật thành công.");
            resp.sendRedirect(req.getContextPath() + "/hotel/manage-room");
        }
    }
}
