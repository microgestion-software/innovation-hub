# Spring Boot 2 to 3 Migration

## Overview

This project is a simple Task Management API built with Spring Boot 2.7.0. It serves as a base application to demonstrate the migration process from Spring Boot 2.7.x to 3.2.x using OpenRewrite. The application provides basic CRUD operations for managing tasks, utilizing an H2 in-memory database.

## Repository Structure

- `start/`: Contains the initial version of the project using Spring Boot 2.7.0
- `finish/`: Contains the migrated version of the project using Spring Boot 3.2.x

## Features

- Create, Read, Update, and Delete tasks
- Filter tasks by status
- RESTful API design
- In-memory H2 database
- Multi-layered architecture (Controller, Service, Repository)

## Technologies

- Java 17
- Spring Boot 2.7.0 (in `start/`)
- Spring Boot 3.2.x (in `finish/`)
- Spring Data JPA
- H2 Database
- Maven
- OpenRewrite (for migration)

## Getting Started

### Prerequisites

- JDK 17
- Maven 3.6+

### Running the Application

1. Clone the repository:
   ```
   git clone https://microgestion-software/innovation-hub.git
   cd openrewrite/spring-boot-rewrite/spring-boot-2-to-3
   ```

2. Choose the version you want to run:
   - For Spring Boot 2.7.0: `cd start/legacy-spring-boot-app`
   - For Spring Boot 3.2.x: `cd finish/legacy-spring-boot-app`

3. Build the project:
   ```
   mvn clean install
   ```

4. Run the application:
   ```
   mvn spring-boot:run
   ```

The application will start running at `http://localhost:8080`.

## API Usage

You can interact with the API using tools like Postman or by simply running curl commands. Here are some examples:

### List all tasks

```bash
curl -X GET http://localhost:8080/api/tasks
```

### Get a specific task

Replace `{id}` with the actual task ID:

```bash
curl -X GET http://localhost:8080/api/tasks/{id}
```

### Create a new task

```bash
curl -X POST http://localhost:8080/api/tasks \
     -H "Content-Type: application/json" \
     -d '{
         "title": "New Task",
         "description": "Description of the new task",
         "status": "PENDING"
     }'
```

### Update an existing task

Replace `{id}` with the actual task ID:

```bash
curl -X PUT http://localhost:8080/api/tasks/{id} \
     -H "Content-Type: application/json" \
     -d '{
         "title": "Updated Task",
         "description": "Updated description",
         "status": "IN_PROGRESS"
     }'
```

### Delete a task

Replace `{id}` with the actual task ID:

```bash
curl -X DELETE http://localhost:8080/api/tasks/{id}
```

### Get tasks by status

Replace `{status}` with either PENDING, IN_PROGRESS, or COMPLETED:

```bash
curl -X GET http://localhost:8080/api/tasks/status/{status}
```

## Database

The application uses an H2 in-memory database. You can access the H2 console at `http://localhost:8080/h2-console` with the following details:

- JDBC URL: `jdbc:h2:mem:taskdb`
- Username: `sa`
- Password: `microgestion`

## Migrating to Spring Boot 3.2.x using OpenRewrite

This project demonstrates the migration from Spring Boot 2.7.x to 3.2.x using OpenRewrite. OpenRewrite is an automated refactoring tool that can help streamline the migration process.

### Steps to Migrate

1. Add the OpenRewrite plugin to your `pom.xml`:

```xml
<plugins>
  <plugin>
    <groupId>org.openrewrite.maven</groupId>
    <artifactId>rewrite-maven-plugin</artifactId>
    <version>5.39.2</version>
    <configuration>
      <exportDatatables>true</exportDatatables>
      <activeRecipes>
        <recipe>org.openrewrite.java.spring.boot3.UpgradeSpringBoot_3_2</recipe>
      </activeRecipes>
    </configuration>
    <dependencies>
      <dependency>
        <groupId>org.openrewrite.recipe</groupId>
        <artifactId>rewrite-spring</artifactId>
        <version>5.18.0</version>
      </dependency>
    </dependencies>
  </plugin>
</plugins>
```

2. Run the OpenRewrite migration:

```bash
mvn rewrite:run
```

3. Review the changes made by OpenRewrite. The tool will update your dependencies, configuration files, and code to be compatible with Spring Boot 3.2.x.

4. Address any remaining issues or warnings. While OpenRewrite can handle many aspects of the migration, some changes may require manual intervention.

5. Test your application thoroughly to ensure everything works as expected with the new Spring Boot version.


For a detailed walkthrough of the changes made during migration, compare the code in the `start/` and `finish/` directories.

## License

This project is licensed under the MIT License - see the [LICENSE](../../../LICENSE) file for details.
