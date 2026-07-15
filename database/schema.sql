-- Residence Issue / Complaint Management System
-- MySQL / MariaDB schema + seed data
-- Location field uses "block"

CREATE DATABASE IF NOT EXISTS complaintdb;
USE complaintdb;

CREATE TABLE IF NOT EXISTS users (
    userId   INT PRIMARY KEY AUTO_INCREMENT,
    fullname VARCHAR(100),
    username VARCHAR(50),
    password VARCHAR(100),
    role     VARCHAR(30)
);

CREATE TABLE IF NOT EXISTS complaints (
    complaintId  INT PRIMARY KEY AUTO_INCREMENT,
    studentId    INT,
    complaint    TEXT,
    block        VARCHAR(20),
    room         VARCHAR(20),
    status       VARCHAR(30),
    dateReported DATE,
    viewedDate   DATE,
    fixedDate    DATE,
    FOREIGN KEY (studentId) REFERENCES users(userId)
);

-- Seed users (one per role)
INSERT INTO users (fullname, username, password, role) VALUES
('John Student',   'student',   '123', 'Student'),
('Mary Reception', 'reception', '123', 'Receptionist'),
('Peter Employee', 'employee',  '123', 'Employee');

-- Sample complaints for demo (studentId 1 = John Student)
INSERT INTO complaints (studentId, complaint, block, room, status, dateReported, viewedDate, fixedDate) VALUES
(1, 'Leaking tap in the bathroom basin', 'A', '12', 'Not Fixed', CURDATE(), NULL, NULL),
(1, 'Broken light switch near the door', 'B', '05', 'Viewed', CURDATE(), CURDATE(), NULL),
(1, 'Window latch will not close properly', 'C', '21', 'Fixed', CURDATE(), CURDATE(), CURDATE());
