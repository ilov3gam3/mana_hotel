<%@ page import="java.util.ArrayList" %>
<%@ page import="Model.Booking" %>
<%@ page import="Dao.BookingDao" %>
<%@ page import="Controller.PaymentController" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<%@include file="../master/head.jsp" %>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css" integrity="sha512-Kc323vGBEqzTmouAECnVceyQqyqdsSiqLQISBL29aUW4U/M7pSPA/gEUZQqv1cwx4OnYxTxve5UMg5GT6L4JJg==" crossorigin="anonymous" referrerpolicy="no-referrer" />
<body>
<!-- ======= Header ======= -->
<%@include file="../master/header.jsp" %>

<!-- End Header -->
<!-- ======= Sidebar ======= -->
<%@include file="../master/sidebar.jsp" %>
<!-- End Sidebar-->
<main id="main" class="main">
    <div class="pagetitle">
        <h1>Dashboard</h1>
        <nav>
            <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="<%=request.getContextPath()%>/">Home</a></li>
                <li class="breadcrumb-item active">Dashboard</li>
            </ol>
        </nav>
    </div><!-- End Page Title -->
    <section class="section dashboard">
        <div class="row">
            <% ArrayList<Booking> bookings = BookingDao.getAllBookingsOfHotel(request.getSession().getAttribute("hotel").toString()); %>
            <table class="table datatable">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Loại phòng</th>
                        <th>Số phòng</th>
                        <th>Ngày check in</th>
                        <th>Ngày check out</th>
                        <th>Trạng thái check in</th>
                        <th>Trạng thái check out</th>
                        <th>Trạng thái</th>
                        <th>Giá</th>
                        <th>Đặt lúc</th>
                    </tr>
                </thead>
                <tbody>
                    <% for (int i = 0; i < bookings.size(); i++) { %>
                        <tr>
                            <td><%=bookings.get(i).id%></td>
                            <td><%=bookings.get(i).room_type_name%></td>
                            <td><%=bookings.get(i).room_number%></td>
                            <td><%=bookings.get(i).check_in_date.toString().replace("00:00:00.0", "")%></td>
                            <td><%=bookings.get(i).check_out_date.toString().replace("00:00:00.0", "")%></td>
                            <td>
                                <a href="<%=request.getContextPath()%>/hotel/change-booking-checkin-checkout?type=checkin&booking_id=<%=bookings.get(i).id%>">
                                    <% if(bookings.get(i).is_checked_in) { %>
                                        <button class="btn btn-danger">Hủy check in</button>
                                    <% } else { %>
                                        <button class="btn btn-success">Xác nhận check in</button>
                                    <% } %>
                                </a>
                            </td>
                            <td>
                                <a href="<%=request.getContextPath()%>/hotel/change-booking-checkin-checkout?type=checkout&booking_id=<%=bookings.get(i).id%>">
                                    <% if(bookings.get(i).is_checked_out) { %>
                                        <button class="btn btn-danger">Hủy check out</button>
                                    <% } else { %>
                                        <button class="btn btn-success">Xác nhận check out</button>
                                    <% } %>
                                </a>
                            </td>
                            <td><%=bookings.get(i).status%></td>
                            <td><%=bookings.get(i).payment_id == 0 ? String.format("%,d", bookings.get(i).temp_price * PaymentController.GetVNPayUrlServlet.countDays(bookings.get(i).check_in_date, bookings.get(i).check_out_date)) : String.format("%,d", bookings.get(i).price)%></td>
                            <td><%=bookings.get(i).created_at%></td>
                        </tr>
                    <% } %>
                </tbody>
            </table>
        </div>
    </section>

</main>
<!-- End #main -->

<!-- ======= Footer ======= -->
<%@include file="../master/footer.jsp" %>
<!-- End Footer -->

<%@include file="../master/js.jsp" %>
</body>
</html>