<%@ page import="java.util.ArrayList" %>
<%@ page import="Model.*" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<%@include file="../master/head.jsp" %>
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
        <li class="breadcrumb-item active">Xem giao dịch của bạn</li>
      </ol>
    </nav>
  </div><!-- End Page Title -->
  <section class="section dashboard">
    <div class="col-12">
      <table class="table datatable">
        <thead>
        <tr>
          <th>ID</th>
          <th>Số tiền</th>
          <th>txnRef</th>
          <th>orderInfo</th>
          <th>bankCode</th>
          <th>transactionNo</th>
          <th>Trạng thái</th>
          <th>cardType</th>
          <th>bankTranNo</th>
          <th>Tạo lúc</th>
          <th>Thanh toán lúc</th>
        </tr>
        </thead>
        <tbody>
        <% ArrayList<Payment> payments = (ArrayList<Payment>) request.getAttribute("payments");%>
        <% for (int i = 0; i < payments.size(); i++) { %>
          <% if (request.getParameter("id") != null) { %>
            <% if (payments.get(i).id == Integer.parseInt(request.getParameter("id"))) {%>
                <tr>
                  <td><%=payments.get(i).id%></td>
                  <td><%=payments.get(i).amount%></td>
                  <td><%=payments.get(i).txnRef%></td>
                  <td><%=payments.get(i).orderInfo%></td>
                  <td><%=payments.get(i).bankCode%></td>
                  <td><%=payments.get(i).transactionNo%></td>
                  <td><%=payments.get(i).transactionStatus.getDescription()%></td>
                  <td><%=payments.get(i).cardType%></td>
                  <td><%=payments.get(i).bankTranNo%></td>
                  <td><%=payments.get(i).created_at%></td>
                  <td><%=payments.get(i).paid_at%></td>
                </tr>
            <% break; } %>
          <% } else {%>
            <tr>
              <td><%=payments.get(i).id%></td>
              <td><%=String.format("%,d", payments.get(i).amount)%></td>
              <td><%=payments.get(i).txnRef%></td>
              <td><%=payments.get(i).orderInfo%></td>
              <td><%=payments.get(i).bankCode%></td>
              <td><%=payments.get(i).transactionNo%></td>
              <td><%=payments.get(i).transactionStatus.getDescription()%></td>
              <td><%=payments.get(i).cardType%></td>
              <td><%=payments.get(i).bankTranNo%></td>
              <td><%=payments.get(i).created_at%></td>
              <td><%=payments.get(i).paid_at%></td>
            </tr>
          <% }%>

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