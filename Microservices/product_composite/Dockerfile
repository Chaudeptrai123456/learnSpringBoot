# Stage 1: Extract the JAR layers
FROM eclipse-temurin:22.0.2_9-jre-jammy as builder
WORKDIR extracted
ADD ./build/libs/*.jar app.jar
RUN java -Djarmode=layertools -jar app.jar extract

# Stage 2: Build the final image
FROM eclipse-temurin:22.0.2_9-jre-jammy
WORKDIR application

# Copy the extracted layers to the final image
COPY --from=builder extracted/dependencies/ ./
COPY --from=builder extracted/spring-boot-loader/ ./
COPY --from=builder extracted/snapshot-dependencies/ ./
COPY --from=builder extracted/application/ ./

# Expose the port the application will run on
EXPOSE 8080

# Specify the entry point for the Spring Boot application
ENTRYPOINT ["java","org.springframework.boot.loader.launch.JarLauncher"]
