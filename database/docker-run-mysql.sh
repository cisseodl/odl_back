#!/bin/bash
# ============================================================================
# Script Linux/Mac pour lancer MySQL 8.0 avec Docker
# Projet : Orange Digital Learning (ODL)
# ============================================================================

echo "Lancement de MySQL 8.0 dans Docker..."
echo ""

docker run -d \
  --name mysql-odl \
  -p 3306:3306 \
  -e MYSQL_ROOT_PASSWORD=root \
  -e MYSQL_DATABASE=odcawslearning \
  -v mysql-odl-data:/var/lib/mysql \
  --restart unless-stopped \
  mysql:8.0

echo ""
echo "MySQL est en cours de démarrage..."
echo "Attendez quelques secondes puis vérifiez avec: docker logs mysql-odl"
echo ""

