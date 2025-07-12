# 🧑‍💼 User Service – E-Commerce Microservice

This service handles all user-related operations including **authentication**, **user profile management**, and **seller profile management**. It is one of the core services of the `ecommerce-microservices` architecture.

---

## 📦 Modules

- **Auth Module**  
  Handles registration, login, JWT token generation, role-based access control.

- **User Profile Module**  
  Buyer profile management (name, contact info, address, etc.).

- **Seller Profile Module**  
  Seller-specific profile info such as business details and verification documents.

---

## 📂 Directory Structure

```
user-service/
├── src/
│   ├── main/java/com/ecommerce/user_service/
│   │   ├── auth/
│   │   ├── user/
│   │   ├── seller/
│   │   ├── common/
│   └── resources/
│       ├── application.yml
│       └── ...
├── Dockerfile
├── README.md
├── pom.xml
├── docs/
│   ├── auth_service_db_schema.md
│   ├── auth_service_design.md
│   └── ...

```

---

## 🔐 API Endpoints

### 🔸 Auth

| Method | Endpoint                 | Description                   |
|--------|--------------------------|-------------------------------|
| POST   | `/api/v1/auth/register`  | Register a new user or seller |
| POST   | `/api/v1/auth/login`     | Login with credentials        |
| GET    | `/api/v1/auth/protected` | Test endpoint for token       |

### 🔸 User Profile

| Method | Endpoint                     | Description                  |
|--------|------------------------------|------------------------------|
| POST   | `/api/v1/users`              | Register a user profile      |
| GET    | `/api/v1/users/{userId}`     | Get user profile by ID       |
| PATCH  | `/api/v1/users/{userId}`     | Update user profile by ID    |

### 🔸 Seller Profile

| Method | Endpoint                        | Description                |
|--------|----------------------------------|----------------------------|
| POST   | `/api/v1/sellers`               | Register seller profile    |
| GET    | `/api/v1/sellers/{userId}`      | Get seller profile by ID   |
| PATCH  | `/api/v1/sellers/{userId}`      | Update seller profile      |

> 📄 Detailed request/response schemas available in the `/docs` directory.

---

## 🛠️ Setup & Development

### Prerequisites
- Java 17+
- Maven 3.8+
- Docker (optional, for containerization)

### Build & Run

```bash
# Build the project
mvn clean install

# Run the service
mvn spring-boot:run
```

### Environment Variables
- Configure database and JWT secrets in `src/main/resources/application.yml`.

### Testing

```bash
mvn test
```

---

## 📘 Documentation

- **Auth Module Design:** Detailed explanation of authentication, registration, JWT token handling, and role-based access control is available in [Auth Module Design](docs/auth_service_design.md).
- **User & Seller Module Design:** The user and seller modules manage buyer and seller profiles, including registration, profile updates, and access control. Each module uses DTOs for request/response validation and enforces that only the profile owner can update their data. Business logic is separated into service classes for maintainability.
- **API Models:** All request and response payloads are defined as DTOs in the codebase, ensuring type safety and validation. API responses are consistently wrapped in a standard response object for clarity.
- **Error Handling:** The service uses custom exceptions and a global exception handler to provide clear, consistent error messages and appropriate HTTP status codes.
- **Security:** Endpoints for sensitive operations require authentication and proper roles (USER or SELLER), enforced via JWT and Spring Security. Only the resource owner can update their profile.
- **Kafka Events:** The user-service communicates with other microservices (such as product, inventory, or catalog services) using Kafka events. For example, when a seller profile is registered or updated, a corresponding event is published to a Kafka topic to notify other services of the change. This enables eventual consistency and decoupled communication between services. Kafka configuration (topics, producer/consumer settings) is managed in the codebase, and event payloads are defined as DTOs for type safety.

---

## 🚀 Future Enhancements

- 🔁 Refresh token support
- ✅ Seller KYC verification flow
- 📧 Email verification during registration

**MIT License** | Built with ☕ and Spring Boot
