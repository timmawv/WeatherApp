FROM maven:3.8.4-openjdk-17-slim AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -Dmaven.test.skip

FROM tomcat
COPY --from=builder /app/target/*.war /usr/local/tomcat/webapps/
CMD ["catalina.sh", "run"]