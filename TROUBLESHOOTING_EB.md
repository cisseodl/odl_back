# Guide de Dépannage - Elastic Beanstalk

## Problème : Environnement "Degraded" après déploiement

Si votre environnement affiche un statut "Degraded" (Dégradé) avec des problèmes de santé, suivez ces étapes :

## Étape 1 : Vérifier les Logs

### Via la Console AWS
1. Allez dans votre environnement Elastic Beanstalk
2. Cliquez sur **"Journaux"** (Logs) dans le menu de gauche
3. Cliquez sur **"Demander les journaux"** (Request logs)
4. Sélectionnez **"Last 100 lines"** ou **"Last 24 hours"**
5. Téléchargez et examinez les logs

### Via EB CLI
```bash
cd Back/odl_back
eb logs
```

## Étape 2 : Vérifier les Variables d'Environnement

Allez dans **Configuration** → **Software** et vérifiez que toutes ces variables sont configurées :

### Variables Requises

```
SPRING_DATASOURCE_URL=jdbc:mysql://votre-rds-endpoint:3306/odcawslearning?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&useUnicode=true&characterEncoding=UTF-8&connectTimeout=60000&socketTimeout=60000
SPRING_DATASOURCE_USERNAME=votre-username
SPRING_DATASOURCE_PASSWORD=votre-password
AWS_ACCESS_KEY=votre-access-key
AWS_SECRET_KEY=votre-secret-key
AWS_REGION=us-east-1
AWS_BUCKET_NAME=odc-learning-bucket
SERVER_PORT=5000
SERVER_SERVLET_CONTEXT_PATH=/awsodclearning
SPRING_PROFILES_ACTIVE=production
```

**Important :** Remplacez toutes les valeurs par vos vraies valeurs !

## Étape 3 : Problèmes Courants et Solutions

### Problème 1 : Erreur de Connexion à la Base de Données

**Symptômes dans les logs :**
```
Cannot connect to database
Connection refused
Access denied for user
```

**Solutions :**
1. **Vérifier que RDS et Elastic Beanstalk sont dans le même VPC**
   - Allez dans **Configuration** → **Instances**
   - Notez le VPC ID
   - Vérifiez que votre instance RDS est dans le même VPC

2. **Vérifier les Security Groups**
   - Allez dans **EC2** → **Security Groups**
   - Trouvez le Security Group de votre instance RDS
   - Ajoutez une règle entrante :
     - Type : MySQL/Aurora (port 3306)
     - Source : Le Security Group de votre environnement Elastic Beanstalk

3. **Vérifier l'URL de connexion**
   - Assurez-vous que `SPRING_DATASOURCE_URL` contient l'endpoint correct de RDS
   - Format : `jdbc:mysql://endpoint-rds.region.rds.amazonaws.com:3306/odcawslearning?...`

### Problème 2 : Application ne démarre pas (Port 5000)

**Symptômes dans les logs :**
```
Port already in use
Failed to start application
```

**Solutions :**
1. Vérifiez que `SERVER_PORT=5000` est configuré dans les variables d'environnement
2. Vérifiez que le fichier `.ebextensions/01-java.config` est présent
3. Redémarrez l'environnement après avoir configuré le port

### Problème 3 : Erreur S3 (InvalidAccessKeyId)

**Symptômes dans les logs :**
```
InvalidAccessKeyId
The AWS Access Key Id you provided does not exist
```

**Solutions :**
1. Vérifiez que `AWS_ACCESS_KEY` et `AWS_SECRET_KEY` sont correctement configurés
2. Vérifiez que les credentials ont les permissions nécessaires pour S3
3. Vérifiez que le bucket S3 existe et est accessible

### Problème 4 : Erreur de Health Check

**Symptômes :**
- L'environnement reste en "Degraded"
- Health check échoue

**Solutions :**
1. Vérifiez que votre application répond sur le port 5000
2. Testez l'endpoint de health check :
   ```
   https://votre-url.elasticbeanstalk.com/awsodclearning/actuator/health
   ```
3. Si vous n'avez pas d'endpoint de health check, ajoutez-en un dans votre application Spring Boot

### Problème 5 : Erreur de Mémoire (OutOfMemoryError)

**Symptômes dans les logs :**
```
OutOfMemoryError
Java heap space
```

**Solutions :**
1. Augmentez la mémoire allouée dans `.ebextensions/01-java.config` :
   ```yaml
   JVMOptions: "-Xmx1024m -Xms512m"
   ```
2. Redéployez l'application

## Étape 4 : Vérifier la Configuration de la Plateforme

1. Allez dans **Configuration** → **Software**
2. Vérifiez que la plateforme est correcte : **Corretto 11 running on 64bit Amazon Linux 2/3.10.1**

## Étape 5 : Redémarrer l'Environnement

Après avoir corrigé les variables d'environnement :

1. Allez dans **Actions** → **Redémarrer l'environnement**
2. Attendez que l'environnement redémarre (2-3 minutes)
3. Vérifiez le statut de santé

## Étape 6 : Vérifier les Logs en Temps Réel

### Via la Console AWS
1. Allez dans **Journaux** → **Demander les journaux**
2. Sélectionnez **"Last 100 lines"**
3. Téléchargez et examinez les logs

### Via EB CLI
```bash
eb logs --stream
```

## Commandes Utiles pour le Diagnostic

```bash
# Voir le statut de l'environnement
eb status

# Voir les logs
eb logs

# Voir les logs en temps réel
eb logs --stream

# Voir les événements récents
eb events

# Ouvrir la console dans le navigateur
eb console

# Voir la configuration
eb config
```

## Vérification Rapide

### Checklist de Vérification

- [ ] Variables d'environnement configurées (base de données, AWS credentials)
- [ ] RDS et Elastic Beanstalk dans le même VPC
- [ ] Security Groups configurés correctement
- [ ] Port 5000 configuré
- [ ] Logs examinés pour identifier l'erreur exacte
- [ ] Application redémarrée après modification des variables

## Test de l'Application

Une fois que l'environnement est en état "OK" :

1. Récupérez l'URL de votre environnement (visible dans le dashboard)
2. Testez l'endpoint :
   ```
   https://votre-url.elasticbeanstalk.com/awsodclearning/api/...
   ```

## Support Supplémentaire

Si le problème persiste :

1. **Examinez les logs détaillés** pour identifier l'erreur exacte
2. **Vérifiez les événements** dans la console Elastic Beanstalk
3. **Vérifiez les métriques CloudWatch** pour voir les ressources utilisées
4. **Consultez la documentation AWS** pour les erreurs spécifiques

## Exemple de Logs à Rechercher

### Logs de Démarrage Normaux
```
Starting application...
Application started successfully
Listening on port 5000
```

### Logs d'Erreur à Identifier
```
ERROR: Cannot connect to database
ERROR: Port 5000 already in use
ERROR: InvalidAccessKeyId
ERROR: OutOfMemoryError
```
