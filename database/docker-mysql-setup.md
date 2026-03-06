# 🐳 Configuration MySQL avec Docker - ODL

## 📋 Prérequis

- **Docker Desktop** installé et démarré sur Windows
- Vérifier que Docker fonctionne : `docker --version`

---

## 🚀 Commande Docker Run (Recommandée)

### Option 1 : MySQL avec mot de passe "root" (Simple)

```bash
docker run -d \
  --name mysql-odl \
  -p 3306:3306 \
  -e MYSQL_ROOT_PASSWORD=root \
  -e MYSQL_DATABASE=odcawslearning \
  -v mysql-odl-data:/var/lib/mysql \
  --restart unless-stopped \
  mysql:8.0
```

**Explication de la commande :**
- `docker run -d` : Lance le conteneur en mode détaché (en arrière-plan)
- `--name mysql-odl` : Donne un nom au conteneur pour faciliter sa gestion
- `-p 3306:3306` : Mappe le port 3306 de votre machine vers le port 3306 du conteneur
- `-e MYSQL_ROOT_PASSWORD=root` : Définit le mot de passe root à "root"
- `-e MYSQL_DATABASE=odcawslearning` : Crée automatiquement la base de données au démarrage
- `-v mysql-odl-data:/var/lib/mysql` : Crée un volume nommé pour persister les données (les données survivent à l'arrêt du conteneur)
- `--restart unless-stopped` : Redémarre automatiquement le conteneur si Docker redémarre
- `mysql:8.0` : Image MySQL version 8.0 (dernière version stable de la série 8.0)

**⚠️ Note importante :** MySQL ne permet pas un mot de passe complètement vide pour des raisons de sécurité. Utilisez "root" comme mot de passe, puis modifiez `application.properties` :

```properties
spring.datasource.password=root
```

---

### Option 2 : MySQL avec mot de passe vide (via variable d'environnement)

```bash
docker run -d \
  --name mysql-odl \
  -p 3306:3306 \
  -e MYSQL_ALLOW_EMPTY_PASSWORD=yes \
  -e MYSQL_DATABASE=odcawslearning \
  -v mysql-odl-data:/var/lib/mysql \
  --restart unless-stopped \
  mysql:8.0
```

**Explication :**
- `-e MYSQL_ALLOW_EMPTY_PASSWORD=yes` : Permet un mot de passe vide pour root
- Les autres paramètres sont identiques à l'Option 1

**⚠️ Note :** Cette option fonctionne, mais MySQL peut afficher des avertissements. L'Option 1 est plus standard.

---

### Option 3 : Version complète avec toutes les options (Production-like)

```bash
docker run -d \
  --name mysql-odl \
  -p 3306:3306 \
  -e MYSQL_ROOT_PASSWORD=root \
  -e MYSQL_DATABASE=odcawslearning \
  -e MYSQL_CHARACTER_SET_SERVER=utf8mb4 \
  -e MYSQL_COLLATION_SERVER=utf8mb4_unicode_ci \
  -v mysql-odl-data:/var/lib/mysql \
  -v "$(pwd)/database/my.cnf:/etc/mysql/conf.d/custom.cnf:ro" \
  --restart unless-stopped \
  mysql:8.0
```

**Explication supplémentaire :**
- `-e MYSQL_CHARACTER_SET_SERVER=utf8mb4` : Définit l'encodage par défaut (compatible avec votre script SQL)
- `-e MYSQL_COLLATION_SERVER=utf8mb4_unicode_ci` : Définit la collation par défaut
- `-v "$(pwd)/database/my.cnf:..."` : Monte un fichier de configuration personnalisé (optionnel)

---

## 📝 Commandes Utiles pour Gérer le Conteneur

### Vérifier que le conteneur tourne

```bash
docker ps
```

Vous devriez voir `mysql-odl` dans la liste.

### Voir les logs du conteneur

```bash
docker logs mysql-odl
```

### Arrêter le conteneur

```bash
docker stop mysql-odl
```

### Redémarrer le conteneur

```bash
docker start mysql-odl
```

### Supprimer le conteneur (⚠️ ATTENTION : supprime aussi les données si pas de volume)

```bash
docker rm -f mysql-odl
```

### Se connecter à MySQL depuis le conteneur

```bash
# Avec mot de passe "root"
docker exec -it mysql-odl mysql -u root -proot

# Avec mot de passe vide (Option 2)
docker exec -it mysql-odl mysql -u root
```

### Se connecter depuis votre machine (via client MySQL local)

```bash
# Si vous avez MySQL client installé
mysql -h 127.0.0.1 -P 3306 -u root -proot

# Ou depuis l'application, utilisez simplement localhost:3306
```

---

## ⚙️ Configuration de application.properties

### Si vous utilisez l'Option 1 (mot de passe "root")

Modifiez `odc_learning_api-master/src/main/resources/application.properties` :

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/odcawslearning?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=root
```

### Si vous utilisez l'Option 2 (mot de passe vide)

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/odcawslearning?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=
```

---

## 🔄 Workflow Complet

### 1. Lancer MySQL avec Docker

```bash
docker run -d \
  --name mysql-odl \
  -p 3306:3306 \
  -e MYSQL_ROOT_PASSWORD=root \
  -e MYSQL_DATABASE=odcawslearning \
  -v mysql-odl-data:/var/lib/mysql \
  --restart unless-stopped \
  mysql:8.0
```

### 2. Attendre quelques secondes que MySQL démarre

```bash
# Vérifier les logs pour voir "ready for connections"
docker logs mysql-odl
```

### 3. (Optionnel) Exécuter le script SQL de configuration

```bash
# Se connecter au conteneur
docker exec -i mysql-odl mysql -u root -proot < database/setup_database.sql

# Ou si vous avez MySQL client local
mysql -h 127.0.0.1 -P 3306 -u root -proot < database/setup_database.sql
```

**Note :** Si vous avez utilisé `-e MYSQL_DATABASE=odcawslearning` dans la commande docker run, la base existe déjà ! Vous pouvez juste exécuter la partie création d'utilisateur du script.

### 4. Lancer votre application Spring Boot

L'application se connectera automatiquement à MySQL et créera les tables via Hibernate.

---

## 💾 Gestion des Volumes Docker

### Lister les volumes

```bash
docker volume ls
```

Vous devriez voir `mysql-odl-data`.

### Inspecter le volume (voir où sont stockées les données)

```bash
docker volume inspect mysql-odl-data
```

### Supprimer le volume (⚠️ ATTENTION : supprime toutes les données)

```bash
docker volume rm mysql-odl-data
```

### Sauvegarder les données

```bash
# Créer un dump SQL
docker exec mysql-odl mysqldump -u root -proot odcawslearning > backup.sql

# Restaurer depuis un dump
docker exec -i mysql-odl mysql -u root -proot odcawslearning < backup.sql
```

---

## 🆘 Dépannage

### Erreur : "Port 3306 is already allocated"

Un autre service MySQL utilise déjà le port 3306. Solutions :

```bash
# Option 1 : Utiliser un autre port (ex: 3307)
docker run -d --name mysql-odl -p 3307:3306 -e MYSQL_ROOT_PASSWORD=root mysql:8.0

# Puis dans application.properties, changez le port :
# spring.datasource.url=jdbc:mysql://localhost:3307/odcawslearning...

# Option 2 : Arrêter le service MySQL local
# Windows : Services → MySQL → Arrêter
```

### Erreur : "Container name already exists"

```bash
# Supprimer l'ancien conteneur
docker rm -f mysql-odl

# Relancer la commande docker run
```

### Le conteneur s'arrête immédiatement

```bash
# Voir les logs pour comprendre l'erreur
docker logs mysql-odl

# Vérifier que Docker Desktop est bien démarré
```

### Connexion refusée depuis l'application

```bash
# Vérifier que le conteneur tourne
docker ps

# Vérifier les logs
docker logs mysql-odl

# Tester la connexion manuellement
docker exec -it mysql-odl mysql -u root -proot -e "SELECT 1;"
```

---

## 🎯 Avantages de Docker pour MySQL

✅ **Pas d'installation locale** : Pas besoin d'installer MySQL sur Windows  
✅ **Isolation** : MySQL tourne dans un conteneur séparé  
✅ **Facilité de gestion** : Start/Stop en une commande  
✅ **Persistance** : Les données sont sauvegardées dans un volume Docker  
✅ **Portabilité** : Même configuration sur tous les environnements  
✅ **Nettoyage facile** : Supprimer le conteneur = désinstaller MySQL  

---

*Guide créé pour le projet ODL - Orange Digital Center*

