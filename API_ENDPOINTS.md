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
