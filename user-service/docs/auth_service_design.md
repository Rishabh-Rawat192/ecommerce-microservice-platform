# 🔐 Auth Service Design

The **Auth Service** module is responsible for handling user authentication, registration, and authorization. This service uses JWT tokens for session management and includes features like role-based access control (RBAC).

---

## 🔑 Core Components

### 1. **User Registration**
- **POST /api/auth/register**  
  Allows users to register as either buyers or sellers. During registration:
    - User details (name, email, password, etc.) are validated.
    - Passwords are hashed before storage using `BCrypt` (or a similar algorithm).
    - A confirmation email could be sent to verify email addresses.

### 2. **User Login**
- **POST /api/auth/login**  
  Allows users to authenticate using their credentials:
    - If credentials are valid, a JWT token is generated and sent in the response.
    - The token is signed using a secret key and includes user details and roles.

### 3. **JWT Token Generation**
- The token contains:
    - `user_id`: The ID of the authenticated user.
    - `roles`: A list of roles assigned to the user (e.g., "USER", "SELLER").
    - `exp`: Expiration timestamp of the token.

### 4. **User Authentication**
- **GET /api/auth/me**  
  This endpoint returns the authenticated user's details:
    - The JWT token is sent in the `Authorization` header.
    - The token is verified, and user data is returned in response if the token is valid.

---

## 🔒 Security Mechanisms

1. **Password Hashing**:  
   All passwords are hashed using a secure hashing algorithm (e.g., `BCrypt`) before storing them in the database.

2. **JWT Token**:  
   JWT is used for authentication and authorization:
    - The token is signed using a secret key, making it tamper-proof.
    - It is passed in the `Authorization` header as a bearer token.

3. **Role-Based Access Control (RBAC)**:  
   Users can have one or more roles (e.g., "USER", "SELLER"). These roles are embedded in the JWT and used for access control at various endpoints.

4. **Session Expiration**:  
   Tokens are short-lived, typically with an expiration time (e.g., 1 hour). After expiration, the user must log in again to obtain a new token.

---

## 📝 Database Design

- **User Table**  
  The `user` table will store the user details like:
    - `id`, `email`, `password_hash`, `first_name`, `last_name`, `roles`

- **Role Table**  
  The `role` table will store roles such as:
    - `id`, `role_name` (e.g., "USER", "SELLER")

- **Role-User Relationship**  
  A many-to-many relationship between `users` and `roles`.

---

## 🚀 API Design

- **POST /api/auth/register**  
  **Request:**
  ```json
  {
    "email": "user@example.com",
    "password": "password123",
    "first_name": "John",
    "last_name": "Doe",
    "role": "USER"
  }
  ```
  **Response:**
  ```json
  {
    "message": "Registration successful"
  }
  ```

- **POST /api/auth/login**  
  **Request:**
  ```json
  {
    "email": "user@example.com",
    "password": "password123"
  }
  ```
  **Response:**
  ```json
  {
    "token": "JWT_TOKEN_HERE"
  }
  ```

- **GET /api/auth/me**  
  **Response:**
  ```json
  {
    "user_id": 1,
    "first_name": "John",
    "last_name": "Doe",
    "roles": ["USER"]
  }
  ```

---

## 🚀 Future Improvements

- **Email Verification**: Implement email verification during registration.
- **Password Reset**: Add a password reset functionality via email.
- **Refresh Token**: Implement a refresh token mechanism for longer sessions.

---

**MIT License** | Built with ☕ and Spring Boot