package Controller;

import Dao.BookingDao;
import Dao.PaymentDao;
import Model.Booking;
import Model.BookingStatus;
import Util.Config;
import Util.VNPayUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class PaymentController {
    @WebServlet("/customer/get-vnpay-url")
    public static class GetVNPayUrlServlet extends HttpServlet {
        public static int countDays(Date from_date, Date to_date) {
            LocalDate startDate = from_date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate endDate = to_date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);
            return (int) daysBetween;
        }
        @Override
        protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            String booking_id = req.getParameter("booking_id");
            Booking booking = BookingDao.getBookingWithId(booking_id);
            if (booking == null) {
                req.getSession().setAttribute("mess", "warning|Đơn đặt phòng này không tồn tại.");
                resp.sendRedirect(req.getContextPath() + "/customer/booking");
            } else {
                if (booking.payment_id == 0 && booking.status == BookingStatus.NOT_PAID){
                    String vnp_Version = "2.1.0";
                    String vnp_Command = "pay";
                    String orderType = "other";
                    long amount = booking.temp_price * 100L * countDays(booking.check_in_date, booking.check_out_date);
                    String bankCode = req.getParameter("bankCode");
                    String vnp_TxnRef = VNPayUtil.getRandomNumber(8);
                    String vnp_IpAddr = VNPayUtil.getIpAddress(req);
                    String vnp_TmnCode = Config.vnp_TmnCode;
                    Map<String, String> vnp_Params = new HashMap<>();
                    vnp_Params.put("vnp_Version", vnp_Version);
                    vnp_Params.put("vnp_Command", vnp_Command);
                    vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
                    vnp_Params.put("vnp_Amount", String.valueOf(amount));
                    vnp_Params.put("vnp_CurrCode", "VND");
                    if (bankCode != null && !bankCode.isEmpty()) {
                        vnp_Params.put("vnp_BankCode", bankCode);
                    }
                    vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
                    String vnp_OrderInfo = UUID.randomUUID() + "-" + System.currentTimeMillis() + "|" + booking.id;
                    vnp_Params.put("vnp_OrderInfo", vnp_OrderInfo);
                    vnp_Params.put("vnp_OrderType", orderType);
                    String locate = req.getParameter("language");
                    if (locate != null && !locate.isEmpty()) {
                        vnp_Params.put("vnp_Locale", locate);
                    } else {
                        vnp_Params.put("vnp_Locale", "vn");
                    }
                    vnp_Params.put("vnp_ReturnUrl", Config.vnp_ReturnUrl);
                    vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
                    Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
                    SimpleDateFormat sqlFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String vnp_CreateDate = formatter.format(cld.getTime());
                    vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
                    cld.add(Calendar.MINUTE, 15);
                    String vnp_ExpireDate = formatter.format(cld.getTime());
                    vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);
                    List fieldNames = new ArrayList(vnp_Params.keySet());
                    Collections.sort(fieldNames);
                    StringBuilder hashData = new StringBuilder();
                    StringBuilder query = new StringBuilder();
                    Iterator itr = fieldNames.iterator();
                    while (itr.hasNext()) {
                        String fieldName = (String) itr.next();
                        String fieldValue = (String) vnp_Params.get(fieldName);
                        if ((fieldValue != null) && (fieldValue.length() > 0)) {
                            //Build hash data
                            hashData.append(fieldName);
                            hashData.append('=');
                            hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                            //Build query
                            query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                            query.append('=');
                            query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                            if (itr.hasNext()) {
                                query.append('&');
                                hashData.append('&');
                            }
                        }
                    }
                    String queryUrl = query.toString();
                    String vnp_SecureHash = VNPayUtil.hmacSHA512(Config.secretKey, hashData.toString());
                    queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
                    String paymentUrl = Config.vnp_PayUrl + "?" + queryUrl;
                    com.google.gson.JsonObject job = new JsonObject();
                    job.addProperty("code", "00");
                    job.addProperty("message", "success");
                    job.addProperty("data", paymentUrl);
                    Gson gson = new Gson();
                    resp.getWriter().write(gson.toJson(job));
                } else {
                    req.getSession().setAttribute("mess", "warning|Đơn đặt phòng đã thanh toán hoặc đã bị hủy.");
                    resp.sendRedirect(req.getContextPath() + "/customer/booking");
                }
            }
        }
    }

    @WebServlet("/customer/vnpay-result")
    public static class GetVNPayResult extends HttpServlet{
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            Map<String, String> fields = new HashMap<>();
            for (Enumeration<?> params = req.getParameterNames(); params.hasMoreElements();) {
                String fieldName = URLEncoder.encode((String) params.nextElement(), StandardCharsets.US_ASCII.toString());
                String fieldValue = URLEncoder.encode(req.getParameter(fieldName), StandardCharsets.US_ASCII.toString());
                if ((fieldValue != null) && (fieldValue.length() > 0)) {
                    fields.put(fieldName, fieldValue);
                }
            }
            fields.remove("vnp_SecureHashType");
            fields.remove("vnp_SecureHash");
            String signValue = VNPayUtil.hashAllFields(fields);
            if (signValue.equals(req.getParameter("vnp_SecureHash"))){
                SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
                SimpleDateFormat sqlFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String amount = req.getParameter("vnp_Amount").replaceFirst("00", "");
                String paid_at = req.getParameter("vnp_PayDate");
                try {
                    paid_at = sqlFormatter.format(formatter.parse(paid_at));
                } catch (ParseException e) {
                    req.getSession().setAttribute("mess", "error|Đã có lỗi xảy ra.");
                    resp.sendRedirect(req.getContextPath() + "/user/recharge-balance");
                }
                String vnp_TransactionStatus = req.getParameter("vnp_TransactionStatus");
                String vnp_TransactionNo = req.getParameter("vnp_TransactionNo");
                String vnp_BankTranNo = req.getParameter("vnp_BankTranNo");
                String vnp_CardType = req.getParameter("vnp_CardType");
                String vnp_BankCode = req.getParameter("vnp_BankCode");
                String vnp_OrderInfo = req.getParameter("vnp_OrderInfo");
                String vnp_TxnRef = req.getParameter("vnp_TxnRef");
                String customer_id = req.getSession().getAttribute("customer").toString();
                if (PaymentDao.handleVNPayResult(amount, paid_at, vnp_TransactionStatus, vnp_TransactionNo, vnp_BankTranNo, vnp_CardType, vnp_BankCode, vnp_OrderInfo, vnp_TxnRef, customer_id)) {
                    req.getSession().setAttribute("mess", "success|Thanh toán thành công.");
                    resp.sendRedirect(req.getContextPath() + "/customer/booking");
                } else {
                    req.getSession().setAttribute("mess", "error|Đã có lỗi xảy ra.");
                    resp.sendRedirect(req.getContextPath() + "/customer/booking");
                }
            } else {
                req.getSession().setAttribute("mess", "error|Chữ kí không hợp lệ.");
                resp.sendRedirect(req.getContextPath() + "/customer/booking");
            }
        }
    }
}
