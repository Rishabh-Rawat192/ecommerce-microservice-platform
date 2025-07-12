# 📦 Product Service – E-Commerce Microservice

This service manages all product-related operations for the e-commerce platform, including product registration, retrieval, update, and deletion. It is designed as a Spring Boot microservice and integrates with authentication and seller verification mechanisms.

---

## 📂 Directory Structure

```
product-service/
├── src/
│   ├── main/java/com/ecommerce/product_service/
│   │   ├── controller/
│   │   ├── service/
│   │   ├── dto/
│   │   ├── entity/
│   │   ├── repository/
│   │   ├── config/
│   │   ├── exception/
│   └── resources/
│       ├── application.yml
│       └── ...
├── Dockerfile
├── pom.xml
├── README.md
└── ...
```

---

## 🔐 API Endpoints

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

---

## 🛠️ Setup & Development

### Prerequisites
- Java 17+
- Maven 3.8+
- Docker (optional)

### Build & Run

```bash
# Build the project
mvn clean install

# Run the service
mvn spring-boot:run
```

### Environment Variables
- Configure database, Kafka, and security settings in `src/main/resources/application.yml`.

---

## 🧪 Testing

```bash
mvn test
```

---

## 📘 Documentation & Design

- **API Models:** Product registration, update, and response use DTOs such as `ProductRegisterRequest`, `ProductUpdateRequest`, and `ProductResponse`. All API responses are wrapped in a standard `ApiResponse` object for consistency.
- **Error Handling:** Custom exceptions (e.g., `ApiException`) and a global exception handler ensure meaningful error messages and proper HTTP status codes for all API errors.
- **Security:** Endpoints for product creation, update, and deletion require authentication and the `SELLER` role, enforced via JWT and Spring Security configuration. Only the product owner (seller) can modify or delete their products.
- **Kafka Integration:** The service integrates with inventory and catalog services using Kafka. When a product is created, updated, or deleted, corresponding events (`ProductCreatedEvent`, `ProductUpdatedEvent`, `ProductDeletedEvent`) are published to Kafka topics. The service also consumes seller status updates from Kafka to ensure only active sellers can manage products. Kafka configuration is managed in `KafkaTopicProperties` and consumer logic in `SellerStatusConsumerService`.

---

## 🚀 Future Enhancements

- Product image upload support
- Advanced product search and filtering

**MIT License** | Built with ☕ and Spring Boot
