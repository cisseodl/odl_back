# TD vs Évaluations (examen de fin de cours)

Ce document clarifie la différence entre **TD (Travaux Dirigés)** et **Évaluations** (examen de fin de cours / certification). Les deux sont gérés dans la même table backend `evaluations` mais ont des rôles distincts.

---

## 1. TD — Travaux Dirigés

| Aspect | Description |
|--------|-------------|
| **Associé à** | Une **leçon** (lesson) |
| **Type en base** | `Evaluations.EvaluationType.TP` |
| **Où on le crée** | Dashboard instructeur → **TDs** (onglet / page TDs) |
| **Champs utilisés** | `courseId`, **`lessonId`** (obligatoire), `tpInstructions`, `tpFileUrl`, type = TP |
| **Côté apprenant** | Affiché dans la section **« Activités associées à cette leçon »** (Labs, **TD**, Quiz) sous chaque leçon. L’apprenant fait le TD dans le contexte d’une leçon. |

En résumé : le TD est un travail pratique **lié à une leçon** (ex. exercice après une vidéo).

---

## 2. Évaluation — Examen de fin de cours (certification)

| Aspect | Description |
|--------|-------------|
| **Associé à** | Le **cours** uniquement (pas de leçon) |
| **Type en base** | `Evaluations.EvaluationType.QUIZ` |
| **Leçon** | **`lesson_id` = null** (examen global du cours) |
| **Où on le crée** | Dashboard instructeur → **Évaluations** (création « Évaluation » avec type QUIZ, choix du cours, questions) |
| **Côté apprenant** | Proposé **une fois le cours terminé** (100 % des leçons). L’apprenant passe l’examen ; score ≥ 70 % → **certification** (certificat). Non affiché dans la section Activités des leçons. |

En résumé : l’évaluation est un **examen de fin de parcours** pour valider la compréhension du cours et débloquer la certification.

---

## 3. Récap côté dashboard instructeur

| Création | Entité / type | Liaison | Affichage apprenant |
|----------|----------------|--------|----------------------|
| **Cours** | Course | - | Catalogue, inscription |
| **Module** | Module | Course | Contenu du cours |
| **Leçon** | Lesson | Module | Contenu par leçon |
| **Lab** | LabDefinition | **Lesson** | Section Activités de la leçon |
| **TD** | Evaluations (type **TP**) | **Lesson** + Course | Section Activités de la leçon |
| **Quiz** | Quiz | **Lesson** | Section Activités de la leçon |
| **Évaluation** | Evaluations (type **QUIZ**, **sans leçon**) | **Course** uniquement | Examen de fin de cours → certification |

---

## 4. Implémentation technique

- **Backend**  
  - `EvaluationsRepository.findCourseExamsByCourseId(courseId)` : retourne les évaluations de type QUIZ **et** `lesson IS NULL` (examen de fin de cours).  
  - `getCourseExam(courseId)` utilise cette méthode (pas tous les QUIZ du cours).  
  - Les TD sont les évaluations de type TP ; elles ont une `lesson` renseignée.

- **Front apprenant**  
  - Section « Activités associées à cette leçon » : Labs + **TD (getTPsByCourse filtré par leçon)** + Quiz. Pas d’« Évaluation » (examen) ici.  
  - Examen de fin de cours : appel à `getCourseExam(courseId)` lorsque le cours est complété ; redirection vers l’examen puis résultats / certificat si ≥ 70 %.

- **Admin / instructeur**  
  - **TDs** : formulaire avec choix de la **leçon** (et du cours).  
  - **Évaluations** : formulaire avec choix du **cours** et type QUIZ, sans leçon (examen de certification).
