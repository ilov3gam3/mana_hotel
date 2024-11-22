package Controller;

import Dao.AdminDao;
import Dao.CustomerDao;
import Dao.HotelDao;
import Model.Admin;
import Model.Customer;
import Model.Hotel;
import Util.Config;
import Util.Mail;
import Util.UploadImage;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.mindrot.jbcrypt.BCrypt;
import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AuthController {
    @WebServlet("/login")
    public static class login extends HttpServlet {
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            req.getRequestDispatcher("/views/authentication/login.jsp").forward(req, resp);
        }

        @Override
        protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
            String email = req.getParameter("email");
            Customer customer = CustomerDao.getCustomerWithEmail(email);
            if (customer == null){
                req.getSession().setAttribute("mess", "warning|Sai email hoặc mật khẩu");
                resp.sendRedirect(req.getContextPath() + "/login");
            } else {
                if (!customer.is_verified){
                    req.getSession().setAttribute("mess", "warning|Tài khoản chưa được kích hoạt");
                    resp.sendRedirect(req.getContextPath() + "/login");
                } else {
                    if (customer.is_blocked){
                        req.getSession().setAttribute("mess", "warning|Tài khoản đã bị chặn");
                        resp.sendRedirect(req.getContextPath() + "/login");
                    } else {
                        String password = req.getParameter("password");
                        if (BCrypt.checkpw(password, customer.password)){
                            req.getSession().setAttribute("mess", "success|Đăng nhập thành công.");
                            req.getSession().setAttribute("customer", customer.id);
                            req.getSession().removeAttribute("admin");
                            req.getSession().removeAttribute("hotel");
                            resp.sendRedirect(req.getContextPath() + "/");
                        } else {
                            req.getSession().setAttribute("mess", "warning|Sai email hoặc mật khẩu");
                            resp.sendRedirect(req.getContextPath() + "/login");
                        }
                    }
                }
            }
        }
    }

    @WebServlet("/register")
    public static class register extends HttpServlet {
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            req.getRequestDispatcher("/views/authentication/register.jsp").forward(req, resp);
        }

        @Override
        protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
            String password = req.getParameter("password");
            String re_password = req.getParameter("re_password");
            if (password.equals(re_password)) {
                String email = req.getParameter("email");
                if (CustomerDao.checkEmail(email)) {
                    String phone = req.getParameter("phone");
                    if (CustomerDao.checkPhone(phone)) {
                        String token = "verify_email-" + UUID.randomUUID() + "-" +  System.currentTimeMillis();
                        ExecutorService executorService = Executors.newSingleThreadExecutor();
                        executorService.submit(() -> {
                            try {
                                String url = Config.app_url + req.getContextPath() + "/verify-email?token=" + token;
                                String html = "Chúc mừng bạn đã đăng kí thành công, vui lòng nhấn vào <a href='url'>đây</a> để xác thực email của bạn.".replace("url", url);
                                Mail.send(email, "Đăng kí tài khoản", html);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                        executorService.shutdown();
                        String name = req.getParameter("name");
                        String dob = req.getParameter("dob");
                        String is_verified = "false";
                        String avatar = "assets/img/profile-img.jpg";
                        String salt = BCrypt.gensalt();
                        String hash_password = BCrypt.hashpw(password, salt);
                        boolean check = CustomerDao.addNewCustomer(name, email, phone, dob, avatar, hash_password, is_verified, token);
                        if (check) {
                            req.getSession().setAttribute("mess", "success|Đăng kí thành công, vui lòng kiểm tra email");
                            resp.sendRedirect(req.getContextPath() + "/login");
                        } else {
                            req.getSession().setAttribute("mess", "error|Lỗi server");
                            resp.sendRedirect(req.getContextPath() + "/register");
                        }
                    } else {
                        req.getSession().setAttribute("mess", "warning|Số điện thoại đã được sử dụng");
                        resp.sendRedirect(req.getContextPath() + "/register");
                    }
                } else {
                    req.getSession().setAttribute("mess", "warning|Email đã được sử dụng");
                    resp.sendRedirect(req.getContextPath() + "/register");
                }
            } else {
                req.getSession().setAttribute("mess", "warning|Mật khẩu không trùng khớp");
                resp.sendRedirect(req.getContextPath() + "/register");
            }
        }
    }
    @WebServlet("/verify-email")
    public static class verify extends HttpServlet{
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
            String token = req.getParameter("token");
            boolean check = CustomerDao.verifyCustomerWithToken(token);
            if (check){
                req.getSession().setAttribute("mess", "success|Kích hoạt tài khoản thành công");
                resp.sendRedirect(req.getContextPath() + "/login");
            } else {
                req.getSession().setAttribute("mess", "warning|Đường dẫn không tồn tại.");
                resp.sendRedirect(req.getContextPath() + "/");
            }
        }
    }
    @WebServlet("/logout")
    public static class logout extends HttpServlet{
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            req.getSession().removeAttribute("customer");
            req.getSession().removeAttribute("admin");
            req.getSession().removeAttribute("hotel");
            resp.sendRedirect(req.getContextPath() + "/");
        }
    }

    @WebServlet("/login-google")
    public static class loginGoogle extends HttpServlet{
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            String authorizationCode = req.getParameter("code");
            GoogleAuthorizationCodeFlow googleAuthorizationCodeFlow = new GoogleAuthorizationCodeFlow.Builder(
                    new NetHttpTransport(),
                    new JacksonFactory(),
                    Config.google_oauth_client_id,
                    Config.google_oauth_client_secret,
                    Arrays.asList("openid", "profile", "email")
            ).build();
            GoogleTokenResponse googleTokenResponse = new GoogleAuthorizationCodeTokenRequest(
                    new NetHttpTransport(),
                    new JacksonFactory(),
                    "https://oauth2.googleapis.com/token",
                    Config.google_oauth_client_id,
                    Config.google_oauth_client_secret,
                    authorizationCode,
                    Config.google_oauth_redirect_uri
            ).execute();
            GoogleIdToken googleIdToken = googleTokenResponse.parseIdToken();
            GoogleIdToken.Payload payload = googleIdToken.getPayload();
            String email = payload.getEmail();
            String name = (String) payload.get("name");
            String avatar = (String) payload.get("picture");
            if (CustomerDao.checkEmail(email)){ // chua co acc
                boolean check = CustomerDao.addNewCustomer(name, email, avatar, "true");
                if (check){
                    Customer customer = CustomerDao.getCustomerWithEmail(email);
                    req.getSession().setAttribute("mess", "success|Đăng nhập thành công.");
                    assert customer != null;
                    req.getSession().setAttribute("customer", customer.id);
                    resp.sendRedirect(req.getContextPath() + "/");
                } else {
                    req.getSession().setAttribute("mess", "error|Lỗi server");
                    resp.sendRedirect(req.getContextPath() + "/register");
                }
            } else {
                Customer customer = CustomerDao.getCustomerWithEmail(email);
                req.getSession().setAttribute("mess", "success|Đăng nhập thành công.");
                assert customer != null;
                req.getSession().setAttribute("customer", customer.id);
                resp.sendRedirect(req.getContextPath() + "/");
            }
        }
    }

    @WebServlet("/forgot-password")
    public static class forgotPasswordForm extends HttpServlet{
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            req.getRequestDispatcher("/views/authentication/forgot-password.jsp").forward(req, resp);
        }

        @Override
        protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            String email = req.getParameter("email");
            String token = "forgot_password-" + UUID.randomUUID() + "-" +  System.currentTimeMillis();
            boolean check = CustomerDao.updateTokenForgotPassword(token, email);
            if (check){
                ExecutorService executorService = Executors.newSingleThreadExecutor();
                executorService.submit(() -> {
                    try {
                        String url = Config.app_url + req.getContextPath() + "/reset-password?token=" + token;
                        String html = "Vui lòng nhấn vào <a href='url'>đây</a> để đặt lại mật khẩu của bạn.".replace("url", url);
                        Mail.send(email, "Đặt lại mật khẩu", html);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                executorService.shutdown();
                req.getSession().setAttribute("mess", "success|Vui lòng kiểm tra email");
                resp.sendRedirect(req.getContextPath() + "/forgot-password");
            } else {
                req.getSession().setAttribute("mess", "error|Lỗi server");
                resp.sendRedirect(req.getContextPath() + "/forgot-password");
            }

        }
    }
    @WebServlet("/reset-password")
    public static class forgotPassword extends HttpServlet{
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            req.getRequestDispatcher("/views/authentication/reset-password.jsp").forward(req, resp);
        }

        @Override
        protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            String token = req.getParameter("token");
            String password = req.getParameter("password");
            String re_password = req.getParameter("re_password");
            if (password.equals(re_password)){
                boolean check = CustomerDao.resetPassword(BCrypt.hashpw(password, BCrypt.gensalt()), token);
                if (check){
                    req.getSession().setAttribute("mess", "success|Đặt lại mật khẩu thành công");
                    resp.sendRedirect(req.getContextPath() + "/login");
                } else {
                    req.getSession().setAttribute("mess", "error|Lỗi server");
                    resp.sendRedirect(req.getContextPath() + "/forgot-password");
                }
            } else {
                req.getSession().setAttribute("mess", "warning|Mật khẩu không trùng khớp");
                resp.sendRedirect(req.getContextPath() + "/reset-password?token=" + token);
            }
        }
    }

    @WebServlet("/admin/login")
    public static class adminLogin extends HttpServlet{
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            req.getRequestDispatcher("/views/admin/login.jsp").forward(req, resp);
        }

        @Override
        protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            String username = req.getParameter("username");
            String password = req.getParameter("password");
            Admin admin = AdminDao.adminLogin(username, password);
            if (admin == null){
                req.getSession().setAttribute("mess", "warning|Sai username hoặc mật khẩu");
                resp.sendRedirect(req.getContextPath() + "/admin/login");
            } else {
                req.getSession().setAttribute("mess", "success|Đăng nhập thành công.");
                req.getSession().setAttribute("admin", admin.id);
                req.getSession().removeAttribute("customer");
                req.getSession().removeAttribute("hotel");
                resp.sendRedirect(req.getContextPath() + "/");
            }
        }
    }

    @WebServlet("/hotel/login")
    public static class hotelLogin extends HttpServlet{
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            req.getRequestDispatcher("/views/hotel/login.jsp").forward(req, resp);
        }

        @Override
        protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
            String email = req.getParameter("email");
            String password = req.getParameter("password");
            Hotel hotel = HotelDao.hotelLogin(email, password);
            if (hotel != null){
                if (hotel.is_verified){
                    if (!hotel.is_blocked){
                        req.getSession().setAttribute("mess", "success|Đăng nhập thành công.");
                        req.getSession().setAttribute("hotel", hotel.id);
                        req.getSession().removeAttribute("admin");
                        req.getSession().removeAttribute("customer");
                        resp.sendRedirect(req.getContextPath() + "/");
                    } else {
                        req.getSession().setAttribute("mess", "error|Tài khoản này đã bị block, vui lòng liên hệ admin.");
                        resp.sendRedirect(req.getContextPath() + "/hotel/login");
                    }
                } else {
                    req.getSession().setAttribute("mess", "error|Tài khoản này chưa được xác thực, vui lòng liên hệ admin.");
                    resp.sendRedirect(req.getContextPath() + "/hotel/login");
                }
            } else {
                req.getSession().setAttribute("mess", "error|Đăng nhập không thành công.");
                resp.sendRedirect(req.getContextPath() + "/hotel/login");
            }
        }
    }
    @WebServlet("/hotel-register")
    @MultipartConfig(
            fileSizeThreshold = 1024 * 1024,
            maxFileSize = 1024 * 1024 * 50,
            maxRequestSize = 1024 * 1024 * 50
    )
    public static class HotelRegister extends HttpServlet{
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            req.getRequestDispatcher("/views/authentication/hotel-register.jsp").forward(req, resp);
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
                req.getSession().setAttribute("mess", "success|Thêm mới hotel thành công, chờ admin duyệt.");
                resp.sendRedirect(req.getContextPath() + "/hotel-register");
            } else {
                req.getSession().setAttribute("mess", "error|Đã có lỗi xảy ra.");
                resp.sendRedirect(req.getContextPath() + "/hotel-register");
            }
        }
    }
}
