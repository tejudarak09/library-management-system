# Library Management System

A Java-based library management system built with **Spring Boot**, **MySQL**, and a modern,
responsive **HTML/CSS/vanilla-JS** frontend (redesigned UI with dashboard stat cards,
sticky nav, and polished tables/forms). It manages books, members, and borrowing
records through RESTful APIs.

## Features

- **Books** — add, update, delete, list, and search by title or author
- **Members** — add, update, delete, list, and search by name
- **Borrowing** — borrow books (14-day loan), return books, view all records, filter overdue ones
- **Exception handling** — custom `ResourceNotFoundException` / `LibraryException` with a global handler returning clean JSON errors
- **OOP design** — entities encapsulate business rules (`borrowCopy()`, `returnCopy()`, `isOverdue()`, `markReturned()`)

## Prerequisites

| Software | Version | Download |
|----------|---------|----------|
| JDK      | 17+     | https://adoptium.net (Temurin 17) |
| Maven    | 3.8+    | https://maven.apache.org/download.cgi |
| MySQL    | 8.x     | https://dev.mysql.com/downloads/mysql/ |

Verify after installing:

```bash
java -version
mvn -version
mysql --version
```

## Setup

### 1. Create the database

```sql
CREATE DATABASE library_db;
```

### 2. Configure credentials

`src/main/resources/application.properties` reads the database credentials from
environment variables, defaulting to a local MySQL (`root` with an empty password).
If your MySQL `root` account has a password (most installs do), set it via env vars
before running — that way the file never needs editing:

```bash
# Windows (Command Prompt)
set SPRING_DATASOURCE_USERNAME=root
set SPRING_DATASOURCE_PASSWORD=your-mysql-password
mvn spring-boot:run
```

(Alternatively, edit `application.properties` and put your password after the colon
in `${SPRING_DATASOURCE_PASSWORD:...}`.) The database `library_db` is created
automatically on first run.

### 3. Run the app

From the project root:

```bash
mvn spring-boot:run
```

Hibernate will create the tables automatically (`spring.jpa.hibernate.ddl-auto=update`).

### 4. Open the UI

Go to **http://localhost:8080** in your browser — the home page shows library stats, and the nav links take you to Books, Members, and Borrow/Return pages.

## API Endpoints

### Books — `/api/books`
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET    | `/api/books` | List all books |
| GET    | `/api/books/{id}` | Get a book by id |
| GET    | `/api/books/search?title=...&author=...` | Search by title and/or author |
| POST   | `/api/books` | Add a book |
| PUT    | `/api/books/{id}` | Update a book |
| DELETE | `/api/books/{id}` | Delete a book |

### Members — `/api/members`
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET    | `/api/members` | List all members |
| GET    | `/api/members/{id}` | Get a member by id |
| GET    | `/api/members/search?name=...` | Search members by name |
| POST   | `/api/members` | Add a member |
| PUT    | `/api/members/{id}` | Update a member |
| DELETE | `/api/members/{id}` | Delete a member |

### Borrowing — `/api/borrow`
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET    | `/api/borrow` | List all borrow records |
| GET    | `/api/borrow/{id}` | Get a record by id |
| GET    | `/api/borrow/member/{memberId}` | Records for one member |
| GET    | `/api/borrow/overdue` | Overdue records |
| POST   | `/api/borrow` | Borrow a book — body: `{"bookId": 1, "memberId": 2}` |
| PUT    | `/api/borrow/{id}/return` | Return a borrowed book |

Example:

```bash
# Add a book
curl -X POST http://localhost:8080/api/books \
  -H "Content-Type: application/json" \
  -d '{"title":"Clean Code","author":"Robert C. Martin","isbn":"9780132350884","category":"Programming","totalCopies":3,"availableCopies":3}'

# Borrow it for member 1
curl -X POST http://localhost:8080/api/borrow \
  -H "Content-Type: application/json" \
  -d '{"bookId":1,"memberId":1}'
```

## Deployment

Don't want localhost-only? The project ships with a multi-stage `Dockerfile`,
a `.dockerignore`, and env-var-based DB config. Follow the click-by-click guide in
**[DEPLOY.md](DEPLOY.md)** to host it free on Render (Docker) with a free
TiDB Cloud / Aiven MySQL database.

## Project Structure

```
src/main/java/com/teju/library/
├── LibraryManagementSystemApplication.java
├── model/        # JPA entities: Book, Member, BorrowRecord
├── repository/   # Spring Data JPA repositories
├── service/      # Business logic (BookService, MemberService, BorrowService)
├── controller/   # REST controllers
└── exception/    # Custom exceptions + global handler
src/main/resources/
├── application.properties
└── static/       # HTML/CSS/JS frontend (index, books, members, borrow)
```

## Tech Stack

Java 17 · Spring Boot 3.2.5 · Spring Data JPA / Hibernate · MySQL 8 · HTML/CSS/vanilla JS
