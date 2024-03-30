FROM openjdk:17-alpine
COPY build/libs/project2-0.0.1-SNAPSHOT.jar /project2-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=EC2", "-jar", "project2-0.0.1-SNAPSHOT.jar"]

ENV SPRING_DATASOURCE_USERNAME=postgres
ENV SPRING_DATASOURCE_PASSWORD=postgres
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/swe304


