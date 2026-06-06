FROM eclipse-temurin:17-jre-alpine
LABEL maintainer="WMS Team"
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 8888
ENTRYPOINT ["java", "-jar", "app.jar"]
