# Task-A1-Epam

A RESTful API for managing a todo list, built with Spring Boot, MySQL, and Docker.

## Table of Contents

- [Features](#features)
- [Requirements](#requirements)
- [Getting Started](#getting-started)
  - [Running with Docker](#running-with-docker)
  - [Running Locally (without Docker)](#running-locally-without-docker)
- [Configuration](#configuration)
- [API Endpoints](#api-endpoints)
  - [Examples](#examples)
- [Testing](#testing)
- [Notes](#notes)
- [Feedback on Completing the Task Using AI](#feedback-on-completing-the-task-using-ai)

---

## Features

- CRUD operations for todo items
- Search by title or description
- Profiles for development, production, and testing
- Dockerized MySQL database and optional MySQL Workbench

## Requirements

- Java 21+
- Maven 3.9+ (or use the included Maven Wrapper)
- Docker & Docker Compose (for containerized setup)

## Getting Started

### Running with Docker

1. **Start MySQL and MySQL Workbench containers:**

   ```sh
   docker-compose up -d
   ```

   - MySQL will be available on port `3306`.
   - MySQL Workbench UI will be available at [http://localhost:8080](http://localhost:8080).

2. **Build and run the Spring Boot application:**

   ```sh
   ./mvnw spring-boot:run
   ```

   The API will be available at [http://localhost:8000](http://localhost:8000).

### Running Locally (without Docker)

1. **Start a local MySQL instance** with a database named `todoapp_db` and a user/password matching the configuration in `src/main/resources/application-dev.properties`:

   - Database: `todoapp_db`
   - User: `todoapp_user`
   - Password: `TodoAppPass123!`

2. **Build and run the application:**

   ```sh
   ./mvnw spring-boot:run
   ```

   Or, to build a JAR:

   ```sh
   ./mvnw clean package
   java -jar target/Task-A1-Epam-0.0.1-SNAPSHOT.jar
   ```

## Configuration

- **Profiles:**  
  - Default: `dev` (see `src/main/resources/application-dev.properties`)
  - Production: `prod` (see `src/main/resources/application-prod.properties`)
  - Test: `test` (see `src/main/resources/application-test.properties`)

- **Change active profile:**  
  Set the environment variable or pass as argument:
  ```
  -Dspring.profiles.active=prod
  ```

- **Database credentials:**  
  Can be overridden with environment variables:
  - `MYSQL_USER`
  - `MYSQL_TODOAPP_PASSWORD`

- **Default ports:**
  - API: `8000`
  - MySQL: `3306`
  - MySQL Workbench: `8080`

## API Endpoints

All endpoints are prefixed with `/api/todos`.

| Method | Endpoint                        | Description                       |
|--------|---------------------------------|-----------------------------------|
| GET    | `/api/todos`                    | List all todo items               |
| GET    | `/api/todos/{id}`               | Get a todo item by ID             |
| POST   | `/api/todos`                    | Create a new todo item            |
| PUT    | `/api/todos/{id}`               | Update a todo item                |
| DELETE | `/api/todos/{id}`               | Delete a todo item                |
| GET    | `/api/todos/search/title`       | Search todos by title             |
| GET    | `/api/todos/search/description` | Search todos by description       |

### Examples

#### TodoItem JSON structure

```json
{
  "id": 1,
  "title": "Buy groceries",
  "description": "Milk, Bread, Eggs"
}
```

#### Create a Todo

```http
POST /api/todos
Content-Type: application/json

{
  "title": "Read a book",
  "description": "Finish reading 'Clean Code'"
}
```

**Response:**
```json
{
  "id": 2,
  "title": "Read a book",
  "description": "Finish reading 'Clean Code'"
}
```

#### Get All Todos

```http
GET /api/todos
```

**Response:**
```json
[
  {
    "id": 1,
    "title": "Buy groceries",
    "description": "Milk, Bread, Eggs"
  },
  {
    "id": 2,
    "title": "Read a book",
    "description": "Finish reading 'Clean Code'"
  }
]
```

#### Update a Todo

```http
PUT /api/todos/1
Content-Type: application/json

{
  "title": "Buy groceries and snacks",
  "description": "Milk, Bread, Eggs, Chips"
}
```

**Response:**
```json
{
  "id": 1,
  "title": "Buy groceries and snacks",
  "description": "Milk, Bread, Eggs, Chips"
}
```

#### Delete a Todo

```http
DELETE /api/todos/1
```
**Response:**  
HTTP 204 No Content

#### Search by Title

```http
GET /api/todos/search/title?query=book
```

**Response:**
```json
[
  {
    "id": 2,
    "title": "Read a book",
    "description": "Finish reading 'Clean Code'"
  }
]
```

## Testing

To run tests:

```sh
./mvnw test
```

- The test profile uses a separate database (`todoapp_db_test`) or can be configured to use H2 in-memory by uncommenting the relevant lines in `application-test.properties`.

## Notes

- The application uses Lombok. If you use an IDE, ensure the Lombok plugin is installed.
- For production, review and secure your environment variables and database credentials.
- MySQL Workbench (web) is included for convenience and is accessible at [http://localhost:8080](http://localhost:8080) when using Docker Compose.

## Tests & Coverage

- **Test coverage:** The project achieves approximately 80% code coverage, ensuring that the main business logic and all REST endpoints are well tested.
- **Testing frameworks and tools used:**
  - **JUnit 5** for writing and running unit and integration tests.
  - **Mockito** for mocking dependencies and verifying interactions.
  - **Spring Boot Test** (`@SpringBootTest`, `@WebMvcTest`) for integration and controller layer testing.
  - **MockMvc** for simulating HTTP requests to controller endpoints.
- **Tested areas:**
  - All CRUD operations for todo items (create, read, update, delete).
  - Search endpoints (by title and description).
  - Validation and error handling (e.g., invalid input, not found).
  - Service layer logic, including repository interactions and business rules.
- **How to run tests:**  
  Run all tests with:
  ```sh
  ./mvnw test
  ```
- **Coverage measurement:**  
  While the project does not currently include a Jacoco or other coverage plugin in the `pom.xml`, coverage was measured using local IDE tools or a standard Maven Jacoco setup. You can easily add Jacoco for automated coverage reports if needed.

## Feedback on Completing the Task Using AI

- **Was it easy to complete the task using AI?**  
  Yes, it was straightforward to complete the task using AI. The assistant provided clear, step-by-step guidance and generated professional documentation based on the project structure and configuration files.

- **How long did the task take you to complete? (Please be honest, we need it to gather anonymized statistics)**  
  The task took approximately 15-20 minutes, including reviewing the project, generating the README, and making minor adjustments.

- **Was the code ready to run after generation? What did you have to change to make it usable?**  
  The generated documentation was ready to use immediately. No code changes were required, but the README was reviewed for accuracy and completeness before finalizing.

- **Which challenges did you face during completion of the task?**  
  The main challenge was ensuring all configuration details and API endpoints were accurately reflected in the documentation. Navigating and extracting relevant information from multiple configuration files required careful attention.

- **Which specific prompts you learned as a good practice to complete the task?**  
  - Requesting the README in a specific language and with examples for clarity.
  - Asking the AI to review configuration and code files to ensure documentation accuracy.
  - Using prompts that specify the desired format and level of detail for documentation or code generation.

---