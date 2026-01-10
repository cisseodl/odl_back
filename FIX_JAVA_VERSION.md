# Correction du Problème de Version Java

## Problème Identifié

L'erreur dans les logs indique :
```
UnsupportedClassVersionError: com/odc/aws_learning/AwsLearningApplication has been compiled by a more recent version of the Java Runtime (class file version 58.0), this version of the Java Runtime only recognizes class file versions up to 55.0
```

**Explication :**
- **Class file version 58.0** = Java 14
- **Class file version 55.0** = Java 11 (version utilisée par Elastic Beanstalk)
- Votre JAR a été compilé avec Java 14, mais Elastic Beanstalk utilise Java 11

## Solution 1 : Recompiler avec Java 11 (Recommandé)

### Étape 1 : Vérifier votre version Java locale

```bash
java -version
```

Vous devriez voir quelque chose comme :
```
openjdk version "11.0.x"
```

Si vous voyez une version supérieure (14, 17, etc.), vous devez utiliser Java 11 pour compiler.

### Étape 2 : Installer Java 11 (si nécessaire)

#### Windows
1. Téléchargez Java 11 depuis : https://adoptium.net/temurin/releases/?version=11
2. Installez Java 11
3. Configurez `JAVA_HOME` pour pointer vers Java 11

#### Vérifier JAVA_HOME
```powershell
$env:JAVA_HOME
```

Si ce n'est pas Java 11, définissez-le :
```powershell
$env:JAVA_HOME = "C:\Program Files\Java\jdk-11"
```

### Étape 3 : Recompiler le JAR avec Java 11

```bash
cd Back/odl_back
mvn clean package -DskipTests
```

**Important :** Assurez-vous que Maven utilise Java 11 :
```bash
mvn -version
```

Vous devriez voir :
```
Java version: 11.0.x
```

### Étape 4 : Vérifier le JAR généré

Le nouveau JAR devrait être compatible avec Java 11.

### Étape 5 : Redéployer sur Elastic Beanstalk

1. Téléversez le nouveau JAR via la console AWS
2. Ou utilisez EB CLI : `eb deploy`

## Solution 2 : Changer la Plateforme Elastic Beanstalk vers Java 17

Si vous préférez utiliser Java 17 (ou plus récent) au lieu de Java 11 :

### Étape 1 : Modifier la Plateforme

1. Allez dans votre environnement Elastic Beanstalk
2. Cliquez sur **Configuration** → **Plateforme**
3. Cliquez sur **Modifier**
4. Changez la plateforme de :
   - `Corretto 11 running on 64bit Amazon Linux 2/3.10.1`
   
   Vers :
   - `Corretto 17 running on 64bit Amazon Linux 2/3.10.1`
   (ou `Corretto 21` si disponible`)

5. Cliquez sur **Appliquer**

### Étape 2 : Mettre à jour le pom.xml (optionnel)

Si vous changez vers Java 17, vous pouvez mettre à jour le `pom.xml` :

```xml
<properties>
    <java.version>17</java.version>
    ...
</properties>
```

### Étape 3 : Recompiler et redéployer

```bash
cd Back/odl_back
mvn clean package -DskipTests
```

Puis redéployez le JAR.

## Solution 3 : Forcer Maven à utiliser Java 11

Si vous avez plusieurs versions de Java installées, vous pouvez forcer Maven à utiliser Java 11 :

### Créer/modifier `.mvn/jvm.config`

Créez le fichier `Back/odl_back/.mvn/jvm.config` :

```
-Dmaven.compiler.source=11
-Dmaven.compiler.target=11
```

### Ou utiliser JAVA_HOME

```powershell
# Windows PowerShell
$env:JAVA_HOME = "C:\Program Files\Java\jdk-11"
mvn clean package -DskipTests
```

## Vérification

Après avoir recompilé, vérifiez que le JAR est compatible :

```bash
# Vérifier la version du class file
javap -verbose -cp target/awsodclearning.jar com.odc.aws_learning.AwsLearningApplication | findstr "major version"
```

Vous devriez voir :
- **55** pour Java 11 ✅
- **58** pour Java 14 ❌ (incompatible)

## Commandes Rapides

### Recompiler avec Java 11
```bash
cd Back/odl_back
# Vérifier la version Java
java -version
# Si ce n'est pas Java 11, définir JAVA_HOME
# Puis compiler
mvn clean package -DskipTests
```

### Vérifier la version du JAR
```bash
# Windows
javap -verbose -cp target/awsodclearning.jar com.odc.aws_learning.AwsLearningApplication | findstr "major"
```

## Recommandation

**Je recommande la Solution 1** (recompiler avec Java 11) car :
- Votre `pom.xml` est déjà configuré pour Java 11
- Elastic Beanstalk utilise Java 11 par défaut
- C'est la solution la plus simple et la plus compatible

Une fois le JAR recompilé avec Java 11, redéployez-le sur Elastic Beanstalk et l'erreur devrait disparaître.
