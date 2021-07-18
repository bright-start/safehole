docker-compose stop safehole && docker-compose rm safehole
docker-compose rmi safehole:1.0.0

docker-compose stop mysql-safehole && docker-compose rm mysql-safehole
docker-compose rmi mysql-safehole:1.0.0