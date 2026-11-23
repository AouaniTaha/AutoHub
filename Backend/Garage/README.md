# AutoHub — Garage microservice

Microservice Garage (Spring Boot + Eureka client). Pour l'instant : enregistrement dans Eureka uniquement.

| Élément | Valeur |
| --- | --- |
| Port | `8085` |
| Nom Eureka | `GARAGE` |
| Stack | Spring Boot 3.5 + Spring Cloud 2025.0 |

## Démarrage

1. Démarrer le serveur Eureka (à la racine du repo) :

   ```bash
   cd ../../eureka-server
   mvn spring-boot:run
   ```

   Dashboard : http://localhost:8761

2. Lancer Garage :

   ```bash
   mvn spring-boot:run
   ```

3. Vérifier dans le dashboard Eureka (http://localhost:8761) que **`GARAGE`** apparaît en `UP`.

## Lancer une deuxième instance

```bash
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8095
```
