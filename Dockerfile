# ---------- Build stage: compile the Spring Boot jar ----------
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copy pom first for better layer caching, then sources
COPY pom.xml .
COPY src ./src

RUN mvn -q -DskipTests package

# ---------- Runtime stage: slim JRE + the jar ----------
FROM eclipse-temurin:17-jre
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

# Render (and most hosts) inject $PORT; default to 8080 locally
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
