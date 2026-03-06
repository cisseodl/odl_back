# 🗄️ Configuration de la Base de Données MySQL - ODL

## 📋 Informations de Configuration

D'après le fichier `application.properties`, la configuration actuelle est :

- **Nom de la base de données** : `odcawslearning`
- **Port MySQL** : `3306` (port par défaut)
- **Utilisateur** : `root`
- **Mot de passe** : (vide)
- **Hôte** : `localhost`

## 🚀 Méthodes d'Exécution du Script SQL

### Méthode 1 : Via Terminal/Invite de Commande MySQL

#### Sur Windows (PowerShell ou CMD)

```powershell
# Se connecter à MySQL (remplacez 'root' par votre utilisateur si différent)
mysql -u root -p

# Une fois connecté, exécuter le script
source D:\Mes projets\ODL\odc_learning_api-master\database\setup_database.sql

# Ou directement depuis le terminal (sans se connecter d'abord)
mysql -u root -p < "D:\Mes projets\ODL\odc_learning_api-master\database\setup_database.sql"
```

#### Sur Linux/Mac

```bash
# Se connecter à MySQL
mysql -u root -p

# Une fois connecté, exécuter le script
source /chemin/vers/odc_learning_api-master/database/setup_database.sql

# Ou directement depuis le terminal
mysql -u root -p < /chemin/vers/odc_learning_api-master/database/setup_database.sql
```

**Note** : Le `-p` vous demandera de saisir votre mot de passe. Si root n'a pas de mot de passe, utilisez simplement `mysql -u root`.

---

### Méthode 2 : Via MySQL Workbench

1. **Ouvrir MySQL Workbench**
2. **Se connecter** à votre instance MySQL locale
3. **Ouvrir le script** :
   - Menu : `File` → `Open SQL Script`
   - Naviguer vers : `odc_learning_api-master/database/setup_database.sql`
4. **Exécuter le script** :
   - Cliquer sur l'icône ⚡ (Execute) ou appuyer sur `Ctrl+Shift+Enter`
   - Ou sélectionner tout le contenu et exécuter

---

### Méthode 3 : Via DBeaver

1. **Ouvrir DBeaver**
2. **Se connecter** à votre base de données MySQL
3. **Ouvrir le script** :
   - Menu : `File` → `Open File`
   - Naviguer vers : `odc_learning_api-master/database/setup_database.sql`
4. **Exécuter le script** :
   - Cliquer sur l'icône ▶️ (Execute SQL Script) ou appuyer sur `Ctrl+Enter`
   - Ou sélectionner tout le contenu et exécuter

---

### Méthode 4 : Via phpMyAdmin (si installé)

1. **Ouvrir phpMyAdmin** dans votre navigateur (généralement `http://localhost/phpmyadmin`)
2. **Se connecter** avec vos identifiants MySQL
3. **Onglet SQL** :
   - Cliquer sur l'onglet "SQL" en haut
   - Copier-coller le contenu du fichier `setup_database.sql`
   - Cliquer sur "Exécuter"

---

## ⚙️ Configuration Après Création

### Option A : Utiliser l'utilisateur root (Configuration actuelle)

Si vous gardez l'utilisateur `root` avec un mot de passe vide, aucune modification n'est nécessaire dans `application.properties`.

Si `root` a un mot de passe, modifiez `application.properties` :

```properties
spring.datasource.username=root
spring.datasource.password=VotreMotDePasseRoot
```

### Option B : Utiliser un utilisateur dédié (RECOMMANDÉ pour la sécurité)

1. **Modifiez le script SQL** : Dans `setup_database.sql`, remplacez `'VotreMotDePasseSecurise123!'` par votre mot de passe.

2. **Modifiez `application.properties`** :

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/odcawslearning?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC
spring.datasource.username=odc_learning_user
spring.datasource.password=VotreMotDePasseSecurise123!
```

---

## ✅ Vérification

Après l'exécution du script, vérifiez que tout fonctionne :

### 1. Vérifier que la base existe

```sql
SHOW DATABASES LIKE 'odcawslearning';
```

### 2. Vérifier les privilèges de l'utilisateur

```sql
SHOW GRANTS FOR 'odc_learning_user'@'localhost';
-- Ou pour root :
SHOW GRANTS FOR 'root'@'localhost';
```

### 3. Tester la connexion depuis l'application

Lancez le backend Spring Boot. Si tout est correct :
- L'application se connectera à la base de données
- Hibernate créera automatiquement les tables (grâce à `ddl-auto=update`)
- Vous verrez les logs de création des tables dans la console

---

## 🔒 Sécurité

**⚠️ IMPORTANT** : 

- Ne commitez JAMAIS le fichier `application.properties` avec des mots de passe en production
- Utilisez des variables d'environnement ou un fichier de configuration externe pour les mots de passe
- En production, créez toujours un utilisateur dédié avec des privilèges limités (pas `ALL PRIVILEGES`)

---

## 🆘 Dépannage

### Erreur : "Access denied for user"

- Vérifiez que l'utilisateur existe : `SELECT user, host FROM mysql.user;`
- Vérifiez les privilèges : `SHOW GRANTS FOR 'utilisateur'@'localhost';`
- Réexécutez `FLUSH PRIVILEGES;` après avoir modifié les privilèges

### Erreur : "Unknown database"

- Vérifiez que la base existe : `SHOW DATABASES;`
- Vérifiez le nom dans `application.properties` (doit être exactement `odcawslearning`)

### Erreur : "Can't connect to MySQL server"

- Vérifiez que le service MySQL est démarré
- Vérifiez le port (3306 par défaut)
- Vérifiez que MySQL écoute sur localhost

---

*Document créé pour le projet ODL - Orange Digital Center*

