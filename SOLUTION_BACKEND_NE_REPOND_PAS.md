# 🔧 Solution : Backend ne répond pas sur Elastic Beanstalk

## 🔍 Diagnostic du Problème

D'après les logs Elastic Beanstalk, le problème est clair :

### ❌ Symptômes
1. **Nginx erreur 502** : `connect() failed (111: Connection refused) while connecting to upstream, upstream: "http://127.0.0.1:5000/"`
2. **Le backend démarre** : On voit des requêtes Hibernate dans `/var/log/web.stdout.log`
3. **Mais le backend ne répond pas** : Nginx ne peut pas se connecter au port 5000

### 🔍 Causes Possibles

1. **L'application crash après le démarrage** (le plus probable)
   - Le `CommandLineRunner` s'exécute (on voit les requêtes Hibernate)
   - Mais l'application crash avant de démarrer complètement le serveur Tomcat
   - Résultat : Nginx ne peut pas se connecter

2. **Erreur dans le CommandLineRunner**
   - Si une exception non gérée se produit dans `run()`, elle peut faire crasher l'application
   - Les logs montrent des requêtes Hibernate, mais pas de message de succès

3. **Problème de mémoire**
   - L'application peut manquer de mémoire et être tuée par le système

## ✅ Solutions

### Solution 1 : Vérifier les Logs Complets (PRIORITAIRE)

Dans Elastic Beanstalk, allez dans **Logs** → **Request Logs** → **Full Logs** et cherchez :

1. **Messages de démarrage Spring Boot** :
   ```
   === DÉMARRAGE DE L'APPLICATION ===
   Started AwsLearningApplication in X seconds
   ```

2. **Erreurs après le démarrage** :
   - Exceptions Java
   - Erreurs de connexion à la base
   - Erreurs lors de la création de l'admin

3. **Messages du CommandLineRunner** :
   ```
   === EXÉCUTION DU CommandLineRunner ===
   === CommandLineRunner TERMINÉ AVEC SUCCÈS ===
   ```

### Solution 2 : Vérifier que l'Application Démarre Complètement

Les logs doivent montrer :
```
Started AwsLearningApplication in X.XXX seconds (JVM running for X.XXX)
```

Si ce message n'apparaît pas, l'application crash avant de démarrer complètement.

### Solution 3 : Vérifier les Variables d'Environnement

Dans Elastic Beanstalk :
1. Allez dans **Configuration** → **Software**
2. Vérifiez que `PORT` est défini à `5000` (ou laissez-le vide pour utiliser la valeur par défaut)
3. Vérifiez que `RDS_PASSWORD` est défini

### Solution 4 : Redémarrer l'Environnement

1. Allez dans **Actions** → **Restart Environment**
2. Attendez que le redémarrage soit terminé
3. Vérifiez les logs pour voir si l'application démarre complètement

### Solution 5 : Vérifier le Procfile

Le `Procfile` doit être :
```
web: java -jar awsodclearning.jar --server.port=5000 --server.servlet.context-path=/awsodclearning
```

Assurez-vous que :
- Le fichier JAR s'appelle bien `awsodclearning.jar`
- Le port est bien `5000`
- Le context-path est bien `/awsodclearning`

## 🚨 Actions Immédiates

### 1. Vérifier les Logs Complets

Dans CloudWatch Logs, cherchez le groupe :
```
/aws/elasticbeanstalk/[votre-environnement]/var/log/web.stdout.log
```

Recherchez :
- `Started AwsLearningApplication` → Si présent, l'application démarre
- `Exception` ou `Error` → Si présent, c'est la cause du crash
- `=== DÉMARRAGE DE L'APPLICATION ===` → Si présent, nos nouveaux logs fonctionnent

### 2. Vérifier le Statut de l'Application

Dans Elastic Beanstalk :
1. Allez dans **Monitoring** → **Health**
2. Vérifiez le statut de l'environnement
3. Si le statut est "Degraded" ou "Severe", il y a un problème

### 3. Vérifier les Métriques

Dans Elastic Beanstalk :
1. Allez dans **Monitoring** → **Metrics**
2. Vérifiez :
   - **CPU Utilization** : Si > 90%, l'application peut être tuée
   - **Memory Utilization** : Si > 90%, l'application peut être tuée
   - **Request Count** : Si 0, l'application ne reçoit pas de requêtes

## 📝 Notes Importantes

1. **Les logs Hibernate dans web.stdout.log** indiquent que l'application démarre, mais pas qu'elle reste en vie
2. **L'erreur 502 de Nginx** indique que le backend ne répond pas, même s'il a démarré
3. **Les nouveaux logs** que nous avons ajoutés (`=== DÉMARRAGE DE L'APPLICATION ===`) permettront de voir exactement où l'application crash

## 🔄 Prochaines Étapes

1. **Redéployez l'application** avec les nouveaux logs
2. **Vérifiez les logs complets** dans CloudWatch
3. **Cherchez les messages de démarrage** que nous avons ajoutés
4. **Identifiez l'erreur exacte** qui fait crasher l'application
