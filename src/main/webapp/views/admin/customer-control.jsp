<%@ page import="java.util.ArrayList" %>
<%@ page import="Model.Admin" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<%@include file="../master/head.jsp"%>
<body>
<!-- ======= Header ======= -->
<%@include file="../master/header.jsp"%>
<!-- End Header -->
<!-- ======= Sidebar ======= -->
<%@include file="../master/sidebar.jsp"%>
<!-- End Sidebar-->
<main id="main" class="main">
    <div class="pagetitle">
        <h1>Dashboard</h1>
        <nav>
            <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="<%=request.getContextPath()%>/">Quản lý người dùng</a></li>
                <li class="breadcrumb-item active">Quản lý người dùng</li>
            </ol>
        </nav>
    </div><!-- End Page Title -->
    <section class="section dashboard">
        <div class="row">
            <table class="table datatable">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Tên</th>
                    <th>Email</th>
                    <th>Số điện thoại</th>
                    <th>Ảnh đại diện</th>
                    <th>Tình trạng xác thực</th>
                    <th>Đăng kí lúc</th>
                    <th>Chặn</th>
                </tr>
                </thead>
                <tbody>
                <%ArrayList<Customer> customers = (ArrayList<Customer>) request.getAttribute("customers");%>
                <% for (int i = 0; i < customers.size(); i++) { %>
                <tr>
                    <td><%=customers.get(i).id%></td>
                    <td><%=customers.get(i).name%></td>
                    <td><%=customers.get(i).email%></td>
                    <td><%=customers.get(i).phone%></td>
                    <td><a href="<%=customers.get(i).avatar.startsWith("http") || customers.get(i).avatar.startsWith("https") ? customers.get(i).avatar : request.getContextPath() + "/" + customers.get(i).avatar%>">Xem ảnh</a></td>
                    <td><%=customers.get(i).is_verified ? "<span style='color: green'>Đã xác thực</span>" : "<span style='color: red'>Chưa xác thực</span>"%></td>
                    <td><%=customers.get(i).created_at%></td>
                    <td>
                        <div class="row">
                            <% if (customers.get(i).is_blocked) { %>
                            <a href="<%=request.getContextPath()%>/admin/block-customer?customer_id=<%=customers.get(i).id%>">
                                <button class="btn btn-success">Bỏ chặn</button>
                            </a>
                            <% } else { %>
                                <div class="col-6">
                                    <a href="<%=request.getContextPath()%>/admin/block-customer?customer_id=<%=customers.get(i).id%>">
                                        <button class="btn btn-danger">Chặn</button>
                                    </a>
                                </div>
                            <% } %>
                        </div>
                    </td>
                </tr>
                <% } %>
                </tbody>
            </table>
        </div>
    </section>

</main>
<!-- End #main -->
<!-- ======= Footer ======= -->
<%@include file="../master/footer.jsp"%>
<!-- End Footer -->

<%@include file="../master/js.jsp"%>

</body>

</html>