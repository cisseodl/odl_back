# Installation d'Amazon Corretto 11 sur Windows

Amazon Corretto 11 est la distribution OpenJDK utilisée par votre environnement Elastic Beanstalk actuel.

## Méthode 1 : Installation via le site officiel (Recommandé)

### Étape 1 : Télécharger Amazon Corretto 11

1. Allez sur : https://aws.amazon.com/corretto/
2. Cliquez sur **"Download Corretto 11"**
3. Sélectionnez **Windows x64** (si vous êtes sur Windows 64-bit)
4. Téléchargez le fichier `.msi` (installateur Windows)

**Lien direct :** https://github.com/corretto/corretto-11/releases

### Étape 2 : Installer

1. Double-cliquez sur le fichier `.msi` téléchargé
2. Suivez l'assistant d'installation
3. **Important** : Cochez l'option **"Set JAVA_HOME variable"** si elle est proposée
4. Cliquez sur **"Install"**

### Étape 3 : Vérifier l'installation

Ouvrez un nouveau terminal PowerShell et exécutez :

```powershell
java -version
```

Vous devriez voir quelque chose comme :
```
openjdk version "11.0.x" 2024-xx-xx LTS
OpenJDK Runtime Environment Corretto-11.0.x.x.1 (build 11.0.x+8-LTS)
OpenJDK 64-Bit Server VM Corretto-11.0.x.x.1 (build 11.0.x+8-LTS, mixed mode, sharing)
```

## Méthode 2 : Installation via Chocolatey (Si vous avez Chocolatey)

```powershell
choco install corretto11jdk
```

## Méthode 3 : Installation via winget (Windows Package Manager)

```powershell
winget install Amazon.Corretto.11
```

## Configuration de JAVA_HOME

### Vérifier JAVA_HOME

```powershell
$env:JAVA_HOME
```

### Si JAVA_HOME n'est pas défini

1. Trouvez le chemin d'installation de Corretto (généralement : `C:\Program Files\Amazon Corretto\jdk11.x.x_x`)
2. Définissez JAVA_HOME :

**Pour la session actuelle :**
```powershell
$env:JAVA_HOME = "C:\Program Files\Amazon Corretto\jdk11.0.23_9"
```

**Pour définir de manière permanente :**
1. Ouvrez **Paramètres Windows** → **Système** → **À propos de** → **Paramètres système avancés**
2. Cliquez sur **"Variables d'environnement"**
3. Sous **"Variables système"**, cliquez sur **"Nouveau"**
4. Nom de la variable : `JAVA_HOME`
5. Valeur de la variable : `C:\Program Files\Amazon Corretto\jdk11.0.23_9` (ajustez selon votre version)
6. Cliquez sur **"OK"**
7. Modifiez la variable **Path** et ajoutez : `%JAVA_HOME%\bin`
8. Cliquez sur **"OK"** partout

### Redémarrer le terminal

Fermez et rouvrez votre terminal PowerShell pour que les changements prennent effet.

## Vérification finale

```powershell
java -version
javac -version
mvn -version
```

Tous devraient afficher la version 11.

## Important : Mettre à jour le pom.xml pour Java 11

Assurez-vous que votre `pom.xml` est configuré pour Java 11 :

```xml
<properties>
    <java.version>11</java.version>
    ...
</properties>
```

Et dans `maven-compiler-plugin` :
```xml
<source>11</source>
<target>11</target>
```

## Compiler le projet avec Corretto 11

Une fois Corretto 11 installé :

```powershell
cd Back/odl_back
mvn clean package -DskipTests
```

Le JAR sera maintenant compilé avec Java 11, compatible avec votre environnement Elastic Beanstalk actuel.
