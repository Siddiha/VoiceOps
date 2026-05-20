# Step 1 — Build the app using Maven
# We use a Maven image so you don't need Maven installed on your computer
FROM maven:3.9.6-eclipse-temurin-17 AS build

# Set working directory inside the container
WORKDIR /app

# Copy pom.xml first — Docker caches this layer
# So if you only change code, it won't re-download dependencies every time
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Now copy the actual source code
COPY src ./src

# Build the JAR file (skip tests here — we run them in CI/CD)
RUN mvn clean package -DskipTests

# ---

# Step 2 — Run the app using a smaller image
# We don't need Maven to run, just Java
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copy only the JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

# Tell Docker this container listens on port 8080
EXPOSE 8080

# Start the Spring Boot app
ENTRYPOINT ["java", "-jar", "app.jar"]