# Complaint Management System

A role-based complaint management web application built with **Java Servlets, JSP,
JDBC and MySQL/MariaDB**, following the MVC pattern (Model + DAO + Servlet + JSP).

## Roles

Any visitor can **create an account** (student, employee or receptionist) from the
login page and then sign in. Students and employees can also upload a **profile
picture** shown in the navigation bar.

| Role | Can do |
|------|--------|
| **Student** | Submit a complaint (complaint text, block, room, optional picture) and view own complaints + status |
| **Employee** | View all complaints (with pictures), mark a complaint as *Viewed*, and mark it *Fixed* |
| **Receptionist** | View all complaints (read-only) with pictures, **search by student name**, and **download a PDF** of the complaints with pictures embedded |

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

## Run with Docker (app + MySQL)

The repo ships a `Dockerfile` (multi-stage build → Tomcat 9) and a
`docker-compose.yml` that also starts MySQL and auto-loads `database/schema.sql`.

```bash
docker compose up --build
# open http://localhost:8080/  (served at the root context)
```

`docker compose down -v` stops everything and removes the database volume.

## Deploy to a cloud host

Any host that can run a Docker image works. The image reads its database
connection from `DB_URL`, `DB_USER`, `DB_PASSWORD`, and listens on `$PORT`
(falls back to `8080`), so it fits Railway/Render/Fly.io out of the box.

**Railway** (easiest all-in-one)
1. New Project → *Deploy from GitHub repo* → pick this repo (it builds the `Dockerfile`).
2. Add a **MySQL** plugin to the same project.
3. On the app service, set variables:
   `DB_URL=jdbc:mysql://<MYSQLHOST>:<MYSQLPORT>/<MYSQLDATABASE>?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC`,
   `DB_USER=<MYSQLUSER>`, `DB_PASSWORD=<MYSQLPASSWORD>` (use the plugin's values).
4. Load the schema once against the managed DB (`mysql ... < database/schema.sql`).

**Render**: New → *Web Service* → Docker; add a MySQL (Render add-on or a free
external DB such as Aiven), then set the same three env vars and load the schema.

**Fly.io / Koyeb**: `fly launch` / deploy the Docker image, attach a MySQL, set
the same env vars.
