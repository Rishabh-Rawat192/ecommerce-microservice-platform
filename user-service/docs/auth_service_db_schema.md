# 🛡️ Auth Service – Database Schema

This document outlines the database schema for the `auth` module of the User Service in the E-commerce Microservices architecture. It manages user authentication, roles, and credentials securely.

---

## 📊 Tables Overview

### 1. `users`
Stores user credentials and login metadata.

| Column Name     | Type         | Constraints                     | Description                          |
|------------------|--------------|----------------------------------|--------------------------------------|
| `id`             | UUID         | Primary Key, Auto-generated      | Unique identifier for the user       |
| `email`          | VARCHAR(100) | Unique, Not Null                 | User's login email address           |
| `password`       | VARCHAR(255) | Not Null                         | Hashed password                      |
| `is_verified`    | BOOLEAN      | Default: false                   | Email verification flag              |
| `created_at`     | TIMESTAMP    | Default: now()                   | Record creation time                 |
| `updated_at`     | TIMESTAMP    | Auto-updated                     | Record update time                   |

---

### 2. `roles`
Stores system roles like USER, SELLER, ADMIN.

| Column Name     | Type         | Constraints                     | Description                          |
|------------------|--------------|----------------------------------|--------------------------------------|
| `id`             | SERIAL       | Primary Key                      | Unique identifier for the role       |
| `name`           | VARCHAR(50)  | Unique, Not Null                 | Role name (e.g., ROLE_USER)          |

---

### 3. `user_roles`
Join table to allow many-to-many mapping of users and roles.

| Column Name     | Type     | Constraints                             | Description                  |
|------------------|----------|------------------------------------------|------------------------------|
| `user_id`        | UUID     | FK → users(id), Composite Primary Key    | Linked user                  |
| `role_id`        | INT      | FK → roles(id), Composite Primary Key    | Assigned role                |

---

## 🔐 Notes

- **Password Storage**: Use `BCrypt` to hash passwords before storing.
- **Role Management**: Allows multiple roles per user.
- **Email Uniqueness**: Enforced at DB level to prevent duplicate registrations.
- **Indexing**: Add indexes on `email`, `role_id`, and `user_id` for fast lookups.

---

## 🛠️ Example SQL Snippets

```sql
CREATE TABLE users (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  email VARCHAR(100) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  is_verified BOOLEAN DEFAULT false,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE roles (
  id SERIAL PRIMARY KEY,
  name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE user_roles (
  user_id UUID REFERENCES users(id),
  role_id INT REFERENCES roles(id),
  PRIMARY KEY (user_id, role_id)
);