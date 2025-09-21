# Inventario API - Prueba Técnica

## Tecnologías utilizadas
- Java 21
- Spring Boot
- Spring Data JPA
- Gradle
- PostgreSQL
- Swagger / Springdoc
- JUnit 5 y Mockito

## Requisitos previos
- Java 21
- PostgreSQL corriendo en puerto local

## Configuración
Configura tu `application.properties` con tus credenciales de base de datos:

```
# Configuración de la base de datos
spring.datasource.url=jdbc:postgresql://localhost:5432/inventory
spring.datasource.username=postgres
spring.datasource.password=pgdb

# Configuración de JPA / Hibernate
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

## Ejecucion
Desde línea de comandos:
```
./gradlew bootRun
```

## Documentacion Swagger
Al ejecutar, acceder a:
```
http://localhost:8080/swagger-ui.html
```

### Endpoints principales

- GET /api/products - Listar productos
- GET /api/products/{id} - Obtener producto
- POST /api/products - Crear producto
- PUT /api/products/{id} - Actualizar producto
- DELETE /api/products/{id} - Eliminar producto
- 
- GET /api/products/{id}/stock - Consultar stock
- POST /api/products/{id}/movements - Registrar entrada/salida
- GET /api/products/{id}/movements - Historial de movimientos

## Pruebas
Para ejecutar pruebas
```
./gradlew test
```

## Tablas utilizadas en PostgreSQL
```SQL
CREATE TABLE products (
	id BIGSERIAL PRIMARY KEY,
	name VARCHAR(50) NOT NULL,
	description VARCHAR(100),
	price DECIMAL(10,2) NOT NULL
);

CREATE TABLE inventory_movements (
    id BIGSERIAL PRIMARY KEY,
    product_id BIGINT NOT NULL,
    movement_type VARCHAR(10) NOT NULL CHECK (movement_type IN ('IN', 'OUT')),
    quantity INT NOT NULL,
    movement_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_product FOREIGN KEY (product_id) REFERENCES products(id)
);
```
