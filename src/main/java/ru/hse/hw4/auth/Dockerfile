FROM maven:3.9.2 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn package -DskipTests

FROM openjdk:17
WORKDIR /app
COPY --from=build /app/target/hw4-0.0.1-SNAPSHOT.jar /app/hw4-0.0.1-SNAPSHOT.jar
CMD ["java", "-jar", "hw4-0.0.1-SNAPSHOT.jar"]
