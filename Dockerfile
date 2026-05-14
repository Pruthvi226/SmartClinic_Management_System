# Build stage
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /workspace

COPY pom.xml .
RUN mvn -B -DskipTests dependency:go-offline

COPY src ./src
RUN mvn -B clean package -DskipTests

# Run stage
FROM tomcat:9.0-jdk17-temurin

ENV SPRING_PROFILES_ACTIVE=prod \
    CATALINA_OPTS="-Dfile.encoding=UTF-8 -Duser.timezone=UTC"

RUN rm -rf /usr/local/tomcat/webapps/*
COPY --from=build /workspace/target/smart-clinic-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080
CMD ["catalina.sh", "run"]
