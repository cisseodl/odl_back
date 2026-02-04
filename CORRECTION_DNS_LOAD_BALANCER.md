# 🔧 Correction DNS : Pointer vers le Load Balancer

## ❌ Problème Actuel

Votre enregistrement DNS pointe vers :
```
odc-learning-backend-env.eba-ruizssvt.us-east-1.elasticbeanstalk.com
```

C'est le **nom de l'environnement Beanstalk**, pas le **nom du load balancer**.

Pour un enregistrement **A Alias** dans Route 53, il faut pointer directement vers le **load balancer**.

## ✅ Solution

### Étape 1: Trouver le Nom du Load Balancer

**Dans AWS Console** :

1. Allez dans **Elastic Beanstalk** → votre environnement (`odc-learning-backend-env`)
2. Allez dans **Configuration** → **Load balancer** (ou **Capacity** → **Load balancer**)
3. Cherchez le **nom DNS** du load balancer

Il devrait ressembler à quelque chose comme :
```
awseb-e-rafruf9ypt-xxx-xxx.us-east-1.elb.amazonaws.com
```

**OU** :

1. Allez dans **EC2** → **Load Balancers**
2. Cherchez le load balancer associé à votre environnement Beanstalk
3. Le nom DNS est affiché dans les détails

### Étape 2: Corriger l'Enregistrement DNS dans Route 53

**Dans Route 53** → **Hosted zones** → **smart-odc.com** :

1. **Modifiez** l'enregistrement `api.smart-odc.com` (Type A, Alias)
2. **Changez la valeur** :
   - **Avant** : `odc-learning-backend-env.eba-ruizssvt.us-east-1.elasticbeanstalk.com`
   - **Après** : Le nom DNS du load balancer (ex: `awseb-e-rafruf9ypt-xxx-xxx.us-east-1.elb.amazonaws.com`)

**OU** (méthode recommandée) :

1. **Supprimez** l'enregistrement actuel
2. **Créez un nouvel enregistrement** :
   - **Nom** : `api`
   - **Type** : **A**
   - **Alias** : **Oui**
   - **Route traffic to** : **Alias to Application and Classic Load Balancer**
   - **Region** : **us-east-1**
   - **Sélectionnez** le load balancer dans la liste déroulante
   - **Enregistrer**

Cette méthode est meilleure car Route 53 détecte automatiquement le bon load balancer.

### Étape 3: Vérifier la Configuration HTTPS

**Dans Elastic Beanstalk** → **Configuration** → **Load balancer** :

1. Vérifiez qu'il y a un **listener** sur le **port 443** (HTTPS)
2. Vérifiez que le certificat ACM (`api.smart-odc.com`) est attaché
3. Si ce n'est pas le cas, ajoutez-le :
   - **Port** : 443
   - **Protocol** : HTTPS
   - **SSL certificate** : Sélectionnez le certificat ACM pour `api.smart-odc.com`
   - **Save** → **Apply**

### Étape 4: Créer l'Admin dans RDS

Le backend crash probablement au démarrage à cause de la base vide.

**Exécutez le script SQL** `database/create_admin_simple.sql` dans DBeaver connecté à RDS.

### Étape 5: Redémarrer Beanstalk

1. **Actions** → **Restart Environment**
2. Attendez que le redémarrage soit terminé (health = OK)
3. Vérifiez les logs pour voir si le backend démarre correctement

### Étape 6: Tester

```bash
# Test 1: Vérifier que le DNS résout correctement
nslookup api.smart-odc.com

# Test 2: Tester l'endpoint HTTPS
curl https://api.smart-odc.com/awsodclearning/auth/check-availability

# Devrait retourner : {"ok":true,"message":"API is available"}
```

## 🔍 Comment Trouver le Nom du Load Balancer (Méthode Alternative)

**Via AWS CLI** :

```bash
# Trouver l'environnement Beanstalk
aws elasticbeanstalk describe-environments \
  --environment-names odc-learning-backend-env \
  --region us-east-1

# Le nom du load balancer sera dans la réponse
```

**Via Console AWS** :

1. **EC2** → **Load Balancers**
2. Cherchez un load balancer avec un nom qui contient votre environnement
3. Cliquez dessus → **Description** → **DNS name**

## ⚠️ Note Importante

Le nom de l'environnement Beanstalk (`odc-learning-backend-env.eba-ruizssvt.us-east-1.elasticbeanstalk.com`) fonctionne pour accéder directement à l'environnement, mais pour un enregistrement Route 53 Alias qui doit pointer vers un load balancer, il faut utiliser le **nom DNS du load balancer** directement.

## 📋 Checklist

- [ ] Trouver le nom DNS du load balancer
- [ ] Modifier l'enregistrement DNS dans Route 53 pour pointer vers le load balancer
- [ ] Vérifier que le listener HTTPS (443) est configuré avec le certificat ACM
- [ ] Créer l'admin dans RDS avec le script SQL
- [ ] Redémarrer l'environnement Beanstalk
- [ ] Tester `https://api.smart-odc.com/awsodclearning/auth/check-availability`

Une fois tout ça fait, votre backend devrait être accessible en HTTPS via `https://api.smart-odc.com` !
