# AutoHub — Eureka Server

Serveur de découverte Netflix Eureka pour AutoHub.

| Élément | Valeur |
| --- | --- |
| Port | `8761` |
| Dashboard | http://localhost:8761 |

## Démarrage

```bash
mvn spring-boot:run
```

Les microservices s'enregistrent auprès de ce serveur via la propriété :

```
eureka.client.service-url.defaultZone=http://localhost:8761/eureka
```
