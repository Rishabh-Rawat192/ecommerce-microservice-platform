# Cart Service

The **Cart Service** manages user shopping carts, allowing users to add, update, and remove products before checkout. It integrates with the Catalog Service for product information and uses Kafka for order confirmation events.

## Features
- Add, update, and remove products in user carts
- Retrieve and clear user carts
- Integration with Catalog Service for product validation and details
- Consumes order confirmation events via Kafka to clear carts after successful orders

## API Endpoints

All endpoints are prefixed with `/api/v1/cart`.

| Method | Endpoint                        | Description                        | Auth Required |
|--------|----------------------------------|------------------------------------|--------------|
| GET    | `/`                             | Get the current user's cart        | Yes          |
| DELETE | `/`                             | Delete the current user's cart     | Yes          |
| POST   | `/items`                        | Add a product to the cart          | Yes          |
| GET    | `/items/{cartItemId}`           | Get details of a specific cart item| Yes          |
| PUT    | `/items/{cartItemId}`           | Update a cart item (e.g., quantity)| Yes          |
| DELETE | `/items/{cartItemId}`           | Remove a product from the cart     | Yes          |

## Kafka Integration
- Consumes `order.order.confirmed` events to clear the user's cart after a successful order.

## Modules and Structure
- **Controller Layer:** Exposes REST APIs for cart management.
- **Service Layer:** Business logic for cart operations and event handling.
- **DTOs:** Data transfer objects for API requests, responses, and Kafka events.
- **Repository:** JPA repository for cart persistence.
- **Caching:** Redis is used to cache cart data for performance.
- **Exception Handling:** Centralized error handling for API responses.

## Configuration
- **Database:** PostgreSQL (configurable via `application.yml`)
- **Kafka:** Configured for event-driven communication (see `application.yml`)
- **Redis:** Used for caching cart data
- **Port:** Default 8085

## Running Locally
1. Ensure PostgreSQL, Kafka, and Redis are running (see `docker-compose.yml` in the root project).
2. Build and run the service:
   ```bash
   ./mvnw spring-boot:run
   ```
3. The service will be available at `http://localhost:8085`.

## Environment Variables
- `CART_SERVICE_DATASOURCE_URL` (optional): Override the default DB URL.
- `CART_SERVICE_DB_USERNAME` (optional): DB username.
- `CART_SERVICE_DB_PASSWORD` (optional): DB password.

## Error Handling
All API responses are wrapped in a standard `ApiResponse` object. Validation and business errors are handled gracefully and return meaningful messages.

## Related Services
- **Catalog Service:** Used to validate and fetch product details for cart items.
- **Order Service:** Order confirmation events drive cart clearing.

## Extending the Service
- To add new cart features, update the `Cart` and `CartItem` entities and related DTOs.
- To handle new Kafka events, add consumers in the service layer.

---

For more details, refer to the codebase and configuration files.

