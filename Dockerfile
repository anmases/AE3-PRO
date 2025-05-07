# syntax=docker/dockerfile:1
FROM maven:3.9-eclipse-temurin-17
WORKDIR /src
COPY pom.xml .
COPY .mvn/ .mvn/
COPY mvnw .
RUN ./mvnw -B dependency:resolve

COPY src src
COPY frontend frontend

RUN ./mvnw clean package -Pproduction -DskipTests


FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /src/target/*.war app.war

EXPOSE 8080

CMD java -jar app.war

