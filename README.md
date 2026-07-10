# Complaint Management System

A role-based complaint management web application built with **Java Servlets, JSP,
JDBC and MySQL/MariaDB**, following the MVC pattern (Model + DAO + Servlet + JSP).

## Roles

| Role | Can do |
|------|--------|
| **Student** | Submit a complaint (complaint text, floor, room) and view own complaints + status |
| **Employee** | View all complaints, mark a complaint as *Viewed*, and mark it *Fixed* |
| **Receptionist** | View all complaints (read-only) including complaint, status, viewed date and fixed date |

## Complaint status flow

```
Student submits  ->  Not Fixed
Employee clicks View  ->  Viewed   (viewedDate = today)
Employee clicks Fix   ->  Fixed    (fixedDate  = today)
```

## Project structure

```
ComplaintSystem
├── pom.xml                         Maven war build
├── database/schema.sql             MySQL schema + seed users
└── src/main
    ├── java/tut/ac/za/complaint
    │   ├── model      User, Complaint
    │   ├── dao        DBConnection, UserDAO, ComplaintDAO
    │   └── servlet    LoginServlet, ComplaintServlet,
    │                  ViewComplaintServlet, FixComplaintServlet, LogoutServlet
    └── webapp
        ├── login.jsp
        ├── studentHome.jsp / complaint.jsp / myComplaints.jsp
        ├── employeeHome.jsp
        ├── receptionistHome.jsp
        ├── style.css
        └── WEB-INF/web.xml
```

## Seed accounts (username / password)

| Username | Password | Role |
|----------|----------|------|
| student | 123 | Student |
| employee | 123 | Employee |
| reception | 123 | Receptionist |

## Setup & run

1. **Create the database** (MySQL or MariaDB):

   ```bash
   mysql < database/schema.sql
   ```

   Then create a DB user matching the defaults used by the app (or override via
   env vars — see below):

   ```sql
   CREATE USER 'complaint'@'localhost' IDENTIFIED BY 'complaint123';
   GRANT ALL PRIVILEGES ON complaintdb.* TO 'complaint'@'localhost';
   ```

2. **Configure the connection (optional).** `DBConnection` defaults to
   `jdbc:mysql://localhost:3306/complaintdb` with user `complaint`. Override
   without changing code using environment variables:

   ```bash
   export DB_URL="jdbc:mysql://localhost:3306/complaintdb"
   export DB_USER="complaint"
   export DB_PASSWORD="complaint123"
   ```

3. **Build the WAR:**

   ```bash
   mvn clean package
   ```

4. **Deploy** `target/ComplaintSystem.war` to Tomcat 9 (Servlet 4 / `javax.servlet`)
   and open `http://localhost:8080/ComplaintSystem/login.jsp`.
