# Usa una imagen base oficial con Java (por ejemplo, OpenJDK 17)
FROM openjdk:17-jdk-slim

# Copia el archivo JAR generado por Maven/Gradle
COPY target/demo-0.0.1-SNAPSHOT.jar /app.jar

# Expone el puerto que Spring Boot usa por defecto
EXPOSE 8080

# Ejecuta el JAR con Java
ENTRYPOINT ["java", "-jar", "/app.jar"]