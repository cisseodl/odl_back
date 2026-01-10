# Correction Rapide - Version Java

## Problème

Votre JAR a été compilé avec **Java 14**, mais Elastic Beanstalk utilise **Java 11**.

**Erreur dans les logs :**
```
UnsupportedClassVersionError: class file version 58.0 (Java 14), 
this version only recognizes up to 55.0 (Java 11)
```

## Solution : Recompiler avec Java 11

### Étape 1 : Le pom.xml a été corrigé

Le fichier `pom.xml` a été mis à jour pour compiler avec Java 11 au lieu de Java 14.

### Étape 2 : Recompiler le JAR

```bash
cd Back/odl_back
mvn clean package -DskipTests
```

### Étape 3 : Vérifier que vous utilisez Java 11

```bash
java -version
```

Vous devriez voir `openjdk version "11.0.x"` ou similaire.

Si vous avez une version supérieure, vous devez :
1. Installer Java 11
2. Définir `JAVA_HOME` vers Java 11
3. Ou utiliser le fichier `.mvn/jvm.config` qui a été créé

### Étape 4 : Redéployer sur Elastic Beanstalk

1. Allez sur la console AWS Elastic Beanstalk
2. Sélectionnez votre environnement : **ODC-Learning-Backend-env**
3. Cliquez sur **"Téléverser et déployer"**
4. Sélectionnez le nouveau JAR : `Back/odl_back/target/awsodclearning.jar`
5. Entrez un label de version : `v1.0.1-java11`
6. Cliquez sur **"Déployer"**

### Étape 5 : Vérifier

Après le déploiement, vérifiez les logs. L'erreur `UnsupportedClassVersionError` ne devrait plus apparaître.

## Alternative : Utiliser Java 17 sur Elastic Beanstalk

Si vous préférez utiliser Java 17 :

1. Allez dans **Configuration** → **Plateforme**
2. Changez vers : **Corretto 17 running on 64bit Amazon Linux 2/3.10.1**
3. Mettez à jour le `pom.xml` :
   ```xml
   <java.version>17</java.version>
   ```
   Et dans `maven-compiler-plugin` :
   ```xml
   <source>17</source>
   <target>17</target>
   ```
4. Recompilez et redéployez

## Fichiers Modifiés

- ✅ `pom.xml` : Changé `<source>14</source>` et `<target>14</target>` vers `11`
- ✅ `.mvn/jvm.config` : Créé pour forcer Maven à utiliser Java 11
