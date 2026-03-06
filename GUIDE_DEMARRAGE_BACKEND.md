# 🚀 Guide de Démarrage - Backend Spring Boot ODL

## 📋 Prérequis

### 1. Vérification de la Version Java

Le projet nécessite **Java 11** (défini dans `pom.xml` ligne 18).

#### Commande de vérification :

```bash
java -version
```

**Explication :** Cette commande affiche la version de Java installée sur votre système. Exemple de sortie : `openjdk version "11.0.20"` ou `java version "11.0.20"`.

#### Compatibilité :

- ✅ **Java 11** : Compatible (requis)
- ✅ **Java 17** : Compatible (LTS recommandé)
- ✅ **Java 21** : Compatible (dernière LTS)
- ❌ **Java 8 ou inférieur** : Non compatible
- ❌ **Java 9 ou 10** : Non testé, non recommandé

#### Si Java n'est pas installé ou version incorrecte :

1. **Télécharger Java 11 LTS** : https://adoptium.net/temurin/releases/?version=11
2. **Ou Java 17 LTS** (recommandé) : https://adoptium.net/temurin/releases/?version=17
3. Installer et redémarrer le terminal
4. Vérifier avec `java -version`

---

## ⚙️ Configuration de l'Application

### Correction du Chemin d'Upload

Le fichier `application.properties` contient un chemin absolu spécifique à un autre système :

```properties
file.upload-dir=/Users/abdramanecisse/Desktop/odl/Image/ODLearning
spring.web.resources.static-locations=file:/Users/abdramanecisse/Desktop/odl/Image/ODLearning/
```

**Problème :** Ce chemin Unix (`/Users/...`) ne fonctionnera pas sur Windows.

### Solution : Utiliser le Dossier Existant du Projet

Vous avez déjà un dossier `Image/ODLearning` à la racine du projet. Voici les corrections à apporter :

#### Pour Windows :

Modifiez `src/main/resources/application.properties` lignes 5-6 :

```properties
# Chemin relatif depuis la racine du projet (recommandé)
file.upload-dir=../Image/ODLearning
spring.web.resources.static-locations=file:../Image/ODLearning/

# OU chemin absolu Windows (si vous préférez)
# file.upload-dir=D:\Mes projets\ODL\Image\ODLearning
# spring.web.resources.static-locations=file:/D:/Mes projets/ODL/Image/ODLearning/
```

**Explication :**
- `../Image/ODLearning` : Chemin relatif qui remonte d'un niveau depuis `src/main/resources` vers la racine, puis va dans `Image/ODLearning`
- Le chemin relatif est préférable car il fonctionne sur tous les systèmes
- Si vous utilisez un chemin absolu Windows, utilisez le format avec `/` (pas `\`) et préfixez avec `file:/`

#### Alternative : Créer le Dossier si Nécessaire

Si le dossier n'existe pas, créez-le :

```bash
# Depuis la racine du projet
mkdir -p Image/ODLearning
# Sur Windows PowerShell :
New-Item -ItemType Directory -Force -Path "Image\ODLearning"
```

### Correction du Fichier UploadLink.java (Optionnel mais Recommandé)

Le fichier `src/main/java/com/odc/aws_learning/app/constante/UploadLink.java` contient aussi un chemin hardcodé. Modifiez-le pour utiliser le même chemin :

```java
public static final String DOWNLOAD_LINK = "../Image/ODLearning";
// Ou pour Windows absolu :
// public static final String DOWNLOAD_LINK = "D:\\Mes projets\\ODL\\Image\\ODLearning";
```

**Note :** Ce fichier semble être utilisé pour certains téléchargements. Vérifiez s'il est encore utilisé dans le code.

---

## 🔨 Commandes Maven

### 1. Nettoyage du Projet

```bash
cd odc_learning_api-master
.\mvnw.cmd clean
```

**Explication :**
- `cd odc_learning_api-master` : Se place dans le dossier du projet backend
- `.\mvnw.cmd clean` : Utilise le Maven Wrapper (mvnw.cmd) pour exécuter la phase `clean`
- Supprime le dossier `target/` qui contient les fichiers compilés précédents
- Permet de repartir sur une base propre avant la compilation

**Sur Linux/Mac :**
```bash
./mvnw clean
```

### 2. Installation des Dépendances et Compilation

```bash
.\mvnw.cmd clean install
```

**Explication :**
- `clean install` : Combine deux phases Maven
  - `clean` : Nettoie les anciens fichiers compilés
  - `install` : Télécharge les dépendances depuis Maven Central, compile le code source, exécute les tests, et installe le JAR dans le repository local Maven
- Cette commande peut prendre plusieurs minutes la première fois (téléchargement des dépendances)
- Les dépendances sont mises en cache dans `~/.m2/repository/` pour les prochaines fois

**Sur Linux/Mac :**
```bash
./mvnw clean install
```

### 3. Lancement du Serveur

```bash
.\mvnw.cmd spring-boot:run
```

**Explication :**
- `spring-boot:run` : Utilise le plugin Spring Boot Maven pour compiler et lancer l'application directement
- Compile le code si nécessaire
- Lance le serveur embarqué Tomcat
- L'application sera accessible sur `http://localhost:8080/awsodclearning`

**Sur Linux/Mac :**
```bash
./mvnw spring-boot:run
```

---

## 🎯 Workflow Complet (Première Fois)

```bash
# 1. Vérifier Java (doit être 11+)
java -version

# 2. Aller dans le dossier backend
cd odc_learning_api-master

# 3. Corriger application.properties (voir section Configuration ci-dessus)
# Éditer src/main/resources/application.properties

# 4. Nettoyer et compiler
.\mvnw.cmd clean install

# 5. Lancer le serveur
.\mvnw.cmd spring-boot:run
```

---

## 🚀 Workflow Rapide (Démarrages Suivants)

```bash
# Seulement ces deux commandes suffisent
cd odc_learning_api-master
.\mvnw.cmd spring-boot:run
```

Le plugin Spring Boot recompilera automatiquement si nécessaire.

---

## ✅ Vérification du Démarrage

### 1. Vérifier que le serveur démarre correctement

Après `spring-boot:run`, vous devriez voir dans la console :

```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::        (v2.7.14)

...
Started AwsLearningApplication in X.XXX seconds
```

### 2. Tester l'API

Ouvrez votre navigateur ou utilisez curl :

```bash
# Vérifier que l'API répond
curl http://localhost:8080/awsodclearning/auth/check-availability

# Devrait retourner : "ATK Rest Api works fine"
```

Ou ouvrez dans le navigateur : **http://localhost:8080/awsodclearning/auth/check-availability**

### 3. Vérifier la Base de Données

Si vous voyez des erreurs de connexion à MySQL :
- Vérifiez que MySQL est démarré (Docker ou service local)
- Vérifiez les identifiants dans `application.properties` (lignes 13-15)
- Vérifiez que la base de données `odcawslearning` existe

---

## 🔧 Commandes Utiles

### Build du JAR Exécutable

```bash
.\mvnw.cmd clean package
```

**Explication :** Compile et crée un JAR exécutable dans `target/awsodclearning.jar`

### Lancer le JAR Compilé

```bash
java -jar target/awsodclearning.jar
```

**Explication :** Lance l'application depuis le JAR compilé (utile pour la production)

### Exécuter les Tests

```bash
.\mvnw.cmd test
```

**Explication :** Exécute tous les tests unitaires du projet

### Voir les Dépendances

```bash
.\mvnw.cmd dependency:tree
```

**Explication :** Affiche l'arbre complet des dépendances Maven

---

## 🆘 Dépannage

### Erreur : "JAVA_HOME is not set"

**Solution :**
```bash
# Windows PowerShell
$env:JAVA_HOME = "C:\Program Files\Java\jdk-11"
# Ou définir dans les variables d'environnement système
```

### Erreur : "mvnw: command not found" (Linux/Mac)

**Solution :**
```bash
# Rendre le script exécutable
chmod +x mvnw
```

### Erreur : "Port 8080 is already in use"

**Solution :**
- Trouver le processus : `netstat -ano | findstr :8080` (Windows)
- Arrêter le processus ou changer le port dans `application.properties` : `server.port=8081`

### Erreur : "Cannot connect to MySQL"

**Vérifications :**
1. MySQL est démarré ? (`docker ps` si Docker, ou vérifier le service)
2. Les identifiants dans `application.properties` sont corrects ?
3. La base de données `odcawslearning` existe ?
4. Le port MySQL est 3306 ?

### Erreur : "Failed to create upload directory"

**Solution :**
- Vérifiez que le chemin dans `application.properties` est correct
- Vérifiez les permissions d'écriture sur le dossier
- Créez le dossier manuellement si nécessaire

### Erreur de Compilation : "package does not exist"

**Solution :**
```bash
# Nettoyer et réinstaller
.\mvnw.cmd clean install -U
# -U force la mise à jour des dépendances
```

---

## 📝 Résumé des Commandes Essentielles

```bash
# 1. Vérifier Java
java -version

# 2. Aller dans le dossier backend
cd odc_learning_api-master

# 3. (Première fois) Nettoyer et compiler
.\mvnw.cmd clean install

# 4. Lancer le serveur
.\mvnw.cmd spring-boot:run

# 5. Accéder à l'API
# http://localhost:8080/awsodclearning
```

---

## 🎯 Configuration Finale Recommandée

### application.properties (lignes 5-6)

```properties
# Chemin relatif (fonctionne sur tous les systèmes)
file.upload-dir=../Image/ODLearning
spring.web.resources.static-locations=file:../Image/ODLearning/
```

### Vérification de la Base de Données

Assurez-vous que :
- MySQL est démarré (Docker ou service)
- La base `odcawslearning` existe
- Les identifiants dans `application.properties` sont corrects

---

*Guide créé pour le projet ODL - Orange Digital Center*

