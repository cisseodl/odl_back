# Documentation des Endpoints API

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
*   **Réponse en cas de succès (200 OK) :** `CResponse<TestimonialResponse>`
    *   Retourne les détails du témoignage soumis.
*   **Codes d'erreur possibles :**
    *   `400 Bad Request` : Si le `content` est vide ou ne respecte pas les règles de validation.
    *   `401 Unauthorized` : Si l'utilisateur n'est pas authentifié.
*   **Authentification :** Requiert un utilisateur authentifié (`@PreAuthorize("isAuthenticated()")`).

### GET /api/testimonials

*   **Description :** Permet de récupérer tous les témoignages soumis.
*   **Méthode HTTP :** `GET`
*   **URL de l'endpoint :** `/api/testimonials`
*   **Authentification :** Non spécifié, mais souvent pour les utilisateurs authentifiés, ou peut être public. (Dans l'implémentation, il n'y a pas de `@PreAuthorize` donc il est public si la configuration globale l'autorise ou authentifié par `anyRequest().authenticated()` s'il n'est pas dans `permitAll`).
*   **Réponse (200 OK) :** `CResponse<List<TestimonialResponse>>`
    *   Retourne une liste d'objets `TestimonialResponse`.

### GET /api/testimonials/user/{userId}

*   **Description :** Permet de récupérer tous les témoignages soumis par un utilisateur spécifique.
*   **Méthode HTTP :** `GET`
*   **URL des exemples :**
    *   `GET /api/testimonials/user/123`
*   **Path Variable :**
    *   `userId` (Type: `Long`) : L'identifiant unique de l'utilisateur dont on veut récupérer les témoignages.
*   **Authentification :** Non spécifié. (Dans l'implémentation, il n'y a pas de `@PreAuthorize` donc il est public si la configuration globale l'autorise ou authentifié par `anyRequest().authenticated()` s'il n'est pas dans `permitAll`).
*   **Réponse (200 OK) :** `CResponse<List<TestimonialResponse>>`
    *   Retourne une liste d'objets `TestimonialResponse`.

---

## Endpoints pour la gestion des Avis (Reviews)

### POST /api/courses/{courseId}/reviews

*   **Description :** Permet à un utilisateur authentifié de soumettre un avis (note et commentaire) pour un cours spécifique.
*   **Méthode HTTP :** `POST`
*   **URL des exemples :**
    *   `POST /api/courses/123/reviews?rating=5&comment=Excellent%20cours%20!`
*   **Path Variable :**
    *   `courseId` (Type: `Long`) : L'identifiant unique du cours concerné.
*   **Paramètres de requête (`@RequestParam`) :**
    *   `rating` (Type: `Integer`) : La note donnée au cours (par exemple, de 1 à 5).
    *   `comment` (Type: `String`) : Le texte de l'avis.
*   **Authentification :** Requiert un utilisateur authentifié.
*   **Réponse (200 OK) :** `CResponse<ReviewResponseDto>`
    *   Retourne les détails de l'avis soumis, incluant les informations simplifiées de l'utilisateur et du cours.

### GET /api/courses/{courseId}/reviews

*   **Description :** Permet de récupérer tous les avis d'un cours spécifique. La réponse inclut désormais le nom de l'utilisateur ayant posté l'avis et le titre du cours.
*   **Méthode HTTP :** `GET`
*   **URL des exemples :**
    *   `GET /api/courses/123/reviews`
*   **Path Variable :**
    *   `courseId` (Type: `Long`) : L'identifiant unique du cours.
*   **Authentification :** Requiert un utilisateur authentifié.
*   **Réponse (200 OK) :** `CResponse<List<ReviewResponseDto>>`
    *   Retourne une liste d'objets `ReviewResponseDto`.

### GET /api/reviews/all

*   **Description :** Permet de récupérer tous les avis de la plateforme. La réponse inclut désormais le nom de l'utilisateur ayant posté l'avis et le titre du cours.
*   **Méthode HTTP :** `GET`
*   **URL des exemples :**
    *   `GET /api/reviews/all`
*   **Authentification :** Requiert le rôle `ADMIN` (`@PreAuthorize("hasRole('ADMIN')")`).
*   **Réponse (200 OK) :** `CResponse<List<ReviewResponseDto>>`
    *   Retourne une liste d'objets `ReviewResponseDto`.

### DELETE /api/reviews/{reviewId}

*   **Description :** Permet à un administrateur de supprimer un avis par son ID.
*   **Méthode HTTP :** `DELETE`
*   **URL des exemples :**
    *   `DELETE /api/reviews/42`
*   **Path Variable :**
    *   `reviewId` (Type: `Long`) : L'identifiant unique de l'avis à supprimer.
*   **Authentification :** Requiert le rôle `ADMIN` (`@PreAuthorize("hasRole('ADMIN')")`).
*   **Réponse (200 OK) :** `CResponse<?>` (message de succès)
    *   Exemple : `{ "success": true, "message": "Review with ID: 42 deleted successfully." }`
*   **Réponse (404 Not Found) :** `CResponse<?>` (message d'erreur si l'avis n'est pas trouvé)
    *   Exemple : `{ "success": false, "message": "Review not found with ID: 99." }`