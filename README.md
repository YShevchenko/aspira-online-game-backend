# Imaginary Online Game Service

A RESTful web service for managing character experience and levels in an online game.

## Configuration

The level configuration is loaded from `src/main/resources/levels-config.yaml`.

### Format
The levels configuration defines level ranges and the experience required to advance to the next level.
- **range**: A string defining the level range (e.g., `"1-9"`).
- **experience**: The amount of experience required to level up from any level in this range.

**Important Rules:**
1.  **Start Level**: Must start from level 1.
2.  **Positive Experience**: Experience must be greater than 0 for all levels except the last one.
3.  **Last Level**: The max level is defined with an open-ended range (e.g., `"100-"`).

### Example
```
levels:
  - range: "1-9"
    experience: 100
  - range: "10-99"
    experience: 200
  - range: "100-"
    experience: 0
```

## API Documentation (Swagger)

Once the application is running, you can access the interactive API documentation at:

`http://localhost:8088/swagger-ui/index.html`

## Running with Docker

### Prerequisites
- Docker
- Docker Compose

### Build and Run
To build the image and start the service:

```bash
docker-compose up --build
```

The service will be available at `http://localhost:8088`.

## Development

### Code Formatting
This project uses **Spotless** with Google Java Format.

To check formatting:
```bash
./gradlew spotlessCheck
```

To apply formatting automatically:
```bash
./gradlew spotlessApply
```

### Testing & Coverage
This project uses **JaCoCo** for code coverage.

To run tests and generate the coverage report:
```bash
./gradlew test jacocoTestReport
```

**Requirement:** Minimum **80%** code coverage is enforced. The build will fail if coverage drops below this threshold.
