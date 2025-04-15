# IoT Sensor Manager API

API REST desarrollada en Java con Spring Boot para gestionar sensores IoT, dispositivos, ubicaciones, empresas y roles. Incluye suscripciones MQTT e integración con PostgreSQL.

## 🚀 Tecnologías

- Java 17
- Spring Boot 3
- Spring Data JPA
- Spring Security
- MQTT (Eclipse Paho)
- PostgreSQL
- Maven


## 🔌 Endpoints principales

Los endpoints siguen la estructura REST:

- `GET /api/v1/sensordata`
- `POST /api/v1/sensordata`
- `PUT /api/v1/sensordata/{id}`
- `DELETE /api/v1/sensordata/{id}`

Consulta Swagger para más detalles (ver abajo).

## 🔒 Seguridad

Incluye configuración básica de seguridad con Spring Security y autenticación en memoria. En entorno productivo se recomienda reemplazar con JWT o OAuth2.

## 📡 MQTT

Se conecta al broker configurado en `application.properties` para recibir datos de sensores en tiempo real.

## 🔍 Documentación API (Swagger)

Disponible en `http://localhost:8080/swagger-ui/index.html`

## ⚙️ Configuración de Base de Datos

Edita `src/main/resources/application.properties` para definir tus credenciales y host:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/iot_db
spring.datasource.username=postgres
spring.datasource.password=tu_password
```

## 📘 API Documentation - Swagger UI

Nuestra API RESTful está documentada con [Swagger UI](https://swagger.io/tools/swagger-ui/), permitiendo explorar, probar y validar los endpoints desde una interfaz web amigable.

### 🚀 Acceso a Swagger

Una vez que el proyecto esté corriendo, puedes acceder directamente sin autenticación:

```
http://localhost:8080/swagger-ui.html
```

No se requieren credenciales para acceder a la documentación de Swagger. Puedes utilizar los endpoints públicos libremente desde la interfaz.

---

### 📦 Habilitar/Deshabilitar Swagger

La documentación Swagger se puede habilitar/deshabilitar modificando las siguientes propiedades en `application.properties`:

```properties
springdoc.api-docs.enabled=true
springdoc.swagger-ui.enabled=true
```
