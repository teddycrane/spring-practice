FROM maven:3.8.4-eclipse-temurin-16

WORKDIR /spring-practice
COPY . .
RUN mvn clean install
RUN mvn package -DskipTests

CMD ["java", "-jar", "./target/spring-practice-0.0.1.jar"]