# --- Stage 1: The Builder ---
FROM eclipse-temurin:21-jdk-ubi9-minimal AS builder

RUN microdnf install -y findutils && microdnf clean all

# Set the build directory
WORKDIR /build

# Copy only the necessary build files first to leverage Docker's cache
COPY notes-api/build.gradle.kts notes-api/settings.gradle.kts notes-api/gradlew ./
COPY notes-api/gradle ./gradle

# Copy the application source code
COPY notes-api/src ./src

# Build the application
RUN ./gradlew bootJar --no-daemon


FROM eclipse-temurin:21-jre-ubi9-minimal

# Create a specific group and user for our application
# -r creates a system user (no home dir by default)
# -g specifies the group
# -s /sbin/nologin prevents the user from being used for shell login
RUN groupadd -r -g 1001 appgroup && \
    useradd -r -u 1001 -g appgroup -s /sbin/nologin appuser

RUN microdnf update -y curl --best && microdnf clean all

# Set the working directory
WORKDIR /app

# Copy the JAR file and set ownership at the same time
# This is more efficient than a separate 'CHOWN' layer
COPY --from=builder --chown=appuser:appgroup /build/build/libs/*.jar /app/notes-api.jar

# Expose the port
EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=3s --start-period=10s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Switch to the non-root user
# All subsequent commands (like ENTRYPOINT) will run as 'appuser'
USER appuser

# Run the application
ENTRYPOINT ["java", "-jar", "/app/notes-api.jar"]