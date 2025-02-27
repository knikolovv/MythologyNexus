<div align="center">
  <h1>MythologyNexus</h1>
  <p>A back-end platform for managing and exploring mythological data via a RESTful API</p>
  <img src="https://img.shields.io/badge/Java-100%25-orange" alt="Java">
  <img src="https://img.shields.io/badge/Spring%20Boot-Yes-brightgreen" alt="Spring Boot">
</div>

---

## Table of Contents

- **Features**
- **Getting Started**
  - **Prerequisites**
  - **Installation**
- **API Endpoints**
- **Testing**
- **License**

---

## Features

- **RESTful API:** Manage mythology data with well-defined endpoints.
- **Java Spring Boot:** Built using Spring Boot for rapid development.
- **Hibernate ORM:** Seamless database integration using Hibernate.
- **JUnit Integration:** Robust testing to ensure code quality.
- **Jacoco Test Coverage:** Automatically generates detailed reports ensuring thorough testing.

---

## Getting Started

### Prerequisites

- **Java JDK 8+**  
  [Download Java](https://www.oracle.com/java/technologies/javase-downloads.html)
- **Maven** for dependency management  
  [Download Maven](https://maven.apache.org/download.cgi)
- **Database:** MySQL, PostgreSQL, or another relational database (configure in `src/main/resources/application.properties`)

### Installation

1. **Clone the repository:**

   ```bash
   git clone https://github.com/knikolovv/MythologyNexus.git
   cd MythologyNexus
   ```

2. **Configure your database settings:**

Edit the configuration file (e.g., application.properties) with your database URL, username, and password.

3. **Build the project:**

   ```bash
   mvn clean install
   ```

4. **Run the application:**

   ```bash
   mvn spring-boot:run
   ```

The service will be available at http://localhost:8080.

---

## API Endpoints
Interact with the mythology data through these sample endpoints for mythology entities:

- GET /mythologies – Retrieve all mythology entries.
- GET /mythologies/{id} – Retrieve a specific mythology entry.
- POST /mythologies – Create a new mythology entry.
- PATCH /mythologies/{id} – Update an existing entry.
- DELETE /mythologies/{id} – Remove an entry.

For mythology characters entities:

- GET /characters – Retrieve all character entries.
- GET /characters/{name} – Retrieve a specific character entry
- GET /characters/filter -Retrieve all characters from specific character type using request paramater.
- POST /characters – Create a new character entry.
- PATCH /characters/{id} - Update an existing entry.
- PATCH /characters/{characterName}/associate-with/{associateCharacterName} - Associate an entry with another one of the same type.
- DELETE /characters/{id} - Remove an entry.

And similar endpoints for the rest of the entity types.

---

## Testing

The project uses JUnit for unit and integration testing. Run tests with:

  ```bash
  mvn test
  ```

---

## License 

This project is licensed under the MIT License.

<div align="center">
  <h1>Enjoy</h1>
</div>
