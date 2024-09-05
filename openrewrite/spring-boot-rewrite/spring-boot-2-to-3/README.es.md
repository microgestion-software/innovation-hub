# Migración de Spring Boot 2 a 3

## Visión general

Este proyecto es una API simple de Gestión de Tareas construida con Spring Boot 2.7.0. Sirve como una aplicación base para demostrar el proceso de migración de Spring Boot 2.7.x a 3.2.x utilizando OpenRewrite. La aplicación proporciona operaciones CRUD básicas para gestionar tareas, utilizando una base de datos H2 en memoria.

## Estructura del Repositorio

- `start/`: Contiene la versión inicial del proyecto utilizando Spring Boot 2.7.0
- `finish/`: Contiene la versión migrada del proyecto utilizando Spring Boot 3.2.x

## Características

- Crear, Leer, Actualizar y Eliminar tareas
- Filtrar tareas por estado
- Diseño API RESTful
- Base de datos H2 en memoria
- Arquitectura multicapa (Controlador, Servicio, Repositorio)

## Tecnologías

- Java 17
- Spring Boot 2.7.0 (en `start/`)
- Spring Boot 3.2.x (en `finish/`)
- Spring Data JPA
- Base de datos H2
- Maven
- OpenRewrite (para migración)

## Primeros pasos

### Prerrequisitos

- JDK 17
- Maven 3.6+

### Ejecutar la Aplicación

1. Clonar el repositorio:
   ```
   git clone https://microgestion-software/innovation-hub.git
   cd openrewrite/spring-boot-rewrite/spring-boot-2-to-3
   ```

2. Elegir la versión que quieres ejecutar:
   - Para Spring Boot 2.7.0: `cd start/legacy-spring-boot-app`
   - Para Spring Boot 3.2.x: `cd finish/legacy-spring-boot-app`

3. Construir el proyecto:
   ```
   mvn clean install
   ```

4. Ejecutar la aplicación:
   ```
   mvn spring-boot:run
   ```

La aplicación se iniciará en `http://localhost:8080`.

## Uso de la API

Puedes interactuar con la API utilizando herramientas como Postman o simplemente ejecutando comandos curl. Aquí hay algunos ejemplos:

### Listar todas las tareas

```bash
curl -X GET http://localhost:8080/api/tasks
```

### Obtener una tarea específica

Reemplaza `{id}` con el ID real de la tarea:

```bash
curl -X GET http://localhost:8080/api/tasks/{id}
```

### Crear una nueva tarea

```bash
curl -X POST http://localhost:8080/api/tasks \
     -H "Content-Type: application/json" \
     -d '{
         "title": "Nueva Tarea",
         "description": "Descripción de la nueva tarea",
         "status": "PENDING"
     }'
```

### Actualizar una tarea existente

Reemplaza `{id}` con el ID real de la tarea:

```bash
curl -X PUT http://localhost:8080/api/tasks/{id} \
     -H "Content-Type: application/json" \
     -d '{
         "title": "Tarea Actualizada",
         "description": "Descripción actualizada",
         "status": "IN_PROGRESS"
     }'
```

### Eliminar una tarea

Reemplaza `{id}` con el ID real de la tarea:

```bash
curl -X DELETE http://localhost:8080/api/tasks/{id}
```

### Obtener tareas por estado

Reemplaza `{status}` con PENDING, IN_PROGRESS o COMPLETED:

```bash
curl -X GET http://localhost:8080/api/tasks/status/{status}
```

## Base de datos

La aplicación utiliza una base de datos H2 en memoria. Puedes acceder a la consola H2 en `http://localhost:8080/h2-console` con los siguientes detalles:

- URL JDBC: `jdbc:h2:mem:taskdb`
- Usuario: `sa`
- Contraseña: `microgestion`

## Migración a Spring Boot 3.2.x utilizando OpenRewrite

Este proyecto demuestra la migración de Spring Boot 2.7.x a 3.2.x utilizando OpenRewrite. OpenRewrite es una herramienta de refactorización automatizada que puede ayudar a agilizar el proceso de migración.

### Pasos para Migrar

1. Añade el plugin OpenRewrite a tu `pom.xml`:

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

2. Ejecuta la migración de OpenRewrite:

```bash
mvn rewrite:run
```

3. Revisa los cambios realizados por OpenRewrite. La herramienta actualizará tus dependencias, archivos de configuración y código para que sean compatibles con Spring Boot 3.2.x.

4. Aborda cualquier problema o advertencia restante. Aunque OpenRewrite puede manejar muchos aspectos de la migración, algunos cambios pueden requerir intervención manual.

5. Prueba tu aplicación a fondo para asegurarte de que todo funciona como se espera con la nueva versión de Spring Boot.

Para una explicación detallada de los cambios realizados durante la migración, compara el código en los directorios `start/` y `finish/`.

## Licencia

Este proyecto está licenciado bajo la Licencia MIT - consulta el archivo [LICENSE.md](LICENSE.md) para más detalles.