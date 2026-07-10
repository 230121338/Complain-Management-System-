# --- Build stage: compile the WAR with Maven ---
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -q -B dependency:go-offline
COPY src ./src
RUN mvn -q -B clean package

# --- Runtime stage: Tomcat 9 (javax.servlet) ---
FROM tomcat:9-jre17
# Serve the app at the root context ("/") instead of /ComplaintSystem
RUN rm -rf /usr/local/tomcat/webapps/ROOT
COPY --from=build /app/target/ComplaintSystem.war /usr/local/tomcat/webapps/ROOT.war

# Database connection (override in your host's env / dashboard)
ENV DB_URL="jdbc:mysql://db:3306/complaintdb?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC" \
    DB_USER="complaint" \
    DB_PASSWORD="complaint123"

# Honor the platform-provided $PORT (Railway/Render); default 8080 locally.
COPY docker-entrypoint.sh /docker-entrypoint.sh
RUN chmod +x /docker-entrypoint.sh
EXPOSE 8080
ENTRYPOINT ["/docker-entrypoint.sh"]
CMD ["catalina.sh", "run"]
