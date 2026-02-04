# 🔍 Diagnostic des Logs Elastic Beanstalk

## 📊 Analyse des Logs Fournis

### ✅ Ce qui fonctionne

1. **Backend démarre** : Les logs montrent que Spring Boot démarre
2. **CommandLineRunner s'exécute** : On voit les requêtes Hibernate qui cherchent l'utilisateur
3. **Connexion à la base RDS** : Les requêtes SQL s'exécutent

### ❌ Problèmes identifiés

1. **Nginx retourne 502** : `connect() failed (111: Connection refused) while connecting to upstream`
   - Le backend ne répond pas sur `http://127.0.0.1:5000/`
   - Cela signifie que le backend démarre mais crash ou ne démarre pas complètement

2. **Utilisateur admin non créé** : 
   - Le CommandLineRunner cherche `cisseodl@gmail.com` mais ne le trouve pas
   - Il devrait créer l'utilisateur s'il n'existe pas, mais on ne voit pas de log de création

3. **PasswordResetRunner échoue** :
   - Cherche `mamadou.kane@odl.sn` et `awa.diop@odl.sn` mais ne les trouve pas
   - C'est normal car la base a été vidée

## 🔧 Solution Immédiate

### Étape 1: Vérifier les Logs Complets du Backend

Dans Elastic Beanstalk, allez dans **Logs** → **Request Logs** ou **Last 100 Lines** et cherchez :
- Des erreurs de démarrage Spring Boot
- Des erreurs de connexion à la base de données
- Des erreurs lors de la création de l'utilisateur

### Étape 2: Créer l'Admin Manuellement dans RDS

Comme le backend ne démarre pas complètement (502), créons l'admin directement dans RDS :

1. **Connectez-vous à RDS** depuis DBeaver avec les credentials Elastic Beanstalk
2. **Exécutez le script** : `database/create_admin_rds.sql`

### Étape 3: Vérifier Pourquoi le Backend Crash

Les erreurs 502 indiquent que le backend ne répond pas. Causes possibles :

1. **Erreur au démarrage** : Le backend crash avant de démarrer complètement
2. **Port incorrect** : Le backend écoute sur un autre port que 5000
3. **Erreur de connexion RDS** : Le backend ne peut pas se connecter à RDS
4. **Erreur lors de la création de l'admin** : Le CommandLineRunner crash

## 🚨 Action Immédiate Requise

### Option 1: Créer l'Admin dans RDS (Recommandé)

Exécutez ce SQL directement dans DBeaver connecté à RDS :

```sql
-- Supprimer l'utilisateur existant s'il existe
DELETE FROM admins WHERE user_id IN (SELECT id FROM user WHERE email = 'cisseodl@gmail.com');
DELETE FROM user WHERE email = 'cisseodl@gmail.com';

-- Créer l'utilisateur admin
INSERT INTO user (
    full_name, email, password, phone, activate, role, created_at, last_modified_at
) VALUES (
    'CisseOdl',
    'cisseodl@gmail.com',
    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
    '0000000000',
    true,
    'ADMIN',
    NOW(),
    NOW()
);

-- Récupérer l'ID
SET @user_id = LAST_INSERT_ID();

-- Créer l'entité Admin
INSERT INTO admins (user_id, activate, created_at, last_modified_at)
VALUES (@user_id, true, NOW(), NOW());

-- Lier l'admin à l'utilisateur
UPDATE user SET admin_id = (SELECT id FROM admins WHERE user_id = @user_id)
WHERE id = @user_id;
```

### Option 2: Vérifier les Logs Complets

Dans Elastic Beanstalk :
1. Allez dans **Logs** → **Request Logs**
2. Cherchez les erreurs après les requêtes Hibernate
3. Vérifiez s'il y a des exceptions Java

### Option 3: Redémarrer l'Environnement

1. Allez dans **Actions** → **Restart Environment**
2. Attendez que le redémarrage soit terminé
3. Vérifiez les logs pour voir si le CommandLineRunner crée l'admin

## 📝 Notes Importantes

1. **Le hash du mot de passe** dans le SQL est temporaire. Le vrai hash sera généré par Spring Boot au redémarrage.

2. **Les erreurs 502** indiquent que le backend ne répond pas. Il faut d'abord résoudre ce problème avant de pouvoir se connecter.

3. **Le CommandLineRunner** devrait créer l'admin automatiquement, mais il semble y avoir un problème qui empêche le backend de démarrer complètement.

## 🔍 Prochaines Étapes

1. ✅ Créer l'admin manuellement dans RDS
2. ✅ Vérifier les logs complets pour identifier l'erreur du backend
3. ✅ Redémarrer l'environnement Elastic Beanstalk
4. ✅ Tester la connexion avec `cisseodl@gmail.com` / `cisse@2025`
