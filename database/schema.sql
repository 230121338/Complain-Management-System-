-- Complaint Management System schema and seed data (MySQL / MariaDB)

CREATE DATABASE IF NOT EXISTS complaintdb;
USE complaintdb;

CREATE TABLE IF NOT EXISTS users (
    userId       INT PRIMARY KEY AUTO_INCREMENT,
    fullname     VARCHAR(100),
    username     VARCHAR(50),
    password     VARCHAR(100),
    role         VARCHAR(30),
    profileImage LONGBLOB,
    profileType  VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS complaints (
    complaintId  INT PRIMARY KEY AUTO_INCREMENT,
    studentId    INT,
    complaint    TEXT,
    floor        VARCHAR(20),
    room         VARCHAR(20),
    status       VARCHAR(30),
    dateReported DATE,
    viewedDate   DATE,
    fixedDate    DATE,
    image        LONGBLOB,
    imageType    VARCHAR(100),
    FOREIGN KEY (studentId) REFERENCES users(userId)
);

-- Seed users (one per role)
INSERT INTO users (fullname, username, password, role) VALUES
('John Student',   'student',   '123', 'Student'),
('Mary Reception', 'reception', '123', 'Receptionist'),
('Peter Employee', 'employee',  '123', 'Employee');
