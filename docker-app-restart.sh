docker-compose -f docker-app-compose.yml down

mvn clean install -DskipTests

docker rmi $1-service

docker-compose -f docker-app-compose.yml up -d