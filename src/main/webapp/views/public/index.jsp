<%@ page import="Model.RoomType" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="Dao.RoomTypeDao" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<%@include file="../master/head.jsp" %>
<%--<link href="<%=request.getContextPath()%>/assets/css/bootstrap-rating.css" rel="stylesheet">--%>
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
            <div class="col-12 row">
                <%ArrayList<RoomType> roomTypesTop3Rate = RoomTypeDao.selectTop3Rating();%>
                <%=roomTypesTop3Rate.isEmpty() ? "Không tìm thấy phòng nào" : ""%>
                <div class="card">
                    <div class="card-body">
                        <h5 class="card-title">Các phòng có lượt đánh giá tốt nhất</h5>
                        <div class="row row-cols-1 row-cols-sm-2 row-cols-md-3 g-3">
                            <% for (int i = 0; i < roomTypesTop3Rate.size(); i++) { %>
                            <div class="col">
                                <div class="card shadow-sm">
                                    <img style="object-fit: cover" class="bd-placeholder-img card-img-top" width="100%" height="225" src="<%=request.getContextPath()%>/<%=roomTypesTop3Rate.get(i).images[0].url%>" alt="">
                                    <div class="card-body">
                                        <p class="card-text"><%=roomTypesTop3Rate.get(i).hotel_name%></p>
                                        <p class="card-text"><%=roomTypesTop3Rate.get(i).name%></p>
                                        <p class="card-text" style="max-height: 100px; min-height: 100px;overflow: hidden;text-overflow: ellipsis"><%=roomTypesTop3Rate.get(i).description%></p>
                                        <p class="card-text"><%=roomTypesTop3Rate.get(i).price%>(vnd)</p>
                                        <div class="d-flex justify-content-between align-items-center">
                                            <div class="btn-group">
                                                <a href="<%=request.getContextPath()%>/room-type?id=<%=roomTypesTop3Rate.get(i).id%>">
                                                    <button type="button" class="btn btn-outline-secondary">Xem chi tiết</button>
                                                </a>
                                            </div>
                                            <small class="text-muted"><%=roomTypesTop3Rate.get(i).booked%> lượt đặt</small>
                                            <% if (roomTypesTop3Rate.get(i).rating == null) { %>
                                            <small class="text-muted">Chưa có đánh giá</small>
                                            <% } else { %>
                                            <small class="text-muted"><%=roomTypesTop3Rate.get(i).rating%><img style="max-height: 20px" src="${pageContext.request.contextPath}/assets/img/star-yellow.png" alt=""></small>
                                            <% } %>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <% } %>
                        </div>
                    </div>
                </div>
            </div>

            <div class="col-12 row">
                <%ArrayList<RoomType> roomTypesTop3Book = RoomTypeDao.selectTop3Book();%>
                <%=roomTypesTop3Rate.isEmpty() ? "Không tìm thấy phòng nào" : ""%>
                <div class="card">
                    <div class="card-body">
                        <h5 class="card-title">Các phòng có nhiều lượt đặt nhất</h5>
                        <div class="row row-cols-1 row-cols-sm-2 row-cols-md-3 g-3">
                            <% for (int i = 0; i < roomTypesTop3Book.size(); i++) { %>
                            <div class="col">
                                <div class="card shadow-sm">
                                    <img style="object-fit: cover" class="bd-placeholder-img card-img-top" width="100%" height="225" src="<%=request.getContextPath()%>/<%=roomTypesTop3Book.get(i).images[0].url%>" alt="">
                                    <div class="card-body">
                                        <p class="card-text"><%=roomTypesTop3Book.get(i).hotel_name%></p>
                                        <p class="card-text"><%=roomTypesTop3Book.get(i).name%></p>
                                        <p class="card-text" style="max-height: 100px; min-height: 100px;overflow: hidden;text-overflow: ellipsis"><%=roomTypesTop3Book.get(i).description%></p>
                                        <p class="card-text"><%=roomTypesTop3Book.get(i).price%>(vnd)</p>
                                        <div class="d-flex justify-content-between align-items-center">
                                            <div class="btn-group">
                                                <a href="<%=request.getContextPath()%>/room-type?id=<%=roomTypesTop3Book.get(i).id%>">
                                                    <button type="button" class="btn btn-outline-secondary">Xem chi tiết</button>
                                                </a>
                                            </div>
                                            <small class="text-muted"><%=roomTypesTop3Book.get(i).booked%> lượt đặt</small>
                                            <% if (roomTypesTop3Book.get(i).rating == null) { %>
                                            <small class="text-muted">Chưa có đánh giá</small>
                                            <% } else { %>
                                            <small class="text-muted"><%=roomTypesTop3Book.get(i).rating%><img style="max-height: 20px" src="${pageContext.request.contextPath}/assets/img/star-yellow.png" alt=""></small>
                                            <% } %>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <% } %>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>

</main>
<!-- End #main -->

<!-- ======= Footer ======= -->
<%@include file="../master/footer.jsp" %>
<!-- End Footer -->

<%@include file="../master/js.jsp" %>
<%--<script type="text/javascript" src="<%=request.getContextPath()%>/assets/js/bootstrap-rating.js"></script>--%>
</body>
</html>