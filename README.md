# 🛒 E-commerce Microservices System

A backend microservice architecture for an e-commerce platform, built with Spring Boot. Each service is responsible for a specific domain and communicates over REST and/or messaging systems.

---

## 🚀 High-Level Overview

This system consists of multiple services, each responsible for a single business domain:

- **User Service**: Handles user registration, login, JWT-based authentication, and role-based authorization.
- **Gateway Service**: Acts as an API Gateway, routing requests to appropriate services and handling cross-cutting concerns like authentication and logging.
- **Product Service** *(TODO)*: Manages product catalogs and inventory.
- **Order Service** *(TODO)*: Handles cart management, order placement, and tracking.
- **Payment Service** *(optional, TODO)*: Processes payment-related logic.
- **Notification Service** *(optional, TODO)*: Sends emails or in-app notifications.

All services use **PostgreSQL**, and communication is enabled through **REST APIs** (later can support Kafka or RabbitMQ).

---

## 📦 Tech Stack

- **Language**: Java 17
- **Framework**: Spring Boot 3.x
- **Database**: PostgreSQL
- **Authentication**: JWT
- **Containerization**: Docker & Docker Compose

---

## 🛠️ Getting Started

### 1. Prerequisites

- Java 17
- Docker & Docker Compose
- Maven 3.9+

---

### 2. Configure Environment Variables

Create a `.env` file in the root directory of the project and add the following variables:

```env
USER_SERVICE_DB=<user_service_db>
USER_SERVICE_DB_USERNAME=<user_service_user>
USER_SERVICE_DB_PASSWORD=<user_service_password>
USER_SERVICE_DATASOURCE_URL=<jdbc url>
USER_SERVICE_JWT_SECRET=<secret>
USER_SERVICE_JWT_EXPIRATION=<Milliseconds>
USER_SERVICE_HOST=user-service
PRODUCT_SERVICE_HOST=product-service
```
### 3. Start Dependencies (DBs, etc.)

Make sure Docker is installed, then run:
```bash
docker-compose up -d
```
This command will start PostgreSQL and any shared services needed across microservices.


### 📄 Notes
Only the User Service is implemented. Other services are marked as TODO for future development.
Public endpoints (e.g., /login, /register) do not require authentication.
Protected endpoints require a valid JWT token in the Authorization header.
