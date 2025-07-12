# Catalog Service

The **Catalog Service** manages product catalog data, exposes product information to other services and clients, and integrates with inventory and other domain services using Kafka events.

## Features
- CRUD operations for catalog products
- Product filtering and pagination
- Price retrieval for multiple products (internal API)
- Product existence check (internal API)
- Integration with Inventory and Product services via Kafka

## API Endpoints

All endpoints are prefixed with `/api/v1/catalog`.

| Method | Endpoint                        | Description                                 | Auth Required |
|--------|----------------------------------|---------------------------------------------|--------------|
| GET    | `/products`                     | List products with filtering and pagination | No           |
| GET    | `/products/{productId}`         | Get details of a single product by ID       | No           |

**Internal Endpoints** (for service-to-service communication):
- `HEAD /internal/catalog/products/{productId}`: Check if a product exists
- `POST /internal/catalog/products/prices`: Get prices for a list of product IDs

## Kafka Integration
- Consumes product and inventory events to keep the catalog in sync
- Topics: `product.product.created`, `product.product.updated`, `product.product.deleted`, `inventory.stock.status-updated`

## Modules and Structure
- **Controller Layer:** Exposes REST APIs for public and internal use
- **Service Layer:** Business logic for product management and event handling
- **DTOs:** Data transfer objects for API requests, responses, and Kafka events
- **Repository:** JPA repository for catalog product persistence
- **Exception Handling:** Centralized error handling for API responses

## Configuration
- **Database:** PostgreSQL (configurable via `application.yml`)
- **Kafka:** Configured for event-driven communication (see `application.yml`)
- **Port:** Default 8083

## Running Locally
1. Ensure PostgreSQL and Kafka are running (see `docker-compose.yml` in the root project).
2. Build and run the service:
   ```bash
   ./mvnw spring-boot:run
   ```
3. The service will be available at `http://localhost:8083`.

## Environment Variables
- `CATALOG_SERVICE_DATASOURCE_URL` (optional): Override the default DB URL.

## Error Handling
All API responses are wrapped in a standard `ApiResponse` object. Validation and business errors are handled gracefully and return meaningful messages.

## Related Services
- **Inventory Service:** Stock status is synchronized via Kafka
- **Product Service:** Product lifecycle events are consumed for catalog updates
- **User and Seller Modules:** User and seller information may be referenced for product ownership and management

## Extending the Service
- To add new product attributes, update the `CatalogProduct` entity and related DTOs
- To handle new Kafka events, add consumers in the service layer

---

For more details, refer to the codebase and configuration files.

