# Device Manager

**Device Manager** is a RESTful API service for managing a database of devices and their respective brands. It is built using Spring Boot and provides functionality for both users and administrators, with separate controllers and features for managing devices and brands.

## Table of Contents

- [Features](#features)
- [Technologies](#technologies)
- [Architecture](#architecture)
- [Installation](#installation)
- [Configuration](#configuration)
- [Usage](#usage)
    - [User Endpoints](#user-endpoints)
    - [Admin Endpoints](#admin-endpoints)
- [Testing](#testing)

## Features

- **Device and Brand Management**: Manage devices and brands with separate controllers for users and administrators.
- **Pagination**: Pagination support for retrieving large sets of data.
- **Docker Support**: Easily containerized using Docker and Docker Compose.
- **GitHub CI**: Continuous Integration is set up via GitHub Actions, automating tasks such as running tests, building the project, and ensuring code quality with each push request.
- **Swagger UI**: API documentation available via Swagger UI.

## Technologies

- **Java 17**
- **Spring Boot 3.x** (REST, Data JPA, Validation, Test)
- **MySQL and H2** (Databases)
- **Docker & Docker Compose** (Containerization)
- **Swagger** (API Documentation)
- **JUnit & Mockito** (Unit and Integration Testing)
- **Lombok** (Code Simplification)

## Architecture

The project follows a microservices-based architecture, separating concerns between user and administrator roles. It includes:

- **Controllers** for user-specific and admin-specific operations.
- **Services** for handling business logic.
- **Data Transfer Objects (DTOs)** to encapsulate data being transferred between layers.
- **Mappers** for converting between entities and DTOs.

## Installation

1. **Clone the repository**:
   ```bash
   git clone https://github.com/KossCode/device-manager.git
   ```
   
2. **Navigate into the project directory**:
   ```bash
   cd device-manager
   ```
   
3. **Run with Docker:** Ensure Docker is installed on your system.  
     Start the application using Docker Compose:
   ```bash
   docker-compose up
   ```

4. **Run locally without Docker:**

- **Install MySQL and set up a database.**
- **Configure database properties in `application-dev.properties`:**
  ```bash
    spring.datasource.url=jdbc:mysql://localhost:3306/device_manager_db
    spring.datasource.username=root
    spring.datasource.password=root
  ```
- **Build and run the project:**
    ```bash
    mvn spring-boot:run
    ```
## Configuration

The configuration for different environments is managed using Spring profiles. The active profile can be set in the `application.properties` file:

   ```bash
   spring.profiles.active=dev
   ```
   
To configure development settings, adjust `application-dev.properties`.
### Key Properties
- **Database URL:** Configure MySQL database connection details in `application-dev.properties`.
- **Swagger UI:** Accessible at `/swagger/v3/api-docs`.

## Usage
The API has separate endpoints for users and administrators. Here’s a breakdown of the key functionalities:

### User Endpoints

#### Brand Endpoints:
1. **GET** `/api/v1/user/brands`: Fetch all available brands.
2. **POST** `/api/v1/user/brands`: Add a new brand.
3. **PUT** `/api/v1/user/brands/{id}`: Update an existing brand.
4. **DELETE** `/api/v1/user/brands/{id}`: Delete a brand.

#### Device Endpoints:
1. **GET** `/api/v1/user/devices/{id}`: Fetch a specific device by its ID.
2. **GET** `/api/v1/user/devices/brands/{brand}`: Retrieve devices by brand name.
3. **GET** `/api/v1/user/devices`: Fetch a paginated list of devices.
4. **POST** `/api/v1/user/devices`: Add a new device.
5. **PUT** `/api/v1/user/devices/{id}`: Update an existing device.
6. **PATCH** `/api/v1/user/devices/{id}`: Patch a device (update only specific fields).
7. **DELETE** `/api/v1/user/devices/{id}`: Delete a device by its ID.

### Admin Endpoints
1. **GET** `/api/v1/admin/devices`: Retrieve all devices.
2. **POST** `/api/v1/admin/devices/bulk`: Create devices in bulk.
3. **DELETE** `/api/v1/admin/devices/bulk`: Delete all devices.

## Testing
Unit tests cover various layers of the application: services, controllers, and exceptions. Integration tests ensure that the application works as expected with external dependencies like the database.

To run the tests:

```bash
mvn test
```
Test groups:

- **Service Tests**: Testing business logic.
- **Controller Tests**: Testing request and response handling.
- **Exception Tests**: Verifying custom exceptions.
- **Integration Tests**: Testing end-to-end functionality, including database interaction.

### Example Test Case (Controller)
```
@Test
void testUpdateBrandSuccess() throws Exception {
when(brandService.updateBrand(Mockito.anyLong(), Mockito.any(BrandDTO.class))).thenReturn(brandDTO);

    String jsonContent = "{\"name\": \"UpdatedBrand\"}";

    mockMvc.perform(put("/api/v1/user/brands/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonContent))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success", is(true)))
            .andExpect(jsonPath("$.data.name", is("UpdatedBrand")));
}
```
