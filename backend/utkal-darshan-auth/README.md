# Utkal Darshan Auth API (Spring Boot + MySQL)

This backend provides beginner-friendly authentication endpoints for your website:

- `POST /register`
- `POST /login`

Passwords are hashed using **BCrypt** (no plain-text passwords).

## 1) Prerequisites

- Java 17+
- Maven
- MySQL running locally

## 2) Create DB + table (optional)

Spring Boot can auto-create the table (`spring.jpa.hibernate.ddl-auto=update`).
If you want to create it yourself, run this SQL:

```sql
CREATE DATABASE IF NOT EXISTS utkal_darshan;
USE utkal_darshan;

CREATE TABLE IF NOT EXISTS users (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(120) NOT NULL,
  email VARCHAR(190) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
```

## 3) Configure MySQL credentials

Edit:

- `src/main/resources/application.properties`

Update:

- `spring.datasource.username`
- `spring.datasource.password`

## 4) Run the backend

From this folder:

```bash
mvn spring-boot:run
```

API base URL:

- `http://localhost:8080`

## 5) Test endpoints quickly

Register:

```bash
curl -X POST http://localhost:8080/register ^
  -H "Content-Type: application/json" ^
  -d "{\"name\":\"Suvam\",\"email\":\"suvam@example.com\",\"password\":\"secret123\"}"
```

Login:

```bash
curl -X POST http://localhost:8080/login ^
  -H "Content-Type: application/json" ^
  -d "{\"email\":\"suvam@example.com\",\"password\":\"secret123\"}"
```

## 6) Frontend integration

Your frontend should call `/register` or `/login` using `fetch()`.
On success:

- `localStorage.setItem("isLoggedIn","true")`
- (bonus) store the JWT token returned by the API:
  - `localStorage.setItem("token", data.token)`

