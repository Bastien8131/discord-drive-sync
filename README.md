# discord-drive-sync

### [EN]

The project code is not perfect; one of my goals is also to deepen my knowledge of Java. I strive to produce quality code and do things the right way.

## Project status
The project is currently under development; I haven't yet met my goals for a v1.
I am currently working on the issue: [Controller for upload file](https://github.com/Bastien8131/discord-drive-sync/issues/15)

## Goal
The main objectives of this API are as follows:
- Synchronize all files between a Discord server and S3 cloud storage (Garage, in my case).
- Automatically manage categories, channels, messages, and files on the Discord server.
- Use the Discord server as an interface (frontend) for S3 storage and other features.
- Provide a REST API allowing a web interface to access data stored on Discord and in the cloud.

## Tool
This API is developed in Java using Spring Boot, Hibernate, JPA, and Flyway.
I also use JDA (Java Discord API) to interact with the Discord server via a bot, as well as the AWS SDK to connect to S3 cloud storage.
Finally, I use a PostgreSQL database to store and synchronize data between Discord and S3 cloud storage.

---
---

### [FR]

Le code du projet n'est pas parfait ; l'un de mes objectifs est aussi d'approfondir mes connaissances en Java. Je m'efforce de produire un code de qualité et faire les chose bien

## Project status
Le projet est actuellement en cours de développement ; je n'ai pas encore atteint les objectifs fixés pour la version 1.
Je travaille actuellement sur le ticket suivant : [Controller for upload file](https://github.com/Bastien8131/discord-drive-sync/issues/15)

## Goal
Les principaux objectifs de cette API sont les suivants :
- Synchroniser tous les fichiers entre un serveur Discord et un stockage cloud S3 (Garage, dans mon cas).
- Gérer automatiquement les catégories, les salons, les messages et les fichiers sur le serveur Discord.
- Utiliser le serveur Discord comme interface (frontend) pour le stockage S3 et d'autres fonctionnalités.
- Fournir une API REST permettant à une interface web d'accéder aux données présentes sur Discord et dans le cloud.

## Tool

Cette API est développée en Java à l'aide de Spring Boot, Hibernate, JPA et Flyway.
J'utilise également JDA (Java Discord API) pour interagir avec le serveur Discord via un bot, ainsi que le SDK AWS pour me connecter au stockage cloud S3.
Enfin, j'utilise une base de données PostgreSQL pour sauvegarder et synchroniser les données entre Discord et le cloud S3.
