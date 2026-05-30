cat > api-gateway/Dockerfile << 'EOF'
FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app
RUN apk add --no-cache wget unzip && \
    wget -q https://services.gradle.org/distributions/gradle-8.5-bin.zip && \
    unzip -q gradle-8.5-bin.zip -d /opt && \
    rm gradle-8.5-bin.zip
ENV PATH="/opt/gradle-8.5/bin:$PATH"
COPY gradle/ gradle/
COPY gradlew settings.gradle build.gradle ./
COPY src/ src/
RUN gradle bootJar -x test --no-daemon
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
RUN addgroup -S spring && adduser -S spring -G spring
COPY --from=builder /app/build/libs/*.jar app.jar
RUN chown spring:spring app.jar
USER spring
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
EOF

cat > auth/Dockerfile << 'EOF'
FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app
RUN apk add --no-cache wget unzip && \
    wget -q https://services.gradle.org/distributions/gradle-8.5-bin.zip && \
    unzip -q gradle-8.5-bin.zip -d /opt && \
    rm gradle-8.5-bin.zip
ENV PATH="/opt/gradle-8.5/bin:$PATH"
COPY gradle/ gradle/
COPY gradlew settings.gradle build.gradle ./
COPY src/ src/
RUN gradle bootJar -x test --no-daemon
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
RUN addgroup -S spring && adduser -S spring -G spring
COPY --from=builder /app/build/libs/*.jar app.jar
RUN chown spring:spring app.jar
USER spring
EXPOSE 8086
ENTRYPOINT ["java", "-jar", "app.jar"]
EOF

cat > members/Dockerfile << 'EOF'
FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app
RUN apk add --no-cache wget unzip && \
    wget -q https://services.gradle.org/distributions/gradle-8.5-bin.zip && \
    unzip -q gradle-8.5-bin.zip -d /opt && \
    rm gradle-8.5-bin.zip
ENV PATH="/opt/gradle-8.5/bin:$PATH"
COPY gradle/ gradle/
COPY gradlew settings.gradle build.gradle ./
COPY src/ src/
RUN gradle bootJar -x test --no-daemon
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
RUN addgroup -S spring && adduser -S spring -G spring
COPY --from=builder /app/build/libs/*.jar app.jar
RUN chown spring:spring app.jar
USER spring
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
EOF

cat > attendance/Dockerfile << 'EOF'
FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app
RUN apk add --no-cache wget unzip && \
    wget -q https://services.gradle.org/distributions/gradle-8.5-bin.zip && \
    unzip -q gradle-8.5-bin.zip -d /opt && \
    rm gradle-8.5-bin.zip
ENV PATH="/opt/gradle-8.5/bin:$PATH"
COPY gradle/ gradle/
COPY gradlew settings.gradle build.gradle ./
COPY src/ src/
RUN gradle bootJar -x test --no-daemon
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
RUN addgroup -S spring && adduser -S spring -G spring
COPY --from=builder /app/build/libs/*.jar app.jar
RUN chown spring:spring app.jar
USER spring
EXPOSE 8085
ENTRYPOINT ["java", "-jar", "app.jar"]
EOF

cat > notifications/Dockerfile << 'EOF'
FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app
RUN apk add --no-cache wget unzip && \
    wget -q https://services.gradle.org/distributions/gradle-8.5-bin.zip && \
    unzip -q gradle-8.5-bin.zip -d /opt && \
    rm gradle-8.5-bin.zip
ENV PATH="/opt/gradle-8.5/bin:$PATH"
COPY gradle/ gradle/
COPY gradlew settings.gradle build.gradle ./
COPY src/ src/
RUN gradle bootJar -x test --no-daemon
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
RUN addgroup -S spring && adduser -S spring -G spring
COPY --from=builder /app/build/libs/*.jar app.jar
RUN chown spring:spring app.jar
USER spring
EXPOSE 8087
ENTRYPOINT ["java", "-jar", "app.jar"]
EOF