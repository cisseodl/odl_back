# 🔧 Solution au Problème de Connexion après Vidage de la Base de Données

## 🔍 Diagnostic du Problème

L'erreur **"Failed to fetch"** indique que le backend n'est pas accessible. Cela peut être dû à :

1. **Backend non démarré** ⚠️ (le plus probable)
2. **Base de données vide** (utilisateur admin supprimé)
3. **Problème de connexion réseau**

## ✅ Solution Étape par Étape

### Étape 1: Vérifier que le Backend est Démarré

```bash
# Vérifier si le backend tourne
ps aux | grep java | grep awsodclearning

# Si rien n'apparaît, le backend n'est pas démarré
```

### Étape 2: Démarrer le Backend

Le backend créera automatiquement l'utilisateur admin au démarrage grâce au `CommandLineRunner` dans `AwsLearningApplication.java`.

**Option A: Démarrer avec Maven**
```bash
cd /Users/abdramanecisse/Desktop/odl/back/odl_back
./mvnw spring-boot:run
```

**Option B: Démarrer avec le JAR**
```bash
cd /Users/abdramanecisse/Desktop/odl/back/odl_back
java -jar target/awsodclearning.jar
```

### Étape 3: Vérifier les Logs au Démarrage

Lors du démarrage, vous devriez voir dans les logs que l'utilisateur admin est créé automatiquement :

```
User created: cisseodl@gmail.com
Admin created for user: ...
```

### Étape 4: Créer l'Admin Manuellement (si nécessaire)

Si le backend ne démarre pas ou si vous préférez créer l'admin manuellement :

1. **Ouvrez DBeaver**
2. **Connectez-vous à la base de données** `odcawslearning`
3. **Exécutez le script SQL** : `database/create_admin_user.sql`

Ou exécutez directement ce SQL :

```sql
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
)
ON DUPLICATE KEY UPDATE activate = true;

-- Récupérer l'ID
SET @user_id = (SELECT id FROM user WHERE email = 'cisseodl@gmail.com');

-- Créer l'entité Admin
INSERT INTO admins (user_id, activate, created_at, last_modified_at)
VALUES (@user_id, true, NOW(), NOW())
ON DUPLICATE KEY UPDATE activate = true;

-- Lier l'admin à l'utilisateur
UPDATE user SET admin_id = (SELECT id FROM admins WHERE user_id = @user_id)
WHERE id = @user_id;
```

**⚠️ IMPORTANT**: Le hash du mot de passe dans le script SQL est temporaire. Le vrai hash BCrypt sera généré par Spring Boot au démarrage.

### Étape 5: Identifiants de Connexion

Une fois le backend démarré et l'admin créé, utilisez ces identifiants :

| Champ | Valeur |
|-------|--------|
| **Email** | `cisseodl@gmail.com` |
| **Mot de passe** | `cisse@2025` |

### Étape 6: Vérifier la Connexion

1. **Vérifiez que le backend répond** :
   ```bash
   curl http://localhost:5000/awsodclearning/auth/check-availability
   ```
   
   Devrait retourner : `{"ok":true,"message":"API is available"}`

2. **Testez la connexion depuis le frontend** :
   - Ouvrez le frontend admin : `http://localhost:3000` (ou votre URL)
   - Connectez-vous avec les identifiants ci-dessus

## 🐛 Dépannage

### Erreur: "Connection refused"

**Cause**: Le backend n'est pas démarré ou écoute sur un autre port.

**Solution**:
1. Vérifiez le port dans `application.properties` : `server.port=5000`
2. Vérifiez qu'aucun autre processus n'utilise le port 5000 :
   ```bash
   lsof -i :5000
   ```

### Erreur: "Database connection failed"

**Cause**: La base de données MySQL n'est pas accessible.

**Solution**:
1. Vérifiez que MySQL est démarré :
   ```bash
   # Sur macOS
   brew services list | grep mysql
   # Ou
   mysql.server status
   ```

2. Vérifiez les credentials dans `application.properties` :
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/odcawslearning
   spring.datasource.username=root
   spring.datasource.password=votre_mot_de_passe
   ```

### Erreur: "User not found" après connexion

**Cause**: L'utilisateur admin n'a pas été créé correctement.

**Solution**:
1. Vérifiez dans DBeaver que l'utilisateur existe :
   ```sql
   SELECT * FROM user WHERE email = 'cisseodl@gmail.com';
   SELECT * FROM admins WHERE user_id = (SELECT id FROM user WHERE email = 'cisseodl@gmail.com');
   ```

2. Si l'utilisateur n'existe pas, exécutez le script SQL `create_admin_user.sql`

3. Redémarrez le backend

## 📝 Notes Importantes

1. **Création automatique**: Le backend crée automatiquement l'utilisateur admin au démarrage s'il n'existe pas (via `AwsLearningApplication.java`)

2. **Hash du mot de passe**: Le mot de passe est hashé avec BCrypt par Spring Security. Le hash dans le script SQL est temporaire et sera remplacé au démarrage.

3. **Rôle ADMIN**: L'utilisateur doit avoir :
   - Un enregistrement dans la table `user` avec `role = 'ADMIN'`
   - Un enregistrement dans la table `admins` lié à l'utilisateur
   - La relation bidirectionnelle correctement configurée

4. **Base de données**: Assurez-vous que la base de données `odcawslearning` existe et est accessible.

## ✅ Checklist de Vérification

- [ ] MySQL est démarré et accessible
- [ ] La base de données `odcawslearning` existe
- [ ] Le backend Spring Boot est démarré
- [ ] Le backend écoute sur le port 5000 (ou celui configuré)
- [ ] L'utilisateur admin existe dans la table `user`
- [ ] L'entité Admin existe dans la table `admins`
- [ ] La relation entre User et Admin est correcte
- [ ] Le frontend peut accéder à l'URL du backend

## 🚀 Commandes Rapides

```bash
# 1. Démarrer le backend
cd /Users/abdramanecisse/Desktop/odl/back/odl_back
./mvnw spring-boot:run

# 2. Vérifier que le backend répond
curl http://localhost:5000/awsodclearning/auth/check-availability

# 3. Vérifier les logs pour voir si l'admin est créé
# (regardez les logs du backend)
```

---

**Date de création**: 28 janvier 2026  
**Dernière mise à jour**: 28 janvier 2026
