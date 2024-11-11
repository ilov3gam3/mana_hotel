<%@ page import="java.util.ArrayList" %>
<%@ page import="Util.EscapeCharacters" %>
<%@ page import="Model.*" %>
<%@ page import="Util.Json" %>
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
                <li class="breadcrumb-item"><a href="<%=request.getContextPath()%>/">Home</a></li>
                <li class="breadcrumb-item active">Quản lý loại phòng</li>
            </ol>
        </nav>
    </div><!-- End Page Title -->
    <section class="section dashboard">
        <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#verticalycentered">
            Thêm mới loại phòng
        </button>
        <div class="modal fade" id="verticalycentered" tabindex="-1">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Thêm mới loại phòng</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <form action="<%=request.getContextPath()%>/hotel/manage-room-type" method="post" enctype="multipart/form-data">
                        <div class="modal-body">
                            <div class="col-12">
                                <label for="name" class="form-label">Tên</label>
                                <div class="input-group has-validation">
                                    <input type="text" name="name" class="form-control" id="name" required>
                                    <div class="invalid-feedback">Vui lòng nhập tên</div>
                                </div>
                            </div>
                            <div class="col-12">
                                <label for="description" class="form-label">Mô tả</label>
                                <div class="input-group has-validation">
                                    <textarea class="form-control" name="description" id="description" rows="15" required></textarea>
                                    <div class="invalid-feedback">Vui lòng nhập mô tả</div>
                                </div>
                            </div>

                            <div class="col-12">
                                <label for="utilities" class="form-label">Tiện ích</label>
                                <div class="input-group has-validation">
                                    <select class="reload_chosen" style="width: 100%;" name="utility_ids"
                                            id="utilities" multiple>
                                        <% ArrayList<Utility> utilities = (ArrayList<Utility>) request.getAttribute("utilities");%>
                                        <% for (int i = 0; i < utilities.size(); i++) { %>
                                            <option value="<%=utilities.get(i).id%>"><%=utilities.get(i).name%>
                                            </option>
                                        <% } %>
                                    </select>
                                    <div class="invalid-feedback">Vui lòng chọn loại phòng</div>
                                </div>
                            </div>

                            <div class="col-12">
                                <label for="area" class="form-label">Diện tích phòng (m²)</label>
                                <div class="input-group has-validation">
                                    <input type="number" min="0" name="area" class="form-control" id="area" required>
                                    <div class="invalid-feedback">Vui lòng nhập diện tích phòng</div>
                                </div>
                            </div>

                            <div class="col-12">
                                <label for="beds" class="form-label">Số giường ngủ</label>
                                <div class="input-group has-validation">
                                    <input type="number" min="0" name="beds" class="form-control" id="beds" required>
                                    <div class="invalid-feedback">Vui lòng nhập số giường ngủ</div>
                                </div>
                            </div>

                            <div class="col-12">
                                <label for="images" class="form-label">Ảnh</label>
                                <div class="input-group has-validation">
                                    <input type="file" accept="image/*" name="images" class="form-control" id="images"
                                           required multiple>
                                    <div class="invalid-feedback">Vui lòng nhập ảnh</div>
                                </div>
                            </div>

                            <div class="col-12">
                                <label for="price" class="form-label">Giá phòng (vnd)</label>
                                <div class="input-group has-validation">
                                    <input type="number" min="0" name="price" class="form-control" id="price" required>
                                    <div class="invalid-feedback">Vui lòng nhập giá phòng</div>
                                </div>
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
        <div class="modal fade" id="verticalycentered2" tabindex="-1">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Cập nhật loại phòng</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <form action="<%=request.getContextPath()%>/hotel/update-room-type" method="post" enctype="multipart/form-data">
                        <div class="modal-body">
                            <input type="hidden" name="id" id="id_update">
                            <div class="col-12">
                                <label for="name_update" class="form-label">Tên</label>
                                <div class="input-group has-validation">
                                    <input type="text" name="name" class="form-control" id="name_update" required>
                                    <div class="invalid-feedback">Vui lòng nhập tên</div>
                                </div>
                            </div>
                            <div class="col-12">
                                <label for="description_update" class="form-label">Mô tả</label>
                                <div class="input-group has-validation">
                                    <textarea class="form-control" name="description" id="description_update" rows="10" required></textarea>
                                    <div class="invalid-feedback">Vui lòng nhập mô tả</div>
                                </div>
                            </div>

                            <div class="col-12">
                                <label for="utilities_update" class="form-label">Tiện ích</label>
                                <div class="input-group has-validation">
                                    <select class="reload_chosen" style="width: 100%;" name="utility_ids"
                                            id="utilities_update" multiple>
                                        <% for (int i = 0; i < utilities.size(); i++) { %>
                                        <option value="<%=utilities.get(i).id%>"><%=utilities.get(i).name%>
                                        </option>
                                        <% } %>
                                    </select>
                                    <div class="invalid-feedback">Vui lòng chọn loại phòng</div>
                                </div>
                            </div>

                            <div class="col-12">
                                <label for="area_update" class="form-label">Diện tích phòng (m²)</label>
                                <div class="input-group has-validation">
                                    <input type="number" min="0" name="area" class="form-control" id="area_update" required>
                                    <div class="invalid-feedback">Vui lòng nhập diện tích phòng</div>
                                </div>
                            </div>

                            <div class="col-12">
                                <label for="beds_update" class="form-label">Số giường ngủ</label>
                                <div class="input-group has-validation">
                                    <input type="number" min="0" name="beds" class="form-control" id="beds_update" required>
                                    <div class="invalid-feedback">Vui lòng nhập số giường ngủ</div>
                                </div>
                            </div>

                            <div class="col-12">
                                <label for="images_update" class="form-label">Ảnh</label>
                                <div class="input-group has-validation">
                                    <input type="file" accept="image/*" name="images" class="form-control" id="images_update"
                                            multiple>
                                    <div class="invalid-feedback">Vui lòng nhập ảnh</div>
                                </div>
                            </div>

                            <div class="col-12">
                                <label for="price_update" class="form-label">Giá phòng (vnd)</label>
                                <div class="input-group has-validation">
                                    <input type="number" min="0" name="price" class="form-control" id="price_update" required>
                                    <div class="invalid-feedback">Vui lòng nhập giá phòng</div>
                                </div>
                            </div>

                            <div class="col-12 mt-1">
                                <div class="row" id="preview_image">

                                </div>
                                <p class="text-danger" hidden id="number_images_delete"></p>
                                <input type="hidden" name="images_to_delete" id="images_to_delete">
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
                    <th>Mô tả</th>
                    <th>Số giường</th>
                    <th>Diện tích</th>
                    <th>Giá</th>
                    <th>Bị ẩn</th>
                    <th>Cập nhật</th>
                </tr>
                </thead>
                <tbody>
                <%ArrayList<RoomType> roomTypes = (ArrayList<RoomType>) request.getAttribute("roomTypes");%>
                <% for (int i = 0; i < roomTypes.size(); i++) { %>
                <tr>
                    <td><%=roomTypes.get(i).id%></td>
                    <td><%=roomTypes.get(i).name%></td>
                    <td><%=roomTypes.get(i).description%></td>
                    <td><%=roomTypes.get(i).beds%></td>
                    <td><%=roomTypes.get(i).area%></td>
                    <td><%=roomTypes.get(i).price%></td>
                    <td>
                        <% if (roomTypes.get(i).hidden) { %>
                            <a href="<%=request.getContextPath()%>/hotel/change-room-type-hidden?room_type_id=<%=roomTypes.get(i).id%>">
                                <button class="btn btn-success">Bỏ ẩn</button>
                            </a>
                        <% } else { %>
                            <a href="<%=request.getContextPath()%>/hotel/change-room-type-hidden?room_type_id=<%=roomTypes.get(i).id%>">
                                <button class="btn btn-warning">Ẩn</button>
                            </a>
                        <% } %>
                    </td>
                    <td>
                        <button onclick="test('<%=roomTypes.get(i).id%>', '<%=roomTypes.get(i).name%>', '<%=EscapeCharacters.escapeSpecialCharacters(roomTypes.get(i).description)%>', <%=roomTypes.get(i).beds%>, <%=roomTypes.get(i).area%>, <%=roomTypes.get(i).price%>, JSON.parse(`<%=Json.toJson(roomTypes.get(i).images).replace("\"", "&quot;")%>`), JSON.parse(`<%=Json.toJson(roomTypes.get(i).utilities).replace("\"", "&quot;")%>`))" class="btn btn-warning" data-bs-toggle="modal" data-bs-target="#verticalycentered2" type="button">Cập nhật</button>
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
    // $("#utilities").chosen();
    let image_ids_to_delete = []
    function test(id, name, des, beds, area, price, images, utilities) {
        console.log(images)
        $("#id_update").val(id)
        $("#name_update").val(name)
        $("#description_update").val(des)
        $("#beds_update").val(beds)
        $("#area_update").val(area)
        $("#price_update").val(price)
        $("#utilities_update").val(utilities.map(item => item.id))
        let html = '';
        for (let i = 0; i < images.length; i++) {
            html += `<div class="col-4">
                        <img onclick="choose_image_to_delete(` + images[i].id + `)" id="` + images[i].id + `" src="<%=request.getContextPath()%>/` + images[i].url + `" alt="" class="m-1 img-thumbnail" style="width: 100%; max-height: 300px; object-fit: cover">
                    </div>`;
        }
        $("#preview_image").html(html)
    }
    function choose_image_to_delete(id) {
        if (image_ids_to_delete.includes(id)){
            image_ids_to_delete = image_ids_to_delete.filter(item => item !== id)
            $("#" + id).removeClass("border-4 border-danger")
        } else {
            image_ids_to_delete.push(id)
            $("#" + id).addClass("border-4 border-danger")
        }
        if (image_ids_to_delete.length === 0){
            $("#number_images_delete").attr("hidden", "true")
        } else {
            $("#number_images_delete").removeAttr("hidden").text(image_ids_to_delete.length + " ảnh sẽ được xóa.")
        }
        $("#images_to_delete").val(image_ids_to_delete.join(','))
    }
</script>
</html>