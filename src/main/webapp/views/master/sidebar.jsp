<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%String customer_id_side_bar = request.getSession().getAttribute("customer") == null ? null : request.getSession().getAttribute("customer").toString();%>
<%String admin_id_side_bar = request.getSession().getAttribute("admin") == null ? null : request.getSession().getAttribute("admin").toString();%>
<%String hotel_id_side_bar = request.getSession().getAttribute("hotel") == null ? null : request.getSession().getAttribute("hotel").toString();%>
<aside id="sidebar" class="sidebar">

    <ul class="sidebar-nav" id="sidebar-nav">
        <% if (admin_id_side_bar == null || hotel_id_side_bar == null){ %>
            <li class="nav-item">
                <a class="nav-link collapsed" href="<%=request.getContextPath() + "/search"%>">
                    <i class="bi bi-search"></i>
                    <span>Tìm kiếm phòng</span>
                </a>
            </li>
        <% } %>
        <% if (customer_id_side_bar != null) { %>

        <li class="nav-item">
            <a class="nav-link collapsed" href="<%=request.getContextPath() + "/customer/profile"%>">
                <i class="bi bi-person"></i>
                <span>Trang cá nhân</span>
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link collapsed" href="<%=request.getContextPath() + "/customer/booking"%>">
                <i class="bi bi-card-checklist"></i>
                <span>Xem đặt phòng</span>
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link collapsed" href="<%=request.getContextPath() + "/customer/transaction"%>">
                <i class="bi bi-cash-coin"></i>
                <span>Xem lịch sử giao dịch</span>
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link collapsed" href="<%=request.getContextPath() + "/logout"%>">
                <i class="bi bi-box-arrow-right"></i>
                <span>Đăng xuất</span>
            </a>
        </li>
        <% } else if (admin_id_side_bar != null) { %>
        <li class="nav-item">
            <a class="nav-link collapsed"
               href="<%=request.getContextPath() + "/admin/admin-control?admin_id=" + admin_id_side_bar%>">
                <i class="bi bi-person"></i>
                <span>Trang cá nhân</span>
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link collapsed" href="<%=request.getContextPath() + "/admin/admin-control"%>">
                <i class="bi bi-file-earmark-person-fill"></i>
                <span>Quản lý Admin</span>
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link collapsed" href="<%=request.getContextPath() + "/admin/customer-control"%>">
                <i class="bi bi-file-earmark-person"></i>
                <span>Quản lý người dùng</span>
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link collapsed" href="<%=request.getContextPath() + "/admin/hotel-control"%>">
                <i class="bx bx-building-house"></i>
                <span>Quản lý khách sạn</span>
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link collapsed" href="<%=request.getContextPath() + "/logout"%>">
                <i class="bi bi-box-arrow-right"></i>
                <span>Đăng xuất</span>
            </a>
        </li>
        <% } else if (hotel_id_side_bar != null) { %>
        <li class="nav-item">
            <a class="nav-link collapsed" href="<%=request.getContextPath() + "/hotel/profile"%>">
                <i class="bi bi-person"></i>
                <span>Trang cá nhân</span>
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link collapsed" href="<%=request.getContextPath() + "/hotel/manage-utility"%>">
                <i class="ri-chat-settings-fill"></i>
                <span>Quản lý tiện ích</span>
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link collapsed" href="<%=request.getContextPath() + "/hotel/manage-room-type"%>">
                <i class="bi bi-house-door-fill"></i>
                <span>Quản lý loại phòng</span>
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link collapsed" href="<%=request.getContextPath() + "/hotel/manage-room"%>">
                <i class="bi bi-door-open"></i>
                <span>Quản lý phòng</span>
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link collapsed" href="<%=request.getContextPath() + "/hotel/booking-control"%>">
                <i class="bi bi-door-open"></i>
                <span>Quản lý đơn đặt phòng</span>
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link collapsed" href="<%=request.getContextPath() + "/hotel/statistic"%>">
                <i class="ri-bar-chart-2-fill"></i>
                <span>Thống kê</span>
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link collapsed" href="<%=request.getContextPath() + "/logout"%>">
                <i class="bi bi-box-arrow-right"></i>
                <span>Đăng xuất</span>
            </a>
        </li>
        <% } %>
    </ul>

</aside>
