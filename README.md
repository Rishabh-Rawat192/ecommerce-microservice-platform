# 🛒 E-commerce Microservices System

A backend microservice architecture for an e-commerce platform, built with Spring Boot. Each service is responsible for a specific domain and communicates over REST and/or messaging systems.

---

## 🚀 High-Level Overview

This system consists of multiple services, each responsible for a single business domain:

- **User Service**: Handles user registration, login, JWT-based authentication, and role-based authorization.
- **Gateway Service**: Acts as an API Gateway, routing requests to appropriate services and handling cross-cutting concerns like authentication and logging.
- **Product Service**: Manages products for Seller endpoints, including CRUD operations and product management. Sends asynchronous events for catalog updates.
- **Catalog Service** : Manages product catalog for user-facing queries, including product search, filtering, and sorting. This is a **read-heavy** service and consumes product events.
- **Inventory Service** : Manages inventory levels, stock updates, and product availability. Supports stock reservation and consistency with order placement. This is a **write-heavy** service.
- **Cart Service** : Manages shopping cart operations, including adding/removing/updating items and calculating totals. Maintains a cart per user session.
- **Order Service** : Handles order placement, validation, order history, and order status updates. Coordinates with inventory and (optionally) payment services for atomic order creation.

All services use **PostgreSQL**, and communication is enabled through **Kafka** for asynchronous communication
between them but REST is used for synchronous communication wherever its required.

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

PRODUCT_SERVICE_DB=<product_service_db>
PRODUCT_SERVICE_DB_USERNAME=<product_service_user>
PRODUCT_SERVICE_DB_PASSWORD=<product_service_password>
PRODUCT_SERVICE_DATASOURCE_URL=<jdbc url>
PRODUCT_SERVICE_HOST=product-service

CATALOG_SERVICE_DB=<catalog_service_db>
CATALOG_SERVICE_DB_USERNAME=<catalog_service_user>
CATALOG_SERVICE_DB_PASSWORD=<catalog_service_password>
CATALOG_SERVICE_DATASOURCE_URL=<jdbc url>
CATALOG_SERVICE_HOST=catalog-service

INVENTORY_SERVICE_DB=<inventory_service_db>
INVENTORY_SERVICE_DB_USERNAME=<inventory_service_user>
INVENTORY_SERVICE_DB_PASSWORD=<inventory_service_password>
INVENTORY_SERVICE_DATASOURCE_URL=<jdbc url>
INVENTORY_SERVICE_HOST=inventory-service

CART_SERVICE_DB=<cart_service_db>
CART_SERVICE_DB_USERNAME=<cart_service_user>
CART_SERVICE_DB_PASSWORD=<cart_service_password>
CART_SERVICE_DATASOURCE_URL=<jdbc url>
CART_SERVICE_HOST=cart-service

ORDER_SERVICE_DB=<order_service_db>
ORDER_SERVICE_DB_USERNAME=<order_service_user>
ORDER_SERVICE_DB_PASSWORD=<order_service_password>
ORDER_SERVICE_DATASOURCE_URL=<jdbc url>
ORDER_SERVICE_HOST=order-service
```
### 3. Start Dependencies (DBs, etc.)

Make sure Docker is installed, then run:
```bash
docker-compose up -d
```
This command will start PostgreSQL and any shared services needed across microservices.


### 📄 Notes
Public endpoints (e.g., /api/v1/login, /api/v1/register) do not require authentication.
Protected endpoints require a valid JWT token in the Authorization header.
