<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<a href="#" class="back-to-top d-flex align-items-center justify-content-center"><i class="bi bi-arrow-up-short"></i></a>

<!-- Vendor JS Files -->
<script src="<%=request.getContextPath()%>/assets/vendor/apexcharts/apexcharts.min.js"></script>
<script src="<%=request.getContextPath()%>/assets/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>
<script src="<%=request.getContextPath()%>/assets/vendor/chart.js/chart.umd.js"></script>
<script src="<%=request.getContextPath()%>/assets/vendor/echarts/echarts.min.js"></script>
<script src="<%=request.getContextPath()%>/assets/vendor/quill/quill.js"></script>
<script src="<%=request.getContextPath()%>/assets/vendor/simple-datatables/simple-datatables.js"></script>
<script src="<%=request.getContextPath()%>/assets/vendor/tinymce/tinymce.min.js"></script>
<script src="<%=request.getContextPath()%>/assets/vendor/php-email-form/validate.js"></script>
<script src="https://code.jquery.com/jquery-3.4.1.min.js"></script>
<!-- Template Main JS File -->
<script src="<%=request.getContextPath()%>/assets/js/main.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/toastr.min.js" integrity="sha512-VEd+nq25CkR676O+pLBnDW09R7VQX9Mdiij052gVCp5yVH3jGtH70Ho/UUv4mJDsEdTvqRCFZg0NKGiojGnUCw==" crossorigin="anonymous" referrerpolicy="no-referrer"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/chosen/1.8.7/chosen.jquery.min.js"></script>
<script>
    const mess_error = "${error}"
    const mess_success = "${success}"
    const mess_warning = "${warning}"
    const info = "${info}"
    if (mess_error !== ""){
        toastr.error(mess_error, "Lỗi")
    }
    if (mess_success !== ""){
        toastr.success(mess_success, "Thành công")
    }
    if (mess_warning !== ""){
        toastr.warning(mess_warning, "Cảnh báo")
    }
    if (info !== ""){
        toastr.info(info, "Thông tin")
    }
    $(".modal").on('shown.bs.modal', function () {
        $(".reload_chosen").chosen('destroy').chosen();
    });
</script>
<% if (request.getSession().getAttribute("hotel") != null) { %>
<script>
    const websocket = new WebSocket((window.location.protocol === 'https:' ? 'wss://' : 'ws://') + window.location.host + "<%=request.getContextPath()%>/my-websocket");
    websocket.onopen = function () {
        websocket.send("subscribe:<%=request.getSession().getAttribute("hotel")%>");
    }
    websocket.onmessage = (event) => {
        toastr.success(event.data);
    }
</script>
<% } %>