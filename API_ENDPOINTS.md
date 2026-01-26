# Points d'Accès API - Témoignages

Cette section documente les points d'accès (endpoints) relatifs aux témoignages.

## Base URL de l'API (avec chemin de contexte)
La base URL de l'API est construite à partir du `server.servlet.context-path` défini dans `application.properties`.
Pour cet environnement, la base URL est :
`http://localhost:5000/awsodclearning` (ou `https://api.smart-odc.com/awsodclearning` en production)

---

### Endpoints : Témoignages (`/api/testimonials`)

| Méthode | Endpoint Complet                                 | Description                                 | Sécurité      |
| :------ | :----------------------------------------------- | :------------------------------------------ | :------------ |
| `POST`  | `/awsodclearning/api/testimonials`             | Ajoute un nouveau témoignage.               | Authentifié   |
| `GET`   | `/awsodclearning/api/testimonials`             | Récupère la liste de tous les témoignages. | Public        |
| `GET`   | `/awsodclearning/api/testimonials/user/{userId}` | Récupère les témoignages d'un utilisateur spécifique. | Public        |