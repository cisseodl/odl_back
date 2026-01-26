
## Endpoint pour la soumission de Témoignages

### POST /api/testimonials

*   **Description :** Permet à un utilisateur authentifié de soumettre un témoignage pour la plateforme.
*   **Méthode HTTP :** `POST`
*   **URL de l'endpoint :** `/api/testimonials`
*   **Corps de la requête (Request Body - JSON) :**
    ```json
    {
      "content": "Votre témoignage ici..."
    }
    ```
    *   `content` (Type: `String`) : Le texte du témoignage.
*   **Réponse en cas de succès (200 OK) :**
    *   Un message de succès ou l'objet du témoignage créé.
    *   Exemple : `{ "success": true, "message": "Témoignage soumis avec succès." }`
*   **Codes d'erreur possibles :**
    *   `400 Bad Request` : Si le `content` est vide ou ne respecte pas les règles de validation (par exemple, longueur minimale).
    *   `401 Unauthorized` : Si l'utilisateur n'est pas authentifié.
*   **Authentification :** Requiert un utilisateur authentifié.

### Directives d'implémentation Backend (Java/Spring Boot) :

1.  **Création de l'Entité `Testimonial`** (`com.odc.aws_learning.app.entity.Testimonial.java`) :
    *   Propriétés : `id` (Long, clé primaire), `content` (String), `user` (relation ManyToOne avec l'entité `User` pour l'auteur du témoignage), `createdAt` (LocalDateTime, date de création).

2.  **Création du Repository `TestimonialRepository`** (`com.odc.aws_learning.app.repository.TestimonialRepository.java`) :
    *   Interface étendant `JpaRepository<Testimonial, Long>`.

3.  **Création des DTOs `TestimonialRequest` et `TestimonialResponse`** :
    *   `TestimonialRequest` : Contient le champ `content` pour la réception du témoignage.
    *   `TestimonialResponse` : Peut inclure l'ID, le contenu, l'auteur (utilisateur simplifié) et la date de création.

4.  **Création du Service `TestimonialService`** (`com.odc.aws_learning.app.service.TestimonialService.java`) :
    *   Méthode `addTestimonial(TestimonialRequest request, User user)` :
        *   Effectue les validations nécessaires sur le `request.content`.
        *   Crée une nouvelle instance de l'entité `Testimonial`.
        *   Associe le `user` authentifié au témoignage.
        *   Enregistre le témoignage via `TestimonialRepository`.
        *   Retourne le `TestimonialResponse` de l'objet créé.

5.  **Création du Contrôleur `TestimonialController`** (`com.odc.aws_learning.app.controller.TestimonialController.java`) :
    *   Annotation `@RestController` et `@RequestMapping("/api/testimonials")`.
    *   Méthode `POST` pour `/` (ou l'URL de base) :
        *   Accepte `@RequestBody TestimonialRequest request`.
        *   Utilise `@AuthenticationPrincipal UserDetails userDetails` (ou un type `User` personnalisé si défini) pour récupérer l'utilisateur authentifié.
        *   Appelle `TestimonialService.addTestimonial()`.
        *   Retourne un `ResponseEntity` avec le statut approprié (ex: `HttpStatus.CREATED` ou `HttpStatus.OK`) et la réponse.

6.  **Mise à jour de la Configuration de Sécurité (Spring Security) :**
    *   S'assurer que l'endpoint `POST /api/testimonials` est autorisé pour les utilisateurs authentifiés.

---
