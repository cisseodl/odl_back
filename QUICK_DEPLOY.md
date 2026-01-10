# Déploiement Rapide sur Elastic Beanstalk

## Méthode la Plus Simple (Console AWS)

### 1. Préparer le JAR
```bash
cd Back/odl_back
mvn clean package -DskipTests
```

### 2. Aller sur la Console AWS
1. Ouvrez https://console.aws.amazon.com/elasticbeanstalk
2. Sélectionnez votre environnement : **ODC-Learning-Backend-env**
3. Cliquez sur **"Téléverser et déployer"** (ou "Upload and deploy")

### 3. Téléverser le JAR
1. Cliquez sur **"Choisir un fichier"**
2. Naviguez vers : `Back/odl_back/target/awsodclearning.jar`
3. Entrez un **Label de version** : `v1.0.0-2026-01-10`
4. Cliquez sur **"Déployer"**

### 4. Attendre le déploiement
- Le déploiement prend généralement 3-5 minutes
- Surveillez les logs pour voir la progression

### 5. Vérifier la configuration
Après le déploiement, allez dans **Configuration** → **Software** et configurez les variables d'environnement :

#### Variables d'Environnement Requises :

```
SPRING_DATASOURCE_URL=jdbc:mysql://votre-rds-endpoint:3306/odcawslearning?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
SPRING_DATASOURCE_USERNAME=votre-username
SPRING_DATASOURCE_PASSWORD=votre-password
AWS_ACCESS_KEY=votre-access-key
AWS_SECRET_KEY=votre-secret-key
AWS_REGION=us-east-1
AWS_BUCKET_NAME=odc-learning-bucket
SERVER_PORT=5000
SERVER_SERVLET_CONTEXT_PATH=/awsodclearning
```

### 6. Redémarrer l'environnement
Après avoir configuré les variables, redémarrez l'environnement :
- Cliquez sur **Actions** → **Redémarrer l'environnement**

## Vérification

### Vérifier que l'application fonctionne
1. Cliquez sur l'URL de votre environnement (visible dans le dashboard)
2. Testez l'endpoint : `https://votre-url.elasticbeanstalk.com/awsodclearning/api/health` (si vous avez un endpoint de health check)

### Vérifier les logs
1. Allez dans **Journaux** (Logs)
2. Cliquez sur **Demander les journaux** pour voir les logs récents
3. Vérifiez qu'il n'y a pas d'erreurs

## Problèmes Courants

### L'application ne démarre pas
- Vérifiez les logs pour voir l'erreur exacte
- Vérifiez que toutes les variables d'environnement sont correctement configurées
- Vérifiez que votre base de données RDS est accessible depuis Elastic Beanstalk

### Erreur de connexion à la base de données
- Vérifiez que RDS et Elastic Beanstalk sont dans le même VPC
- Vérifiez les Security Groups
- Vérifiez l'URL de connexion dans `SPRING_DATASOURCE_URL`

### Erreur S3
- Vérifiez que les credentials AWS sont corrects
- Vérifiez que le bucket S3 existe et est accessible
- Vérifiez les permissions IAM

## Note Importante sur le Port

Elastic Beanstalk utilise le port **5000** par défaut. Votre application Spring Boot doit écouter sur ce port. Le fichier `.ebextensions/01-java.config` configure cela automatiquement.

Si vous avez besoin de changer le port, modifiez la variable d'environnement `SERVER_PORT` dans la configuration de l'environnement.
