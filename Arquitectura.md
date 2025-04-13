```mermaid
graph TD
    subgraph Cliente
        A[Postman / Frontend] -->|Solicitud HTTP| B[Spring Boot API]
    end

    subgraph Backend
        B -->|Validación y lógica de negocio| C[Controladores]
        C -->|Invoca servicios| D[Servicios]
        D -->|Accede a datos| E[Repositorios]
        E -->|Persistencia| F[(Base de Datos PostgreSQL)]
    end

    subgraph IoT
        G[Dispositivos IoT] -->|MQTT| H[Broker MQTT]
        H -->|Mensajes MQTT| I[MqttSubscriber]
        I -->|Llama a servicio| D
    end
    ```