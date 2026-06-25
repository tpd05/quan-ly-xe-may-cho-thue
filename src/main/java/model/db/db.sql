CREATE DATABASE IF NOT EXISTS thue_xe_may
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE thue_xe_may;

CREATE TABLE TaiKhoan (
    userID INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    hoTen VARCHAR(100) NOT NULL,
    soDienThoai VARCHAR(15) UNIQUE,
    email VARCHAR(100),
    soCCCD VARCHAR(20) UNIQUE
);

CREATE TABLE DoiTac (
    maDoiTac INT PRIMARY KEY AUTO_INCREMENT,
    userID INT  UNIQUE,
    FOREIGN KEY (userID) REFERENCES TaiKhoan(userID)
        ON DELETE CASCADE
);

CREATE TABLE ChiNhanh (
    maChiNhanh INT PRIMARY KEY AUTO_INCREMENT,
    maDoiTac INT,
    tenChiNhanh VARCHAR(100),
    diaDiem VARCHAR(255),
    FOREIGN KEY (maDoiTac) REFERENCES DoiTac(maDoiTac)
        ON DELETE CASCADE
);

CREATE TABLE XeMay (
    maXe INT PRIMARY KEY AUTO_INCREMENT,
    maChiNhanh INT,
    hangXe VARCHAR(100) NOT NULL,
    dongXe VARCHAR(100) NOT NULL,
    doiXe INT,
    dungTich FLOAT,
    urlHinhAnh TEXT,
    bienSo VARCHAR(20) UNIQUE NOT NULL,
    trangThai VARCHAR(50) NOT NULL,
    giaNgay DECIMAL(10,2),
    giaTuan DECIMAL(10,2),
    FOREIGN KEY (maChiNhanh) REFERENCES ChiNhanh(maChiNhanh)
);

CREATE TABLE DonThue (
    maDonThue INT PRIMARY KEY AUTO_INCREMENT,
    userID INT,
    diaChiNhanXe VARCHAR(255),
    trangThai VARCHAR(50),
    ngayDat DATETIME,
    FOREIGN KEY (userID) REFERENCES TaiKhoan(userID)
);

CREATE TABLE ChiTietDonThue (
    maChiTiet INT PRIMARY KEY AUTO_INCREMENT,
    maDonThue INT,
    maXe INT,
    thoiGianBatDau DATETIME,
    thoiGianKetThuc DATETIME,
    thoiGianTra DATETIME,
    donGia INT,
    FOREIGN KEY (maDonThue) REFERENCES DonThue(maDonThue)
        ON DELETE CASCADE,
    FOREIGN KEY (maXe) REFERENCES XeMay(maXe)
);

CREATE TABLE ThanhToan (
    maThanhToan INT PRIMARY KEY AUTO_INCREMENT,
    maDonThue INT,
    soTien DECIMAL(10,2),
    phuongThuc VARCHAR(50),
    thoiGianTao DATETIME,
    trangThai VARCHAR(50),
    FOREIGN KEY (maDonThue) REFERENCES DonThue(maDonThue)
        ON DELETE CASCADE
);