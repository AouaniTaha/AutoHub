# Instances du microservice Payment

Les deux instances ont le même nom Eureka (`PAYMENT`) et doivent être lancées depuis le dossier `Backend/Payment`.

1. Démarrer le premier Eureka sur `172.20.10.3:8761`, puis, quand disponible, le second sur son port configuré (par exemple `172.20.10.3:8762`).
2. Première instance Payment : `./mvnw spring-boot:run` (Windows : `mvnw.cmd spring-boot:run`). Elle écoute sur le port `8087`.
3. Deuxième instance, dans un autre terminal : `./mvnw spring-boot:run -Dspring-boot.run.profiles=instance-2` (Windows : `mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=instance-2`). Elle écoute sur le port `8088`.
4. Pour enregistrer les deux instances sur les deux Eureka, définir `EUREKA_SERVERS` avant leur démarrage avec les deux URL séparées par une virgule, en conservant l'adresse IP exacte du deuxième serveur, par exemple : `http://172.20.10.3:8761/eureka,http://<IP_EUREKA_2>:8762/eureka`. Si l'environnement n'a qu'un seul Eureka actif, les deux instances s'enregistrent sur `172.20.10.3:8761` par défaut.

Les deux instances partagent la base H2 fichier `./data/autohub_payment` pour voir les mêmes paiements et moyens de paiement. Enregistrez un moyen de paiement par `POST /api/payment-methods`, puis un paiement par `POST /api/payments` avec son `paymentMethodId`.
