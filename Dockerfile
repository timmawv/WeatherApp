FROM maven:3.8.4-openjdk-17-slim AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -Dmaven.test.skip

FROM tomcat:10.1.18-jdk17
COPY --from=builder /app/target/*.war /usr/local/tomcat/webapps/