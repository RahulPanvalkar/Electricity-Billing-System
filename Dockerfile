# First Stage: Build the application
FROM maven:3.8-openjdk-17 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Second Stage: Use Tomcat to deploy the WAR
FROM tomcat:9.0-jdk17-temurin
WORKDIR /usr/local/tomcat/webapps/
COPY --from=builder /app/target/Electricity-Billing-System.war ./ROOT.war

EXPOSE 8080

