package Controller;

import Dao.AdminDao;
import Dao.CustomerDao;
import Dao.HotelDao;
import Model.Admin;
import Model.Customer;
import Model.Hotel;
import Util.UploadImage;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

public class AdminController {
    @WebServlet("/admin/admin-control")
    public static class adminControl extends HttpServlet{
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            ArrayList<Admin> admins = AdminDao.getAllAdmins();
            if (admins == null){
                req.getSession().setAttribute("mess", "error|Đã có lỗi xảy ra.");
                resp.sendRedirect(req.getContextPath() + "/admin");
            } else {
                req.setAttribute("admins", admins);
                req.getRequestDispatcher("/views/admin/admin-control.jsp").forward(req, resp);
            }
        }
        @Override
        protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
            String name = req.getParameter("name");
            String username = req.getParameter("username");
            String password = req.getParameter("password");
            if (AdminDao.createAdmin(name, username, password)){
                req.getSession().setAttribute("mess", "success|Tạo admin mới thành công.");
                resp.sendRedirect(req.getContextPath() + "/admin/admin-control");
            } else {
                req.getSession().setAttribute("mess", "error|Đã có lỗi xảy ra.");
                resp.sendRedirect(req.getContextPath() + "/admin/admin-control");
            }
        }
    }

    @WebServlet("/admin/update-admin")
    public static class createAdmin extends HttpServlet{
        @Override
        protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
            String id = req.getParameter("id");
            String name = req.getParameter("name");
            String username = req.getParameter("username");
            String password = req.getParameter("password");
            if (AdminDao.updateAdmin(id, name, username, password)){
                req.getSession().setAttribute("mess", "success|Tạo admin mới thành công.");
            } else {
                req.getSession().setAttribute("mess", "error|Lỗi hệ thống.");
            }
            resp.sendRedirect(req.getContextPath() + "/admin/admin-control");
        }
    }

    @WebServlet("/admin/customer-control")
    public static class customerControl extends HttpServlet{
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            ArrayList<Customer> customers = CustomerDao.getAllCustomers();
            if (customers == null){
                req.getSession().setAttribute("mess", "error|Đã có lỗi xảy ra.");
                resp.sendRedirect(req.getContextPath() + "/admin");
            } else {
                req.setAttribute("customers", customers);
                req.getRequestDispatcher("/views/admin/customer-control.jsp").forward(req, resp);
            }
        }
    }

    @WebServlet("/admin/hotel-control")
    @MultipartConfig(
            fileSizeThreshold = 1024 * 1024,
            maxFileSize = 1024 * 1024 * 50,
            maxRequestSize = 1024 * 1024 * 50
    )
    public static class hotelControl extends HttpServlet{
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            ArrayList<Hotel> hotels = HotelDao.getAllHotels();
            if (hotels == null){
                req.getSession().setAttribute("mess", "error|Đã có lỗi xảy ra.");
                resp.sendRedirect(req.getContextPath() + "/admin");
            } else {
                req.setAttribute("hotels", hotels);
                req.getRequestDispatcher("/views/admin/hotel-control.jsp").forward(req, resp);
            }
        }

        @Override
        protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            String name = req.getParameter("name");
            String email = req.getParameter("email");
            String password = req.getParameter("password");
            String address = req.getParameter("address");
            String gg_map_link = req.getParameter("gg_map_link");
            String newFileName = UploadImage.saveImage(req, "avatar");
            if (HotelDao.createNewHotel(name, email, newFileName, password, address, gg_map_link)){
                req.getSession().setAttribute("mess", "success|Thêm mới hotel thành công.");
                resp.sendRedirect(req.getContextPath() + "/admin/hotel-control");
            } else {
                req.getSession().setAttribute("mess", "error|Đã có lỗi xảy ra.");
                resp.sendRedirect(req.getContextPath() + "/admin/hotel-control");
            }
        }
    }
    @WebServlet("/admin/delete-admin")
    public static class deleteAdmin extends HttpServlet{
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            String admin_id = req.getParameter("admin_id");
            String current_admin_id = req.getSession().getAttribute("admin").toString();
            if (admin_id.equals(current_admin_id)) {
                req.getSession().setAttribute("mess", "warning|Bạn không thể xóa chỉnh bản thân.");
            } else {
                if (AdminDao.deleteAdmin(admin_id)) {
                    req.getSession().setAttribute("mess", "success|Xóa thành công.");
                } else {
                    req.getSession().setAttribute("mess", "error|Lỗi hệ thống.");
                }
            }
            resp.sendRedirect(req.getContextPath() + "/admin/admin-control");
        }
    }
}
