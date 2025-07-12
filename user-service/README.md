# User Service

The **User Service** handles all user-related operations including authentication, user profile management, and seller profile management. It is a core service in the e-commerce microservices architecture.

## Features
- User registration and authentication (JWT-based)
- Buyer and seller profile management
- Role-based access control
- Seller verification and business details

## API Endpoints

All endpoints are prefixed with `/api/v1`.

### Auth
| Method | Endpoint                 | Description                   |
|--------|--------------------------|-------------------------------|
| POST   | `/auth/register`         | Register a new user           |
| POST   | `/auth/login`            | User login and JWT issuance   |

### User
| Method | Endpoint                 | Description                   |
|--------|--------------------------|-------------------------------|
| GET    | `/users/me`              | Get current user profile      |
| PUT    | `/users/me`              | Update user profile           |

### Seller
| Method | Endpoint                 | Description                   |
|--------|--------------------------|-------------------------------|
| GET    | `/sellers/me`            | Get current seller profile    |
| PUT    | `/sellers/me`            | Update seller profile         |

- All endpoints (except registration/login) require a valid JWT.

## Modules and Structure
- **Auth Module:** Registration, login, JWT, and role management
- **User Profile Module:** Buyer profile management
- **Seller Profile Module:** Seller business and verification
- **DTOs:** Data transfer objects for API requests and responses
- **Repository:** JPA repository for user and seller persistence
- **Exception Handling:** Centralized error handling for API responses

## Configuration
- **Database:** PostgreSQL (configurable via `application.yml`)
- **Port:** Default 8080

## Running Locally
1. Ensure PostgreSQL is running (see `docker-compose.yml` in the root project).
2. Build and run the service:
   ```bash
   ./mvnw spring-boot:run
   ```
3. The service will be available at `http://localhost:8080`.

## Environment Variables
- `USER_SERVICE_DATASOURCE_URL` (optional): Override the default DB URL.
- `USER_SERVICE_DB_USERNAME` (optional): DB username.
- `USER_SERVICE_DB_PASSWORD` (optional): DB password.
- `USER_SERVICE_JWT_SECRET` (JWT secret for token generation/validation)

## Error Handling
All API responses are wrapped in a standard `ApiResponse` object. Validation and business errors are handled gracefully and return meaningful messages.

## Related Services
- **Product Service:** For seller product management
- **Order Service:** For user order management
- **Gateway Service:** For authentication and routing

## Extending the Service
- To add new user or seller features, update the relevant entities and DTOs.
- To add new authentication logic, update the Auth module.

---

For more details, refer to the codebase and configuration files.
