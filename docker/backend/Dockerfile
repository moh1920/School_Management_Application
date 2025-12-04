#Étape 1 : build du JAR avec Maven
  FROM maven:3.9.6-eclipse-temurin-17 AS build
  WORKDIR /build
  COPY pom.xml .
  RUN mvn dependency:go-offline
  COPY src ./src
  RUN mvn clean package -DskipTests

#Étape 2 : image légère avec Amazon Corretto
  FROM amazoncorretto:17
  ARG APP_VERSION=1.0.0
  WORKDIR /app
#  Copy and rename the JAR to a fixed name
  COPY --from=build /build/target/*.jar /app/app.jar
#  Configuration de l'environnement
  ENV SPRING_DATASOURCE_URL=jdbc:postgresql://postgres-sql-SchoolManagement:5432/SchoolManagement
  EXPOSE 8020
  CMD java -jar -Dspring.datasource.url=${SPRING_DATASOURCE_URL}  /app/app.jar