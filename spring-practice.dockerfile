FROM maven:3.8.4-eclipse-temurin-16

WORKDIR /spring-practice
COPY . .
RUN mvn clean install

# RUN curl -LJO https://raw.githubusercontent.com/vishnubob/wait-for-it/master/wait-for-it.sh
# CMD ["./wait-for-it.sh", "docker-mysql:3306", "--", "./app.sh"]

RUN mvn spring-boot:run