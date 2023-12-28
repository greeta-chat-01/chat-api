docker-compose -f docker-app-compose.yml down
docker-compose down
docker-compose up -d

mvn clean install -DskipTests

docker-compose -f docker-app-compose.yml up -d
