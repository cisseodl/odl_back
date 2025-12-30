# 📊 Guide d'Insertion des Données de Test - ODL

## 📋 Contenu du Script

Le script `insert_dummy_data.sql` insère les données suivantes :

- ✅ **3 Catégories** : Cloud Computing, Développement Web, Cybersécurité
- ✅ **1 Cohorte** : Cohorte 2025
- ✅ **5 Apprenants** : Liés à la cohorte
- ✅ **2 Formateurs** : Utilisateurs avec rôles ADMIN et USER
- ✅ **4 Cours** : Répartis dans les différentes catégories

---

## 🚀 Exécution via DBeaver

### Méthode 1 : Exécution Directe (Recommandée)

1. **Ouvrir DBeaver**
   - Lancez DBeaver sur votre machine

2. **Se connecter à la base de données**
   - Créez une nouvelle connexion MySQL si ce n'est pas déjà fait
   - **Hôte** : `localhost`
   - **Port** : `3306`
   - **Base de données** : `odcawslearning`
   - **Utilisateur** : `root`
   - **Mot de passe** : `root`
   - Testez la connexion et cliquez sur "Terminer"

3. **Ouvrir le script SQL**
   - Dans DBeaver, allez dans le menu : `File` → `Open File`
   - Naviguez vers : `odc_learning_api-master/database/insert_dummy_data.sql`
   - Le fichier s'ouvrira dans un nouvel onglet SQL

4. **Sélectionner la base de données**
   - Dans le panneau de gauche, double-cliquez sur la base `odcawslearning`
   - Ou dans l'éditeur SQL, assurez-vous que la base est sélectionnée dans le menu déroulant en haut

5. **Exécuter le script**
   - **Option A** : Cliquez sur l'icône ▶️ **"Execute SQL Script"** (ou `Ctrl+Alt+X`)
   - **Option B** : Sélectionnez tout le contenu (`Ctrl+A`) puis exécutez (`Ctrl+Enter`)
   - Le script s'exécutera et vous verrez les messages de succès

6. **Vérifier les résultats**
   - Vous devriez voir des messages comme "1 row affected" pour chaque INSERT
   - À la fin du script, une requête SELECT affichera le nombre d'enregistrements par table

---

### Méthode 2 : Copier-Coller dans l'Éditeur SQL

1. **Ouvrir un nouvel éditeur SQL**
   - Dans DBeaver, cliquez sur l'icône **"New SQL Editor"** (ou `Ctrl+Shift+Enter`)
   - Ou faites un clic droit sur la base `odcawslearning` → `SQL Editor` → `New SQL Script`

2. **Ouvrir le fichier SQL**
   - Ouvrez `insert_dummy_data.sql` dans un éditeur de texte (Notepad++, VS Code, etc.)
   - Copiez tout le contenu (`Ctrl+A` puis `Ctrl+C`)

3. **Coller dans DBeaver**
   - Collez le contenu dans l'éditeur SQL de DBeaver (`Ctrl+V`)

4. **Exécuter**
   - Cliquez sur ▶️ **"Execute SQL Script"** ou appuyez sur `Ctrl+Alt+X`

---

## ✅ Vérification des Données

### Vérification Rapide dans DBeaver

1. **Explorer les tables**
   - Dans le panneau de gauche, développez `odcawslearning` → `Tables`
   - Faites un clic droit sur une table (ex: `categorie`) → `View Data`
   - Vous devriez voir les données insérées

2. **Requête de vérification**
   - Exécutez cette requête dans l'éditeur SQL :

```sql
SELECT 'Catégories' AS Table_Name, COUNT(*) AS Count FROM categorie
UNION ALL
SELECT 'Cohortes', COUNT(*) FROM cohorte
UNION ALL
SELECT 'Apprenants', COUNT(*) FROM apprenants
UNION ALL
SELECT 'Utilisateurs', COUNT(*) FROM user
UNION ALL
SELECT 'Cours', COUNT(*) FROM courses;
```

**Résultat attendu :**
```
Table_Name    | Count
--------------|------
Catégories    | 3
Cohortes      | 1
Apprenants    | 5
Utilisateurs  | 2
Cours         | 4
```

---

## 🔐 Identifiants de Connexion pour les Formateurs

Deux comptes formateurs ont été créés avec le mot de passe : **`password123`**

| Email | Rôle | Mot de passe |
|-------|------|--------------|
| `mamadou.kane@odl.sn` | ADMIN | `password123` |
| `awa.diop@odl.sn` | USER | `password123` |

Vous pouvez vous connecter à l'application avec ces identifiants.

---

## 🆘 Dépannage

### Erreur : "Table doesn't exist"

**Cause :** Les tables n'ont pas encore été créées par Hibernate.

**Solution :**
1. Assurez-vous que le backend Spring Boot a démarré au moins une fois
2. Hibernate créera automatiquement les tables grâce à `ddl-auto=update`
3. Vérifiez que les tables existent : `SHOW TABLES;` dans DBeaver

### Erreur : "Foreign key constraint fails"

**Cause :** Les IDs des tables parentes ne correspondent pas.

**Solution :**
1. Vérifiez les IDs réels dans les tables :
   ```sql
   SELECT id FROM categorie;
   SELECT id FROM cohorte;
   ```
2. Ajustez les IDs dans le script si nécessaire
3. Ou supprimez les données et réexécutez le script dans l'ordre

### Erreur : "Duplicate entry"

**Cause :** Le script a déjà été exécuté.

**Solution :**
1. Supprimez les données existantes :
   ```sql
   DELETE FROM courses;
   DELETE FROM user WHERE learner_id IS NULL;
   DELETE FROM apprenants;
   DELETE FROM cohorte;
   DELETE FROM categorie;
   ```
2. Réexécutez le script

### Les données ne s'affichent pas dans l'application

**Vérifications :**
1. Redémarrez le backend Spring Boot après l'insertion
2. Vérifiez que l'application se connecte bien à la bonne base de données
3. Vérifiez les logs du backend pour des erreurs de connexion

---

## 📝 Notes Importantes

1. **Ordre d'exécution** : Le script respecte l'ordre des clés étrangères :
   - Catégories → Cours
   - Cohorte → Apprenants
   - Apprenants → Users (pour les apprenants)

2. **IDs auto-générés** : Les IDs sont générés automatiquement. Si vous réexécutez le script, les IDs peuvent être différents.

3. **Images des cours** : Les chemins d'images sont fictifs. Pour afficher les images, vous devrez uploader de vraies images via l'interface.

4. **Mots de passe** : Tous les formateurs ont le même mot de passe hashé (`password123`). En production, utilisez des mots de passe uniques et sécurisés.

---

## 🎯 Prochaines Étapes

Après avoir inséré les données :

1. **Redémarrer le backend** si nécessaire
2. **Se connecter** avec un compte formateur (`mamadou.kane@odl.sn` / `password123`)
3. **Explorer le dashboard** pour voir les catégories, cours, et apprenants
4. **Créer des chapitres** pour les cours via l'interface
5. **Ajouter du contenu** aux cours (PDF, liens, etc.)

---

*Guide créé pour le projet ODL - Orange Digital Learning*

