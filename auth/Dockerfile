# --- ETAPA 1: Compilación ---
# Usamos directamente la imagen oficial de Gradle con Java 21
# Esto te ahorra descargar el zip con wget y descomprimirlo a mano
FROM gradle:8.5.0-jdk21 AS builder
WORKDIR /app

# Copiamos solo los archivos necesarios para preparar la compilación
COPY settings.gradle build.gradle ./
COPY src/ src/

# Compilamos el JAR saltando los tests
RUN gradle bootJar -x test --no-daemon

# --- ETAPA 2: Imagen de Ejecución ---
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Crear usuario seguro para Spring
RUN addgroup -S spring && adduser -S spring -G spring

# Copiar el JAR generado en la etapa anterior
COPY --from=builder /app/build/libs/*.jar app.jar
RUN chown spring:spring app.jar

# Cambiar al usuario seguro
USER spring

# SOLUCIÓN DINÁMICA DE PUERTOS PARA RENDER:
# En lugar de fijar 8086 u 8087, le decimos al contenedor que escuche 
# la variable PORT de Render, y si no existe, use el 8087 por defecto.
ENV PORT=8087
EXPOSE 8087

ENTRYPOINT ["java", "-jar", "app.jar"]
