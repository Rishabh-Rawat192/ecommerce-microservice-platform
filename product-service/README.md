# Product Service

The **Product Service** manages all product-related operations for the e-commerce platform, including product registration, retrieval, update, and deletion. It is designed as a Spring Boot microservice and integrates with authentication and seller verification mechanisms.

## Features
- Register, update, and delete products
- Retrieve product details and seller's products
- Integrates with authentication and seller verification
- Publishes product events to Kafka for other services

## API Endpoints

All endpoints are prefixed with `/api/v1/products`.

| Method | Endpoint                | Description                                 | Auth Required |
|--------|-------------------------|---------------------------------------------|--------------|
| GET    | `/mine`                 | Get all products for authenticated seller   | SELLER        |
| POST   | `/`                     | Register a new product                      | SELLER        |
| GET    | `/{productId}`          | Get product by ID                           | No           |
| PATCH  | `/{productId}`          | Update product by ID                        | SELLER        |
| DELETE | `/{productId}`          | Delete product by ID                        | SELLER        |

- All seller endpoints require a valid JWT and SELLER role.
- Product registration and updates are only allowed for active sellers.

## Kafka Integration
- Publishes product events (created, updated, deleted) to Kafka for other services (e.g., catalog, inventory).

## Modules and Structure
- **Controller Layer:** Exposes REST APIs for product management.
- **Service Layer:** Business logic for product operations and event handling.
- **DTOs:** Data transfer objects for API requests, responses, and Kafka events.
- **Repository:** JPA repository for product persistence.
- **Exception Handling:** Centralized error handling for API responses.

## Configuration
- **Database:** PostgreSQL (configurable via `application.yml`)
- **Kafka:** Configured for event-driven communication (see `application.yml`)
- **Port:** Default 8082

## Running Locally
1. Ensure PostgreSQL and Kafka are running (see `docker-compose.yml` in the root project).
2. Build and run the service:
   ```bash
   ./mvnw spring-boot:run
   ```
3. The service will be available at `http://localhost:8082`.

## Environment Variables
- `PRODUCT_SERVICE_DATASOURCE_URL` (optional): Override the default DB URL.
- `PRODUCT_SERVICE_DB_USERNAME` (optional): DB username.
- `PRODUCT_SERVICE_DB_PASSWORD` (optional): DB password.

## Error Handling
All API responses are wrapped in a standard `ApiResponse` object. Validation and business errors are handled gracefully and return meaningful messages.

## Related Services
- **User Service:** For authentication and seller verification.
- **Catalog Service:** Consumes product events for catalog updates.
- **Inventory Service:** Consumes product events for inventory initialization.

## Extending the Service
- To add new product features, update the `Product` entity and related DTOs.
- To handle new Kafka events, add producers in the service layer.

---

For more details, refer to the codebase and configuration files.

**MIT License** | Built with ☕ and Spring Boot
