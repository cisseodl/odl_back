# 📋 Guide pour Accéder aux Logs Elastic Beanstalk

## 🔍 Méthode 1 : Via la Console AWS Elastic Beanstalk

1. **Connectez-vous à la Console AWS** : https://console.aws.amazon.com
2. **Allez dans Elastic Beanstalk** : Recherchez "Elastic Beanstalk" dans la barre de recherche
3. **Sélectionnez votre environnement** : Cliquez sur le nom de votre environnement (ex: `odl-backend-env`)
4. **Accédez aux Logs** :
   - Dans le menu de gauche, cliquez sur **"Logs"**
   - Cliquez sur **"Request Logs"** → **"Last 100 Lines"** pour voir les dernières 100 lignes
   - OU cliquez sur **"Request Logs"** → **"Full Logs"** pour télécharger tous les logs
   - OU cliquez sur **"Last 100 Lines"** pour voir les logs en temps réel

## 🔍 Méthode 2 : Via CloudWatch Logs

1. **Connectez-vous à la Console AWS**
2. **Allez dans CloudWatch** : Recherchez "CloudWatch" dans la barre de recherche
3. **Accédez aux Logs** :
   - Dans le menu de gauche, cliquez sur **"Logs"** → **"Log groups"**
   - Cherchez le groupe de logs de votre environnement (ex: `/aws/elasticbeanstalk/odl-backend-env/var/log/eb-engine.log`)
   - Cliquez sur le groupe de logs
   - Sélectionnez le stream de logs le plus récent
   - Les logs s'affichent avec la date et l'heure

## 🔍 Méthode 3 : Via AWS CLI

```bash
# Installer AWS CLI si ce n'est pas déjà fait
# Windows: https://aws.amazon.com/cli/
# Mac: brew install awscli
# Linux: sudo apt-get install awscli

# Configurer vos credentials AWS
aws configure

# Télécharger les logs Elastic Beanstalk
aws elasticbeanstalk request-environment-info \
    --environment-name odl-backend-env \
    --info-type tail

# Récupérer les logs
aws elasticbeanstalk retrieve-environment-info \
    --environment-name odl-backend-env \
    --info-type tail
```

## 📊 Logs Importants à Surveiller

### Logs de l'Application Spring Boot
- **Fichier** : `/var/log/eb-engine.log` ou `/var/log/web.stdout.log`
- **Contenu** : Tous les logs de l'application Spring Boot, y compris :
  - Les logs de démarrage
  - Les logs de transactions (`=== DÉBUT INSCRIPTION AU COURS ===`)
  - Les erreurs (`❌❌❌ ERREUR LORS DE L'INSCRIPTION AU COURS ❌❌❌`)

### Logs Nginx
- **Fichier** : `/var/log/nginx/access.log` et `/var/log/nginx/error.log`
- **Contenu** : Requêtes HTTP et erreurs Nginx

### Logs du Système
- **Fichier** : `/var/log/messages` ou `/var/log/syslog`
- **Contenu** : Logs système Linux

## 🔍 Recherche dans les Logs

### Rechercher les Erreurs d'Inscription
Dans CloudWatch Logs, utilisez la fonction de recherche avec :
```
=== DÉBUT INSCRIPTION AU COURS ===
```

### Rechercher les Erreurs de Transaction
```
Transaction silently rolled back
```

### Rechercher les Erreurs Générales
```
❌❌❌ ERREUR
```

## ⚙️ Configuration des Logs dans l'Application

Les logs sont maintenant configurés pour apparaître dans CloudWatch via :
- **logback-spring.xml** : Configuration des appenders et niveaux de logs
- **application.properties** : Configuration des niveaux de logs par package
- **Logger SLF4J** : Remplacement de tous les `System.out.println` par des loggers

## 📝 Format des Logs

Les logs suivent le format :
```
2024-02-11 11:49:46.123 [http-nio-5000-exec-1] INFO  c.o.a.a.s.CourseService - === DÉBUT INSCRIPTION AU COURS ===
```

Où :
- `2024-02-11 11:49:46.123` : Date et heure
- `[http-nio-5000-exec-1]` : Thread
- `INFO` : Niveau de log
- `c.o.a.a.s.CourseService` : Classe
- `=== DÉBUT INSCRIPTION AU COURS ===` : Message

## 🚨 En Cas de Problème

Si vous ne voyez toujours pas les logs :
1. **Vérifiez que l'application est déployée** : Allez dans Elastic Beanstalk → Environment → Health
2. **Vérifiez les permissions IAM** : Assurez-vous que votre rôle IAM a les permissions CloudWatchLogs
3. **Vérifiez la configuration des logs** : Assurez-vous que `logback-spring.xml` est dans `src/main/resources`
4. **Redéployez l'application** : Parfois, un redéploiement est nécessaire pour activer les nouveaux logs
