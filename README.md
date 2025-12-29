# Mandates Resolutions UI

## Project Overview
This project demonstrates a refactored architecture following the separation of concerns principle and MVC pattern for a Mandates and Resolutions management system.

## Architecture

### Before Refactoring
Previously, the `MandatesResolutionUIController` combined multiple responsibilities:
- HTTP request handling
- Business logic processing
- Data validation
- String manipulation utilities
- All in a single class

### After Refactoring
The code has been refactored into distinct layers:

#### 1. Controller Layer (`com.example.mandates.controller`)
**MandatesResolutionController**
- Handles HTTP requests and responses
- Validates request data using Spring's validation framework
- Delegates business logic to the service layer
- Returns appropriate HTTP status codes
- No business logic implementation

#### 2. Service Layer (`com.example.mandates.service`)
**MandatesResolutionService**
- Contains all business logic
- Processes mandate requests with validation
- Handles director validation
- Manages mandate status updates
- Coordinates between controller and data storage
- Uses utility classes for common operations

#### 3. Model/DTO Layer (`com.example.mandates.model` and `com.example.mandates.dto`)

**Models:**
- `Mandate` - Entity representing a mandate
- `Director` - Entity representing a director

**DTOs:**
- `MandateRequestDTO` - Request data for creating mandates
- `MandateResponseDTO` - Response data for mandate operations
- `DirectorValidationDTO` - Request data for director validation

#### 4. Utility Layer (`com.example.mandates.util`)

**ValidationUtils**
- `isValidRegistration()` - Validates company registration numbers
- `isValidIdNumber()` - Validates ID numbers
- `isValidEmail()` - Validates email addresses
- Extracted from original controller's validation logic

**StringUtils**
- `normalizeRegistration()` - Normalizes registration numbers (extracted from `normReg`)
- `deduplicateCommaString()` - Removes duplicates from comma-separated strings (extracted from `dedupeComma`)
- `sanitize()` - Removes special characters
- `capitalizeWords()` - Capitalizes words in strings

## Benefits of Refactoring

### 1. Separation of Concerns
- Each class has a single, well-defined responsibility
- Easy to locate and modify specific functionality
- Reduced coupling between components

### 2. Testability
- Service layer can be unit tested independently
- Utility classes can be tested in isolation
- Controller can be tested with mocked services

### 3. Reusability
- Utility methods can be used across different services
- Business logic is centralized and not duplicated
- DTOs ensure consistent data structures

### 4. Maintainability
- Clear structure makes onboarding easier
- Changes to business logic don't affect HTTP handling
- Easier to debug and trace issues

### 5. Scalability
- Easy to add new endpoints without cluttering existing code
- Service methods can be extended or overridden
- New validation rules can be added to utility classes

## API Endpoints

### Mandates
- `POST /api/mandates` - Create a new mandate
- `GET /api/mandates` - Get all mandates
- `GET /api/mandates/{id}` - Get mandate by ID
- `PATCH /api/mandates/{id}/status` - Update mandate status
- `GET /api/mandates/search?registration={reg}` - Search by registration

### Directors
- `POST /api/mandates/directors/validate` - Validate a director
- `GET /api/mandates/directors` - Get all validated directors
- `GET /api/mandates/directors/{id}` - Get director by ID

## Building and Running

### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher

### Build
```bash
mvn clean install
```

### Run
```bash
mvn spring-boot:run
```

The application will start on port 8080.

## Testing

### Run Tests
```bash
mvn test
```

### Example Request
```bash
curl -X POST http://localhost:8080/api/mandates \
  -H "Content-Type: application/json" \
  -d '{
    "mandateNumber": "MAN-001",
    "companyRegistration": "2023/123456/07",
    "companyName": "Example Company",
    "directorIds": "1,2,3"
  }'
```

## Project Structure
```
src/main/java/com/example/mandates/
├── MandatesResolutionsApplication.java    # Main application class
├── controller/
│   └── MandatesResolutionController.java  # HTTP endpoint handling
├── service/
│   └── MandatesResolutionService.java     # Business logic
├── model/
│   ├── Mandate.java                        # Mandate entity
│   └── Director.java                       # Director entity
├── dto/
│   ├── MandateRequestDTO.java             # Request DTO
│   ├── MandateResponseDTO.java            # Response DTO
│   └── DirectorValidationDTO.java         # Validation DTO
└── util/
    ├── ValidationUtils.java                # Validation utilities
    └── StringUtils.java                    # String utilities
```

## Key Principles Applied

1. **Single Responsibility Principle (SRP)** - Each class has one reason to change
2. **Dependency Inversion Principle (DIP)** - Controller depends on service abstraction
3. **Don't Repeat Yourself (DRY)** - Common utilities extracted and reused
4. **Model-View-Controller (MVC)** - Clear separation of concerns
5. **Data Transfer Object (DTO)** - Explicit API contracts

## Future Enhancements

- Add database persistence layer
- Implement repository pattern
- Add comprehensive error handling
- Implement security with Spring Security
- Add API documentation with OpenAPI/Swagger
- Implement caching for frequently accessed data
- Add transaction management
