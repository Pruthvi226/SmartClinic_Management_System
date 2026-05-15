FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /workspace

COPY pom.xml .
RUN mvn -B -ntp -DskipTests dependency:go-offline

COPY src ./src
RUN mvn -B -ntp clean package -DskipTests

FROM tomcat:9.0-jdk17-temurin

ENV SPRING_PROFILES_ACTIVE=prod \
    CATALINA_OPTS="-Dfile.encoding=UTF-8 -Duser.timezone=UTC"

RUN rm -rf /usr/local/tomcat/webapps/*
RUN apt-get update \
    && apt-get install -y --no-install-recommends curl \
    && rm -rf /var/lib/apt/lists/*

COPY --from=build /workspace/target/smart-clinic.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080
HEALTHCHECK --interval=30s --timeout=5s --start-period=90s --retries=5 \
    CMD curl -fsS http://localhost:8080/login >/dev/null || exit 1

CMD ["catalina.sh", "run"]
