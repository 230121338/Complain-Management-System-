# Residence Complaint Management System

A role-based residence **issue / complaint tracker** for student housing.  
Students report problems by **block** and room; employees track and fix them; receptionists view progress.

Built with **Java Servlets, JSP, JDBC and MySQL/MariaDB** using an MVC-style structure (Model + DAO + Servlet + JSP).

## Why this project

Student residences often lose maintenance requests in chats and paper notes. This system gives a clear workflow:

```
Student submits  →  Not Fixed
Employee views   →  Viewed   (viewedDate = today)
Employee fixes   →  Fixed    (fixedDate  = today)
```

## Roles

| Role | Can do |
|------|--------|
| **Student** | Submit a complaint (text, **block**, room) and view own complaints + status |
| **Employee** | View all complaints, mark as *Viewed*, mark as *Fixed* |
| **Receptionist** | View all complaints (read-only), including status, viewed date and fixed date |

## Features

- Role-based login and page access
- Complaint submission with **block + room** location
- Status workflow: Not Fixed → Viewed → Fixed
- Employee action buttons (View / Fix)
- Receptionist read-only board
- MySQL schema + seed users + sample complaints
- Docker Compose (app + MySQL) for one-command local run
- Env-based DB config for cloud deploy (Railway / Render / Fly)

## Project structure

```
ComplaintSystem
├── pom.xml                         Maven war build
├── database/schema.sql             MySQL schema + seed users + sample data
├── docker-compose.yml              App + MySQL
├── Dockerfile
└── src/main
    ├── java/tut/ac/za/complaint
    │   ├── model      User, Complaint
    │   ├── dao        DBConnection, UserDAO, ComplaintDAO
    │   └── servlet    Login, Complaint, View, Fix, Logout
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

### 1. Create the database (MySQL or MariaDB)

```bash
mysql < database/schema.sql
```

Then create a DB user matching the app defaults (or override via env vars):

```sql
CREATE USER 'complaint'@'localhost' IDENTIFIED BY 'complaint123';
GRANT ALL PRIVILEGES ON complaintdb.* TO 'complaint'@'localhost';
```

### 2. Configure the connection (optional)

`DBConnection` defaults to `jdbc:mysql://localhost:3306/complaintdb` with user `complaint`. Override without changing code:

```bash
export DB_URL="jdbc:mysql://localhost:3306/complaintdb"
export DB_USER="complaint"
export DB_PASSWORD="complaint123"
```

### 3. Build the WAR

```bash
mvn clean package
```

### 4. Deploy

Deploy `target/ComplaintSystem.war` to Tomcat 9 (Servlet 4 / `javax.servlet`)  
and open `http://localhost:8080/ComplaintSystem/login.jsp`.

## Run with Docker (app + MySQL)

```bash
docker compose up --build
# open http://localhost:8080/  (served at the root context)
```

`docker compose down -v` stops everything and removes the database volume.

## Deploy to a cloud host

Any host that can run a Docker image works. The image reads its database connection from `DB_URL`, `DB_USER`, `DB_PASSWORD`, and listens on `$PORT` (falls back to `8080`).

**Railway** (easiest all-in-one)
1. New Project → *Deploy from GitHub repo* → pick this repo (builds the `Dockerfile`).
2. Add a **MySQL** plugin to the same project.
3. On the app service, set:
   `DB_URL=jdbc:mysql://<MYSQLHOST>:<MYSQLPORT>/<MYSQLDATABASE>?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC`,
   `DB_USER=<MYSQLUSER>`, `DB_PASSWORD=<MYSQLPASSWORD>` (use the plugin's values).
4. Load the schema once against the managed DB (`mysql ... < database/schema.sql`).

**Render**: New → *Web Service* → Docker; add a MySQL (Render add-on or free external DB such as Aiven), then set the same three env vars and load the schema.

**Fly.io / Koyeb**: `fly launch` / deploy the Docker image, attach a MySQL, set the same env vars.

## Tech stack

- Java 17
- Servlets / JSP (MVC pattern)
- JDBC + MySQL Connector
- Maven
- Docker / Docker Compose

## Future improvements

- Priority + category fields
- Staff assignment
- Search / filter by block, status, date
- Admin dashboard with stats
- Password hashing (BCrypt)
- Spring Boot rewrite of the same domain
