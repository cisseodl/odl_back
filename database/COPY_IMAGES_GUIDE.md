# 📸 Guide de Copie des Images pour les Cours - ODL

## 📋 Inventaire des Images Disponibles

### Images dans `Image/ODLearning/` (racine)

| Fichier | Description | Usage suggéré |
|---------|-------------|---------------|
| `amazon-web-services.png` | Logo AWS | ✅ **AWS Fundamentals** |
| `awsiam.png` | Logo AWS IAM | Alternative pour AWS |
| `JavaScript-Logo.png` | Logo JavaScript | ✅ **React.js Avancé** |
| `kubernate.png` | Logo Kubernetes | ✅ **Docker et Kubernetes** |
| `computer-vision-banner.png` | Bannière Computer Vision | ✅ **Sécurité des Applications Web** |
| `python.png` | Logo Python | Python/Data Science |
| `TensorFlow_logo.svg.png` | Logo TensorFlow | Machine Learning |
| `Sql_data_base_with_logo.svg.png` | Logo SQL | Base de données |
| `Power-BI-Microsoft-logo.png` | Logo Power BI | Business Intelligence |
| `ai.png` | Logo IA | Intelligence Artificielle |
| `data.jpg` | Image Data | Data Science |
| `23086798.png` | Image générique | Divers |
| `c823e53b3a1a7b0d36a9.png` | Image générique | Divers |
| `resau info.jpeg` | Image réseau | Réseaux |

### Images dans `Image/ODLearning/config/`

Images de configuration (homepage, login, about) - non utilisées pour les cours.

### Images dans `Image/ODLearning/cours/`

Images de chapitres/cours - nombreuses images JPEG/PNG, mais avec des noms générés automatiquement.

---

## 🚀 Commandes PowerShell pour Copier les Images

### Étape 1 : Créer le dossier de destination

```powershell
# Se placer dans le dossier backend
cd "odc_learning_api-master"

# Créer le dossier uploads/cours s'il n'existe pas
New-Item -ItemType Directory -Force -Path "uploads\cours"
```

**Explication :** Crée le dossier `uploads/cours` dans le backend où les images des cours seront stockées.

---

### Étape 2 : Copier les images sélectionnées

```powershell
# Depuis la racine du projet (D:\Mes projets\ODL)
# Copier les images sélectionnées vers uploads/cours/

Copy-Item "Image\ODLearning\amazon-web-services.png" -Destination "odc_learning_api-master\uploads\cours\amazon-web-services.png"
Copy-Item "Image\ODLearning\JavaScript-Logo.png" -Destination "odc_learning_api-master\uploads\cours\JavaScript-Logo.png"
Copy-Item "Image\ODLearning\computer-vision-banner.png" -Destination "odc_learning_api-master\uploads\cours\computer-vision-banner.png"
Copy-Item "Image\ODLearning\kubernate.png" -Destination "odc_learning_api-master\uploads\cours\kubernate.png"
```

**Explication :** Copie les 4 images sélectionnées pour les cours dans le dossier `uploads/cours/` du backend.

---

### Étape 3 : Vérifier que les fichiers ont été copiés

```powershell
# Vérifier le contenu du dossier uploads/cours
Get-ChildItem "odc_learning_api-master\uploads\cours"
```

**Explication :** Affiche la liste des fichiers copiés pour vérifier que tout s'est bien passé.

---

## 📝 Commandes Complètes (Copier-Coller)

```powershell
# 1. Aller dans le dossier backend
cd "odc_learning_api-master"

# 2. Créer le dossier uploads/cours
New-Item -ItemType Directory -Force -Path "uploads\cours"

# 3. Revenir à la racine du projet
cd ..

# 4. Copier les images
Copy-Item "Image\ODLearning\amazon-web-services.png" -Destination "odc_learning_api-master\uploads\cours\amazon-web-services.png"
Copy-Item "Image\ODLearning\JavaScript-Logo.png" -Destination "odc_learning_api-master\uploads\cours\JavaScript-Logo.png"
Copy-Item "Image\ODLearning\computer-vision-banner.png" -Destination "odc_learning_api-master\uploads\cours\computer-vision-banner.png"
Copy-Item "Image\ODLearning\kubernate.png" -Destination "odc_learning_api-master\uploads\cours\kubernate.png"

# 5. Vérifier
Get-ChildItem "odc_learning_api-master\uploads\cours"
```

---

## 🗄️ Exécution du Script SQL

Après avoir copié les images, exécutez le script SQL `update_course_images.sql` dans DBeaver :

1. **Ouvrir DBeaver**
2. **Ouvrir le script** : `odc_learning_api-master/database/update_course_images.sql`
3. **Exécuter le script** : Cliquez sur ▶️ "Execute SQL Script" (ou `Ctrl+Alt+X`)

---

## ✅ Vérification

### 1. Vérifier les fichiers copiés

```powershell
Get-ChildItem "odc_learning_api-master\uploads\cours"
```

**Résultat attendu :**
```
amazon-web-services.png
JavaScript-Logo.png
computer-vision-banner.png
kubernate.png
```

### 2. Vérifier la base de données

Exécutez dans DBeaver :
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

### 3. Tester dans l'application

1. Redémarrer le backend si nécessaire
2. Ouvrir l'application frontend
3. Aller sur la page d'accueil ou la liste des cours
4. Les images devraient maintenant s'afficher

---

## 🔍 Mapping des Images

| Cours | Image Assignée | Fichier Source |
|-------|----------------|----------------|
| **AWS Fundamentals** | `amazon-web-services.png` | `Image/ODLearning/amazon-web-services.png` |
| **React.js Avancé** | `JavaScript-Logo.png` | `Image/ODLearning/JavaScript-Logo.png` |
| **Sécurité des Applications Web** | `computer-vision-banner.png` | `Image/ODLearning/computer-vision-banner.png` |
| **Docker et Kubernetes** | `kubernate.png` | `Image/ODLearning/kubernate.png` |

---

## 🆘 Dépannage

### Les images ne s'affichent pas

1. **Vérifier que les fichiers existent** :
   ```powershell
   Test-Path "odc_learning_api-master\uploads\cours\amazon-web-services.png"
   ```

2. **Vérifier les permissions** : Les fichiers doivent être lisibles

3. **Vérifier l'URL** : L'URL construite par le frontend est :
   ```
   http://localhost:8080/awsodclearning/downloads/cours/{imagePath}
   ```
   Testez cette URL directement dans le navigateur

4. **Vérifier les logs du backend** : Regardez s'il y a des erreurs 404

5. **Vérifier le chemin dans la base** : Le `image_path` doit être exactement le nom du fichier (sans chemin)

---

## 📝 Notes Importantes

1. **Structure des dossiers** :
   - Images des cours : `uploads/cours/`
   - Images de config : `uploads/config/`
   - Avatars : `uploads/avatar/`

2. **Format des chemins** :
   - Dans la base de données : juste le nom du fichier (ex: `amazon-web-services.png`)
   - Le frontend construit l'URL complète automatiquement

3. **Redémarrage** : Après avoir copié les images, vous n'avez pas besoin de redémarrer le backend, mais c'est recommandé pour être sûr.

---

*Guide créé pour le projet ODL - Orange Digital Center*

