FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

COPY build/libs/*.jar app.jar

EXPOSE 9092

ENTRYPOINT ["java", "-jar", "app.jar"]