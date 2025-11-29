CREATE DATABASE IF NOT EXISTS exam_proctoring;
USE exam_proctoring;

-- Table for Admin Users
CREATE TABLE IF NOT EXISTS admins (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50),
    email VARCHAR(100) UNIQUE,
    password VARCHAR(50)
);

-- Table for Students
CREATE TABLE IF NOT EXISTS students (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50),
    email VARCHAR(100) UNIQUE,
    password VARCHAR(50)
);

-- Table for Questions
CREATE TABLE IF NOT EXISTS questions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    question TEXT,
    optA VARCHAR(100),
    optB VARCHAR(100),
    optC VARCHAR(100),
    optD VARCHAR(100),
    correctOpt INT
);

-- Table for Exam Results
CREATE TABLE IF NOT EXISTS results (
    id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT,
    score INT,
    warnings INT DEFAULT 0,
    exam_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES students(id)
);

-- Insert a default Admin (Email: admin@test.com, Password: 123)
INSERT INTO admins (name, email, password) VALUES ('Super Admin', 'admin@test.com', '123');

-- Insert Dummy Questions
INSERT INTO questions (question, optA, optB, optC, optD, correctOpt) VALUES 
('Which keyword is used to inherit a class in Java?', 'this', 'extends', 'super', 'implement', 2),
('What is the size of int variable?', '8 bit', '16 bit', '32 bit', '64 bit', 3);