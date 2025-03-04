FROM maven:3-eclipse-temurin-17 AS build
COPY . .
RUN mvn clean package -DskipTests
FROM eclipse-temurin:17-alpine
COPY --from=build /target/AttendanceSystem-1.0-SNAPSHOT.jar Attendance.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=render", "-jar", "Attendance.jar"]
EXPOSE 8080