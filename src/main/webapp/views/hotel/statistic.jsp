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
                <li class="breadcrumb-item active">Thống kê</li>
            </ol>
        </nav>
    </div><!-- End Page Title -->
    <section class="section dashboard">
        <div class="row" id="app">
            <div class="row col-12">
                <h3 class="text-center">Biểu đồ doanh thu</h3>
                <p style="visibility: hidden">{{key}}</p>
                <div class="row">
                    <div class="col-4">
                        <button class="btn btn-success" style="width: 100%" data-bs-toggle="modal" data-bs-target="#bar_chart_revenue_modal">Chọn loại phòng</button>
                        <div class="modal fade" id="bar_chart_revenue_modal" tabindex="-1">
                            <div class="modal-dialog modal-dialog-centered">
                                <div class="modal-content container">
                                    <div class="modal-header">
                                        <h5 class="modal-title">Chọn loại phòng</h5>
                                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                    </div>
                                    <div class="modal-body">
                                        <button v-on:click="if (roomTypes.every(object => object.selected)) { roomTypes.forEach(object => object.selected = false); } else { roomTypes.forEach(object => object.selected = true); }; init_chart_revenue();key++;" style="width: 100%" :class="roomTypes.every(object => object.selected) ? 'm-1 btn btn-info' : 'm-1 btn btn-outline-info'">{{ roomTypes.every(object => object.selected) ? 'Bỏ chọn tất cả' : 'Chọn tất cả' }}</button>
                                        <template v-for="value in roomTypes">
                                            <button v-on:click="value.selected = !value.selected; key++; init_chart_revenue(); key++" style="width: 100%" :class="value.selected === false ? 'm-1 btn btn-outline-info' : 'm-1 btn btn-info'">{{value.name}}</button>
                                        </template>
                                    </div>
                                    <div class="modal-footer">
                                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div v-if="chart_to_show != 3" class="col-6">
                        <div class="row">
                            <div class="row col-6">
                                <label for="from_date" class="col-sm-4 col-form-label">Từ ngày</label>
                                <div class="col-sm-8">
                                    <input v-on:change="init_chart_revenue();" v-model="bar_chart_revenue_from_date" type="date" class="form-control" id="from_date">
                                </div>
                            </div>
                            <div class="row mb-3 col-6">
                                <label for="to_date" class="col-sm-4 col-form-label">Đến ngày</label>
                                <div class="col-sm-8">
                                    <input v-on:change="init_chart_revenue();" v-model="bar_chart_revenue_to_date" type="date" class="form-control" id="to_date">
                                </div>
                            </div>
                        </div>
                    </div>
                    <div v-if="chart_to_show != 3" class="col-2">
                        <button v-on:click="bar_chart_revenue_from_date='';bar_chart_revenue_to_date='';init_chart_revenue();" class="btn btn-success">
                            Toàn thời gian
                        </button>
                    </div>
                    <div class="col-8" v-if="chart_to_show == 3">
                        <div class="row col-6">
                            <label for="year" class="col-sm-4 col-form-label">Chọn năm</label>
                            <div class="col-sm-8">
                                <input class="form-control" id="year" type="number" min="1900" max="2099" step="1" v-on:change="init_chart_revenue();" v-model="revenue_chart_year" />
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row col-12 d-flex justify-content-center m-1">
                <div class="col-8 row">
                    <div class="col-4">
                        <button style="width: 100%" v-on:click="chart_to_show = 1; init_chart_revenue();" :class="chart_to_show === 1 ? 'btn btn-primary' : 'btn btn-outline-primary'">Xem biểu đồ cột</button>
                    </div>
                    <div class="col-4">
                        <button style="width: 100%" v-on:click="chart_to_show = 2; init_chart_revenue();" :class="chart_to_show === 2 ? 'btn btn-primary' : 'btn btn-outline-primary'">Xem biểu đồ tròn</button>
                    </div>
                    <div class="col-4">
                        <button style="width: 100%" v-on:click="chart_to_show = 3; init_chart_revenue();" :class="chart_to_show === 3 ? 'btn btn-primary' : 'btn btn-outline-primary'">Xem biểu đồ đường</button>
                    </div>
                </div>
            </div>
            <canvas v-show="chart_to_show === 1" id="bar_chart_revenue" style="max-height: 400px;width: 100%"></canvas>
            <canvas v-show="chart_to_show === 2" id="pie_chart_revenue" style="max-height: 400px;width: 100%"></canvas>
            <canvas v-show="chart_to_show === 3" id="line_chart_revenue" style="max-height: 400px;width: 100%"></canvas>
            <div class="row col-12">
                <h3 class="text-center">Biểu đồ doanh thu</h3>
            </div>
        </div>
    </section>
</main>
<!-- End #main -->

<!-- ======= Footer ======= -->
<%@include file="../master/footer.jsp" %>
<!-- End Footer -->

<%@include file="../master/js.jsp" %>
<script src="https://cdnjs.cloudflare.com/ajax/libs/vue/2.7.10/vue.min.js" integrity="sha512-H8u5mlZT1FD7MRlnUsODppkKyk+VEiCmncej8yZW1k/wUT90OQon0F9DSf/2Qh+7L/5UHd+xTLrMszjHEZc2BA==" crossorigin="anonymous" referrerpolicy="no-referrer"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/axios/1.7.7/axios.min.js" integrity="sha512-DdX/YwF5e41Ok+AI81HI8f5/5UsoxCVT9GKYZRIzpLxb8Twz4ZwPPX+jQMwMhNQ9b5+zDEefc+dcvQoPWGNZ3g==" crossorigin="anonymous" referrerpolicy="no-referrer"></script>
<script>
    var app = new Vue({
        el: "#app",
        data: {
            key: 0,
            payments: [],
            roomTypes: [],
            rooms: [],
            reviews: [],
            bar_chart_revenue: null,
            pie_chart_revenue: null,
            line_chart_revenue: null,
            chart_to_show: 1,
            bar_chart_revenue_from_date: '',
            bar_chart_revenue_to_date: '',
            revenue_chart_year: new Date().getFullYear(),
            months: ['Tháng 1', 'Tháng 2', 'Tháng 3', 'Tháng 4', 'Tháng 5', 'Tháng 6', 'Tháng 7', 'Tháng 8', 'Tháng 9', 'Tháng 10', 'Tháng 11', 'Tháng 12'],
        },
        created(){
            this.get_data()
        },
        methods: {
            get_data(){
                axios.get('<%=request.getContextPath()%>/hotel/api/hotel-get-statistics-data')
                    .then((res) => {
                        this.payments = JSON.parse(res.data.bookings)
                        this.roomTypes = JSON.parse(res.data.roomTypes)
                        for (let i = 0; i < this.roomTypes.length; i++) {
                            this.roomTypes[i].selected = false
                        }
                        this.init_line_chart_revenue()
                    })
            },
            init_chart_revenue(){
                let xValues = [];
                let yValues = [];
                for (let i = 0; i < this.roomTypes.length; i++) {
                    if (this.roomTypes[i].selected){
                        xValues.push(this.roomTypes[i].name)
                        let temp = 0
                        for (let j = 0; j < this.payments.length; j++) {
                            if (this.bar_chart_revenue_from_date === '' && this.bar_chart_revenue_to_date === ''){
                                if (this.payments[j].room_type_id == this.roomTypes[i].id && this.payments[j].transactionStatus === 'SUCCESS'){
                                    temp += this.payments[j].amount
                                }
                            } else {
                                let from_date = new Date(this.bar_chart_revenue_from_date);
                                let to_date = new Date(this.bar_chart_revenue_to_date);
                                if (isNaN(from_date) && !isNaN(to_date)){
                                    if (to_date > new Date(this.payments[j].paid_at) && this.payments[j].room_type_id == this.roomTypes[i].id && this.payments[j].transactionStatus === 'SUCCESS'){
                                        temp += this.payments[j].amount
                                    }
                                } else if (!isNaN(from_date) && isNaN(to_date)){
                                    if (from_date < new Date(this.payments[j].paid_at) && this.payments[j].room_type_id == this.roomTypes[i].id && this.payments[j].transactionStatus === 'SUCCESS'){
                                        temp += this.payments[j].amount
                                    }
                                } else {
                                    if (to_date > new Date(this.payments[j].paid_at) && from_date < new Date(this.payments[j].paid_at) && this.payments[j].room_type_id == this.roomTypes[i].id && this.payments[j].transactionStatus === 'SUCCESS'){
                                        temp += this.payments[j].amount
                                    }
                                }
                            }

                        }
                        yValues.push(temp)
                    }
                }
                if (this.chart_to_show === 1){
                    if (this.bar_chart_revenue == null){
                        this.bar_chart_revenue = new Chart('bar_chart_revenue', {
                            type: 'bar',
                            data: {
                                labels: xValues,
                                datasets: [{
                                    data: yValues
                                }]
                            },
                            options: {
                                plugins: {
                                    legend: {
                                        display: false
                                    },
                                    title: {
                                        display: true,
                                        text: 'Biểu đồ cột doanh thu'
                                    }
                                }
                            }
                        });
                    }
                    else {
                        this.bar_chart_revenue.data.datasets[0].data = yValues;
                        this.bar_chart_revenue.data.labels = xValues;
                        this.bar_chart_revenue.update();
                    }
                }
                if (this.chart_to_show === 2){
                    if (this.pie_chart_revenue == null){
                        this.pie_chart_revenue = new Chart('pie_chart_revenue', {
                            type: "pie",
                            data: {
                                labels: xValues,
                                datasets: [{
                                    data: yValues
                                }]
                            },
                            options: {
                                plugins: {
                                    legend: {
                                        display: false
                                    },
                                    title: {
                                        display: true,
                                        text: 'Biểu đồ tròn doanh thu'
                                    }
                                }
                            }
                        })
                    } else {
                        this.pie_chart_revenue.data.datasets[0].data = yValues;
                        this.pie_chart_revenue.data.labels = xValues;
                        this.pie_chart_revenue.update();
                    }
                }
                if (this.chart_to_show === 3){
                    this.init_line_chart_revenue();
                }
            },
            init_line_chart_revenue(){
                let dataSet = [];
                for (let i = 0; i < this.roomTypes.length; i++){
                    if (this.roomTypes[i].selected){
                        let array_month_amount = new Array(12).fill(0);
                        for (let j = 0; j < this.payments.length; j++){
                            let temp_date = new Date(this.payments[j].paid_at)
                            if (temp_date.getFullYear() === parseInt(this.revenue_chart_year) && this.payments[j].room_type_id == this.roomTypes[i].id){
                                array_month_amount[temp_date.getMonth()] += this.payments[j].amount
                            }
                        }
                        dataSet.push({
                            label: this.roomTypes[i].name,
                            fill: false,
                            data: array_month_amount
                        })
                    }
                }
                if (this.line_chart_revenue == null){
                    this.line_chart_revenue = new Chart('line_chart_revenue', {
                        type: 'line',
                        data: {
                            labels: this.months,
                            datasets: dataSet
                        },
                        options: {
                            responsive: true,
                            scales: {
                                x: {
                                    beginAtZero: true,
                                    title: {
                                        display: true,
                                        text: 'Months'
                                    }
                                },
                                y: {
                                    beginAtZero: true,
                                    title: {
                                        display: true,
                                        text: 'Values'
                                    }
                                }
                            }
                        }
                    })
                } else {
                    this.line_chart_revenue.data.datasets = dataSet
                    this.line_chart_revenue.update()
                }
            },
        }
    })
</script>
</body>
</html>