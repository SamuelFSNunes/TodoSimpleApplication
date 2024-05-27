# Build stage with Java 21 and Maven
FROM maven:3.9-eclipse-temurin-21-alpine AS build

# Set working directory
WORKDIR /app

# Copy project files
COPY pom.xml .
COPY src ./src

# Build the project with Maven
RUN mvn clean package

# Use uma imagem base do JDK 21
FROM openjdk:21-slim
LABEL authors="Samuel Nunes"

# Define o diretório de trabalho dentro do container
WORKDIR /app

# Copia o arquivo JAR da aplicação para o diretório de trabalho
COPY target/todosimple-0.0.1-SNAPSHOT.jar /app/todosimple.jar

# Expõe a porta que a aplicação Spring Boot vai usar
EXPOSE 8080

# Define o comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "todosimple.jar"]
