# Déploiement Elastic Beanstalk – éviter 413 (Content Too Large)

## Pourquoi l’upload échoue encore avec 413 ?

Si vous déployez **uniquement le JAR** (upload du fichier `awsodclearning.jar`), les dossiers **`.platform`** et **`.ebextensions`** ne sont pas envoyés. La config Nginx (limite 500M) n’est donc **jamais appliquée** et la valeur par défaut (1 Mo) reste active.

---

## Pour l’équipe (console EB) : un seul build, toujours le bon fichier à uploader

À chaque **`mvn package`** (ou `mvnw.cmd clean package -DskipTests`), Maven produit automatiquement :

- **`target/awsodclearning.jar`** (comme avant)
- **`target/odl-back-deploy.zip`** ← **c’est ce fichier qu’il faut uploader sur EB**

Le ZIP contient déjà le JAR, le Procfile, `.ebextensions` et `.platform`. Plus besoin de script ni de se rappeler quoi mettre dans le ZIP.

**Règle pour toute l’équipe :**  
Après un build, déployer en uploadant **`target/odl-back-deploy.zip`** dans la console Elastic Beanstalk (Upload and deploy). Ne pas uploader le JAR seul.

---

## Option pipeline CI/CD (CodePipeline + CodeBuild)

Si vous utilisez **AWS CodePipeline + CodeBuild** (déploiement depuis Git) :

1. Le fichier **`buildspec.yml`** à la racine du backend est déjà configuré : à chaque build, CodeBuild produit un artifact qui contient **JAR + Procfile + .ebextensions + .platform**.
2. Configurez votre pipeline pour que l’étape **Deploy** vers Elastic Beanstalk utilise l’artifact produit par CodeBuild (pas seulement le JAR).
3. À chaque push sur Git, le pipeline build et déploie automatiquement le bon bundle. Vous ne créez plus de zip à la main.

Si votre pipeline build actuellement le JAR puis déploie uniquement ce JAR, modifiez la phase “Build” pour utiliser ce `buildspec.yml` (et que l’artifact soit le contenu de `deploy_bundle`, pas seulement `target/*.jar`).

**Résumé** : une seule fois vous configurez le pipeline pour utiliser `buildspec.yml` ; ensuite chaque déploiement (à chaque push ou manuellement) envoie automatiquement le bon bundle à EB.

---

## Solution manuelle : déployer un ZIP complet

Il faut déployer un **ZIP** qui contient à la racine :

- `awsodclearning.jar`
- `Procfile`
- `.ebextensions/` (tout le dossier)
- `.platform/` (tout le dossier, avec `nginx/conf.d/client_max_body_size.conf`)

### Méthode 1 : script PowerShell (recommandé)

```powershell
# 1. Build du JAR
$env:JAVA_HOME = "C:\Program Files\Java\jdk-17"
.\mvnw.cmd clean package -DskipTests

# 2. Créer le ZIP de déploiement
.\build-deploy-zip.ps1
```

Le script produit **`odl-back-deploy.zip`** à la racine du projet. Uploadez ce fichier dans la console EB (Upload and deploy).

### Méthode 2 : à la main

1. Après `mvn package`, dans le dossier du backend :
   - Copier `target/awsodclearning.jar` à la racine (ou garder en tête son chemin).
2. Créer une archive ZIP dont le **contenu** à la racine du ZIP est :
   - `awsodclearning.jar`
   - `Procfile`
   - Dossier ` .ebextensions` (avec tous les `.config`)
   - Dossier `.platform` (avec `nginx/conf.d/client_max_body_size.conf`).
3. Ne pas zipper le dossier parent : ouvrir le ZIP et vérifier qu’en ouvrant le ZIP on voit tout de suite `awsodclearning.jar`, `Procfile`, `.ebextensions`, `.platform`.

### Déploiement depuis Git / CI

Si vous déployez depuis GitHub (ou autre) via la console EB ou CodeBuild, le **source bundle** doit contenir ces mêmes fichiers. Vérifiez que :

- la build copie le JAR à la racine (ou que EB le prend depuis `target/`),
- et que **`.platform`** et **`.ebextensions`** sont bien présents dans le dépôt et inclus dans l’artifact (ZIP) envoyé à Elastic Beanstalk.

## Vérification sur l’instance

En SSH sur l’instance EB :

```bash
sudo nginx -T | grep -i client_max_body_size
```

Vous devez voir une ligne du type `client_max_body_size 500M;`. Si ce n’est pas le cas, le déploiement n’a pas inclus `.platform` / la config Nginx.

## Limite actuelle

- **500M** dans `.platform/nginx/conf.d/client_max_body_size.conf` et dans `.ebextensions/02-nginx.config`.

Pour un fichier d’environ 3 Mo, 500M est largement suffisant ; le 413 vient du fait que cette config n’est pas appliquée quand on ne déploie que le JAR.
