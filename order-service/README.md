# Order Service

The **Order Service** manages user orders, orchestrates the order lifecycle, and coordinates with cart, inventory, and catalog services. It uses Kafka for event-driven communication with other services.

## Features
- Create, confirm, and cancel user orders
- Retrieve all orders or a specific order for a user
- Integrates with Cart, Inventory, and Catalog services for order processing
- Publishes and consumes Kafka events for order state changes
- Scheduled order expiry and cleanup

## API Endpoints

All endpoints are prefixed with `/api/v1/orders`.

| Method | Endpoint                        | Description                        | Auth Required |
|--------|----------------------------------|------------------------------------|--------------|
| POST   | `/`                             | Create a new order draft           | Yes          |
| GET    | `/`                             | Retrieve all orders for user       | Yes          |
| GET    | `/{orderId}`                    | Retrieve details of an order       | Yes          |
| POST   | `/{orderId}/confirm`            | Confirm an order                   | Yes          |
| POST   | `/{orderId}/cancel`             | Cancel an order                    | Yes          |

**Internal Endpoints** (for service-to-service communication):
- (Add if present under `/internal`)

## Kafka Integration
- Produces and consumes Kafka events to synchronize order state with other microservices
- Produced topics: `order.order.creation-failed`, `order.order.confirmed`, `order.order.confirmation-failed`, `order.order.cancelled`, `order.order.expired`

## Modules and Structure
- **Controller Layer:** Exposes REST APIs for order management
- **Service Layer:** Business logic for order processing, event handling, and scheduling
- **DTOs:** Data transfer objects for API requests, responses, and Kafka events
- **Repository:** JPA repository for order persistence
- **Scheduler:** Handles order expiry and cleanup
- **Exception Handling:** Centralized error handling for API responses

## Configuration
- **Database:** PostgreSQL (configurable via `application.yml`)
- **Kafka:** Configured for event-driven communication (see `application.yml`)
- **Port:** Default 8086

## Running Locally
1. Ensure PostgreSQL and Kafka are running (see `docker-compose.yml` in the root project).
2. Build and run the service:
   ```bash
   ./mvnw spring-boot:run
   ```
3. The service will be available at `http://localhost:8086`.

## Environment Variables
- `ORDER_SERVICE_DATASOURCE_URL` (optional): Override the default DB URL.
- `ORDER_SERVICE_DB_USERNAME` (optional): DB username.
- `ORDER_SERVICE_DB_PASSWORD` (optional): DB password.

## Error Handling
All API responses are wrapped in a standard `ApiResponse` object. Validation and business errors are handled gracefully and return meaningful messages.

## Related Services
- **Cart Service:** Used to fetch and clear cart items during order processing
- **Inventory Service:** Used to reserve and confirm stock for orders
- **Catalog Service:** Used to fetch product and price details for order items

## Extending the Service
- To add new order features, update the `Order` and `OrderItem` entities and related DTOs
- To handle new Kafka events, add producers or consumers in the service layer

---

For more details, refer to the codebase and configuration files.

