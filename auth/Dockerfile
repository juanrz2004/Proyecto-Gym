FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app
RUN apk add --no-cache wget unzip && wget -q https://services.gradle.org/distributions/gradle-8.5-bin.zip && unzip -q gradle-8.5-bin.zip -d /opt && rm gradle-8.5-bin.zip
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