# !/env/bin/bash

echo "Starting docker build";

COUNT=`ls -1 ./target/*.jar 2>/dev/null | wc -l`

if [ $COUNT != 0 ]; then
    echo "Starting built version of application"
    java -jar ./target/spring-practice-0.0.1.jar
else
    echo "Building application"
    mvn clean package -DskipTests -DskipITs
fi
