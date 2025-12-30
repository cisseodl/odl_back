# 🔧 Correction des Images des Cours - Commandes

## 📋 Analyse des Fichiers Disponibles

### Fichiers trouvés dans `Image/ODLearning/` :

| Fichier | Extension | Usage | Match |
|---------|-----------|-------|-------|
| `JavaScript-Logo.png` | `.png` | ✅ **React.js Avancé** | Parfait (JavaScript = React) |
| `computer-vision-banner.png` | `.png` | ✅ **Sécurité** | Bon (computer/vision = sécurité) |
| `kubernate.png` | `.png` | ✅ **Docker/Kubernetes** | Parfait (Kubernetes) |
| `amazon-web-services.png` | `.png` | ✅ **AWS Fundamentals** | Déjà fonctionne |

---

## 🚀 Commandes PowerShell pour Copier les Images

### Commande Complète (Copier-Coller)

```powershell
# Se placer à la racine du projet
cd "D:\Mes projets\ODL"

# Créer le dossier uploads/cours s'il n'existe pas
New-Item -ItemType Directory -Force -Path "odc_learning_api-master\uploads\cours"

# Copier les 3 images manquantes (AWS est déjà copié)
Copy-Item "Image\ODLearning\JavaScript-Logo.png" -Destination "odc_learning_api-master\uploads\cours\JavaScript-Logo.png"
Copy-Item "Image\ODLearning\computer-vision-banner.png" -Destination "odc_learning_api-master\uploads\cours\computer-vision-banner.png"
Copy-Item "Image\ODLearning\kubernate.png" -Destination "odc_learning_api-master\uploads\cours\kubernate.png"

# Vérifier que les fichiers ont été copiés
Get-ChildItem "odc_learning_api-master\uploads\cours" | Select-Object Name, Length
```

### Commandes Séparées (Une par Une)

```powershell
# 1. Aller à la racine du projet
cd "D:\Mes projets\ODL"

# 2. Créer le dossier (si nécessaire)
New-Item -ItemType Directory -Force -Path "odc_learning_api-master\uploads\cours"

# 3. Copier JavaScript-Logo.png (pour React)
Copy-Item "Image\ODLearning\JavaScript-Logo.png" -Destination "odc_learning_api-master\uploads\cours\JavaScript-Logo.png"

# 4. Copier computer-vision-banner.png (pour Sécurité)
Copy-Item "Image\ODLearning\computer-vision-banner.png" -Destination "odc_learning_api-master\uploads\cours\computer-vision-banner.png"

# 5. Copier kubernate.png (pour Docker/Kubernetes)
Copy-Item "Image\ODLearning\kubernate.png" -Destination "odc_learning_api-master\uploads\cours\kubernate.png"

# 6. Vérifier
Get-ChildItem "odc_learning_api-master\uploads\cours"
```

---

## 🗄️ Requêtes SQL UPDATE

### Script SQL Complet

Exécutez dans DBeaver le fichier : `fix_course_images.sql`

Ou copiez-collez ces requêtes :

```sql
USE odcawslearning;

-- React.js Avancé
UPDATE courses 
SET image_path = 'JavaScript-Logo.png'
WHERE title = 'React.js Avancé';

-- Sécurité des Applications Web
UPDATE courses 
SET image_path = 'computer-vision-banner.png'
WHERE title = 'Sécurité des Applications Web';

-- Docker et Kubernetes
UPDATE courses 
SET image_path = 'kubernate.png'
WHERE title = 'Docker et Kubernetes';
```

---

## ✅ Vérification

### 1. Vérifier les fichiers copiés

```powershell
Get-ChildItem "odc_learning_api-master\uploads\cours" | Format-Table Name, Length, LastWriteTime
```

**Résultat attendu :**
```
Name                          Length LastWriteTime
----                          ------ -------------
amazon-web-services.png        [taille] [date]
JavaScript-Logo.png            [taille] [date]
computer-vision-banner.png     [taille] [date]
kubernate.png                  [taille] [date]
```

### 2. Vérifier la base de données

```sql
SELECT id, title, image_path FROM courses ORDER BY id;
```

**Résultat attendu :**
```
id | title                              | image_path
---|------------------------------------|---------------------------
1  | AWS Fundamentals                   | amazon-web-services.png
2  | React.js Avancé                    | JavaScript-Logo.png
3  | Sécurité des Applications Web      | computer-vision-banner.png
4  | Docker et Kubernetes               | kubernate.png
```

### 3. Tester les URLs directement

Ouvrez ces URLs dans votre navigateur :
- `http://localhost:8080/awsodclearning/downloads/cours/amazon-web-services.png` ✅
- `http://localhost:8080/awsodclearning/downloads/cours/JavaScript-Logo.png` ✅
- `http://localhost:8080/awsodclearning/downloads/cours/computer-vision-banner.png` ✅
- `http://localhost:8080/awsodclearning/downloads/cours/kubernate.png` ✅

---

## 📝 Mapping Final

| Cours | Fichier Source | Destination | Nom dans la DB |
|-------|----------------|-------------|----------------|
| **AWS Fundamentals** | `Image/ODLearning/amazon-web-services.png` | `uploads/cours/amazon-web-services.png` | `amazon-web-services.png` |
| **React.js Avancé** | `Image/ODLearning/JavaScript-Logo.png` | `uploads/cours/JavaScript-Logo.png` | `JavaScript-Logo.png` |
| **Sécurité** | `Image/ODLearning/computer-vision-banner.png` | `uploads/cours/computer-vision-banner.png` | `computer-vision-banner.png` |
| **Docker/K8s** | `Image/ODLearning/kubernate.png` | `uploads/cours/kubernate.png` | `kubernate.png` |

---

## 🆘 Dépannage

### Erreur : "File not found"

1. Vérifiez que les fichiers sont bien copiés :
   ```powershell
   Test-Path "odc_learning_api-master\uploads\cours\JavaScript-Logo.png"
   ```

2. Vérifiez les noms exacts (sensible à la casse) :
   - `JavaScript-Logo.png` (avec majuscules J et L)
   - Pas `javascript-logo.png` ou `JavaScript-logo.png`

3. Vérifiez l'extension exacte :
   - Tous les fichiers sont en `.png` (pas `.jpg` ou `.jpeg`)

### Les images ne s'affichent toujours pas

1. Redémarrez le backend Spring Boot
2. Vérifiez les logs du backend pour des erreurs 404
3. Testez l'URL directement dans le navigateur
4. Vérifiez que le `DownloadController` utilise bien `@Value("${file.upload-dir}")`

---

*Guide créé pour le projet ODL - Orange Digital Learning*

