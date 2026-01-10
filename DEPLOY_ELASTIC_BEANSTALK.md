# Guide de Déploiement sur AWS Elastic Beanstalk

Ce guide explique comment déployer le JAR du backend sur AWS Elastic Beanstalk.

## Prérequis

1. **AWS CLI installé et configuré**
   ```bash
   aws --version
   aws configure
   ```

2. **EB CLI installé** (Elastic Beanstalk CLI)
   ```bash
   pip install awsebcli
   eb --version
   ```

3. **JAR généré** : `target/awsodclearning.jar`

## Méthode 1 : Via la Console AWS (Interface Web)

### Étape 1 : Accéder à votre environnement
1. Connectez-vous à la console AWS
2. Allez dans **Elastic Beanstalk**
3. Sélectionnez votre environnement : `ODC-Learning-Backend-env`

### Étape 2 : Téléverser une nouvelle version
1. Cliquez sur **"Téléverser et déployer"** ou **"Upload and deploy"**
2. Cliquez sur **"Choisir un fichier"** ou **"Choose file"**
3. Sélectionnez votre fichier `awsodclearning.jar`
4. Entrez un **Label de version** (ex: `v1.0.0-2026-01-10`)
5. Cliquez sur **"Déployer"** ou **"Deploy"**

### Étape 3 : Attendre le déploiement
- Elastic Beanstalk va automatiquement :
  - Téléverser le JAR
  - Redémarrer l'application
  - Vérifier la santé de l'environnement

## Méthode 2 : Via AWS CLI

### Étape 1 : Créer une version d'application
```bash
cd Back/odl_back
aws elasticbeanstalk create-application-version \
  --application-name ODC-Learning-Backend \
  --version-label v1.0.0-$(date +%Y%m%d-%H%M%S) \
  --source-bundle S3Bucket=your-bucket-name,S3Key=awsodclearning.jar \
  --auto-create-application
```

**Note :** Vous devez d'abord téléverser le JAR sur S3.

### Étape 2 : Téléverser le JAR sur S3
```bash
# Créer un bucket S3 (si nécessaire)
aws s3 mb s3://odl-backend-jars

# Téléverser le JAR
aws s3 cp target/awsodclearning.jar s3://odl-backend-jars/
```

### Étape 3 : Déployer la version
```bash
aws elasticbeanstalk update-environment \
  --environment-name ODC-Learning-Backend-env \
  --version-label v1.0.0-20260110-120000
```

## Méthode 3 : Via EB CLI (Recommandé)

### Étape 1 : Initialiser EB CLI dans le projet
```bash
cd Back/odl_back
eb init
```

Répondez aux questions :
- **Select a region** : Choisissez votre région (ex: us-east-1)
- **Select an application to use** : Sélectionnez votre application existante ou créez-en une nouvelle
- **Do you want to set up SSH for your instances?** : (Optionnel) Oui/Non

### Étape 2 : Créer un fichier de configuration `.ebextensions`

Créez le dossier `.ebextensions` à la racine du projet :

```bash
mkdir .ebextensions
```

Créez le fichier `.ebextensions/01-java.config` :

```yaml
option_settings:
  aws:elasticbeanstalk:container:java:
    JVMOptions: "-Xmx512m -Xms256m"
  aws:elasticbeanstalk:application:environment:
    SPRING_PROFILES_ACTIVE: "production"
    SERVER_PORT: 5000
```

### Étape 3 : Créer un fichier `Procfile` (si nécessaire)

Pour Elastic Beanstalk Java, le fichier `Procfile` n'est généralement pas nécessaire car EB détecte automatiquement les applications Java. Cependant, si vous avez besoin de spécifier des options JVM :

Créez `Procfile` à la racine :
```
web: java -jar awsodclearning.jar --server.port=5000
```

### Étape 4 : Déployer
```bash
# Déployer sur l'environnement existant
eb deploy ODC-Learning-Backend-env

# Ou simplement (si vous avez déjà configuré l'environnement par défaut)
eb deploy
```

### Étape 5 : Vérifier le statut
```bash
eb status
eb health
```

## Méthode 4 : Déploiement direct avec EB CLI (Plus simple)

### Étape 1 : Se connecter à votre environnement
```bash
cd Back/odl_back
eb use ODC-Learning-Backend-env
```

### Étape 2 : Déployer directement le JAR
```bash
eb deploy --source target/awsodclearning.jar
```

## Configuration des Variables d'Environnement

### Via la Console AWS
1. Allez dans votre environnement Elastic Beanstalk
2. Cliquez sur **Configuration** → **Software**
3. Dans **Environment properties**, ajoutez :
   - `SPRING_DATASOURCE_URL` : URL de votre base de données RDS
   - `SPRING_DATASOURCE_USERNAME` : Nom d'utilisateur de la base de données
   - `SPRING_DATASOURCE_PASSWORD` : Mot de passe de la base de données
   - `AWS_ACCESS_KEY_ID` : (Si nécessaire pour S3)
   - `AWS_SECRET_ACCESS_KEY` : (Si nécessaire pour S3)
   - `AWS_REGION` : Région AWS (ex: us-east-1)
   - `AWS_S3_BUCKET` : Nom du bucket S3

### Via EB CLI
Créez `.ebextensions/environment.config` :

```yaml
option_settings:
  aws:elasticbeanstalk:application:environment:
    SPRING_DATASOURCE_URL: "jdbc:mysql://your-rds-endpoint:3306/your-database"
    SPRING_DATASOURCE_USERNAME: "your-username"
    SPRING_DATASOURCE_PASSWORD: "your-password"
    AWS_REGION: "us-east-1"
    AWS_S3_BUCKET: "your-bucket-name"
```

## Vérification du Déploiement

### Vérifier les logs
```bash
eb logs
```

### Ouvrir l'application dans le navigateur
```bash
eb open
```

### Vérifier la santé
```bash
eb health --refresh
```

## Commandes Utiles EB CLI

```bash
# Lister les environnements
eb list

# Voir les informations de l'environnement
eb status

# Voir les logs en temps réel
eb logs --stream

# Redémarrer l'environnement
eb restart

# Ouvrir la console dans le navigateur
eb console

# Terminer l'environnement (ATTENTION : supprime l'environnement)
eb terminate
```

## Dépannage

### Problème : L'application ne démarre pas
1. Vérifiez les logs : `eb logs`
2. Vérifiez les variables d'environnement
3. Vérifiez que le port est correctement configuré (par défaut 5000 pour Elastic Beanstalk)

### Problème : Erreur de connexion à la base de données
1. Vérifiez que votre RDS est dans le même VPC que votre environnement EB
2. Vérifiez les Security Groups
3. Vérifiez les variables d'environnement de la base de données

### Problème : Erreur S3
1. Vérifiez les credentials AWS dans les variables d'environnement
2. Vérifiez que le bucket S3 existe et est accessible
3. Vérifiez les permissions IAM

## Structure Recommandée du Projet

```
Back/odl_back/
├── .ebextensions/
│   ├── 01-java.config
│   └── environment.config
├── Procfile (optionnel)
├── target/
│   └── awsodclearning.jar
└── pom.xml
```

## Notes Importantes

1. **Port** : Elastic Beanstalk utilise le port 5000 par défaut. Assurez-vous que votre application Spring Boot écoute sur ce port ou configurez-le via les variables d'environnement.

2. **Base de données** : Si vous utilisez RDS, assurez-vous que :
   - RDS et Elastic Beanstalk sont dans le même VPC
   - Les Security Groups permettent la communication
   - L'URL de connexion est correcte

3. **S3** : Pour les uploads de fichiers, configurez correctement les credentials AWS dans les variables d'environnement.

4. **Health Checks** : Elastic Beanstalk vérifie automatiquement la santé de votre application. Assurez-vous que votre endpoint de health check répond correctement (généralement `/actuator/health` pour Spring Boot).
