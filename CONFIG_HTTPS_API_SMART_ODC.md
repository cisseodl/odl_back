# 🔒 Configurer HTTPS pour api.smart-odc.com (Backend Elastic Beanstalk)

## 🎯 Problème

- **Frontend** (Amplify) : `https://smart-odc.com` et `https://admin.smart-odc.com` → **HTTPS** ✅  
- **Backend** (Beanstalk) : `http://odc-learning-backend-env.eba-ruizssvt.us-east-1.elasticbeanstalk.com` → **HTTP** ❌  

Quand une page en **HTTPS** appelle une API en **HTTP**, le navigateur bloque (Mixed Content).  
Il faut donc que le backend soit accessible en **HTTPS** via **https://api.smart-odc.com**.

---

## 📋 Ce qu’il faut faire (résumé)

1. **Certificat SSL** : Créer ou réutiliser un certificat ACM pour `api.smart-odc.com`.
2. **Domaine personnalisé** : Faire pointer `api.smart-odc.com` vers l’environnement Beanstalk (via Route 53 ou votre DNS).
3. **HTTPS sur le Load Balancer** : Attacher le certificat au listener HTTPS (port 443) du load balancer Beanstalk.
4. **Redirection HTTP → HTTPS** (optionnel mais recommandé).

---

## Étape 1 : Certificat SSL (AWS Certificate Manager – ACM)

1. **Console AWS** → **Certificate Manager (ACM)**  
   - Région : **us-east-1** (même région que Beanstalk).

2. **Request a certificate**  
   - Type : **Request a public certificate**.  
   - Domain names :  
     - `api.smart-odc.com`  
     - (optionnel) `*.smart-odc.com` si vous voulez couvrir d’autres sous-domaines.

3. **Validation** : **DNS validation** (recommandé).  
   - ACM vous donne un enregistrement CNAME à créer dans le DNS de `smart-odc.com`.  
   - Créez-le dans **Route 53** (ou chez votre hébergeur DNS).  
   - Attendez que le statut du certificat soit **Issued**.

4. **Noter l’ARN** du certificat (ex. `arn:aws:acm:us-east-1:016299216814:certificate/xxxx`).

---

## Étape 2 : Domaine personnalisé (DNS)

L’URL Beanstalk ne change pas : c’est juste l’adresse par défaut.  
Pour utiliser **api.smart-odc.com**, il faut que ce nom pointe vers le **Load Balancer** de Beanstalk.

### Option A : Route 53

1. **Route 53** → **Hosted zones** → zone **smart-odc.com**.

2. **Create record** :  
   - **Record name** : `api`  
   - **Record type** : **A**  
   - **Alias** : Oui.  
   - **Route traffic to** : **Alias to Application and Classic Load Balancer**  
   - **Region** : **us-east-1**.  
   - **Choisir** le load balancer de votre environnement Beanstalk  
     (nom du type `awseb-e-xxx-AWSEB-xxx`).

3. Enregistrer.  
   → **api.smart-odc.com** pointe vers le backend Beanstalk.

### Option B : DNS ailleurs (ex. OVH, Cloudflare, etc.)

1. Dans la **console Elastic Beanstalk** :  
   - Environnement → **Configuration** → **Load balancer** (ou **Capacity** selon l’interface).  
   - Noter l’**adresse DNS** du load balancer (ex. `awseb-e-xxx.us-east-1.elb.amazonaws.com`).

2. Chez votre hébergeur DNS, créer :  
   - **Type** : CNAME (ou A si vous pouvez pointer vers une IP).  
   - **Nom** : `api` (ou `api.smart-odc.com` selon l’interface).  
   - **Valeur** : nom DNS du load balancer (ex. `awseb-e-xxx.us-east-1.elb.amazonaws.com`).

---

## Étape 3 : HTTPS sur le Load Balancer (Beanstalk)

1. **Console AWS** → **Elastic Beanstalk** → votre environnement (ex. `odc-learning-backend-env`).

2. **Configuration** → **Edit** sur la section **Load balancer** (ou **Load balancer** dans le menu de gauche).

3. **Listeners** :  
   - Vous devez avoir (ou ajouter) un **listener** sur le **port 443** (HTTPS).  
   - **Port 443** :  
     - **Protocol** : HTTPS  
     - **SSL certificate** : choisir le certificat ACM créé à l’étape 1 (ex. `api.smart-odc.com`).  
   - Garder aussi le **port 80** (HTTP) si vous voulez une redirection vers HTTPS (voir étape 4).

4. **Save** puis **Apply**.

5. Attendre que l’environnement soit mis à jour (health **OK**).

Après ça, le backend est joignable en **HTTPS** sur l’URL du load balancer.  
Si le DNS (étape 2) pointe **api.smart-odc.com** vers ce load balancer, alors **https://api.smart-odc.com** fonctionne.

---

## Étape 4 : Redirection HTTP → HTTPS (recommandé)

Pour que même les appels en `http://api.smart-odc.com` soient redirigés en HTTPS :

1. Dans la **configuration du Load Balancer** Beanstalk :  
   - Listener **80** : au lieu de renvoyer vers le target group, vous pouvez :  
     - Soit le supprimer et gérer la redirection côté application (ex. Spring Boot).  
     - Soit utiliser une **Application Load Balancer** avec une **rule** :  
       - Si protocol HTTP et port 80 → **Redirect** vers `https://#{host}#{path}` (code 301/302).

2. Si votre environnement utilise un **Classic Load Balancer** :  
   - La redirection HTTP→HTTPS se fait souvent en ajoutant un listener HTTPS (443) avec le certificat et en configurant la redirection dans les règles du listener 80 (si l’interface le propose), ou via une petite app de redirection.  
   - Avec un **Application Load Balancer**, les règles de redirection sont plus simples.

En pratique : une fois le listener **443** et le certificat en place, le plus important est que les **fronts** (smart-odc.com et admin.smart-odc.com) appellent **https://api.smart-odc.com**, ce qui évite le Mixed Content.

---

## Étape 5 : Vérifier CORS et URLs dans le code

Vos `application.properties` ont déjà :

```properties
app.frontend.url=https://admin.smart-odc.com
app.dashboard.url=https://admin.smart-odc.com
app.server.base-url=${SERVER_BASE_URL:https://api.smart-odc.com}
```

À garder. Côté **CORS** (Security / WebMvc), les origines autorisées doivent inclure :

- `https://smart-odc.com`
- `https://admin.smart-odc.com`
- (optionnel) `https://api.smart-odc.com`

Comme dans votre `ReviewController` :

```java
@CrossOrigin(origins = {"https://smart-odc.com", "https://*.smart-odc.com", "https://api.smart-odc.com"}, maxAge = 3600)
```

Vérifiez que la config globale CORS (si vous en avez une) autorise bien ces origines en HTTPS.

---

## Étape 6 : Frontend (Amplify) – URL de l’API

- **Admin** : `NEXT_PUBLIC_API_URL` (ou équivalent) = **https://api.smart-odc.com**  
  (sans trailing slash ; le context path `/awsodclearning` est ajouté par l’app).
- **Front apprenant** : même base URL **https://api.smart-odc.com** pour les appels API.

Redéployer les fronts après changement pour que tout utilise bien **https://api.smart-odc.com**.

---

## 🔗 Récap des URLs visées

| Rôle        | URL attendue                          |
|------------|----------------------------------------|
| Backend API | **https://api.smart-odc.com**         |
| Context path | **/awsodclearning** (ex. https://api.smart-odc.com/awsodclearning/auth/signin) |
| Front apprenant | https://smart-odc.com (Amplify)   |
| Front admin    | https://admin.smart-odc.com (Amplify) |

---

## ⚠️ Pourquoi le lien Beanstalk reste en HTTP

Le lien **http://odc-learning-backend-env.eba-ruizssvt.us-east-1.elasticbeanstalk.com** est l’URL **par défaut** de l’environnement. AWS ne la remplace pas par votre domaine personnalisé :

- Cette URL continue d’exister et restera en **HTTP** tant que vous n’ajoutez pas HTTPS sur le load balancer pour cet environnement.
- Votre **vraie** URL pour les apps est **https://api.smart-odc.com**, une fois :
  - le certificat ACM attaché au listener 443,  
  - et le DNS `api.smart-odc.com` pointant vers ce load balancer.

Donc : le fait de cliquer sur le lien Beanstalk et de voir encore l’URL en `http://...elasticbeanstalk.com` est normal. L’important est que **api.smart-odc.com** soit en HTTPS et que les fronts l’utilisent.

---

## 🐛 Si “tout est parti” après un changement

Si après avoir “mis en HTTPS” quelque chose s’est cassé, vérifier :

1. **DNS** : `api.smart-odc.com` pointe bien vers le load balancer Beanstalk (A Alias ou CNAME).
2. **Listener 443** : présent sur le load balancer, avec le bon certificat ACM.
3. **Health checks** : toujours sur le bon port (80 ou 5000 selon votre config) et chemin.
4. **Sécurité du groupe** : le load balancer autorise le trafic 443 (et 80 si utilisé).
5. **Variables d’environnement** Beanstalk : pas d’URL en dur pointant vers l’ancienne config.

Si vous décrivez exactement ce qui ne marche plus (erreur navigateur, timeout, 502, etc.) après ces étapes, on peut cibler la cause (DNS, certificat, listener, ou app).

---

**Résumé** : le problème n’est pas la création de l’admin, mais le fait que le backend doit être servi en **HTTPS** sous **https://api.smart-odc.com** pour éviter le Mixed Content avec Amplify. Les étapes ci-dessus permettent de remettre cette configuration de façon propre.
