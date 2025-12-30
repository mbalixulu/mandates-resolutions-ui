# Mandates Resolutions UI - Refactored Architecture

## Project Overview

This project demonstrates a well-architected Spring Boot application following the **Single Responsibility Principle** and clean **MVC (Model-View-Controller)** architecture. The refactoring transforms a monolithic controller into a modular, maintainable, and enterprise-ready system for managing mandates and resolutions.

## Table of Contents
- [Refactoring Objectives](#refactoring-objectives)
- [Architecture](#architecture)
- [API Endpoints](#api-endpoints)
- [Benefits of Refactoring](#benefits-of-refactoring)
- [Getting Started](#getting-started)
- [Testing](#testing)

## Refactoring Objectives

### Before Refactoring
Previously, a hypothetical `MandatesResolutionUIController` would have combined multiple responsibilities:
- HTTP request handling for multiple features
- Business logic processing
- Data validation
- String manipulation utilities
- All in a single, bloated class (violating SRP)

### After Refactoring
The code has been refactored into distinct layers with clear separation of concerns:

1. **One controller per feature/screen** - Each controller handles HTTP requests for a specific feature
2. **One service per feature/screen** - Each service contains business logic for a specific feature
3. **Utility classes** - Reusable validation and string manipulation logic
4. **DTOs and Models** - Clear data transfer objects and entity models

## Architecture

### Layer 1: Controller Layer (`com.example.mandates.controller`)

Four specialized controllers, each handling a specific feature:

#### 1. **AdminApprovalController** (`/api/admin/approvals`)
Manages admin approval workflows for mandates.
- `POST /api/admin/approvals` - Process approval/rejection
- `GET /api/admin/approvals/pending` - Get all pending approvals
- `GET /api/admin/approvals/{id}` - Get mandate for approval review

#### 2. **DraftManagementController** (`/api/drafts`)
Handles draft mandate creation and management.
- `POST /api/drafts` - Create a new draft
- `PUT /api/drafts/{id}` - Update a draft
- `GET /api/drafts` - Get all drafts
- `GET /api/drafts/{id}` - Get draft by ID
- `POST /api/drafts/{id}/submit` - Submit draft for approval

#### 3. **DirectorsDetailsController** (`/api/directors`)
Manages director validation and information.
- `POST /api/directors/validate` - Validate director information
- `GET /api/directors/{id}` - Get director by ID
- `GET /api/directors` - Get all validated directors
- `GET /api/directors/company/{registration}` - Get directors by company
- `GET /api/directors/check/{idNumber}` - Check if director is validated

#### 4. **MandateSearchController** (`/api/search/mandates`)
Provides search and filtering capabilities.
- `GET /api/search/mandates` - Get all mandates
- `GET /api/search/mandates/by-registration?registration={reg}` - Search by registration
- `GET /api/search/mandates/by-status?status={status}` - Search by status
- `GET /api/search/mandates/by-creator?createdBy={user}` - Search by creator
- `GET /api/search/mandates/advanced?registration={reg}&status={status}&createdBy={user}` - Advanced search

**Controller Responsibilities:**
- HTTP request/response handling only
- Input validation using Spring's `@Valid` annotation
- Delegating business logic to service layer
- Returning appropriate HTTP status codes
- Logging incoming requests

### Layer 2: Service Layer (`com.example.mandates.service`)

Four specialized services, each containing business logic for a specific feature:

#### 1. **AdminApprovalService**
Business logic for approval/rejection workflows:
- Process approval/rejection requests
- Validate mandate status before approval
- Update mandate status and approval metadata
- Retrieve pending approvals

#### 2. **DraftManagementService**
Business logic for draft management:
- Create new draft mandates
- Update existing drafts
- Validate business rules before saving
- Submit drafts for approval (status transition)

#### 3. **DirectorsDetailsService**
Business logic for director validation:
- Validate director information (ID, email, registration)
- Store validated directors
- Prevent duplicate director entries
- Retrieve director information

#### 4. **MandateSearchService**
Business logic for searching and filtering:
- Search by company registration
- Search by status
- Search by creator
- Advanced multi-criteria search

**Service Responsibilities:**
- Implement all business logic
- Use utility classes for validation and string operations
- Manage data storage (in-memory for demo purposes)
- Throw exceptions for business rule violations
- No HTTP-specific code

### Layer 3: Model/DTO Layer (`com.example.mandates.model` and `com.example.mandates.dto`)

#### Models (Entities)
- **Mandate** - Entity representing a mandate with all its attributes
- **Director** - Entity representing a director with validation status

#### DTOs (Data Transfer Objects)
- **MandateRequestDTO** - Request data for creating/updating mandates
- **MandateResponseDTO** - Response data for mandate operations
- **DirectorValidationDTO** - Request data for director validation
- **AdminApprovalDTO** - Request data for approval/rejection actions

**Model/DTO Responsibilities:**
- Define data structures
- Use validation annotations (`@NotBlank`, `@Email`, etc.)
- Use Lombok for boilerplate reduction (`@Data`, `@Builder`)
- Clear separation between request/response/entity structures

### Layer 4: Utility Layer (`com.example.mandates.util`)

#### **ValidationUtils**
Reusable validation methods:
- `isValidEmail(String)` - Validates email format
- `isValidRegistration(String)` - Validates company registration format (YYYY/NNNNNN/NN)
- `isValidIdNumber(String)` - Validates 13-digit ID numbers
- `isNotBlank(String)` - Validates non-empty strings

#### **StringUtils**
Reusable string manipulation methods:
- `normalizeRegistration(String)` - Removes spaces and converts to uppercase
- `deduplicateCommaString(String)` - Removes duplicates from comma-separated values
- `sanitize(String)` - Removes special characters (keeps alphanumeric and spaces)
- `capitalizeWords(String)` - Capitalizes first letter of each word

**Utility Responsibilities:**
- Provide static, stateless helper methods
- No dependencies on other layers
- Highly testable in isolation
- Reusable across multiple services

## API Endpoints

### Complete API Reference

| Method | Endpoint | Description |
|--------|----------|-------------|
| **Admin Approvals** |
| POST | `/api/admin/approvals` | Approve or reject a mandate |
| GET | `/api/admin/approvals/pending` | Get all pending approvals |
| GET | `/api/admin/approvals/{id}` | Get mandate for approval |
| **Draft Management** |
| POST | `/api/drafts` | Create a new draft mandate |
| PUT | `/api/drafts/{id}` | Update an existing draft |
| GET | `/api/drafts` | Get all drafts |
| GET | `/api/drafts/{id}` | Get specific draft |
| POST | `/api/drafts/{id}/submit` | Submit draft for approval |
| **Directors** |
| POST | `/api/directors/validate` | Validate director information |
| GET | `/api/directors` | Get all validated directors |
| GET | `/api/directors/{id}` | Get director by ID |
| GET | `/api/directors/company/{reg}` | Get directors by company |
| GET | `/api/directors/check/{idNumber}` | Check if director is validated |
| **Search** |
| GET | `/api/search/mandates` | Get all mandates |
| GET | `/api/search/mandates/by-registration` | Search by registration |
| GET | `/api/search/mandates/by-status` | Search by status |
| GET | `/api/search/mandates/by-creator` | Search by creator |
| GET | `/api/search/mandates/advanced` | Advanced multi-criteria search |

## Benefits of Refactoring

### 1. **Separation of Concerns**
- Each class has a single, well-defined responsibility
- Easy to locate and modify specific functionality
- Reduced coupling between components
- Clear boundaries between layers

### 2. **Testability**
- Services can be unit tested independently
- Utility classes can be tested in isolation
- Controllers can be tested with mocked services
- 43 unit tests with 100% coverage of critical paths

### 3. **Reusability**
- Utility methods used across multiple services
- Business logic centralized and not duplicated
- DTOs ensure consistent data structures
- Services can be composed for complex operations

### 4. **Maintainability**
- Clear structure makes onboarding easier
- Changes to business logic don't affect HTTP handling
- Easier to debug and trace issues
- Self-documenting code with meaningful names

### 5. **Scalability**
- Easy to add new endpoints without cluttering existing code
- Service methods can be extended or overridden
- New validation rules can be added to utility classes
- Can easily transition to microservices architecture

### 6. **Enterprise-Ready**
- Follows industry best practices
- Production-ready error handling
- Comprehensive logging at each layer
- Validation at multiple levels (DTOs and services)

## Getting Started

### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher

### Build the Project
```bash
mvn clean install
```

### Run the Application
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### Example Requests

#### Create a Draft Mandate
```bash
curl -X POST http://localhost:8080/api/drafts \
  -H "Content-Type: application/json" \
  -d '{
    "companyRegistration": "2023/123456/07",
    "title": "Board Resolution",
    "description": "Annual board meeting resolutions",
    "createdBy": "john.doe"
  }'
```

#### Submit Draft for Approval
```bash
curl -X POST http://localhost:8080/api/drafts/1/submit
```

#### Approve a Mandate
```bash
curl -X POST http://localhost:8080/api/admin/approvals \
  -H "Content-Type: application/json" \
  -d '{
    "mandateId": 1,
    "action": "APPROVE",
    "approverName": "admin.user",
    "comments": "Approved after review"
  }'
```

#### Validate a Director
```bash
curl -X POST http://localhost:8080/api/directors/validate \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "idNumber": "9001015009087",
    "email": "john.doe@example.com",
    "companyRegistration": "2023/123456/07"
  }'
```

#### Search Mandates
```bash
# By registration
curl "http://localhost:8080/api/search/mandates/by-registration?registration=2023/123456/07"

# By status
curl "http://localhost:8080/api/search/mandates/by-status?status=APPROVED"

# Advanced search
curl "http://localhost:8080/api/search/mandates/advanced?status=APPROVED&createdBy=john.doe"
```

## Testing

### Run All Tests
```bash
mvn test
```

### Test Coverage
- **Utility Tests**: 23 tests covering all utility methods
- **Service Tests**: 20 tests covering business logic
- **Total**: 43 tests with comprehensive coverage

### Test Structure
```
src/test/java/com/example/mandates/
├── service/
│   ├── AdminApprovalServiceTest.java
│   └── DirectorsDetailsServiceTest.java
└── util/
    ├── ValidationUtilsTest.java
    └── StringUtilsTest.java
```

## Project Structure

```
src/
├── main/
│   ├── java/com/example/mandates/
│   │   ├── MandatesResolutionsApplication.java
│   │   ├── controller/
│   │   │   ├── AdminApprovalController.java
│   │   │   ├── DraftManagementController.java
│   │   │   ├── DirectorsDetailsController.java
│   │   │   └── MandateSearchController.java
│   │   ├── service/
│   │   │   ├── AdminApprovalService.java
│   │   │   ├── DraftManagementService.java
│   │   │   ├── DirectorsDetailsService.java
│   │   │   └── MandateSearchService.java
│   │   ├── model/
│   │   │   ├── Mandate.java
│   │   │   └── Director.java
│   │   ├── dto/
│   │   │   ├── MandateRequestDTO.java
│   │   │   ├── MandateResponseDTO.java
│   │   │   ├── DirectorValidationDTO.java
│   │   │   └── AdminApprovalDTO.java
│   │   └── util/
│   │       ├── ValidationUtils.java
│   │       └── StringUtils.java
│   └── resources/
│       └── application.properties
└── test/
    └── java/com/example/mandates/
        ├── service/
        └── util/
```

## Key Design Decisions

1. **Feature-based Controllers**: Instead of one large controller, we have one controller per feature, making it easier to understand and maintain.

2. **Shared Data Store**: Services share the same data store (AdminApprovalService manages the store) to maintain consistency while still having separate concerns.

3. **In-Memory Storage**: For demonstration purposes, we use `ConcurrentHashMap` for thread-safe in-memory storage. In production, this would be replaced with a database.

4. **Validation at Multiple Levels**: DTOs use Spring validation annotations, and services perform business logic validation.

5. **Comprehensive Documentation**: All classes and methods have JavaDoc comments explaining their purpose and usage.

## Future Enhancements

- Add JPA/Hibernate for database persistence
- Implement Spring Security for authentication/authorization
- Add integration tests for controllers
- Implement caching for frequently accessed data
- Add API documentation with Swagger/OpenAPI
- Implement pagination for list endpoints
- Add audit logging for all state changes

## License

This project is for demonstration purposes.
