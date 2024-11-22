package Controller;

import Dao.CustomerDao;
import Dao.PaymentDao;
import Model.Customer;
import Model.Payment;
import Util.UploadImage;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.util.ArrayList;

public class CustomerController {
    @WebServlet("/customer/profile")
    public static class profile extends HttpServlet {
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            req.getRequestDispatcher("/views/customer/profile.jsp").forward(req, resp);
        }
    }

    @WebServlet("/customer/change-password")
    public static class changePassword extends HttpServlet {
        @Override
        protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            String customer_id = req.getSession().getAttribute("customer").toString();
            String old_password = req.getParameter("old_password");
            Customer customer = CustomerDao.getCustomerWithId(Integer.parseInt(customer_id));
            assert customer != null;
            if (customer.password == null) {
                updatePassword(req, resp, customer_id);
            } else {
                if (BCrypt.checkpw(old_password, customer.password)) {
                    updatePassword(req, resp, customer_id);
                } else {
                    req.getSession().setAttribute("mess", "warning|Mật khẩu cũ không đúng");
                    resp.sendRedirect(req.getContextPath() + "/customer/profile");
                }
            }
        }

        private void updatePassword(HttpServletRequest req, HttpServletResponse resp, String customer_id) throws IOException {
            String new_password = req.getParameter("new_password");
            String re_password = req.getParameter("re_password");
            if (new_password.equals(re_password)) {
                String new_hash_password = BCrypt.hashpw(new_password, BCrypt.gensalt());
                boolean check = CustomerDao.updatePassword(customer_id, new_hash_password);
                if (check) {
                    req.getSession().setAttribute("mess", "success|Đổi mật khẩu thành công");
                    resp.sendRedirect(req.getContextPath() + "/customer/profile");
                } else {
                    req.getSession().setAttribute("mess", "error|Lỗi hệ thống");
                    resp.sendRedirect(req.getContextPath() + "/customer/profile");
                }
            } else {
                req.getSession().setAttribute("mess", "warning|Mật khẩu không trùng khớp");
                resp.sendRedirect(req.getContextPath() + "/customer/profile");
            }
        }
    }

    @WebServlet("/customer/change-avatar")
    @MultipartConfig(
            fileSizeThreshold = 1024 * 1024,
            maxFileSize = 1024 * 1024 * 50,
            maxRequestSize = 1024 * 1024 * 50
    )
    public static class changeAvatar extends HttpServlet {
        @Override
        protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            try {
                String newFileName = UploadImage.saveImage(req, "avatar");
                String customer_id = req.getSession().getAttribute("customer").toString();
                boolean check = CustomerDao.updateAvatar(customer_id, newFileName);
                if (check) {
                    req.getSession().setAttribute("mess", "success|Cập nhật ảnh đại diện thành công");
                    resp.sendRedirect(req.getContextPath() + "/customer/profile");
                } else {
                    req.getSession().setAttribute("mess", "error|Lỗi hệ thống");
                    resp.sendRedirect(req.getContextPath() + "/customer/profile");
                }
            } catch (Exception e) {
                e.printStackTrace();
                req.getSession().setAttribute("mess", "error|Lỗi hệ thống");
                resp.sendRedirect(req.getContextPath() + "/customer/profile");
            }
        }
    }

    @WebServlet("/customer/update-profile")
    public static class updateProfile extends HttpServlet {
        @Override
        protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            String phone = req.getParameter("phone");
            String email = req.getParameter("email");
            String customer_id = req.getSession().getAttribute("customer").toString();
            if (!CustomerDao.checkEmailExcept(email, customer_id)) {
                req.getSession().setAttribute("mess", "warning|Email đã được sử dụng");
                resp.sendRedirect(req.getContextPath() + "/customer/profile");
            } else {
                if (!CustomerDao.checkPhoneExcept(phone, customer_id)) {
                    req.getSession().setAttribute("mess", "warning|Số điện thoại đã được sử dụng");
                    resp.sendRedirect(req.getContextPath() + "/customer/profile");
                } else {
                    String name = req.getParameter("name");
                    String dob = req.getParameter("dob");
                    boolean check = CustomerDao.updateProfile(name, email, phone, dob, customer_id);
                    if (check) {
                        req.getSession().setAttribute("mess", "success|Cập nhật thành công");
                        resp.sendRedirect(req.getContextPath() + "/customer/profile");
                    } else {
                        req.getSession().setAttribute("mess", "error|Lỗi hệ thống");
                        resp.sendRedirect(req.getContextPath() + "/customer/profile");
                    }
                }
            }
        }
    }

    @WebServlet("/admin/block-customer")
    public static class AdminBlockUser extends HttpServlet {
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            String customer_id = req.getParameter("customer_id");
            if (CustomerDao.blockCustomer(customer_id)) {
                req.getSession().setAttribute("mess", "success|Cập nhật thành công");
                resp.sendRedirect(req.getContextPath() + "/admin/customer-control");
            } else {
                req.getSession().setAttribute("mess", "error|Cập nhật không thành công");
                resp.sendRedirect(req.getContextPath() + "/admin/customer-control");
            }
        }
    }

    @WebServlet("/customer/transaction")
    public static class CustomerViewTransaction extends HttpServlet{
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            String customer_id = req.getSession().getAttribute("customer").toString();
            ArrayList<Payment> payments = PaymentDao.getPaymentsOfCustomer(customer_id);
            req.setAttribute("payments", payments);
            req.getRequestDispatcher("/views/customer/transaction.jsp").forward(req, resp);
        }
    }
}
