# Gateway Service

The **Gateway Service** is the API gateway for the E-Commerce microservices platform. It routes external client requests to the appropriate backend services, handles JWT-based authentication, and centralizes cross-cutting concerns such as logging and security.

## Features
- Centralized routing for all microservices (user, product, catalog, inventory, cart, order, etc.)
- JWT authentication and validation for protected routes
- Custom filter for extracting user information from JWT and forwarding it to backend services

## Routing

The gateway routes requests to the following services based on path:

- `/api/v1/auth/**`, `/api/v1/sellers/**`, `/api/v1/users/**` → user-service
- `/api/v1/products/**` → product-service
- `/api/v1/catalog/**` → catalog-service
- `/api/v1/inventory/**` → inventory-service
- `/api/v1/cart/**` → cart-service
- `/api/v1/orders/**` → order-service

## Authentication

- All routes except `/api/v1/auth/**` and `/api/v1/catalog/**` require a valid JWT in the `Authorization` header.
- The gateway validates the JWT and injects user information (user ID, email, roles) into request headers for downstream services.

## Configuration
- **Port:** 8081 (configurable via `application.yml`)
- **JWT Secret:** Configurable via environment variable `USER_SERVICE_JWT_SECRET`
- **Service Hostnames:** Configurable via environment variables (e.g., `USER_SERVICE_HOST`, `PRODUCT_SERVICE_HOST`, etc.)

## Running Locally
1. Ensure all backend services are running.
2. Start the gateway:
   ```bash
   ./mvnw spring-boot:run
   ```
3. The gateway will be available at `http://localhost:8081`.

## Environment Variables
- `USER_SERVICE_JWT_SECRET` (JWT secret for token validation)
- `USER_SERVICE_HOST`, `PRODUCT_SERVICE_HOST`, `CATALOG_SERVICE_HOST`, `INVENTORY_SERVICE_HOST`, `CART_SERVICE_HOST`, `ORDER_SERVICE_HOST` (service hostnames)

## Modules and Structure
- **Routing:** Configured in `application.yml` using Spring Cloud Gateway
- **JWT Validation:** Implemented in `JwtValidationFilter` and `JwtValidatorService`
- **DTOs:** For user information extracted from JWT

## Extending the Service
- To add new routes, update the `spring.cloud.gateway.routes` section in `application.yml`
- To add new authentication logic, extend the `JwtValidationFilter` or `JwtValidatorService`

---

For more details, refer to the codebase and configuration files.

