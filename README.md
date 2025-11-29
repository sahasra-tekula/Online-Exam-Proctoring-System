Online Exam Proctoring System (OEPS)

A Java Desktop Application for conducting online exams with basic proctoring features. This system allows Admins to manage questions and Students to take exams while monitoring their window activity.

🚀 Version History

v1.0 (Current): Desktop GUI (Swing) + JDBC Database Connectivity + Basic Window Switching Detection.

v2.0 (Planned): Improved UI + Advanced Features.

v3.0 (Planned): Webcam Integration + AI Proctoring.

v4.0 (Future): Complete Rewrite as a Web Application.

🛠️ Tech Stack

Language: Java (JDK 8+)

GUI: Java Swing (JFrame)

Database: MySQL

Connectivity: JDBC (mysql-connector-j)

⚙️ Setup & Installation

1. Database Setup

Open MySQL Workbench or your command line.

Run the script provided in database_setup.sql.

This will create the database exam_proctoring and the required tables.

2. Configure Connection

Open DatabaseConnection.java and ensure your MySQL credentials are correct:

private static final String USER = "root"; // Your MySQL Username
private static final String PASSWORD = "1234"; // Your MySQL Password


3. Run the Application

Compile and run the main class:

javac -cp "lib/mysql-connector-j-9.5.0.jar;." *.java
java -cp "lib/mysql-connector-j-9.5.0.jar;." SmartExamGUI


(Note: On Linux/Mac use : instead of ; in the classpath)

🎮 Usage Guide

Admin Login

Default Email: admin@test.com

Default Password: 123

Features: Add Questions, View Registered Students, View Results.

Student Login

Register a new account from the Login screen.

Login with your credentials.

Take Exam: * Select answers and click "Next".

⚠️ Warning: Switching windows (Alt+Tab) will trigger a proctoring alert!

The exam auto-submits when the timer ends.

📂 Project Structure

SmartExamGUI.java: Entry point for the GUI application.

DatabaseConnection.java: Handles MySQL connection.

Admin.java / Student.java: User models and database operations.

database_setup.sql: SQL script to initialize the database.