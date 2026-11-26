# AutoHub — Architecture Microservices (Eureka Server, Order & Observabilité)

Plateforme distribuée de services et marketplace automobile.

## Membres de l'équipe

- Membre 1 : *Taha Aouini*
- Membre 2 : *Mohamed Ali Abidi*
- Membre 3 : *Emir Kefi*
- Membre 4 : *Adem Brinsi*
- Membre 5 : *Oussama Belgacem*
- Membre 6 : *Oussema Trabelsi*

## Architecture & Ports (`main`)

| Microservice | Dossier | Port par défaut | Nom dans Eureka |
|---|---|---|---|
| Eureka Server (Instance 1 & 2) | `Backend/eureka-server` | `8761` / `8762` | `EUREKA-SERVER` |
| User | `Backend/User` | `8081` | `USER` |
| Vehicle | `Backend/Vehicle` | `8082` | `VEHICLE` |
| Listings | `Backend/Listings` | `8083` | `LISTINGS` |
| Appointement | `Backend/Appointement` | `8084` | `APPOINTMENT` |
| Garage | `Backend/Garage` | `8085` | `GARAGE` |
| **Order** | `Backend/Order` | `8086` (2e instance : `8096`) | `ORDER` |
| Payment | `Backend/Payment` | `8087` | `PAYMENT` |

## Démarrage

### 1. Lancer 2 instances du serveur Eureka en réplication peer-to-peer (sans modifier `Backend/eureka-server`)

> **Important :** Un client Eureka s'enregistre sur le premier serveur joignable de `defaultZone`. Pour que les **deux** serveurs Eureka (`8761` et `8762`) affichent **tous** les microservices en même temps, il faut activer la réplication entre pairs (`8761` ↔ `8762`) via les arguments de lancement :

**Instance 1 (Port `8761` → réplique vers `8762`) :**
```powershell
cd Backend\eureka-server
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.arguments=--server.port=8761 --eureka.instance.hostname=localhost --eureka.client.register-with-eureka=true --eureka.client.fetch-registry=true --eureka.client.service-url.defaultZone=http://127.0.0.1:8762/eureka"
```

**Instance 2 (Port `8762` → réplique vers `8761`) :**
```powershell
cd Backend\eureka-server
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.arguments=--server.port=8762 --eureka.instance.hostname=127.0.0.1 --eureka.client.register-with-eureka=true --eureka.client.fetch-registry=true --eureka.client.service-url.defaultZone=http://localhost:8761/eureka"
```

Dashboards Eureka :
- Instance 1 : `http://localhost:8761`
- Instance 2 : `http://localhost:8762`

### 2. Lancer le microservice Order (2 instances)

Par défaut, `Backend/Order` utilise H2 en mémoire (profil `postgres` disponible via `--spring.profiles.active=postgres`) et s'enregistre automatiquement auprès des deux serveurs Eureka (`http://localhost:8761/eureka,http://localhost:8762/eureka`).
Pour pointer vers un serveur Eureka distant (ex. `172.20.10.3`), définir la variable d'environnement `EUREKA_URI` :
```powershell
$env:EUREKA_URI="http://172.20.10.3:8761/eureka"
```

**Instance 1 (Port `8086`) :**
```powershell
cd Backend\Order
.\mvnw.cmd spring-boot:run
```

**Instance 2 (Port `8096`) :**
```powershell
cd Backend\Order
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.arguments=--server.port=8096"
```

### 3. Endpoints CRUD & Swagger (`Order`)

- Swagger UI : `http://localhost:8086/swagger-ui.html`
- Panier (`CartController`) : `http://localhost:8086/api/carts`
- Commandes (`OrderController`) : `http://localhost:8086/api/orders`
- Console H2 : `http://localhost:8086/h2-console`

### 4. Observabilité — Spring Boot Actuator (`Order`)

Endpoints exposés (`health`, `info`, `metrics`, `beans`, `mappings`, `env`, `loggers`, `threaddump`) :
- `GET http://localhost:8086/actuator/health`
- `GET http://localhost:8086/actuator/info`
- `GET http://localhost:8086/actuator/metrics`
- `GET http://localhost:8086/actuator/beans`
- `GET http://localhost:8086/actuator/mappings`
- `GET http://localhost:8086/actuator/env`
- `GET http://localhost:8086/actuator/loggers`

## Captures d'écran (à ajouter)

<!-- Ajouter vos captures d'écran ici -->
![Dashboard Eureka 8761](Documentation/eureka-dashboard-8761.png)
![Dashboard Eureka 8762](Documentation/eureka-dashboard-8762.png)
![Actuator Health](Documentation/actuator-health.png)
