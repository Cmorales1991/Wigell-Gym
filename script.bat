@echo off
echo Stopping and removing old container...
docker stop wigell-gym
docker rm wigell-gym
docker rmi wigell-api -f

echo Packaging Spring Boot app...
call mvn clean package -DskipTests

echo Building new Docker image...
docker build -t wigell-api .

echo Running container on network wigell-network...
docker run -d -p 6565:6565 --name wigell-gym --network wigell-network wigell-api