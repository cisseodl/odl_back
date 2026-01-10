# Correction de la Configuration S3

## Problème Identifié

L'application démarrait mais échouait lors de l'initialisation du client S3 avec l'erreur :
```
at com.amazonaws.util.VersionInfoUtils.<clinit>(VersionInfoUtils.java:59)
at com.odc.aws_learning.app.config.S3Config.amazonS3(S3Config.java:26)
```

Cela causait :
- Un crash silencieux de l'application après le démarrage initial
- Des erreurs Nginx : `connect() failed (111: Connection refused) while connecting to upstream`
- L'application n'était plus accessible

## Solution Appliquée

La configuration S3 a été améliorée pour :

1. **Utiliser les credentials IAM d'Elastic Beanstalk** : 
   - Si les credentials statiques ne sont pas fournis ou sont "changeMe", le système utilise automatiquement les credentials IAM de l'instance EC2 (via `DefaultAWSCredentialsProviderChain`)
   - C'est la méthode recommandée pour Elastic Beanstalk

2. **Gestion d'erreur robuste** :
   - Ajout de try-catch pour éviter que l'application crash au démarrage
   - Logs détaillés pour le débogage

3. **Configuration flexible** :
   - Les credentials statiques sont optionnels
   - Valeurs par défaut pour la région (us-east-1)

## Configuration Recommandée sur Elastic Beanstalk

### Option 1 : Utiliser IAM Role (Recommandé)

1. **Attacher un IAM Role à l'environnement Elastic Beanstalk** :
   - Allez dans la console AWS → Elastic Beanstalk → Configuration → Security
   - Créez ou sélectionnez un IAM Role avec les permissions S3 suivantes :
     ```json
     {
       "Version": "2012-10-17",
       "Statement": [
         {
           "Effect": "Allow",
           "Action": [
             "s3:PutObject",
             "s3:GetObject",
             "s3:DeleteObject",
             "s3:ListBucket"
           ],
           "Resource": [
             "arn:aws:s3:::odc-learning-bucket",
             "arn:aws:s3:::odc-learning-bucket/*"
           ]
         }
       ]
     }
     ```

2. **Ne pas définir AWS_ACCESS_KEY et AWS_SECRET_KEY** dans les variables d'environnement
   - L'application utilisera automatiquement les credentials IAM

### Option 2 : Utiliser des Credentials Statiques

Si vous préférez utiliser des credentials statiques, configurez-les dans les variables d'environnement Elastic Beanstalk :

```
AWS_ACCESS_KEY=votre-access-key
AWS_SECRET_KEY=votre-secret-key
AWS_REGION=us-east-1
AWS_BUCKET_NAME=odc-learning-bucket
```

## Redéploiement

1. Le nouveau JAR a été compilé avec Java 11 : `target/awsodclearning.jar`
2. Téléchargez ce JAR et redéployez-le sur Elastic Beanstalk
3. Vérifiez les logs après le redéploiement pour confirmer :
   - `Client S3 initialisé avec succès pour la région: us-east-1`
   - `Started AwsLearningApplication in XX seconds`

## Vérification

Après le redéploiement, vérifiez que :
- L'application démarre sans erreur S3
- Les logs montrent "Client S3 initialisé avec succès"
- Nginx peut se connecter à l'application (pas d'erreur 502)
- L'application répond aux requêtes HTTP

## Notes

- Les logs montreront quel type de credentials est utilisé
- Si vous voyez "Utilisation des credentials IAM", c'est que l'IAM Role est correctement configuré
- Si vous voyez "Utilisation des credentials AWS statiques", les variables d'environnement sont utilisées
