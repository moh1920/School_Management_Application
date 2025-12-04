📘 School Management — Full Stack Application
Spring Boot 3.4.12 • Java 17 • Angular 16 • PostgreSQL • Docker • JWT Security


🚀 Description

Ce projet est une application Full Stack de gestion des étudiants (School Management System).
Il intègre :

Un backend Spring Boot sécurisé avec JWT, gestion des administrateurs, CRUD pour les étudiants, import CSV via Spring Batch.

Un frontend Angular 16, design moderne.

Une base de données PostgreSQL orchestrée via Docker.

Un déploiement complet avec Docker Compose, incluant vos images publiées sur Docker Hub.

Il s’agit d’un travail structuré selon les standards professionnels : architecture en couches, sécurité renforcée, conteneurisation, documentation, séparation claire du frontend et du backend.

                      ┌──────────────────────────────────────────┐
                      │                  Frontend                 │
                      │         Angular 16 • PrimeNG • SCSS        │
                      │           (Servi via Nginx Docker)        │
                      └──────────────────────────┬───────────────┘
                                                 │
                                                 │ REST API (JWT)
                                                 │
                      ┌──────────────────────────┴───────────────┐
                      │                  Backend                  │
                      │ Spring Boot 3.4.12 • JWT • Spring Batch  │
                      │  CRUD Students • Auth Admin • CSV Import │
                      └──────────────────────────┬───────────────┘
                                                 │
                                                 │ JDBC / Hibernate
                                                 │
                      ┌──────────────────────────┴───────────────┐
                      │                PostgreSQL                 │
                      │   Dockerized • init.sql Automatique      │
                      └──────────────────────────────────────────┘


🛠 Technologies Utilisées
🔹 Backend

Java 17

Spring Boot 3.4.12

Spring Security (JWT)

Spring Batch

Spring Data JPA / Hibernate

PostgreSQL

Maven

Lombok

🔹 Frontend

Angular 16

TypeScript

PrimeIcons

FontAwesome

SCSS

🔹 DevOps

Docker

Docker Compose

Nginx

Docker Hub Images (public)

sayarimohamed/backendimage

sayarimohamed/frontendimage



📁 Structure du Projet

test-Technique-stage/
└── src/
    └── main/
        └── java/
            └── test_Technique_stage/
                ├── controller/
                │     ├── AuthController.java
                │     └── StudentController.java
                │
                ├── DTOs/
                │     └── (DTO de requêtes & réponses)
                │
                ├── entity/
                │     ├── Admin.java
                │     └── Student.java
                │
                ├── security/
                │     ├── JwtService.java
                │     ├── JwtFilter.java        (si existant)
                │     └── LoginAttemptService.java
                │
                ├── service/
                │     ├── AdminService.java
                │     └── StudentService.java
                │
                ├── repositories/
                │     ├── AdminRepository.java
                │     └── StudentRepository.java
                │
                ├── mappers/
                │     └── (DTO ↔ Entity mappers)
                │
                └── TestTechniqueStageApplication.java



Frontend



test-stage-pfe_UI/
└── Application-Student/
    ├── angular.json
    ├── package.json
    ├── tsconfig.json
    │
    └── src/
        ├── app/
        │   ├── login/
        │   │     ├── login.component.ts
        │   │     ├── login.component.html
        │   │     └── login.component.scss
        │   │
        │   ├── register/
        │   │     ├── register.component.ts
        │   │     ├── register.component.html
        │   │     └── register.component.scss
        │   │
        │   ├── student/
        │   │     ├── student-list.component.ts
        │   │     ├── student-form.component.ts
        │   │     └── student.service.ts
        │   │
        │   ├── services/
        │   │     ├── auth.service.ts
        │   │     └── api.service.ts
        │   │
        │   ├── models/
        │   │     ├── student.model.ts
        │   │     └── admin.model.ts
        │   │
        │   ├── app.component.ts
        │   ├── app.module.ts
        │   └── app-routing.module.ts
        │
        ├── assets/
        └── styles.scss



    
🗄 Configuration Backend (Spring Boot)
application.yml

spring:
  datasource:
    url: jdbc:postgresql://postgres-sql-SchoolManagement:5432/SchoolManagement
    username: sayari
    password: sayari
    driver-class-name: org.postgresql.Driver

  jpa:
    hibernate:
      ddl-auto: update
    show-sql: false
    properties:
      hibernate:
        format_sql: true
    database: postgresql
    database-platform: org.hibernate.dialect.PostgreSQLDialect

  batch:
    jdbc:
      initialize-schema: always

server:
  port: 8020
  servlet:
    context-path: /api/v1

logging:
  level:
    org:
      springframework:
        security: DEBUG    # Debug login Keycloak/JWT

jwt:
  secret: ZmFrZV9zZWNyZXRfZmFrZV9zZWNyZXRfZmFrZV9zZWNyZXRfMTIzNDU2
  expiration-ms: 3600000
🔐 Sécurité
✔ Authentification JWT (Login / Register)
✔ BCrypt Password Hashing
✔ Rate Limiting anti-bruteforce (LoginAttemptService)

Bloque un utilisateur si trop de tentatives échouées → HTTP 429 Too Many Requests

Réinitialisation automatique en cas de succès

📡 Endpoints API
🔑 Authentification

| Méthode | Endpoint         | Description    |
| ------- | ---------------- | -------------- |
| POST    | `/auth/login`    | Connexion JWT  |
| POST    | `/auth/register` | Création Admin |
👨‍🎓 Étudiants
| Méthode | Endpoint                       | Description               |
| ------- | ------------------------------ | ------------------------- |
| GET     | `/student/getAllStudents`      | Pagination                |
| GET     | `/student/getStudentById/{id}` | Récupération              |
| POST    | `/student/createStudent`       | Création                  |
| PUT     | `/student/updateStudent/{id}`  | Mise à jour               |
| DELETE  | `/student/deleteStudent/{id}`  | Suppression               |
| GET     | `/student/search`              | Recherche id/username     |
| GET     | `/student/filter`              | Filtre par niveau         |
| POST    | `/student/upload-students`     | Import CSV (Spring Batch) |


📥 Import CSV — Spring Batch

Upload CSV → stocké temporairement

Job Spring Batch exécuté automatiquement

Mapping ligne → Student

Gestion erreurs + validation

Insertion en base

🎨 Frontend Angular 16
Installation locale
cd test-stage-pfe_UI/Application-Student
npm install
ng serve --open


🐳 Docker — Déploiement Complet
✔ Vos images Docker Hub (publiques)

docker pull sayarimohamed/backendimage
docker pull sayarimohamed/frontendimage

📦 docker-compose.yml (VERSION FINALE)
100% compatible avec vos images Docker Hub

services:

  postgres:
    container_name: postgres-sql-SchoolManagement
    image: postgres
    environment:
      POSTGRES_USER: sayari
      POSTGRES_PASSWORD: sayari
      PGDATA: /var/lib/postgresql/data
      POSTGRES_DB: SchoolManagement
    volumes:
      - postgres:/data/postgres
      - ./postgres/init.sql:/docker-entrypoint-initdb.d/init.sql
    ports:
      - "5432:5432"
    networks:
      - spring-demo
    restart: unless-stopped

  imagebackend:
    container_name: backend_container
    image: sayarimohamed/backendimage
    build:
      context: ../../test-Technique-stage/test-Technique-stage
      dockerfile: ../../docker/backend/Dockerfile
    command: ["java", "-jar", "app.jar"]
    ports:
      - "8020:8020"
    networks:
      - spring-demo
    restart: unless-stopped
    depends_on:
      - postgres

  imagefrontend:
    container_name: frontend_container
    image: sayarimohamed/frontendimage
    build:
      context: ../../test-stage-pfe_UI/Application-Student/
      dockerfile: ../../docker/frentEnd/Dockerfile
    ports:
      - "4200:80"
    networks:
      - spring-demo
    restart: unless-stopped
    depends_on:
      - imagebackend

volumes:
  postgres:

networks:
  spring-demo:


  ▶ Lancer l'application (Docker Compose)
  docker compose up --build


Services disponibles :

Backend : http://localhost:8020/api/v1

Frontend : http://localhost:4200

Base PostgreSQL : port 5432

📸 Screenshots

Login

<img width="1852" height="983" alt="image" src="https://github.com/user-attachments/assets/ce0d6ed9-3764-4d36-9260-895f602e5971" />

Register

<img width="1857" height="976" alt="image" src="https://github.com/user-attachments/assets/0b4234f2-e1fd-4fff-84c9-4c9e936d5f84" />

Dashboard Student

<img width="1847" height="978" alt="image" src="https://github.com/user-attachments/assets/c56552b8-6500-493c-b39d-f9dfc062e6f0" />

Import CSV

<img width="1853" height="978" alt="image" src="https://github.com/user-attachments/assets/07e2e923-a630-4e70-b320-644b788e228c" />

Swagger Documentation

<img width="1835" height="978" alt="image" src="https://github.com/user-attachments/assets/73e39ae8-618c-474c-ab6a-a843bc060438" />

Docker hub

<img width="1835" height="520" alt="image" src="https://github.com/user-attachments/assets/bfc976f6-7c59-487f-b753-64c87f0043c2" />


📄 Licence

Projet réalisé dans le cadre d’un test technique — libre d’utilisation et d’amélioration.


