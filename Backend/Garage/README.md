# AutoHub — Garage microservice

Garage, services & tarifs, horaires, mécaniciens et qualifications.

| Élément | Valeur |
| --- | --- |
| Port | `8085` |
| Nom Eureka | `GARAGE` |
| Base de données | MySQL — `autohub_garage` |
| Stack | Spring Boot 3.5 + Spring Cloud 2025.0 |

## Démarrage

1. Démarrer MySQL (XAMPP/WAMP) sur le port `3306`.
2. Démarrer le serveur Eureka (`Backend/eureka-server`).
3. Lancer Garage :

```bash
mvn spring-boot:run
```

Vérifier :
- http://localhost:8085/api/garages
- Dashboard Eureka : http://localhost:8761 → `GARAGE` doit apparaître en `UP`.

## Lancer une deuxième instance

```bash
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8095
```

## Endpoints exposés

| Méthode | Route | Description |
| --- | --- | --- |
| GET | `/api/garages?ville={v}` | Liste / recherche par ville |
| GET | `/api/garages/{id}` | Détail d'un garage |
| POST | `/api/garages` | Créer un garage |
| GET | `/api/garages/{id}/services` | Catalogue de services |
| POST | `/api/garages/{id}/services` | Créer un service (409 si doublon) |
| GET | `/api/garages/{id}/mechanics` | Liste des mécaniciens |
| POST | `/api/garages/{id}/mechanics` | Ajouter un mécanicien |
| GET | `/api/mechanics/{id}/qualified?serviceTypeCode=…` | Habilitation (Feign pour Appointment) |
