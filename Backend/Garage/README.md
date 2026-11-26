# AutoHub — Garage microservice

Garage, services & tarifs, horaires, mécaniciens et qualifications.

| Élément | Valeur |
| --- | --- |
| Port | `8085` |
| Nom Eureka | `GARAGE` |
| Base de données | MySQL — `autohub_garage` |
| Stack | Spring Boot 3.5 + Spring Cloud 2025.0 + Actuator |

## Démarrage

1. Démarrer MySQL (XAMPP/WAMP) sur le port `3306`.
2. Démarrer le serveur Eureka (`Backend/eureka-server`).
3. Lancer Garage :

```bash
mvn spring-boot:run
```

Vérifier :
- http://localhost:8085/api/garages
- Dashboard Eureka : http://localhost:8761 → `GARAGE` en `UP`
- Actuator : http://localhost:8085/actuator/health

## Lancer une deuxième instance

```bash
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8095
```

## Endpoints exposés

### Garage CRUD

| Méthode | Route | Description |
| --- | --- | --- |
| GET | `/api/garages?ville={v}` | Liste / recherche par ville |
| GET | `/api/garages/{id}` | Détail d'un garage |
| POST | `/api/garages` | Créer un garage |
| PUT | `/api/garages/{id}` | Modifier un garage |
| DELETE | `/api/garages/{id}` | Supprimer un garage |

### ServiceOffer

| Méthode | Route | Description |
| --- | --- | --- |
| GET | `/api/garages/{id}/services` | Catalogue de services |
| POST | `/api/garages/{id}/services` | Créer un service (409 si doublon) |
| PUT | `/api/services/{id}` | Modifier un service |
| DELETE | `/api/services/{id}` | Supprimer un service |

### Mechanic

| Méthode | Route | Description |
| --- | --- | --- |
| GET | `/api/garages/{id}/mechanics` | Liste des mécaniciens |
| POST | `/api/garages/{id}/mechanics` | Ajouter un mécanicien |
| PUT | `/api/mechanics/{id}` | Modifier un mécanicien |
| DELETE | `/api/mechanics/{id}` | Supprimer un mécanicien |
| GET | `/api/mechanics/{id}/qualified?serviceTypeCode=…` | Habilitation (Feign pour Appointment) |

### Actuator (observabilité)

| Route | Description |
| --- | --- |
| `/actuator/health` | Santé du service (db, diskSpace, ping…) |
| `/actuator/info` | Infos du service (nom, version, équipe) |
| `/actuator/metrics` | Liste des métriques disponibles |
| `/actuator/metrics/{name}` | Métrique précise (ex: `jvm.memory.used`, `http.server.requests`) |
