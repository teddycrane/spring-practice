FROM mysql:8.0.20

ENV MYSQL_DATABASE=SPRING \ 
    MYSQL_ROOT_PASSWORD=123456

# ADD spring.sql /docker-entrypoint-initdb.d

EXPOSE 3306

