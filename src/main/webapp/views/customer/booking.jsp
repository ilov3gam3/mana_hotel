<%@ page import="java.util.ArrayList" %>
<%@ page import="Controller.PaymentController" %>
<%@ page import="Model.*" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<%@include file="../master/head.jsp" %>
<style>
    *{
        margin: 0;
        padding: 0;
    }
    .rate {
        float: left;
        height: 46px;
        padding: 0 10px;
    }
    .rate:not(:checked) > input {
        position:absolute;
        top:-9999px;
    }
    .rate:not(:checked) > label {
        float:right;
        width:1em;
        overflow:hidden;
        white-space:nowrap;
        cursor:pointer;
        font-size:30px;
        color:#ccc;
    }
    .rate:not(:checked) > label:before {
        content: '★ ';
    }
    .rate > input:checked ~ label {
        color: #ffc700;
    }
    .rate:not(:checked) > label:hover,
    .rate:not(:checked) > label:hover ~ label {
        color: #deb217;
    }
    .rate > input:checked + label:hover,
    .rate > input:checked + label:hover ~ label,
    .rate > input:checked ~ label:hover,
    .rate > input:checked ~ label:hover ~ label,
    .rate > label:hover ~ input:checked ~ label {
        color: #c59b08;
    }

    /* Modified from: https://github.com/mukulkant/Star-rating-using-pure-css */
</style>
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
                <li class="breadcrumb-item active">Các đơn đặt phòng của bạn</li>
            </ol>
        </nav>
    </div><!-- End Page Title -->
    <section class="section dashboard">
        <form id="getVnPayUrlForm">
            <input type="hidden" name="bankCode" value="">
            <input type="hidden" name="language" value="vn">
            <button onclick="getVnpayUrl()" type="button" class="btn btn-success">Thanh toán</button>
        </form>
        <div class="col-12">
            <table class="table datatable">
                <thead>
                <tr>
                    <th></th>
                    <th>ID</th>
                    <th>Loại phòng</th>
                    <th>Khách sạn</th>
                    <th>Ngày check in</th>
                    <th>Ngày check out</th>
                    <th>Trạng thái</th>
                    <th>Giá</th>
                    <th>Đặt lúc</th>
                    <th class="col-2">Thao tác</th>
                </tr>
                </thead>
                <tbody>
                    <% ArrayList<Booking> bookings = (ArrayList<Booking>) request.getAttribute("bookings");%>
                    <% for (int i = 0; i < bookings.size(); i++) { %>
                        <tr>
                            <td><input class="form-check-input" name="myCheckbox" type="checkbox" value="<%=bookings.get(i).id%>" <%=bookings.get(i).payment_id != 0 && bookings.get(i).status != BookingStatus.NOT_PAID ? "disabled" : ""%>></td>
                            <td><%=bookings.get(i).id%></td>
                            <td><%=bookings.get(i).room_type_name%></td>
                            <td><%=bookings.get(i).hotel_name%></td>
                            <td><%=bookings.get(i).check_in_date.toString().replace("00:00:00.0", "")%></td>
                            <td><%=bookings.get(i).check_out_date.toString().replace("00:00:00.0", "")%></td>
                            <td><%=bookings.get(i).status%></td>
                            <td><%=bookings.get(i).payment_id == 0 ? bookings.get(i).temp_price * PaymentController.GetVNPayUrlServlet.countDays(bookings.get(i).check_in_date, bookings.get(i).check_out_date) : bookings.get(i).price%></td>
                            <td><%=bookings.get(i).created_at%></td>
                            <td class="col-2">
                                <% if (bookings.get(i).payment_id == 0 && bookings.get(i).status == BookingStatus.NOT_PAID) { %>
                                    <div class="col-12 row m-1">
                                        <form>
                                            <button style="width: 100%" onclick="checkCheckbox(<%=bookings.get(i).id%>)" class="btn btn-success" type="button">
                                                Thanh toán
                                            </button>
                                        </form>
                                    </div>
                                    <div class="col-12 row m-1">
                                        <form action="<%=request.getContextPath()%>/customer/cancel-booking" method="get">
                                            <input type="hidden" name="booking_id" value="<%=bookings.get(i).id%>">
                                            <button style="width: 100%" class="btn btn-warning" type="submit">
                                                Hủy phòng
                                            </button>
                                        </form>
                                    </div>
                                <% } else if (bookings.get(i).payment_id != 0 && bookings.get(i).status == BookingStatus.PAID){ %>
                                    <div class="col-12 row m-1">
                                        <form>
                                            <a href="<%=request.getContextPath()%>/customer/transaction?id=<%=bookings.get(i).id%>">
                                                <button type="button" style="width: 100%" class="btn btn-info">
                                                    Xem chi tiết giao dịch
                                                </button>
                                            </a>
                                        </form>
                                    </div>
                                    <div class="col-12 row m-1">
                                        <form>
                                            <% if (bookings.get(i).review.id == 0) { %>
                                                <button onclick="addBookingIdReviewForm(<%=bookings.get(i).id%>)" data-bs-toggle="modal" data-bs-target="#review_form" type="button" style="width: 100%" class="btn btn-success">
                                                    Viết đánh giá
                                                </button>
                                            <% } else { %>
                                                <button onclick="setValueUpdateModal(<%=bookings.get(i).id%>, <%=bookings.get(i).review.rating%>, '<%=bookings.get(i).review.comment%>', <%=bookings.get(i).review.id%>)" data-bs-toggle="modal" data-bs-target="#update_review_form" type="button" style="width: 100%" class="btn btn-warning">
                                                    Cập nhật đánh giá
                                                </button>
                                            <% } %>
                                        </form>
                                    </div>
                                <% } %>
                            </td>
                        </tr>
                    <% } %>
                </tbody>
            </table>
        </div>
        <div class="modal fade" id="review_form" tabindex="-1">
            <div class="modal-dialog modal-lg modal-dialog-centered">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Viết đánh giá</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <form action="<%=request.getContextPath()%>/customer/add-review" method="post">
                        <input type="hidden" name="booking_id" id="booking_id">
                        <div class="modal-body">
                            <div class="col-12 row">
                                <div class="col-3">
                                    <label class="mt-2">Đánh giá của bạn:</label>
                                </div>
                                <div class="col-4">
                                    <div class="rate">
                                        <input type="radio" id="star5" name="rate" value="5" />
                                        <label for="star5" title="text">5 stars</label>
                                        <input type="radio" id="star4" name="rate" value="4" />
                                        <label for="star4" title="text">4 stars</label>
                                        <input type="radio" id="star3" name="rate" value="3" />
                                        <label for="star3" title="text">3 stars</label>
                                        <input type="radio" id="star2" name="rate" value="2" />
                                        <label for="star2" title="text">2 stars</label>
                                        <input type="radio" id="star1" name="rate" value="1" />
                                        <label for="star1" title="text">1 star</label>
                                    </div>
                                </div>
                                <div class="col-5"></div>
                            </div>
                            <div class="col-12 row">
                                <label for="comment" class="form-label">Bình luận</label>
                                <div class="input-group has-validation">
                                    <textarea class="form-control" name="comment" id="comment" rows="10" required></textarea>
                                    <div class="invalid-feedback">Vui lòng nhập bình luận</div>
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
        <div class="modal fade" id="update_review_form" tabindex="-1">
            <div class="modal-dialog modal-lg modal-dialog-centered">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Viết đánh giá</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <form action="<%=request.getContextPath()%>/customer/update-review" method="post">
                        <input type="hidden" name="booking_id" id="update_booking_id">
                        <input type="hidden" name="review_id" id="update_review_id">
                        <div class="modal-body">
                            <div class="col-12 row">
                                <div class="col-3">
                                    <label class="mt-2">Đánh giá của bạn:</label>
                                </div>
                                <div class="col-4">
                                    <div class="rate">
                                        <input type="radio" id="update_star5" name="rate" value="5" />
                                        <label for="update_star5" title="text">5 stars</label>
                                        <input type="radio" id="update_star4" name="rate" value="4" />
                                        <label for="update_star4" title="text">4 stars</label>
                                        <input type="radio" id="update_star3" name="rate" value="3" />
                                        <label for="update_star3" title="text">3 stars</label>
                                        <input type="radio" id="update_star2" name="rate" value="2" />
                                        <label for="update_star2" title="text">2 stars</label>
                                        <input type="radio" id="update_star1" name="rate" value="1" />
                                        <label for="update_star1" title="text">1 star</label>
                                    </div>
                                </div>
                                <div class="col-5"></div>
                            </div>
                            <div class="col-12 row">
                                <label for="update_comment" class="form-label">Bình luận</label>
                                <div class="input-group has-validation">
                                    <textarea class="form-control" name="comment" id="update_comment" rows="10" required></textarea>
                                    <div class="invalid-feedback">Vui lòng nhập bình luận</div>
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
    </section>
</main>
<!-- End #main -->

<!-- ======= Footer ======= -->
<%@include file="../master/footer.jsp" %>
<!-- End Footer -->

<%@include file="../master/js.jsp" %>

</body>
<script>
    function getVnpayUrl() {
        let postData = $("#getVnPayUrlForm").serialize();
        postData += '&booking_ids=' + encodeURIComponent(getCheckboxData())
        $.ajax({
            type: "POST",
            url: '<%=request.getContextPath()%>/customer/get-vnpay-url',
            data: postData,
            dataType: 'JSON',
            success: function (x) {
                console.log(x)
                if (x.code === '00') {
                    if (window.vnpay) {
                        vnpay.open({width: 768, height: 600, url: x.data});
                    } else {
                        location.href = x.data
                    }
                    return false;
                } else {
                    alert(x.Message);
                }
            }
        });
        return false;
    }
    function addBookingIdReviewForm(id) {
        $("#booking_id").val(id)
    }
    function setValueUpdateModal(booking_id, rate, comment, review_id) {
        $("#update_booking_id").val(booking_id)
        $(`input[name="rate"][value="`+rate+`"]`).prop("checked", true)
        $("#update_comment").val(comment)
        $("#update_review_id").val(review_id)
    }
    function getCheckboxData() {
        const checkboxes = document.querySelectorAll('input[name="myCheckbox"]:checked');
        const selectedValues = Array.from(checkboxes).map(checkbox => checkbox.value);
        return selectedValues.join(',');
    }
    function checkCheckbox(value) {
        const checkbox = document.querySelector(`input[name="myCheckbox"][value="`+value+`"]`);
        if (checkbox) {
            checkbox.checked = !checkbox.checked;
        } else {
            console.log("Checkbox with value " + value + " not found.");
        }
    }
</script>
</html>