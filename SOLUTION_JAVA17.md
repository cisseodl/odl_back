# Solution : Utiliser Java 17

Vous avez Java 17 installé, donc nous allons utiliser Java 17 partout.

## Étape 1 : Recompiler le JAR avec Java 17

Le `pom.xml` a été mis à jour pour utiliser Java 17. Recompilez maintenant :

```bash
cd Back/odl_back
mvn clean package -DskipTests
```

## Étape 2 : Changer la Plateforme Elastic Beanstalk vers Java 17

### Via la Console AWS

1. Allez sur https://console.aws.amazon.com/elasticbeanstalk
2. Sélectionnez votre environnement : **ODC-Learning-Backend-env**
3. Cliquez sur **Configuration** dans le menu de gauche
4. Cliquez sur **Plateforme** → **Modifier**
5. Changez la plateforme de :
   - `Corretto 11 running on 64bit Amazon Linux 2/3.10.1`
   
   Vers :
   - `Corretto 17 running on 64bit Amazon Linux 2/3.10.1`
   
   (Si Java 17 n'est pas disponible, choisissez la version la plus proche disponible)

6. Cliquez sur **Appliquer**
7. Attendez que la plateforme soit mise à jour (2-3 minutes)

### Via EB CLI (Alternative)

```bash
cd Back/odl_back
eb platform select "Corretto 17 running on 64bit Amazon Linux 2/3.10.1"
```

## Étape 3 : Redéployer le JAR

Une fois la plateforme mise à jour vers Java 17 :

1. Allez dans votre environnement Elastic Beanstalk
2. Cliquez sur **"Téléverser et déployer"**
3. Sélectionnez le nouveau JAR : `Back/odl_back/target/awsodclearning.jar`
4. Entrez un label de version : `v1.0.2-java17`
5. Cliquez sur **"Déployer"**

## Vérification

Après le déploiement :
- L'erreur `UnsupportedClassVersionError` ne devrait plus apparaître
- L'application devrait démarrer correctement
- Vérifiez les logs pour confirmer que tout fonctionne

## Fichiers Modifiés

- ✅ `pom.xml` : Mis à jour pour Java 17
  - `<java.version>17</java.version>`
  - `<source>17</source>` et `<target>17</target>`
- ✅ `.mvn/jvm.config` : Mis à jour pour Java 17

## Note

Si la plateforme "Corretto 17" n'est pas disponible dans la liste, vous pouvez :
1. Utiliser "Corretto 21" (si disponible)
2. Ou créer une plateforme personnalisée avec Java 17

Mais généralement, "Corretto 17" devrait être disponible dans les options.
