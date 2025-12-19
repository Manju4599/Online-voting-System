Online Voting System (Java + JDBC + MySQL)
A simple command-line Online Voting System built with Java, JDBC, and MySQL.
It supports two roles: User (Voter) and Admin, with separate menus and operations.

Features
User (Voter)
User sign-up with:

Name, Age (must be ≥ 18), Email, Phone, Password, Address (state/district/city/area).

Login using email + password.

After login, user can:

Vote in active elections for their address (only once per election).

Update age and phone number.

Update address.

View voting history with election and candidate names.

View previous election results.

Logout.

Admin
Single admin account stored in database.

Admin login with email and password.

After login, admin can:

Manage elections:

Add election (year, name, description, dates, status, scope address).

Start election (status RUNNING).

Terminate election (status ENDED).

List all elections.

Manage candidates:

Add candidate (name, party, election).

Update candidate.

Delete candidate.

List all candidates.

Manage voters:

List all users.

Update voter age/phone.

Delete voter.

Manage results:

Announce result (set status RESULT_DECLARED).

View previous election results (with vote counts).

Logout.

Technology Stack
Language: Java (console application)

Database: MySQL

Database Access: JDBC (Java Database Connectivity)

JDBC Driver: MySQL Connector/J (mysql-connector-j-9.5.0.jar or similar)

Build/Run: javac and java from command line

Project Structure
text
online_voting_sys/
├─ lib/
│  └─ mysql-connector-j-9.5.0.jar
└─ src/
   └─ com/
      └─ voting/
         ├─ app/
         │  └─ Main.java
         ├─ db/
         │  └─ DBConnection.java
         ├─ model/
         │  ├─ User.java
         │  ├─ Admin.java
         │  ├─ Election.java
         │  ├─ Candidate.java
         │  └─ Vote.java
         ├─ dao/
         │  ├─ UserDAO.java
         │  ├─ AdminDAO.java
         │  ├─ ElectionDAO.java
         │  ├─ CandidateDAO.java
         │  └─ VoteDAO.java
         └─ service/
            ├─ UserService.java
            └─ AdminService.java
Database Setup
Start MySQL and create the database:

sql
CREATE DATABASE IF NOT EXISTS voting_db;
USE voting_db;
Create tables:

sql
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    age INT NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(20) NOT NULL,
    password VARCHAR(100) NOT NULL,
    address VARCHAR(255) NOT NULL
);

CREATE TABLE admin (
    id INT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL
);

INSERT INTO admin (id, name, email, password)
VALUES (1, 'Main Admin', 'admin@voting.com', 'admin123');

CREATE TABLE elections (
    id INT AUTO_INCREMENT PRIMARY KEY,
    year_of_election INT NOT NULL,
    name VARCHAR(200) NOT NULL,
    description VARCHAR(500),
    election_date DATE NOT NULL,
    scheduled_start DATETIME NOT NULL,
    scheduled_end DATETIME NOT NULL,
    status VARCHAR(20) NOT NULL,
    scope_address VARCHAR(255) NOT NULL
);

CREATE TABLE candidates (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    party VARCHAR(100),
    election_id INT NOT NULL,
    FOREIGN KEY (election_id) REFERENCES elections(id) ON DELETE CASCADE
);

CREATE TABLE votes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    election_id INT NOT NULL,
    candidate_id INT NOT NULL,
    vote_time DATETIME NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (election_id) REFERENCES elections(id) ON DELETE CASCADE,
    FOREIGN KEY (candidate_id) REFERENCES candidates(id) ON DELETE CASCADE,
    UNIQUE KEY unique_user_election (user_id, election_id)
);
Configuration
Edit DBConnection.java and set your MySQL credentials:

java
private static final String URL = "jdbc:mysql://localhost:3306/voting_db";
private static final String USER = "<YOUR_DB_USERNAME>";
private static final String PASSWORD = "<YOUR_DB_PASSWORD>";
Ensure the driver is loaded:

java
static {
    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
    } catch (ClassNotFoundException e) {
        System.out.println("MySQL JDBC Driver not found: " + e.getMessage());
    }
}
Build and Run
From the project root:

bash
cd online_voting_sys/src
Compile (Windows)
text
javac -cp ".;C:\Users\Manjunath\projects\online_voting_sys\lib\mysql-connector-j-9.5.0.jar" com\voting\db\DBConnection.java com\voting\model\*.java com\voting\dao\*.java com\voting\service\*.java com\voting\app\Main.java
Run (Windows)
text
java -cp ".;C:\Users\Manjunath\projects\online_voting_sys\lib\mysql-connector-j-9.5.0.jar" com.voting.app.Main
(Adjust paths if your directory or JAR path is different.)

Usage
User sign-up

Choose option 2 “User sign-up”.

Enter details when prompted.

Address format example: Karnataka/Bengaluru/Bengaluru/Jayanagar

Age must be ≥ 18.

User login

Choose option 1 “User login”.

Enter email and password.

After login, use menu:

1 Vote

2 Update age/phone

3 Manage address

4 Voting history & results

5 Logout

Admin login

Choose option 3 “Admin login”.

Default: admin@voting.com / admin123 (unless changed in DB).

Use admin menu to manage elections, candidates, voters, and results.

Future Improvements
Password hashing and stronger security.

Web application version (Servlet/JSP or Spring Boot) with buttons and pages instead of console menus.

Better address handling (separate tables for state/district/city/area).

Role-based access control with more than one admin.
