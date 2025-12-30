@echo off
REM ============================================================================
REM Script Windows pour lancer MySQL 8.0 avec Docker
REM Projet : Orange Digital Learning (ODL)
REM ============================================================================

echo Lancement de MySQL 8.0 dans Docker...
echo.

docker run -d ^
  --name mysql-odl ^
  -p 3306:3306 ^
  -e MYSQL_ROOT_PASSWORD=root ^
  -e MYSQL_DATABASE=odcawslearning ^
  -v mysql-odl-data:/var/lib/mysql ^
  --restart unless-stopped ^
  mysql:8.0

echo.
echo MySQL est en cours de demarrage...
echo Attendez quelques secondes puis verifiez avec: docker logs mysql-odl
echo.
pause

