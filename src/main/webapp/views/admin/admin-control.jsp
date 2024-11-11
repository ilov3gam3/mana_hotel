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
                <li class="breadcrumb-item"><a href="<%=request.getContextPath()%>/">Quản lý admin</a></li>
                <li class="breadcrumb-item active">Quản lý admin</li>
            </ol>
        </nav>
    </div><!-- End Page Title -->
    <section class="section dashboard">
        <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#verticalycentered">
            Thêm mới quản trị viên
        </button>
        <div class="modal fade" id="verticalycentered" tabindex="-1">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Thêm mới quản trị viên</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <form action="<%=request.getContextPath()%>/admin/admin-control" method="post">
                        <div class="modal-body">
                            <div class="col-12">
                                <label for="name" class="form-label">Tên</label>
                                <input type="text" name="name" class="form-control" id="name" required>
                                <div class="invalid-feedback">Nhập tên</div>
                            </div>

                            <div class="col-12">
                                <label for="username" class="form-label">Tên đăng nhập</label>
                                <input type="text" name="username" class="form-control" id="username" required>
                                <div class="invalid-feedback">Nhập tên đăng nhập</div>
                            </div>

                            <div class="col-12">
                                <label for="yourPassword" class="form-label">Mật khẩu</label>
                                <input type="password" name="password" class="form-control" id="yourPassword" required>
                                <div class="invalid-feedback">Please enter your password!</div>
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
        <div class="modal fade" id="update_admin" tabindex="-1">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Cập nhật quản trị viên</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <form action="<%=request.getContextPath()%>/admin/update-admin" method="post">
                        <input type="hidden" id="admin_id" name="id">
                        <div class="modal-body">
                            <div class="col-12">
                                <label for="name_update" class="form-label">Tên</label>
                                <input type="text" name="name" class="form-control" id="name_update" required>
                                <div class="invalid-feedback">Nhập tên</div>
                            </div>

                            <div class="col-12">
                                <label for="username_update" class="form-label">Tên đăng nhập</label>
                                <input type="text" name="username" class="form-control" id="username_update" required>
                                <div class="invalid-feedback">Nhập tên đăng nhập</div>
                            </div>

                            <div class="col-12">
                                <label for="yourPassword_update" class="form-label">Mật khẩu</label>
                                <input type="password" name="password" class="form-control" id="yourPassword_update">
                                <div class="invalid-feedback">Please enter your password!</div>
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
                        <th>Tên đăng nhập</th>
                        <th>avatar</th>
                        <th>Tạo lúc</th>
                        <th>Cập nhật</th>
                    </tr>
                </thead>
                <tbody>
                    <%ArrayList<Admin> admins = (ArrayList<Admin>) request.getAttribute("admins");%>
                    <% for (int i = 0; i < admins.size(); i++) { %>
                        <tr>
                            <td><%=admins.get(i).id%></td>
                            <td><%=admins.get(i).name%></td>
                            <td><%=admins.get(i).username%></td>
                            <td><a href="<%=request.getContextPath()%>/<%=admins.get(i).avatar%>"><%=admins.get(i).avatar%></a></td>
                            <td><%=admins.get(i).created_at%></td>
                            <td>
                                <button id="<%=admins.get(i).id%>" onclick="update_modal_value(<%=admins.get(i).id%>, '<%=admins.get(i).name%>', '<%=admins.get(i).username%>')" data-bs-toggle="modal" data-bs-target="#update_admin" type="button" class="btn btn-warning">Cập nhật</button>
                                <a href="<%=request.getContextPath()%>/admin/delete-admin?admin_id=<%=admins.get(i).id%>"><button type="button" class="btn btn-warning">Xóa</button></a>
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
<script>
    function update_modal_value(id, name, username) {
        $("#admin_id").val(id);
        $("#name_update").val(name);
        $("#username_update").val(username);
    }
    let url = new URL(window.location.href);
    let params = new URLSearchParams(url.search);
    let id = params.get('admin_id');
    if (id){
        $("#" + id).click()
    }
</script>
</html>