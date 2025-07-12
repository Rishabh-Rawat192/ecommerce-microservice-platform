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

- [Auth Module Design](docs/auth_service_design.md)
- [Auth DB Schema](./docs/auth_service_db_schema.md)

---

## 🚀 Future Enhancements

- 🔁 Refresh token support
- ✅ Seller KYC verification flow
- 📧 Email verification during registration

**MIT License** | Built with ☕ and Spring Boot
