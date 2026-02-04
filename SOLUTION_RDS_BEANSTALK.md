# 🔧 Solution pour Recréer l'Admin après Vidage de la Base RDS

## 🎯 Situation Actuelle

- ✅ **Backend déployé** : AWS Elastic Beanstalk
- ✅ **Base de données** : AWS RDS MySQL
- ✅ **Frontend déployé** : AWS Amplify
- ✅ **DNS configurés** : `smart-odc.com` et `admin-smart-odc.com`
- ❌ **Problème** : Base de données vidée, utilisateur admin supprimé

## 🔍 Diagnostic

L'erreur **"Failed to fetch"** vient du fait que :
1. La base de données a été vidée
2. L'utilisateur admin n'existe plus
3. Le backend ne peut pas créer l'admin automatiquement car il est déjà démarré

## ✅ Solution Étape par Étape

### Étape 1: Se Connecter à la Base RDS depuis DBeaver

1. **Ouvrez DBeaver**
2. **Créez une nouvelle connexion MySQL** :
   - **Host**: `awseb-e-rafruf9ypt-stack-awsebrdsdatabase-rfrjq9mjmb0m.ck5a2240egow.us-east-1.rds.amazonaws.com`
   - **Port**: `3306`
   - **Database**: `odcawslearning`
   - **Username**: `root` (ou celui configuré dans RDS)
   - **Password**: (récupérez-le depuis les variables d'environnement Elastic Beanstalk)

3. **Testez la connexion** et connectez-vous

### Étape 2: Exécuter le Script SQL pour Créer l'Admin

1. **Ouvrez le script SQL** : `database/create_admin_rds.sql`
2. **Copiez tout le contenu** du script
3. **Collez-le dans DBeaver** (éditeur SQL)
4. **Exécutez le script** (Ctrl+Alt+X ou bouton "Execute SQL Script")

Le script va :
- ✅ Créer l'utilisateur `cisseodl@gmail.com`
- ✅ Créer l'entité Admin liée
- ✅ Configurer les relations correctement

### Étape 3: Redémarrer le Backend sur Elastic Beanstalk

**IMPORTANT** : Après avoir créé l'utilisateur, vous devez redémarrer le backend pour que le CommandLineRunner mette à jour le hash du mot de passe.

#### Option A: Via la Console AWS

1. Allez sur **AWS Console** → **Elastic Beanstalk**
2. Sélectionnez votre environnement
3. Cliquez sur **"Restart App Server"** ou **"Restart Environment"**

#### Option B: Via AWS CLI

```bash
aws elasticbeanstalk restart-app-server --environment-name votre-environnement
```

#### Option C: Via l'Interface Elastic Beanstalk

1. Ouvrez votre environnement
2. Allez dans **"Configuration"** → **"Software"**
3. Cliquez sur **"Apply"** (cela redémarre l'application)

### Étape 4: Vérifier que le Backend a Redémarré

1. **Vérifiez les logs** dans Elastic Beanstalk :
   - Allez dans **"Logs"** → **"Request Logs"** ou **"Last 100 Lines"**
   - Cherchez les messages de démarrage Spring Boot
   - Vérifiez qu'il n'y a pas d'erreurs de connexion à la base

2. **Testez l'endpoint de santé** :
   ```bash
   curl https://api.smart-odc.com/awsodclearning/auth/check-availability
   ```
   
   Devrait retourner : `{"ok":true,"message":"API is available"}`

### Étape 5: Se Connecter avec les Identifiants Admin

Une fois le backend redémarré, utilisez ces identifiants :

| Champ | Valeur |
|-------|--------|
| **Email** | `cisseodl@gmail.com` |
| **Mot de passe** | `cisse@2025` |
| **URL Frontend Admin** | `https://admin.smart-odc.com` |

## 🔄 Comment ça Fonctionne

### Au Démarrage du Backend

Le `CommandLineRunner` dans `AwsLearningApplication.java` :

1. **Vérifie** si l'utilisateur `cisseodl@gmail.com` existe
2. **Si l'utilisateur n'existe pas** :
   - Crée l'utilisateur
   - Crée l'entité Admin
   - Hash le mot de passe avec BCrypt (`cisse@2025`)
3. **Si l'utilisateur existe déjà** :
   - Ne fait rien (l'utilisateur reste tel quel)

### Problème Actuel

Comme vous avez vidé la base **après** le démarrage du backend, le CommandLineRunner ne s'exécute pas automatiquement. Il faut soit :
- **Option 1** : Créer l'admin manuellement avec le script SQL (recommandé)
- **Option 2** : Redémarrer le backend (le CommandLineRunner créera l'admin)

## 🐛 Dépannage

### Erreur: "Cannot connect to RDS"

**Causes possibles** :
- Les credentials RDS sont incorrects
- Le Security Group RDS n'autorise pas votre IP
- La base RDS n'est pas accessible depuis votre réseau

**Solution** :
1. Vérifiez les credentials dans Elastic Beanstalk → Configuration → Database
2. Ajoutez votre IP au Security Group RDS dans AWS Console
3. Vérifiez que le Security Group autorise les connexions depuis votre IP

### Erreur: "User already exists"

**Cause** : L'utilisateur existe déjà dans la base.

**Solution** :
Le script SQL contient des `DELETE` pour supprimer l'utilisateur existant avant de le recréer. Si vous avez toujours l'erreur, exécutez manuellement :

```sql
DELETE FROM admins WHERE user_id IN (SELECT id FROM user WHERE email = 'cisseodl@gmail.com');
DELETE FROM user WHERE email = 'cisseodl@gmail.com';
```

Puis réexécutez le script.

### Erreur: "Failed to fetch" après création de l'admin

**Causes possibles** :
1. Le backend n'a pas redémarré
2. Le hash du mot de passe n'est pas correct
3. Le backend ne peut pas se connecter à RDS

**Solution** :
1. Vérifiez que le backend a bien redémarré (logs Elastic Beanstalk)
2. Vérifiez que le CommandLineRunner s'est exécuté (cherchez dans les logs)
3. Testez la connexion depuis le frontend

### Le Mot de Passe ne Fonctionne Pas

**Cause** : Le hash BCrypt dans le script SQL est temporaire.

**Solution** :
1. Redémarrez le backend sur Elastic Beanstalk
2. Le CommandLineRunner va mettre à jour le hash avec le bon hash BCrypt pour `cisse@2025`
3. Attendez quelques secondes après le redémarrage
4. Réessayez de vous connecter

## 📋 Checklist de Vérification

- [ ] Connexion à RDS réussie depuis DBeaver
- [ ] Script SQL exécuté avec succès
- [ ] Utilisateur créé dans la table `user`
- [ ] Admin créé dans la table `admins`
- [ ] Relation `user.admin_id` correctement configurée
- [ ] Backend redémarré sur Elastic Beanstalk
- [ ] Logs Elastic Beanstalk montrent le démarrage réussi
- [ ] Endpoint `/auth/check-availability` répond
- [ ] Connexion réussie avec `cisseodl@gmail.com` / `cisse@2025`

## 🚀 Commandes Utiles

### Vérifier l'état de l'environnement Elastic Beanstalk

```bash
aws elasticbeanstalk describe-environments --environment-names votre-environnement
```

### Voir les logs récents

```bash
aws elasticbeanstalk request-environment-info \
  --environment-name votre-environnement \
  --info-type tail
```

### Redémarrer l'environnement

```bash
aws elasticbeanstalk restart-app-server --environment-name votre-environnement
```

## 📝 Notes Importantes

1. **Hash du mot de passe** : Le hash dans le script SQL est temporaire. Le vrai hash BCrypt sera généré par Spring Boot au redémarrage.

2. **CommandLineRunner** : S'exécute uniquement au démarrage de l'application. Si vous créez l'admin manuellement, le CommandLineRunner ne le recréera pas (il vérifie d'abord s'il existe).

3. **Sécurité RDS** : Assurez-vous que votre IP est autorisée dans le Security Group RDS pour pouvoir vous connecter depuis DBeaver.

4. **Variables d'environnement** : Les credentials RDS sont stockés dans les variables d'environnement Elastic Beanstalk. Vous pouvez les voir dans Configuration → Software → Environment Properties.

## 🔐 Récupérer les Credentials RDS depuis Elastic Beanstalk

1. Allez sur **AWS Console** → **Elastic Beanstalk**
2. Sélectionnez votre environnement
3. Allez dans **Configuration** → **Database**
4. Vous verrez les informations de connexion (host, port, database name)
5. Pour le mot de passe, allez dans **Configuration** → **Software** → **Environment Properties**
6. Cherchez `RDS_PASSWORD` ou similaire

---

**Date de création**: 28 janvier 2026  
**Dernière mise à jour**: 28 janvier 2026
