# Build stage
FROM maven:3.9.0-eclipse-temurin-21 AS build
WORKDIR /workspace/app

# copy maven files first to leverage cache
COPY pom.xml .
COPY src ./src

RUN mvn -B -DskipTests package

# Run stage
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /workspace/app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
