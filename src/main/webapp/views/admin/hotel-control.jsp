<%@ page import="java.util.ArrayList" %>
<%@ page import="Model.Hotel" %>
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
                <li class="breadcrumb-item"><a href="<%=request.getContextPath()%>/">Quản lý khách sạn</a></li>
                <li class="breadcrumb-item active">Quản lý khách sạn</li>
            </ol>
        </nav>
    </div><!-- End Page Title -->
    <section class="section dashboard">
        <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#verticalycentered">
            Thêm mới khách sạn
        </button>
        <div class="modal fade" id="verticalycentered" tabindex="-1">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Thêm mới khách sạn</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <form action="<%=request.getContextPath()%>/admin/hotel-control" method="post" enctype="multipart/form-data">
                        <div class="modal-body">
                            <div class="col-12">
                                <label for="name" class="form-label">Tên</label>
                                <div class="input-group has-validation">
                                    <input type="text" name="name" class="form-control" id="name" required>
                                    <div class="invalid-feedback">Please enter your name.</div>
                                </div>
                            </div>

                            <div class="col-12">
                                <label for="email" class="form-label">Email</label>
                                <div class="input-group has-validation">
                                    <input type="email" name="email" class="form-control" id="email" required>
                                    <div class="invalid-feedback">Please enter your email.</div>
                                </div>
                            </div>

                            <div class="col-12">
                                <label for="yourPassword" class="form-label">Mật khẩu</label>
                                <input type="password" name="password" class="form-control" id="yourPassword" required>
                                <div class="invalid-feedback">Please enter your password!</div>
                            </div>

                            <div class="col-12">
                                <label for="address" class="form-label">Địa chỉ</label>
                                <div class="input-group has-validation">
                                    <input type="text" name="address" class="form-control" id="address" required>
                                    <div class="invalid-feedback">Please enter your address.</div>
                                </div>
                            </div>

                            <div class="col-12">
                                <label for="gg_map_link" class="form-label">Nhúng bản đồ (bắt đầu bằng &lt;iframe)</label>
                                <div class="input-group has-validation">
                                    <input type="text" name="gg_map_link" class="form-control" id="gg_map_link" required>
                                    <div class="invalid-feedback">Please enter your gg_map_link.</div>
                                </div>
                            </div>

                            <div class="col-12">
                                <label for="avatar" class="form-label">Ảnh</label>
                                <input type="file" name="avatar" class="form-control" id="avatar" required>
                                <div class="invalid-feedback">Please choose your image!</div>
                            </div>

                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                            <button type="submit" class="btn btn-primary">Save changes</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <div class="row">
            <table class="table datatable">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Tên</th>
                    <th>Email</th>
                    <th>Địa chỉ</th>
                    <th>Nhúng bản đồ</th>
                    <th>Ảnh đại diện</th>
                    <th>Tạo lúc</th>
                    <th>Action</th>
                </tr>
                </thead>
                <tbody>
                <%ArrayList<Hotel> hotels = (ArrayList<Hotel>) request.getAttribute("hotels");%>
                <% for (int i = 0; i < hotels.size(); i++) { %>
                <tr>
                    <td><%=hotels.get(i).id%></td>
                    <td><%=hotels.get(i).name%></td>
                    <td><%=hotels.get(i).email%></td>
                    <td><%=hotels.get(i).address%></td>
                    <td><%=hotels.get(i).gg_map_link%></td>
                    <td><a href="<%=hotels.get(i).avatar.startsWith("http") || hotels.get(i).avatar.startsWith("https") ? hotels.get(i).avatar : request.getContextPath() + "/" + hotels.get(i).avatar%>">Xem ảnh</a></td>
                    <td><%=hotels.get(i).created_at%></td>
                    <td>
                        <div class="row m-1">
                            <% if (hotels.get(i).is_verified) { %>
                            <a href="<%=request.getContextPath()%>/admin/verrify-hotel?hotel_id=<%=hotels.get(i).id%>">
                                <button class="btn btn-success">Hủy xác nhận</button>
                            </a>
                            <% } else { %>
                            <div class="col-6">
                                <a href="<%=request.getContextPath()%>/admin/verrify-hotel?hotel_id=<%=hotels.get(i).id%>">
                                    <button class="btn btn-danger">Xác nhận</button>
                                </a>
                            </div>
                            <% } %>
                        </div>
                        <div class="row m-1">
                            <% if (hotels.get(i).is_blocked) { %>
                            <a href="<%=request.getContextPath()%>/admin/block-hotel?hotel_id=<%=hotels.get(i).id%>">
                                <button class="btn btn-success">Bỏ chặn</button>
                            </a>
                            <% } else { %>
                            <div class="col-6">
                                <a href="<%=request.getContextPath()%>/admin/block-hotel?hotel_id=<%=hotels.get(i).id%>">
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