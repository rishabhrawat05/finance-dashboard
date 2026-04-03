# Finance Dashboard

A RESTful backend API for personal finance management, built with Java 21 and Spring Boot 4. The application provides secure endpoints for managing users, transactions, and dashboard analytics, protected by JWT-based authentication and role-based access control.

---

## Table of Contents

- [Tech Stack](#tech-stack)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
- [Configuration](#configuration)
- [Roles](#roles)
- [API Reference](#api-reference)
  - [Authentication API](#authentication-api)
  - [User API](#user-api)
  - [Transaction API](#transaction-api)
  - [Dashboard API](#dashboard-api)
- [API Documentation](#api-documentation)
- [Running Tests](#running-tests)
- [Project Structure](#project-structure)

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 4.0.5 |
| Security | Spring Security + JWT (JJWT 0.11.5) |
| Persistence | Spring Data JPA (Hibernate) |
| Database | PostgreSQL |
| Validation | Spring Boot Validation (Jakarta Bean Validation) |
| API Docs | SpringDoc OpenAPI 3.0.2 (Swagger UI) |
| Build Tool | Maven (Maven Wrapper included) |
| Utilities | Lombok |

---

## Prerequisites

- Java 21 or above
- Maven 3.9+ (or use the included `mvnw` wrapper)
- PostgreSQL 14 or above

---

## Getting Started

**1. Clone the repository**

```bash
git clone https://github.com/rishabhrawat05/finance-dashboard.git
cd finance-dashboard
```

**2. Create the database**

```sql
CREATE DATABASE finance_dashboard;
```

**3. Configure the application**

See the [Configuration](#configuration) section below.

**4. Build and run**

```bash
# Linux / macOS
./mvnw spring-boot:run

# Windows
mvnw.cmd spring-boot:run
```

The application starts on `http://localhost:8080` by default.

---

## Configuration

Update `src/main/resources/application.properties` with your environment values:

```properties
# Server
server.port=8080

# PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/finance_dashboard
spring.datasource.username=YOUR_DB_USERNAME
spring.datasource.password=YOUR_DB_PASSWORD

# JPA / Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false

# JWT
jwt.secret-key=YOUR_BASE64_ENCODED_SECRET_KEY
```

Do not commit credentials to version control. Use environment variables or a secrets manager in production.

---

## Roles

The application uses role-based access control. The three roles and their permissions are:

| Role | Description |
|---|---|
| `ADMIN` | Full access to all endpoints including user and transaction management |
| `ANALYST` | Can read all transactions and view dashboard summaries |
| `VIEWER` | Can view dashboard summaries only |

All protected endpoints require a valid JWT token in the request header:

```
Authorization: Bearer <token>
```

---

## API Reference

---

### Authentication API

Base path: `/api/auth`

These endpoints are public and do not require authentication.

---

#### POST /api/auth/register

Registers a new user account.

**Request body:**

```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "securePassword123"
}
```

**Responses:**

| Code | Description |
|---|---|
| `201 Created` | User registered successfully. Returns a confirmation message string. |
| `400 Bad Request` | Validation failed on the request body. |

**Screenshot:**
<img width="2679" height="1335" alt="Screenshot 2026-04-03 143053" src="https://github.com/user-attachments/assets/442e82bb-c26a-4832-97ae-ee234b1e6ba9" />

---

#### POST /api/auth/login

Authenticates a user and returns a JWT token.

**Request body:**

```json
{
  "email": "john@example.com",
  "password": "securePassword123"
}
```

**Screenshots:**
<img width="2673" height="1302" alt="Screenshot 2026-04-03 145639" src="https://github.com/user-attachments/assets/c9e7fee9-3529-4520-a361-5393172d98b5" />


**Responses:**

| Code | Description |
|---|---|
| `201 Created` | Login successful. Returns a `LoginResponse` containing the JWT token. |
| `400 Bad Request` | Invalid or malformed request body. |
| `401 Unauthorized` | Credentials are incorrect. |

---

### User API

Base path: `/api/user`

All endpoints require authentication and are restricted to the `ADMIN` role.

---

#### POST /api/user

Creates a new user.

**Required role:** `ADMIN`

**Request body:** `UserRequest` object containing user details.

**Responses:**

| Code | Description |
|---|---|
| `201 Created` | User created successfully. Returns the created `UserResponse`. |
| `400 Bad Request` | Invalid input. |
| `403 Forbidden` | Access denied. |

**Screenshots:**

Role - `ADMIN`
<img width="2022" height="1108" alt="Screenshot 2026-04-03 153338" src="https://github.com/user-attachments/assets/6d60509d-96e3-4a98-9911-41384291590d" />

Role - `VIEWER`
<img width="1994" height="1214" alt="Screenshot 2026-04-03 153449" src="https://github.com/user-attachments/assets/81cb212e-8ed8-4575-945c-7b9c8f45be4c" />

---

#### GET /api/user

Fetches a single user by their UUID.

**Required role:** `ADMIN`

**Query parameters:**

| Parameter | Type | Required | Description |
|---|---|---|---|
| `id` | UUID | Yes | The unique ID of the user to fetch. |

**Responses:**

| Code | Description |
|---|---|
| `200 OK` | Returns the `UserResponse` for the requested user. |
| `403 Forbidden` | Access denied. |
| `404 Not Found` | No user found with the given ID. |

**Screenshot:**
<img width="2008" height="1208" alt="Screenshot 2026-04-03 153655" src="https://github.com/user-attachments/assets/8b5554b9-3a72-4214-abcb-089c0bbc42ad" />

---

#### GET /api/user/all

Returns a paginated list of all users.

**Required role:** `ADMIN`

**Query parameters:**

| Parameter | Type | Required | Description |
|---|---|---|---|
| `page` | int | Yes | Zero-indexed page number. |
| `size` | int | Yes | Number of results per page. |

**Responses:**

| Code | Description |
|---|---|
| `200 OK` | Returns a paginated `Page<UserResponse>`. |
| `403 Forbidden` | Access denied. |

---

#### PUT /api/user

Updates an existing user by their UUID.

**Required role:** `ADMIN`

**Query parameters:**

| Parameter | Type | Required | Description |
|---|---|---|---|
| `id` | UUID | Yes | The unique ID of the user to update. |

**Request body:** `UserRequest` object with the updated fields.

**Responses:**

| Code | Description |
|---|---|
| `200 OK` | Returns the updated `UserResponse`. |
| `403 Forbidden` | Access denied. |
| `404 Not Found` | No user found with the given ID. |

---

#### DELETE /api/user/delete

Deletes a user by their UUID.

**Required role:** `ADMIN`

**Query parameters:**

| Parameter | Type | Required | Description |
|---|---|---|---|
| `id` | UUID | Yes | The unique ID of the user to delete. |

**Responses:**

| Code | Description |
|---|---|
| `204 No Content` | User deleted successfully. |
| `403 Forbidden` | Access denied. |
| `404 Not Found` | No user found with the given ID. |

---

### Transaction API

Base path: `/api/transaction`

All endpoints require authentication. Write operations and single-record reads are restricted to `ADMIN`. Listing all transactions is available to `ADMIN` and `ANALYST`.

---

#### POST /api/transaction

Creates a new transaction.

**Required role:** `ADMIN`

**Request body:** `TransactionRequest` object (validated).

**Responses:**

| Code | Description |
|---|---|
| `201 Created` | Transaction created successfully. Returns the `TransactionResponse`. |
| `400 Bad Request` | Validation failed on the request body. |
| `403 Forbidden` | Access denied. |

**Screenshots:**
<img width="2003" height="1197" alt="Screenshot 2026-04-03 153929" src="https://github.com/user-attachments/assets/7b68a977-af87-4738-b276-b18623cb3e9a" />
<img width="2018" height="1200" alt="Screenshot 2026-04-03 154031" src="https://github.com/user-attachments/assets/945ba927-fa46-4722-bdea-90b2e90d65b4" />
<img width="1997" height="1192" alt="Screenshot 2026-04-03 154102" src="https://github.com/user-attachments/assets/31cdf330-39b1-4070-9eed-3e86d807ede0" />

---

#### GET /api/transaction

Fetches a single transaction by its UUID.

**Required role:** `ADMIN`

**Query parameters:**

| Parameter | Type | Required | Description |
|---|---|---|---|
| `id` | UUID | Yes | The unique ID of the transaction to fetch. |

**Responses:**

| Code | Description |
|---|---|
| `200 OK` | Returns the `TransactionResponse` for the requested transaction. |
| `403 Forbidden` | Access denied. |
| `404 Not Found` | No transaction found with the given ID. |

---

#### GET /api/transaction/all

Returns a paginated list of all transactions.

**Required role:** `ADMIN` or `ANALYST`

**Query parameters:**

| Parameter | Type | Required | Description |
|---|---|---|---|
| `page` | int | Yes | Zero-indexed page number. |
| `size` | int | Yes | Number of results per page. |

**Responses:**

| Code | Description |
|---|---|
| `200 OK` | Returns a paginated `Page<TransactionResponse>`. |
| `403 Forbidden` | Access denied. |

**Screenshot:**
<img width="2018" height="1215" alt="Screenshot 2026-04-03 154301" src="https://github.com/user-attachments/assets/73c1e469-cfa3-4415-ae32-2b105a016b2a" />

---

#### PUT /api/transaction

Updates an existing transaction by its UUID.

**Required role:** `ADMIN`

**Query parameters:**

| Parameter | Type | Required | Description |
|---|---|---|---|
| `id` | UUID | Yes | The unique ID of the transaction to update. |

**Request body:** `TransactionRequest` object (validated).

**Responses:**

| Code | Description |
|---|---|
| `200 OK` | Returns the updated `TransactionResponse`. |
| `403 Forbidden` | Access denied. |
| `404 Not Found` | No transaction found with the given ID. |

---

#### DELETE /api/transaction

Deletes a transaction by its UUID.

**Required role:** `ADMIN`

**Query parameters:**

| Parameter | Type | Required | Description |
|---|---|---|---|
| `id` | UUID | Yes | The unique ID of the transaction to delete. |

**Responses:**

| Code | Description |
|---|---|
| `204 No Content` | Transaction deleted successfully. |
| `403 Forbidden` | Access denied. |
| `404 Not Found` | No transaction found with the given ID. |

---

### Dashboard API

Base path: `/api/dashboard`

All endpoints require authentication and are accessible to `VIEWER`, `ANALYST`, and `ADMIN` roles.

The supported category values are: `FOOD`, `RENT`, `TRANSPORT`, `ENTERTAINMENT`, `SALARY`. The `category` parameter is case-insensitive.

---

#### GET /api/dashboard/summary

Returns an aggregated summary of all financial data including total income, total expenses, net balance, and a category-wise breakdown.

**Required role:** `VIEWER`, `ANALYST`, or `ADMIN`

**Responses:**

| Code | Description |
|---|---|
| `200 OK` | Returns the `DashboardSummaryResponse`. |
| `403 Forbidden` | Access denied. |

**Screenshot:**
<img width="2020" height="1201" alt="Screenshot 2026-04-03 154425" src="https://github.com/user-attachments/assets/61c9ad71-8248-4c5e-bde6-5c81ed6532fe" />

---

#### GET /api/dashboard/category

Returns a dashboard summary filtered by a specific category.

**Required role:** `VIEWER`, `ANALYST`, or `ADMIN`

**Query parameters:**

| Parameter | Type | Required | Description |
|---|---|---|---|
| `category` | string | Yes | One of: `FOOD`, `RENT`, `TRANSPORT`, `ENTERTAINMENT`, `SALARY`. |

**Responses:**

| Code | Description |
|---|---|
| `200 OK` | Returns the `DashboardSummaryResponse` filtered to the given category. |
| `400 Bad Request` | The category value is invalid or unrecognized. |
| `403 Forbidden` | Access denied. |

---

#### GET /api/dashboard/filter

Returns a dashboard summary filtered by both a category and a date range.

**Required role:** `VIEWER`, `ANALYST`, or `ADMIN`

**Query parameters:**

| Parameter | Type | Required | Description |
|---|---|---|---|
| `category` | string | Yes | One of: `FOOD`, `RENT`, `TRANSPORT`, `ENTERTAINMENT`, `SALARY`. |
| `startDate` | date (`YYYY-MM-DD`) | Yes | Start of the date range. Must not be after `endDate`. |
| `endDate` | date (`YYYY-MM-DD`) | Yes | End of the date range. Must not be a future date. |

**Validation rules enforced server-side:**
- `startDate` and `endDate` must not be null.
- `startDate` must not be after `endDate`.
- `endDate` must not exceed the current date.

**Responses:**

| Code | Description |
|---|---|
| `200 OK` | Returns the filtered `DashboardSummaryResponse`. |
| `400 Bad Request` | Invalid date range or unrecognized category. |
| `403 Forbidden` | Access denied. |

---

## API Documentation

Once the application is running, the interactive Swagger UI is available at:

```
http://localhost:8080/swagger-ui/index.html
```

The raw OpenAPI specification is available at:

```
http://localhost:8080/v3/api-docs
```




