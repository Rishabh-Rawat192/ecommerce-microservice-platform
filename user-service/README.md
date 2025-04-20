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
│   ├── main/java/com/ecommerce/user/
│   │   ├── auth/
│   │   ├── userprofile/
│   │   ├── sellerprofile/
│   │   ├── common/
│   │   └── config/
│   └── resources/
│       ├── application.yml
│       └── ...
├── Dockerfile
├── README.md
└── pom.xml
```

---

## 🔐 API Endpoints

### 🔸 Auth

| Method | Endpoint              | Description                    |
|--------|-----------------------|--------------------------------|
| POST   | `/api/auth/register`  | Register as user or seller     |
| POST   | `/api/auth/login`     | Login with credentials         |
| GET    | `/api/auth/me`        | Get current logged-in user     |

### 🔸 User Profile

| Method | Endpoint               | Description         |
|--------|------------------------|---------------------|
| GET    | `/api/user/profile`    | Get user profile    |
| PUT    | `/api/user/profile`    | Update user profile |

### 🔸 Seller Profile

| Method | Endpoint                | Description           |
|--------|-------------------------|-----------------------|
| GET    | `/api/seller/profile`   | Get seller profile    |
| PUT    | `/api/seller/profile`   | Update seller profile |

> 📄 Detailed request/response schemas available in the `/docs` directory.

---

## 📘 Documentation

- [Auth Module Design](./auth_service_design.md)
- `user_profile_design.md`
- `seller_profile_design.md`

---

## 🚀 Future Enhancements

- 🔁 Refresh token support
- ✅ Seller KYC verification flow
- 📧 Email verification during registration


**MIT License** | Built with ☕ and Spring Boot
