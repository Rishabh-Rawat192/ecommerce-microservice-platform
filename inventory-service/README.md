# Inventory Service

The **Inventory Service** manages product stock, handles stock reservations for orders, and synchronizes inventory data with other services using Kafka events.

## Features
- Restock and deduct product inventory
- Stock reservation and confirmation for orders
- Internal APIs for other services to query and reserve stock
- Integration with Product, Order, and Catalog services via Kafka

## API Endpoints

All endpoints are prefixed with `/api/v1/inventory`.

| Method | Endpoint                        | Description                        | Auth Required |
|--------|----------------------------------|------------------------------------|--------------|
| POST   | `/{productId}/restock`          | Restock a product                  | Yes          |
| POST   | `/{productId}/deduct`           | Deduct stock for a product         | Yes          |

**Internal Endpoints** (for service-to-service communication):
- `GET /internal/v1/inventory/{productId}`: Get current stock for a product
- `POST /internal/v1/inventory/reservations`: Reserve stock for an order
- `POST /internal/v1/inventory/reservations/{orderId}/confirm`: Confirm a stock reservation for an order

## Kafka Integration
- Consumes and produces Kafka events to synchronize inventory with other microservices
- Consumed topics: `product.product.created`, `order.order.creation-failed`, `order.order.confirmation-failed`, `order.order.cancelled`, `order.order.expired`
- Produced topic: `inventory.stock.status-updated`

## Modules and Structure
- **Controller Layer:** Exposes REST APIs for public and internal use
- **Service Layer:** Business logic for inventory management and event handling
- **DTOs:** Data transfer objects for API requests, responses, and Kafka events
- **Repository:** JPA repository for inventory persistence
- **Exception Handling:** Centralized error handling for API responses

## Configuration
- **Database:** PostgreSQL (configurable via `application.yml`)
- **Kafka:** Configured for event-driven communication (see `application.yml`)
- **Port:** Default 8084

## Running Locally
1. Ensure PostgreSQL and Kafka are running (see `docker-compose.yml` in the root project).
2. Build and run the service:
   ```bash
   ./mvnw spring-boot:run
   ```
3. The service will be available at `http://localhost:8084`.

## Environment Variables
- `INVENTORY_SERVICE_DATASOURCE_URL` (optional): Override the default DB URL.
- `INVENTORY_SERVICE_DB_USERNAME` (optional): DB username.
- `INVENTORY_SERVICE_DB_PASSWORD` (optional): DB password.

## Error Handling
All API responses are wrapped in a standard `ApiResponse` object. Validation and business errors are handled gracefully and return meaningful messages.

## Related Services
- **Product Service:** New products trigger inventory initialization
- **Order Service:** Order status changes drive stock reservation and release
- **Catalog Service:** Receives stock status updates via Kafka

## Extending the Service
- To add new inventory features, update the `Inventory` entity and related DTOs
- To handle new Kafka events, add consumers in the service layer

---

For more details, refer to the codebase and configuration files.

