FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY ./build/libs/payment-service-0.1.0.jar /app/

ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "payment-service-0.1.0.jar"]
