DROP TABLE IF EXISTS reviews;
DROP TABLE IF EXISTS bookings;
DROP TABLE IF EXISTS payments;
DROP TABLE IF EXISTS room_type_has_images;
DROP TABLE IF EXISTS images;
DROP TABLE IF EXISTS room_types_has_utilities;
DROP TABLE IF EXISTS rooms;
DROP TABLE IF EXISTS utilities;
DROP TABLE IF EXISTS room_types;
DROP TABLE IF EXISTS hotels;
DROP TABLE IF EXISTS admins;
DROP TABLE IF EXISTS customers;
create table customers
(
    id          int primary key identity,
    name        nvarchar(max),
    email       nvarchar(max),
    phone       nvarchar(max),
    dob         date,
    avatar      nvarchar(max),
    password    nvarchar(max),
    is_verified bit,
    is_blocked bit default 0,
    token       nvarchar(max),
    created_at  datetime default current_timestamp,
);
create table admins
(
    id         int primary key identity,
    name       nvarchar(max),
    username   nvarchar(max),
    avatar     nvarchar(max),
    password   nvarchar(max),
    created_at datetime default current_timestamp,
);
create table hotels
(
    id          int primary key identity,
    name        nvarchar(max),
    email       nvarchar(max),
    address     nvarchar(max),
    gg_map_link nvarchar(max),
    avatar      nvarchar(max),
    password    nvarchar(max),
    is_verified bit default 0,
    is_blocked bit default 0,
    created_at  datetime default current_timestamp,
);
create table room_types
(
    id          int primary key identity,
    hotel_id    int foreign key references hotels (id),
    name        nvarchar(max),
    description nvarchar(max),
    beds        int,
    area        float,
    price       int,
    hidden bit default 0
);

create table utilities
(
    id   int primary key identity,
    name nvarchar(max),
);
create table rooms
(
    id           int primary key identity,
    hotel_id     int foreign key references hotels (id),
    number       nvarchar(max),
    room_type_id int foreign key references room_types (id),
    is_available bit,
);
create table room_types_has_utilities
(
    id           int primary key identity,
    utility_id   int foreign key references utilities (id),
    room_type_id int foreign key references room_types (id)
);
create table images
(
    id  int primary key identity,
    url nvarchar(max),
);
create table room_type_has_images
(
    id           int primary key identity,
    room_type_id int foreign key references room_types (id),
    image_id     int foreign key references images (id),
);
create table payments
(
    id                int primary key identity,
    customer_id       int foreign key references customers (id),
    amount            int,
    txnRef            nvarchar(max),
    orderInfo         nvarchar(max),
    bankCode          nvarchar(max),
    transactionNo     nvarchar(max),
    transactionStatus nvarchar(max),
    cardType          nvarchar(max),
    bankTranNo        nvarchar(max),
    created_at        datetime default current_timestamp,
    paid_at           datetime,
);
create table bookings
(
    id             int primary key identity,
    customer_id    int foreign key references customers (id),
    room_id        int foreign key references rooms (id),
    payment_id     int foreign key references payments (id),
    check_in_date  date,
    check_out_date date,
    price          int,
    status         VARCHAR(20),
    is_checked_in bit default 0,
    is_checked_out bit default 0,
    created_at     datetime default current_timestamp,
    updated_at     datetime,
);
create table reviews
(
    id          int primary key identity,
    customer_id int foreign key references customers (id),
    booking_id  int foreign key references bookings (id),
    rating      int check (rating between 1 and 5),
    comment     nvarchar(max),
    created_at  datetime default current_timestamp
);
-- admin password: Admin12345
-- other account password: 123456
SET IDENTITY_INSERT customers ON;
INSERT INTO customers (id, name, email, phone, dob, avatar, password, is_verified, token, created_at)VALUES (1, N'Khách hàng', N'customer@gmail.com', N'0123456789', N'2002-08-05', N'assets/img/profile-img.jpg', N'$2a$10$ldHKnLQQLDUy./8cXjzRhOwJ4VEwpDba77KD5otpxbflZivA7YFkW', 1, null, N'2024-10-29 17:22:43.180');
INSERT INTO customers (id, name, email, phone, dob, avatar, password, is_verified, token, created_at) VALUES (2, N'Quang Minh Trần', N'tranquangminh116@gmail.com', null, null, N'https://lh3.googleusercontent.com/a/ACg8ocKN1g1m8RdA54n1kGuiLiAypRL3c_7PGL2kbvwQzvhra6HjKc0=s96-c', null, 1, null, N'2024-10-30 16:56:42.523');
INSERT INTO customers (id, name, email, phone, dob, avatar, password, is_verified, token, created_at) VALUES (3, N'Tran Quang Minh (K16 DN)', N'minhtqde160524@fpt.edu.vn', null, null, N'https://lh3.googleusercontent.com/a/ACg8ocIYKfmXRkQU1_1Ndg-IRtO3L8Fnlm4RgS7TL3206iUU5YmLww=s96-c', N'$2a$10$gQNwh/eb.2OKCCAyEc6H6u/ADNwLpfJZXe3NNZkA7RlaK.5qkEGea', 1, null, N'2024-10-30 19:30:59.990');
INSERT INTO customers (id, name, email, phone, dob, avatar, password, is_verified, token, created_at) VALUES (4, N'quangminh tran', N'tranquangminh050802@gmail.com', N'0987654321', N'2000-01-01', N'https://lh3.googleusercontent.com/a/ACg8ocKG-xc3_56GLQCR0WuM6poj-v9hBZGvdOF5YCx2-AniMpTIdA=s96-c', null, 1, null, N'2024-10-31 22:30:28.990');
SET IDENTITY_INSERT customers OFF;

SET IDENTITY_INSERT admins ON;
INSERT INTO admins (id, name, username, avatar, password, created_at)VALUES (1, N'Admin đây :))', N'admin', N'assets/img/admin-avatar.jpg', N'$2a$10$90GZEeepyVAEO93J0CO5Qu8A8.1p4N7ru1v14JBfFSHTM4Cxpwusa', N'2024-10-29 17:22:43.190');
SET IDENTITY_INSERT admins OFF;

SET IDENTITY_INSERT hotels ON;
INSERT INTO hotels (id, name, email, address, gg_map_link, avatar, password, created_at)VALUES (1, N'Novotel', N'novotel@gmail.com', N'36 Bạch Đằng, Thạch Thang, Hải Châu, Đà Nẵng', N'<iframe src="https://www.google.com/maps/embed?pb=!1m14!1m8!1m3!1d15335.072189020671!2d108.2237843!3d16.077522!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x3142183afab4a42d%3A0xd52dbe9e28e83835!2zS2jDoWNoIHPhuqFuIE5vdm90ZWwgxJDDoCBO4bq1bmc!5e0!3m2!1svi!2s!4v1728454675497!5m2!1svi!2s" width="600" height="450" style="border:0;" allowfullscreen="" loading="lazy" referrerpolicy="no-referrer-when-downgrade"></iframe>', N'assets/uploads/novotel.jpg', N'$2a$10$ldHKnLQQLDUy./8cXjzRhOwJ4VEwpDba77KD5otpxbflZivA7YFkW', N'2024-10-29 17:22:43.203');
INSERT INTO hotels (id, name, email, address, gg_map_link, avatar, password, created_at) VALUES (2, N'Mường Thanh', N'muongthanh@gmail.com', N'270 Võ Nguyên Giáp, Bắc Mỹ Phú, Ngũ Hành Sơn, Đà Nẵng', N'<iframe src="https://www.google.com/maps/embed?pb=!1m14!1m8!1m3!1d15336.05192140891!2d108.2330777!3d16.0648162!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x314219f5540b195d%3A0x4476dc3ffe0f3386!2zTcaw4budbmcgVGhhbmggR3JhbmQgxJDDoCBO4bq1bmcgSG90ZWw!5e0!3m2!1svi!2s!4v1730281075050!5m2!1svi!2s" width="600" height="450" style="border:0;" allowfullscreen="" loading="lazy" referrerpolicy="no-referrer-when-downgrade"></iframe>', N'assets/uploads/68d757bf-aaba-41ed-af43-c8a67814d9fa-1730281062104.jpg', N'$2a$10$IzPmDZg96TOzm6hz286czuCPRufHKG8FyAx0W/fNOEI/DlIn8sefq', N'2024-10-30 16:37:41.740');
SET IDENTITY_INSERT hotels OFF;

SET IDENTITY_INSERT room_types ON;
INSERT INTO room_types (id, hotel_id, name, description, beds, area, price)VALUES (1, 1, N'Phòng Superior đơn', N'Phòng nghỉ cực kỳ hiện đại và phong cách này của chúng tôi, mang đến không gian hoàn hảo, để Quý khách nghỉ ngơi thư giãn sau một ngày làm việc căng thẳng hoặc vui chơi hết mình. Thậm chí ngay cả với phòng nghỉ có kích thước khiêm tốn nhất, thì các tiện nghi hoàn hảo vẫn sẽ mang lại cho Quý khách cảm giác thoải mái, bình yên và ấm áp như đang ở trong chính ngôi nhà thân yêu của mình. Một số phòng có ban công với hướng nhìn tuyệt đẹp ra sông Hàn hoặc cầu Thuận Phước.', 1, 30, 500000);
INSERT INTO room_types (id, hotel_id, name, description, beds, area, price) VALUES (2, 1, N'Phòng Deluxe', N'Dù là doanh nhân hay du khách nghỉ dưỡng, loại phòng này có hướng nhìn tuyệt đẹp cùng nhiều tiện nghi vượt trội. Các phòng này bao gồm cả phòng Deluxe Corner, đều có thể nhìn ngắm toàn cảnh Sông Hàn và Cầu Rồng, cũng như các bãi biển tuyệt đẹp của thành phố. Bên cạnh đó, một số phòng được thiết kế có ban công, và trang bị thêm giường đôi, để quý khách thoải mái lựa chọn những không gian nghỉ ngơi lý tưởng nhất.', 2, 40, 700000);
INSERT INTO room_types (id, hotel_id, name, description, beds, area, price) VALUES (3, 1, N'Phòng Executive', N'Quý khách ở tại phòng Executive có thể sử dụng miễn phí các dịch vụ tại quầy bar tầng thượng hạng của khách sạn. Có rất nhiều phong cách và kích thước khác nhau cho loại phòng này, tất cả các thiết kế đều đáp ứng đầy đủ những trang thiết bị tiện nghi và hiện đại nhất.
Bất kể lựa chọn trải nghiệm loại phòng nào, quý khách đều được phục vụ đồ uống chào đón khi đến nhận phòng, bữa ăn sáng miễn phí, thoải mái thưởng thức đồ uống không cồn trong ngày cùng cocktail tối miến phí, và nhiều dịch vụ ưu đãi đặc biệt khác. Bên cạnh đó, mỗi phòng đều được trang bị cuộc gọi trong vùng miễn phí, tiện nghi VIP và có hướng nhìn tuyệt đẹp về phía sông Hàn hoặc cầu Thuận Phước.', 2, 50, 1000000);
INSERT INTO room_types (id, hotel_id, name, description, beds, area, price) VALUES (4, 2, N'Phòng Superior Twin', N'Phòng dành cho 2 người. Được thiết kế sang trọng và hoàn hảo với các tiện nghi hiện đại, đáp ứng mọi kỳ nghỉ thư thái của bạn trong một căn phòng khá thoải mái. Phòng này không có tầm nhìn và cửa sổ', 2, 35, 600000);
INSERT INTO room_types (id, hotel_id, name, description, beds, area, price) VALUES (5, 2, N'Phòng Deluxe Twin', N'Được thiết kế sang trọng và hoàn hảo với các tiện nghi hiện đại, đáp ứng mọi kỳ nghỉ thư thái của bạn trong một căn phòng khá đẹp với cửa sổ lớn nhìn ra thành phố.', 2, 45, 1000000);
INSERT INTO room_types (id, hotel_id, name, description, beds, area, price) VALUES (6, 2, N'Phòng Deluxe King', N'Được thiết kế sang trọng và hoàn hảo với các tiện nghi hiện đại, đáp ứng mọi kỳ nghỉ thư thái của bạn trong một căn phòng khá đẹp với cửa sổ lớn nhìn ra thành phố.', 2, 55, 1500000);
SET IDENTITY_INSERT room_types OFF;

SET IDENTITY_INSERT utilities ON;
INSERT INTO utilities (id, name)VALUES (1, N'free wifi');
INSERT INTO utilities (id, name) VALUES (2, N'nhà hàng');
INSERT INTO utilities (id, name) VALUES (3, N'bữa sáng miễn phí');
INSERT INTO utilities (id, name) VALUES (4, N'xe đưa đón sân bay');
INSERT INTO utilities (id, name) VALUES (5, N'gần biển');
INSERT INTO utilities (id, name) VALUES (6, N'gần trung tâm');
INSERT INTO utilities (id, name) VALUES (7, N'Phục vụ phòng 24h');
INSERT INTO utilities (id, name) VALUES (8, N'Quầy bar mini');
INSERT INTO utilities (id, name) VALUES (9, N'Miễn phí sử dụng phòng gym');
INSERT INTO utilities (id, name) VALUES (10, N'Miễn phí sử dụng hồ bơi');
INSERT INTO utilities (id, name) VALUES (11, N'Phòng tắm - Vòi sen');
INSERT INTO utilities (id, name) VALUES (12, N'Dịch vụ giặt ủi');
INSERT INTO utilities (id, name) VALUES (13, N'Máy sấy tóc');
INSERT INTO utilities (id, name) VALUES (14, N'Bàn làm việc');
INSERT INTO utilities (id, name) VALUES (15, N'Két sắt an toàn');
SET IDENTITY_INSERT utilities OFF;

SET IDENTITY_INSERT rooms ON;
INSERT INTO rooms (id, hotel_id, number, room_type_id, is_available)VALUES (1, 1, N'201', 1, 1);
INSERT INTO rooms (id, hotel_id, number, room_type_id, is_available) VALUES (2, 1, N'202', 1, 1);
INSERT INTO rooms (id, hotel_id, number, room_type_id, is_available) VALUES (3, 1, N'203', 1, 1);
INSERT INTO rooms (id, hotel_id, number, room_type_id, is_available) VALUES (4, 1, N'204', 1, 1);
INSERT INTO rooms (id, hotel_id, number, room_type_id, is_available) VALUES (5, 1, N'205', 1, 1);
INSERT INTO rooms (id, hotel_id, number, room_type_id, is_available) VALUES (6, 1, N'206', 1, 1);
INSERT INTO rooms (id, hotel_id, number, room_type_id, is_available) VALUES (7, 1, N'207', 1, 1);
INSERT INTO rooms (id, hotel_id, number, room_type_id, is_available) VALUES (8, 1, N'208', 1, 1);
INSERT INTO rooms (id, hotel_id, number, room_type_id, is_available) VALUES (9, 1, N'209', 1, 1);
INSERT INTO rooms (id, hotel_id, number, room_type_id, is_available) VALUES (10, 1, N'210', 1, 1);
INSERT INTO rooms (id, hotel_id, number, room_type_id, is_available) VALUES (11, 1, N'301', 2, 1);
INSERT INTO rooms (id, hotel_id, number, room_type_id, is_available) VALUES (12, 1, N'302', 2, 1);
INSERT INTO rooms (id, hotel_id, number, room_type_id, is_available) VALUES (13, 1, N'303', 2, 1);
INSERT INTO rooms (id, hotel_id, number, room_type_id, is_available) VALUES (14, 1, N'304', 2, 1);
INSERT INTO rooms (id, hotel_id, number, room_type_id, is_available) VALUES (15, 1, N'305', 2, 1);
INSERT INTO rooms (id, hotel_id, number, room_type_id, is_available) VALUES (16, 1, N'306', 2, 1);
INSERT INTO rooms (id, hotel_id, number, room_type_id, is_available) VALUES (17, 1, N'307', 2, 1);
INSERT INTO rooms (id, hotel_id, number, room_type_id, is_available) VALUES (18, 1, N'308', 2, 1);
INSERT INTO rooms (id, hotel_id, number, room_type_id, is_available) VALUES (19, 1, N'309', 2, 1);
INSERT INTO rooms (id, hotel_id, number, room_type_id, is_available) VALUES (20, 1, N'310', 2, 1);
INSERT INTO rooms (id, hotel_id, number, room_type_id, is_available) VALUES (21, 1, N'401', 3, 1);
INSERT INTO rooms (id, hotel_id, number, room_type_id, is_available) VALUES (22, 1, N'402', 3, 1);
INSERT INTO rooms (id, hotel_id, number, room_type_id, is_available) VALUES (23, 1, N'403', 3, 1);
INSERT INTO rooms (id, hotel_id, number, room_type_id, is_available) VALUES (24, 1, N'404', 3, 1);
INSERT INTO rooms (id, hotel_id, number, room_type_id, is_available) VALUES (25, 1, N'405', 3, 1);
INSERT INTO rooms (id, hotel_id, number, room_type_id, is_available) VALUES (26, 1, N'406', 3, 1);
INSERT INTO rooms (id, hotel_id, number, room_type_id, is_available) VALUES (27, 1, N'407', 3, 1);
INSERT INTO rooms (id, hotel_id, number, room_type_id, is_available) VALUES (28, 1, N'408', 3, 1);
INSERT INTO rooms (id, hotel_id, number, room_type_id, is_available) VALUES (29, 1, N'409', 3, 1);
INSERT INTO rooms (id, hotel_id, number, room_type_id, is_available) VALUES (30, 1, N'410', 3, 1);
INSERT INTO rooms (id, hotel_id, number, room_type_id, is_available) VALUES (31, 2, N'101', 4, 1);
INSERT INTO rooms (id, hotel_id, number, room_type_id, is_available) VALUES (32, 2, N'102', 4, 1);
INSERT INTO rooms (id, hotel_id, number, room_type_id, is_available) VALUES (33, 2, N'103', 4, 1);
INSERT INTO rooms (id, hotel_id, number, room_type_id, is_available) VALUES (34, 2, N'104', 4, 1);
INSERT INTO rooms (id, hotel_id, number, room_type_id, is_available) VALUES (35, 2, N'105', 4, 1);
INSERT INTO rooms (id, hotel_id, number, room_type_id, is_available) VALUES (36, 2, N'106', 4, 1);
INSERT INTO rooms (id, hotel_id, number, room_type_id, is_available) VALUES (37, 2, N'107', 4, 1);
INSERT INTO rooms (id, hotel_id, number, room_type_id, is_available) VALUES (38, 2, N'108', 4, 1);
INSERT INTO rooms (id, hotel_id, number, room_type_id, is_available) VALUES (39, 2, N'109', 4, 1);
INSERT INTO rooms (id, hotel_id, number, room_type_id, is_available) VALUES (40, 2, N'110', 4, 1);
INSERT INTO rooms (id, hotel_id, number, room_type_id, is_available) VALUES (41, 2, N'111', 4, 1);
INSERT INTO rooms (id, hotel_id, number, room_type_id, is_available) VALUES (42, 2, N'112', 4, 1);
INSERT INTO rooms (id, hotel_id, number, room_type_id, is_available) VALUES (43, 2, N'201', 5, 1);
INSERT INTO rooms (id, hotel_id, number, room_type_id, is_available) VALUES (44, 2, N'202', 5, 1);
INSERT INTO rooms (id, hotel_id, number, room_type_id, is_available) VALUES (45, 2, N'203', 5, 1);
INSERT INTO rooms (id, hotel_id, number, room_type_id, is_available) VALUES (46, 2, N'204', 5, 1);
INSERT INTO rooms (id, hotel_id, number, room_type_id, is_available) VALUES (47, 2, N'205', 5, 1);
INSERT INTO rooms (id, hotel_id, number, room_type_id, is_available) VALUES (48, 2, N'206', 5, 1);
INSERT INTO rooms (id, hotel_id, number, room_type_id, is_available) VALUES (49, 2, N'207', 5, 1);
INSERT INTO rooms (id, hotel_id, number, room_type_id, is_available) VALUES (50, 2, N'208', 5, 1);
INSERT INTO rooms (id, hotel_id, number, room_type_id, is_available) VALUES (51, 2, N'209', 5, 1);
INSERT INTO rooms (id, hotel_id, number, room_type_id, is_available) VALUES (52, 2, N'210', 5, 1);
INSERT INTO rooms (id, hotel_id, number, room_type_id, is_available) VALUES (53, 2, N'211', 5, 1);
INSERT INTO rooms (id, hotel_id, number, room_type_id, is_available) VALUES (54, 2, N'212', 5, 1);
INSERT INTO rooms (id, hotel_id, number, room_type_id, is_available) VALUES (55, 2, N'301', 6, 1);
INSERT INTO rooms (id, hotel_id, number, room_type_id, is_available) VALUES (56, 2, N'302', 6, 1);
INSERT INTO rooms (id, hotel_id, number, room_type_id, is_available) VALUES (57, 2, N'303', 6, 1);
INSERT INTO rooms (id, hotel_id, number, room_type_id, is_available) VALUES (58, 2, N'304', 6, 1);
INSERT INTO rooms (id, hotel_id, number, room_type_id, is_available) VALUES (59, 2, N'305', 6, 1);
INSERT INTO rooms (id, hotel_id, number, room_type_id, is_available) VALUES (60, 2, N'306', 6, 1);
INSERT INTO rooms (id, hotel_id, number, room_type_id, is_available) VALUES (61, 2, N'307', 6, 1);
INSERT INTO rooms (id, hotel_id, number, room_type_id, is_available) VALUES (62, 2, N'308', 6, 1);
INSERT INTO rooms (id, hotel_id, number, room_type_id, is_available) VALUES (63, 2, N'309', 6, 1);
INSERT INTO rooms (id, hotel_id, number, room_type_id, is_available) VALUES (64, 2, N'310', 6, 1);
INSERT INTO rooms (id, hotel_id, number, room_type_id, is_available) VALUES (65, 2, N'311', 6, 1);
INSERT INTO rooms (id, hotel_id, number, room_type_id, is_available) VALUES (66, 2, N'312', 6, 1);
SET IDENTITY_INSERT rooms OFF;

SET IDENTITY_INSERT room_types_has_utilities ON;
INSERT INTO room_types_has_utilities (id, utility_id, room_type_id)VALUES (1, 1, 1);
INSERT INTO room_types_has_utilities (id, utility_id, room_type_id) VALUES (2, 7, 1);
INSERT INTO room_types_has_utilities (id, utility_id, room_type_id) VALUES (3, 8, 1);
INSERT INTO room_types_has_utilities (id, utility_id, room_type_id) VALUES (4, 9, 1);
INSERT INTO room_types_has_utilities (id, utility_id, room_type_id) VALUES (5, 10, 1);
INSERT INTO room_types_has_utilities (id, utility_id, room_type_id) VALUES (6, 3, 2);
INSERT INTO room_types_has_utilities (id, utility_id, room_type_id) VALUES (7, 7, 2);
INSERT INTO room_types_has_utilities (id, utility_id, room_type_id) VALUES (8, 8, 2);
INSERT INTO room_types_has_utilities (id, utility_id, room_type_id) VALUES (9, 9, 2);
INSERT INTO room_types_has_utilities (id, utility_id, room_type_id) VALUES (10, 10, 2);
INSERT INTO room_types_has_utilities (id, utility_id, room_type_id) VALUES (11, 1, 3);
INSERT INTO room_types_has_utilities (id, utility_id, room_type_id) VALUES (12, 3, 3);
INSERT INTO room_types_has_utilities (id, utility_id, room_type_id) VALUES (13, 7, 3);
INSERT INTO room_types_has_utilities (id, utility_id, room_type_id) VALUES (14, 8, 3);
INSERT INTO room_types_has_utilities (id, utility_id, room_type_id) VALUES (15, 9, 3);
INSERT INTO room_types_has_utilities (id, utility_id, room_type_id) VALUES (16, 10, 3);
INSERT INTO room_types_has_utilities (id, utility_id, room_type_id) VALUES (17, 1, 4);
INSERT INTO room_types_has_utilities (id, utility_id, room_type_id) VALUES (18, 12, 4);
INSERT INTO room_types_has_utilities (id, utility_id, room_type_id) VALUES (19, 14, 4);
INSERT INTO room_types_has_utilities (id, utility_id, room_type_id) VALUES (20, 15, 4);
INSERT INTO room_types_has_utilities (id, utility_id, room_type_id) VALUES (21, 1, 5);
INSERT INTO room_types_has_utilities (id, utility_id, room_type_id) VALUES (22, 2, 5);
INSERT INTO room_types_has_utilities (id, utility_id, room_type_id) VALUES (23, 8, 5);
INSERT INTO room_types_has_utilities (id, utility_id, room_type_id) VALUES (24, 13, 5);
INSERT INTO room_types_has_utilities (id, utility_id, room_type_id) VALUES (25, 14, 5);
INSERT INTO room_types_has_utilities (id, utility_id, room_type_id) VALUES (26, 15, 5);
INSERT INTO room_types_has_utilities (id, utility_id, room_type_id) VALUES (27, 1, 6);
INSERT INTO room_types_has_utilities (id, utility_id, room_type_id) VALUES (28, 2, 6);
INSERT INTO room_types_has_utilities (id, utility_id, room_type_id) VALUES (29, 4, 6);
INSERT INTO room_types_has_utilities (id, utility_id, room_type_id) VALUES (30, 7, 6);
INSERT INTO room_types_has_utilities (id, utility_id, room_type_id) VALUES (31, 8, 6);
INSERT INTO room_types_has_utilities (id, utility_id, room_type_id) VALUES (32, 10, 6);
SET IDENTITY_INSERT room_types_has_utilities OFF;

SET IDENTITY_INSERT images ON;
INSERT INTO images (id, url)VALUES (1, N'assets/uploads/a77782f7-f46b-4702-b4b2-f04def03a7df-1730280491650.jpg');
INSERT INTO images (id, url) VALUES (2, N'assets/uploads/6aff8392-d221-4919-afb6-66c9d741fc6a-1730280491654.jpg');
INSERT INTO images (id, url) VALUES (3, N'assets/uploads/2222e165-9e22-4191-9c41-bf480a770fed-1730280491657.jpg');
INSERT INTO images (id, url) VALUES (4, N'assets/uploads/c200003d-ee59-4761-b386-c9fb9d472ad2-1730280555332.jpg');
INSERT INTO images (id, url) VALUES (5, N'assets/uploads/fe7367cb-c7f3-4780-8b65-fae39e65f186-1730280555336.jpg');
INSERT INTO images (id, url) VALUES (6, N'assets/uploads/cf49eec8-76c4-44d4-91b6-a4778dcb204e-1730280555339.jpg');
INSERT INTO images (id, url) VALUES (7, N'assets/uploads/40b42749-c8e6-4cf9-84e2-3088016df96a-1730280659727.jpg');
INSERT INTO images (id, url) VALUES (8, N'assets/uploads/e96fbef9-ccd7-4bdc-92e9-742ebfbbb998-1730280659732.jpg');
INSERT INTO images (id, url) VALUES (9, N'assets/uploads/c69ab0f8-0731-4ecd-9a28-b6fa2117b02d-1730280659736.jpg');
INSERT INTO images (id, url) VALUES (10, N'assets/uploads/60395e84-88a8-4319-bf38-a3b0903ad097-1730281357005.jpg');
INSERT INTO images (id, url) VALUES (11, N'assets/uploads/fe0a77bb-8402-4dbc-9a36-f70ce7cf5e2e-1730281357016.jpg');
INSERT INTO images (id, url) VALUES (12, N'assets/uploads/2c6ccdfb-64d5-469e-82d2-e5468e65cbbb-1730281357020.jpg');
INSERT INTO images (id, url) VALUES (13, N'assets/uploads/88575088-3ca1-4113-9cc9-e7b7ad88f26a-1730281437726.jpg');
INSERT INTO images (id, url) VALUES (14, N'assets/uploads/4670b3c3-29fd-4105-808f-1185868b7643-1730281437736.jpg');
INSERT INTO images (id, url) VALUES (15, N'assets/uploads/c6f19436-a4e8-49f7-9dd6-5348be5f426d-1730281437744.jpg');
INSERT INTO images (id, url) VALUES (16, N'assets/uploads/f7556963-406d-4f7c-aa3c-eec05461792b-1730281437753.jpg');
INSERT INTO images (id, url) VALUES (17, N'assets/uploads/a29f14ca-a180-44d8-81fd-25607de58801-1730281437762.jpg');
INSERT INTO images (id, url) VALUES (18, N'assets/uploads/c7577978-fee8-4bc7-8679-3dd85bda93ca-1730281514483.jpg');
INSERT INTO images (id, url) VALUES (19, N'assets/uploads/db19d3ac-b80f-4eee-ae20-00c17a687b4c-1730281514493.jpg');
INSERT INTO images (id, url) VALUES (20, N'assets/uploads/2e413a3d-a1ad-4d12-8d2d-cb25fd0fdf5c-1730281514500.jpg');
INSERT INTO images (id, url) VALUES (21, N'assets/uploads/0953025e-9270-43fc-ab80-fdc71d326fac-1730281514509.jpg');
INSERT INTO images (id, url) VALUES (22, N'assets/uploads/40ddb679-436e-407e-83be-1b989c365ecb-1730281514518.jpg');
SET IDENTITY_INSERT images OFF;

SET IDENTITY_INSERT room_type_has_images ON;
INSERT INTO room_type_has_images (id, room_type_id, image_id)VALUES (1, 1, 1);
INSERT INTO room_type_has_images (id, room_type_id, image_id) VALUES (2, 1, 2);
INSERT INTO room_type_has_images (id, room_type_id, image_id) VALUES (3, 1, 3);
INSERT INTO room_type_has_images (id, room_type_id, image_id) VALUES (4, 2, 4);
INSERT INTO room_type_has_images (id, room_type_id, image_id) VALUES (5, 2, 5);
INSERT INTO room_type_has_images (id, room_type_id, image_id) VALUES (6, 2, 6);
INSERT INTO room_type_has_images (id, room_type_id, image_id) VALUES (7, 3, 7);
INSERT INTO room_type_has_images (id, room_type_id, image_id) VALUES (8, 3, 8);
INSERT INTO room_type_has_images (id, room_type_id, image_id) VALUES (9, 3, 9);
INSERT INTO room_type_has_images (id, room_type_id, image_id) VALUES (10, 4, 10);
INSERT INTO room_type_has_images (id, room_type_id, image_id) VALUES (11, 4, 11);
INSERT INTO room_type_has_images (id, room_type_id, image_id) VALUES (12, 4, 12);
INSERT INTO room_type_has_images (id, room_type_id, image_id) VALUES (13, 5, 13);
INSERT INTO room_type_has_images (id, room_type_id, image_id) VALUES (14, 5, 14);
INSERT INTO room_type_has_images (id, room_type_id, image_id) VALUES (15, 5, 15);
INSERT INTO room_type_has_images (id, room_type_id, image_id) VALUES (16, 5, 16);
INSERT INTO room_type_has_images (id, room_type_id, image_id) VALUES (17, 5, 17);
INSERT INTO room_type_has_images (id, room_type_id, image_id) VALUES (18, 6, 18);
INSERT INTO room_type_has_images (id, room_type_id, image_id) VALUES (19, 6, 19);
INSERT INTO room_type_has_images (id, room_type_id, image_id) VALUES (20, 6, 20);
INSERT INTO room_type_has_images (id, room_type_id, image_id) VALUES (21, 6, 21);
INSERT INTO room_type_has_images (id, room_type_id, image_id) VALUES (22, 6, 22);
SET IDENTITY_INSERT room_type_has_images OFF;

SET IDENTITY_INSERT payments ON;
INSERT INTO payments (id, customer_id, amount, txnRef, orderInfo, bankCode, transactionNo, transactionStatus, cardType, bankTranNo, created_at, paid_at)VALUES (1, 1, 1000000, N'57462498', N'dcaabd6b-da67-4c6c-ab21-801fe41dfa2e-1730281842091|1', N'NCB', N'14641115', N'00', N'ATM', N'VNP14641115', N'2024-10-30 16:53:21.847', N'2024-10-30 16:53:26.000');
INSERT INTO payments (id, customer_id, amount, txnRef, orderInfo, bankCode, transactionNo, transactionStatus, cardType, bankTranNo, created_at, paid_at) VALUES (2, 1, 5000000, N'92138913', N'b2fb115c-e3f7-445d-a18a-792c517cb912-1730282076416|2', N'NCB', N'14641124', N'00', N'ATM', N'VNP14641124', N'2024-10-30 16:54:50.557', N'2024-10-30 16:54:55.000');
INSERT INTO payments (id, customer_id, amount, txnRef, orderInfo, bankCode, transactionNo, transactionStatus, cardType, bankTranNo, created_at, paid_at) VALUES (3, 1, 3000000, N'57386339', N'df5de10f-83d1-4ae4-aaac-b6349a67e01e-1730282125195|3', N'NCB', N'14641130', N'00', N'ATM', N'VNP14641130', N'2024-10-30 16:55:41.437', N'2024-10-30 16:55:45.000');
INSERT INTO payments (id, customer_id, amount, txnRef, orderInfo, bankCode, transactionNo, transactionStatus, cardType, bankTranNo, created_at, paid_at) VALUES (4, 2, 1500000, N'52129176', N'12a0906b-3e11-4cca-b7ab-80add2a13840-1730284852287|4', N'NCB', N'14641232', N'00', N'ATM', N'VNP14641232', N'2024-10-30 17:41:08.173', N'2024-10-30 17:41:12.000');
INSERT INTO payments (id, customer_id, amount, txnRef, orderInfo, bankCode, transactionNo, transactionStatus, cardType, bankTranNo, created_at, paid_at) VALUES (5, 2, 3000000, N'68826408', N'19cc68a3-295b-45c0-a147-c2904df96828-1730284894074|5', N'NCB', N'14641233', N'00', N'ATM', N'VNP14641233', N'2024-10-30 17:41:51.053', N'2024-10-30 17:41:55.000');
INSERT INTO payments (id, customer_id, amount, txnRef, orderInfo, bankCode, transactionNo, transactionStatus, cardType, bankTranNo, created_at, paid_at) VALUES (6, 2, 2100000, N'14527733', N'b83fc361-1dc2-4665-b4f9-f8a53aea1463-1730285063879|6', N'NCB', N'14641239', N'00', N'ATM', N'VNP14641239', N'2024-10-30 17:44:40.870', N'2024-10-30 17:44:45.000');
INSERT INTO payments (id, customer_id, amount, txnRef, orderInfo, bankCode, transactionNo, transactionStatus, cardType, bankTranNo, created_at, paid_at) VALUES (7, 2, 1000000, N'90129185', N'25fb5fef-a193-4198-a7d9-6a0e63613f48-1730285358985|7', N'NCB', N'14641242', N'00', N'ATM', N'VNP14641242', N'2024-10-30 17:49:32.480', N'2024-10-30 17:49:37.000');
INSERT INTO payments (id, customer_id, amount, txnRef, orderInfo, bankCode, transactionNo, transactionStatus, cardType, bankTranNo, created_at, paid_at) VALUES (8, 3, 1400000, N'29939836', N'87ed3a4d-c5bc-4e53-b445-29c023ff49a9-1730291549011|8', N'NCB', N'14641366', N'00', N'ATM', N'VNP14641366', N'2024-10-30 19:32:43.503', N'2024-10-30 19:32:47.000');
INSERT INTO payments (id, customer_id, amount, txnRef, orderInfo, bankCode, transactionNo, transactionStatus, cardType, bankTranNo, created_at, paid_at) VALUES (9, 4, 3000000, N'92299129', N'1d6284ce-0ab9-4d9a-bedf-ba94e82117fe-1730389137587|9', N'NCB', N'14644119', N'00', N'ATM', N'VNP14644119', N'2024-10-31 22:39:10.903', N'2024-10-31 22:39:15.000');
INSERT INTO payments (id, customer_id, amount, txnRef, orderInfo, bankCode, transactionNo, transactionStatus, cardType, bankTranNo, created_at, paid_at) VALUES (10, 4, 6000000, N'59781576', N'80015d61-75a1-4dc9-b239-ad61e41e1bf7-1730389179082|10', N'NCB', N'14644122', N'00', N'ATM', N'VNP14644122', N'2024-10-31 22:39:55.147', N'2024-10-31 22:40:00.000');
INSERT INTO payments (id, customer_id, amount, txnRef, orderInfo, bankCode, transactionNo, transactionStatus, cardType, bankTranNo, created_at, paid_at) VALUES (11, 4, 2000000, N'56859502', N'80a17f8d-8b0f-4d9e-95dc-01ebe45584bf-1730389265682|11', N'NCB', N'14644124', N'00', N'ATM', N'VNP14644124', N'2024-10-31 22:41:20.203', N'2024-10-31 22:41:24.000');
SET IDENTITY_INSERT payments OFF;

SET IDENTITY_INSERT bookings ON;
INSERT INTO bookings (id, customer_id, room_id, payment_id, check_in_date, check_out_date, price, status, created_at, updated_at)VALUES (1, 1, 1, 1, N'2024-11-01', N'2024-11-03', 1000000, N'PAID', N'2024-10-30 16:50:37.423', N'2024-10-30 16:53:21.000');
INSERT INTO bookings (id, customer_id, room_id, payment_id, check_in_date, check_out_date, price, status, created_at, updated_at) VALUES (2, 1, 25, 2, N'2024-11-01', N'2024-11-06', 5000000, N'PAID', N'2024-10-30 16:54:32.573', N'2024-10-30 16:54:50.000');
INSERT INTO bookings (id, customer_id, room_id, payment_id, check_in_date, check_out_date, price, status, created_at, updated_at) VALUES (3, 1, 58, 3, N'2024-11-09', N'2024-11-11', 3000000, N'PAID', N'2024-10-30 16:55:17.247', N'2024-10-30 16:55:41.000');
INSERT INTO bookings (id, customer_id, room_id, payment_id, check_in_date, check_out_date, price, status, created_at, updated_at) VALUES (4, 2, 2, 4, N'2024-11-01', N'2024-11-04', 1500000, N'PAID', N'2024-10-30 17:40:47.680', N'2024-10-30 17:41:08.000');
INSERT INTO bookings (id, customer_id, room_id, payment_id, check_in_date, check_out_date, price, status, created_at, updated_at) VALUES (5, 2, 61, 5, N'2024-11-10', N'2024-11-12', 3000000, N'PAID', N'2024-10-30 17:41:29.900', N'2024-10-30 17:41:51.000');
INSERT INTO bookings (id, customer_id, room_id, payment_id, check_in_date, check_out_date, price, status, created_at, updated_at) VALUES (6, 2, 17, 6, N'2024-11-08', N'2024-11-11', 2100000, N'PAID', N'2024-10-30 17:44:17.617', N'2024-10-30 17:44:40.000');
INSERT INTO bookings (id, customer_id, room_id, payment_id, check_in_date, check_out_date, price, status, created_at, updated_at) VALUES (7, 2, 4, 7, N'2024-11-21', N'2024-11-23', 1000000, N'PAID', N'2024-10-30 17:49:16.320', N'2024-10-30 17:49:32.000');
INSERT INTO bookings (id, customer_id, room_id, payment_id, check_in_date, check_out_date, price, status, created_at, updated_at) VALUES (8, 3, 19, 8, N'2024-11-08', N'2024-11-10', 1400000, N'PAID', N'2024-10-30 19:32:24.617', N'2024-10-30 19:32:43.000');
INSERT INTO bookings (id, customer_id, room_id, payment_id, check_in_date, check_out_date, price, status, created_at, updated_at) VALUES (9, 4, 54, 9, N'2024-11-01', N'2024-11-04', 3000000, N'PAID', N'2024-10-31 22:31:09.050', N'2024-10-31 22:39:12.000');
INSERT INTO bookings (id, customer_id, room_id, payment_id, check_in_date, check_out_date, price, status, created_at, updated_at) VALUES (10, 4, 59, 10, N'2024-11-01', N'2024-11-05', 6000000, N'PAID', N'2024-10-31 22:39:28.767', N'2024-10-31 22:39:56.000');
INSERT INTO bookings (id, customer_id, room_id, payment_id, check_in_date, check_out_date, price, status, created_at, updated_at) VALUES (11, 4, 8, 11, N'2024-11-01', N'2024-11-05', 2000000, N'PAID', N'2024-10-31 22:41:00.673', N'2024-10-31 22:41:21.000');
SET IDENTITY_INSERT bookings OFF;

SET IDENTITY_INSERT reviews ON;
INSERT INTO reviews (id, customer_id, booking_id, rating, comment, created_at)VALUES (1, 2, 4, 5, N'rất tốt, cho 5 sao.', N'2024-10-30 17:42:07.080');
INSERT INTO reviews (id, customer_id, booking_id, rating, comment, created_at) VALUES (2, 2, 5, 3, N'tốt nhưng hôm đó mưa nhân viên khách sạn k mang dù cho mình', N'2024-10-30 17:42:29.487');
INSERT INTO reviews (id, customer_id, booking_id, rating, comment, created_at) VALUES (3, 1, 1, 4, N'tốt', N'2024-10-30 17:43:32.887');
INSERT INTO reviews (id, customer_id, booking_id, rating, comment, created_at) VALUES (4, 1, 2, 5, N'rất tốt, dịch vụ oke', N'2024-10-30 17:43:44.693');
INSERT INTO reviews (id, customer_id, booking_id, rating, comment, created_at) VALUES (5, 1, 3, 5, N'rất tốt, đào ngon', N'2024-10-30 17:43:54.253');
INSERT INTO reviews (id, customer_id, booking_id, rating, comment, created_at) VALUES (6, 2, 6, 5, N'tuyệt vời', N'2024-10-30 17:44:47.457');
INSERT INTO reviews (id, customer_id, booking_id, rating, comment, created_at) VALUES (7, 2, 7, 1, N'quá tệ, cho 1 sao', N'2024-10-30 17:49:42.337');
INSERT INTO reviews (id, customer_id, booking_id, rating, comment, created_at) VALUES (8, 4, 9, 5, N'dịch vụ tuyệt vời, `service` rất là oke', N'2024-10-31 22:40:17.523');
INSERT INTO reviews (id, customer_id, booking_id, rating, comment, created_at) VALUES (9, 4, 10, 4, N'ổn áp, tuy nhiên k đc như kì vọng nên cho 4*', N'2024-10-31 22:40:34.807');
INSERT INTO reviews (id, customer_id, booking_id, rating, comment, created_at) VALUES (10, 4, 11, 2, N'tệ quá :(', N'2024-10-31 22:41:28.367');
SET IDENTITY_INSERT reviews OFF;

select rooms.* from rooms inner join room_types on rooms.room_type_id = room_types.id where room_type_id = ? and hidden = 0 and rooms.id not in (select bookings.room_id from bookings where payment_id is not null and check_in_date < ? and check_out_date > ?);