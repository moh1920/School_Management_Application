🌐 School Management — Full Stack Application
(Backend Spring Boot + Frontend Angular 16 + PostgreSQL + Docker)

📌 Description professionnelle

Cette application constitue une solution complète de gestion des étudiants (School Management System), développée comme un test technique mais pensée selon les standards professionnels d’un projet réel. Elle inclut :

Un backend sécurisé basé sur Spring Boot 3.4.12 avec authentification JWT, validation, gestion d'erreurs, import CSV via Spring Batch, et architecture en couches (Controller, Service, Repository).

Une base de données PostgreSQL orchestrée via Docker.

Un frontend Angular 16 moderne, responsive, FontAwesome, SCSS, et communiquant avec le backend via des services HTTP sécurisés.

Un déploiement entièrement containerisé :
Backend (Spring Boot) + Frontend (Angular compilé et servi via Nginx) + PostgreSQL → orchestrés par Docker Compose.

Ce projet illustre un développement maîtrisé, propre, testé, documenté et conforme aux bonnes pratiques industrielles.

🏗 Architecture du projet
L’application suit une architecture full-stack modulaire avec séparation claire des responsabilités :
┌──────────────────────────────────┐
│            Frontend             │
│        Angular 16 + SCSS        │
│ PrimeNG, FontAwesome, Services  │
└───────────────────────▲─────────┘
                        │ HTTP/REST (Bearer JWT)
┌───────────────────────┴─────────┐
│            Backend               │
│     Spring Boot 3.4.12          │
│ Auth JWT | CRUD Students | Batch│
└───────────────────────▲─────────┘
                        │ JDBC/Hibernate
┌───────────────────────┴─────────┐
│           PostgreSQL             │
│        Dockerized Database       │
└──────────────────────────────────┘




