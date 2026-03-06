# Documentation Technique - ODL Learning Platform

## 📁 1. ARBORESCENCE DES FICHIERS (File Tree)

### Structure du Projet

```
odc_learning_api-master/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/odc/aws_learning/
│   │   │       ├── AwsLearningApplication.java          # Classe principale Spring Boot
│   │   │       ├── ServletInitializer.java              # Initialiseur pour déploiement WAR
│   │   │       ├── LabDataLoader.java                    # CommandLineRunner : Charge les labs exemples
│   │   │       ├── PasswordResetRunner.java             # CommandLineRunner : Réinitialise les mots de passe
│   │   │       │
│   │   │       ├── app/                                 # Module principal de l'application
│   │   │       │   ├── config/
│   │   │       │   │   └── S3Config.java                # Configuration AWS S3
│   │   │       │   │
│   │   │       │   ├── constante/
│   │   │       │   │   ├── COURSE_TYPE.java             # Enum pour les types de cours
│   │   │       │   │   ├── Enumeration.java            # Enums divers (COURSE_STATUT, etc.)
│   │   │       │   │   └── UploadLink.java             # Constantes pour les chemins d'upload
│   │   │       │   │
│   │   │       │   ├── controller/                     # 17 Contrôleurs REST
│   │   │       │   │   ├── AnswerController.java
│   │   │       │   │   ├── ApprenantController.java
│   │   │       │   │   ├── CategorieController.java
│   │   │       │   │   ├── CertificateController.java
│   │   │       │   │   ├── ChapterController.java
│   │   │       │   │   ├── CohorteController.java
│   │   │       │   │   ├── ConfigurationController.java
│   │   │       │   │   ├── CoursesController.java       # ⭐ Nouveau : POST /courses/enroll/{courseId}
│   │   │       │   │   ├── DashboardController.java
│   │   │       │   │   ├── DownloadController.java
│   │   │       │   │   ├── EvaluationsController.java
│   │   │       │   │   ├── FileController.java
│   │   │       │   │   ├── LabController.java           # ⭐ Module Labs (nouveau)
│   │   │       │   │   ├── LearnerChapterController.java
│   │   │       │   │   ├── QuestionsController.java
│   │   │       │   │   ├── QuizController.java
│   │   │       │   │   └── ReponsesController.java
│   │   │       │   │
│   │   │       │   ├── entity/                          # 20 Entités JPA
│   │   │       │   │   ├── Answer.java
│   │   │       │   │   ├── Apprenant.java
│   │   │       │   │   ├── Categorie.java
│   │   │       │   │   ├── Chapter.java
│   │   │       │   │   ├── Cohorte.java
│   │   │       │   │   ├── Configuration.java
│   │   │       │   │   ├── Courses.java                 # ⚠️ Champ 'price' supprimé
│   │   │       │   │   ├── DetailsCourse.java           # Table d'enrollment (User ↔ Course)
│   │   │       │   │   ├── Evaluations.java
│   │   │       │   │   ├── InfoTest.java
│   │   │       │   │   ├── LabDefinition.java           # ⭐ Nouveau : Définition d'un lab
│   │   │       │   │   ├── LabSession.java              # ⭐ Nouveau : Session de lab utilisateur
│   │   │       │   │   ├── LabSessionStatus.java        # ⭐ Enum : STARTING, RUNNING, STOPPED, SUBMITTED
│   │   │       │   │   ├── LearnerChapter.java
│   │   │       │   │   ├── Questions.java
│   │   │       │   │   ├── Quiz.java
│   │   │       │   │   ├── QuizQuestion.java
│   │   │       │   │   ├── QuizReponse.java
│   │   │       │   │   ├── Reponses.java
│   │   │       │   │   └── UserQuizAttempt.java
│   │   │       │   │   # ❌ SUPPRIMÉ : Order.java, OrderItem.java, PaymentTransaction.java
│   │   │       │   │
│   │   │       │   ├── repository/                      # 20 Repositories JPA
│   │   │       │   │   ├── AnswerRepository.java
│   │   │       │   │   ├── ApprenantRepository.java
│   │   │       │   │   ├── CategorieRepository.java
│   │   │       │   │   ├── ChapterRepository.java
│   │   │       │   │   ├── CohorteRepository.java
│   │   │       │   │   ├── ConfigurationRepository.java
│   │   │       │   │   ├── CoursesRepository.java
│   │   │       │   │   ├── DetailsCourseRepo.java
│   │   │       │   │   ├── EvaluationsRepository.java
│   │   │       │   │   ├── InfotestRepository.java
│   │   │       │   │   ├── LabDefinitionRepository.java # ⭐ Nouveau
│   │   │       │   │   ├── LabSessionRepository.java    # ⭐ Nouveau
│   │   │       │   │   ├── LearnerChapterRepository.java
│   │   │       │   │   ├── QuestionsRepository.java
│   │   │       │   │   ├── QuizQuestionRepository.java
│   │   │       │   │   ├── QuizReponseRepository.java
│   │   │       │   │   ├── QuizRepository.java
│   │   │       │   │   ├── ReponsesRepository.java
│   │   │       │   │   └── UserQuizAttemptRepository.java
│   │   │       │   │   # ❌ SUPPRIMÉ : OrderRepository.java, OrderItemRepository.java, PaymentTransactionRepository.java
│   │   │       │   │
│   │   │       │   ├── service/                         # 18 Services
│   │   │       │   │   ├── AnswerService.java
│   │   │       │   │   ├── ApprenantService.java
│   │   │       │   │   ├── CategorieService.java
│   │   │       │   │   ├── CertificateService.java     # Génération de PDF certificats
│   │   │       │   │   ├── ChapterService.java
│   │   │       │   │   ├── CohorteService.java
│   │   │       │   │   ├── ConfigurationService.java
│   │   │       │   │   ├── CourseService.java
│   │   │       │   │   ├── DashboardService.java
│   │   │       │   │   ├── EvaluationsService.java
│   │   │       │   │   ├── LabService.java              # ⭐ Nouveau : Gestion des labs
│   │   │       │   │   ├── LearnerService.java
│   │   │       │   │   ├── QuestionsService.java
│   │   │       │   │   ├── QuizService.java
│   │   │       │   │   ├── ReponsesService.java
│   │   │       │   │   ├── S3Service.java              # Upload vers AWS S3
│   │   │       │   │   ├── SendEmailService.java
│   │   │       │   │   └── UploadFileService.java
│   │   │       │   │   # ❌ SUPPRIMÉ : OrderService.java, PaymentService.java
│   │   │       │   │
│   │   │       │   └── wrapper/                         # DTOs (Data Transfer Objects)
│   │   │       │       ├── ChapterAndCoursePayload.java
│   │   │       │       ├── ChapterPayload.java
│   │   │       │       ├── ConfigurationDto.java
│   │   │       │       ├── DashboardStatsDTO.java
│   │   │       │       ├── Evaluations_QuestionsReponses.java
│   │   │       │       ├── Question_Reponses.java
│   │   │       │       ├── Quiz_Answer.java
│   │   │       │       ├── QuizDTO.java
│   │   │       │       ├── QuizResultDTO.java
│   │   │       │       ├── QuizSubmissionDTO.java
│   │   │       │       └── ValidateChapter.java
│   │   │       │       # ❌ SUPPRIMÉ : OrderSummaryDTO.java, CreateOrderRequest.java, ConfirmPaymentRequest.java
│   │   │       │
│   │   │       ├── auth/                                # Module d'authentification
│   │   │       │   ├── base/
│   │   │       │   │   ├── entity/
│   │   │       │   │   │   ├── BaseEntity.java         # Classe abstraite (id, createdAt, etc.)
│   │   │       │   │   │   └── CEntity.java            # Interface
│   │   │       │   │   └── response/
│   │   │       │   │       ├── CResponse.java          # Wrapper de réponse standardisé
│   │   │       │   │       ├── CResponses.java
│   │   │       │   │       ├── PageData.java
│   │   │       │   │       └── utils/
│   │   │       │   │           ├── Enumeration.java
│   │   │       │   │           └── Utils.java
│   │   │       │   │
│   │   │       │   ├── config/
│   │   │       │   │   ├── CORSFilter.java
│   │   │       │   │   ├── JwtAuthenticationFilter.java # Filtre JWT
│   │   │       │   │   ├── SecurityConfiguration.java  # Configuration Spring Security
│   │   │       │   │   └── SecurityConstants.java
│   │   │       │   │
│   │   │       │   ├── controller/
│   │   │       │   │   ├── AuthenticationController.java
│   │   │       │   │   ├── AuthorizationController.java
│   │   │       │   │   └── UserController.java
│   │   │       │   │
│   │   │       │   ├── dao/
│   │   │       │   │   ├── request/
│   │   │       │   │   │   ├── SigninRequest.java
│   │   │       │   │   │   ├── SignUpRequest.java
│   │   │       │   │   │   └── UpdatePass.java
│   │   │       │   │   └── response/
│   │   │       │   │       └── JwtAuthenticationResponse.java
│   │   │       │   │
│   │   │       │   ├── entities/
│   │   │       │   │   ├── Role.java                   # Enum : USER, LEARNER, ADMIN, SUPERADMIN
│   │   │       │   │   └── User.java                   # ⚠️ Relation Order supprimée
│   │   │       │   │
│   │   │       │   ├── repository/
│   │   │       │   │   └── UserRepository.java
│   │   │       │   │
│   │   │       │   └── service/
│   │   │       │       ├── AuthenticationService.java
│   │   │       │       ├── JwtService.java
│   │   │       │       ├── UserService.java
│   │   │       │       └── impl/
│   │   │       │           ├── AuthenticationServiceImpl.java
│   │   │       │           ├── JwtServiceImpl.java
│   │   │       │           └── UserServiceImpl.java
│   │   │       │
│   │   │       └── configuration/
│   │   │           └── OpenApiConfig.java              # Configuration Swagger/OpenAPI
│   │   │           # ❌ SUPPRIMÉ : StripeConfig.java
│   │   │
│   │   └── resources/
│   │       ├── application.properties                  # ⚠️ Configuration Stripe supprimée
│   │       └── ...
│   │
│   └── test/
│       └── java/
│           └── com/odc/aws_learning/
│               └── ...
│
└── pom.xml                                              # ⚠️ Dépendance Stripe conservée (non utilisée)
```

### Statistiques

- **Contrôleurs** : 20 (17 dans `app.controller` + 3 dans `auth.controller`)
- **Entités** : 20 (héritent de `BaseEntity`)
- **Repositories** : 20
- **Services** : 17
- **DTOs** : 11

---

## 🌐 2. LISTE EXHAUSTIVE DES ENDPOINTS API

### Base URL
```
https://api.smart-odc.com/
```

### Endpoints par Module

#### 🔐 **Authentification & Autorisation** (`/auth`, `/users`, `/resource`)

| Méthode | Endpoint | Description | Sécurité |
|---------|----------|-------------|----------|
| `POST` | `/auth/signup` | Inscription d'un nouvel utilisateur | `permitAll()` |
| `POST` | `/auth/signin` | Connexion (obtention du JWT) | `permitAll()` |
| `GET` | `/auth/forget-pass/{username}` | Demande de réinitialisation de mot de passe | `permitAll()` |
| `POST` | `/auth/change-pass` | Changement de mot de passe | `authenticated()` |
| `POST` | `/auth/create-learner/{cohorteId}` | Création d'un apprenant (avec photo) | `authenticated()` |
| `GET` | `/auth/check-availability` | Vérification de disponibilité de l'API | `permitAll()` |
| `GET` | `/users/get-all/{page}/{size}` | Liste paginée des utilisateurs | `ADMIN` |
| `GET` | `/users/check/{phone}` | Vérifier si un utilisateur existe par téléphone | `permitAll()` |
| `GET` | `/resource` | Endpoint de test d'autorisation | `authenticated()` |

---

#### 📚 **Cours** (`/courses`)

| Méthode | Endpoint | Description | Sécurité |
|---------|----------|-------------|----------|
| `POST` | `/courses/save/{catId}` | Créer un cours (avec image et CourseCreationRequest) | `ADMIN` |
| `GET` | `/courses/read/{id}` | Récupérer un cours par ID (retourne CourseDto) | `permitAll()` |
| `PUT` | `/courses/{id}` | Mettre à jour un cours (avec image et CourseUpdateRequest) | `ADMIN` |
| `DELETE` | `/courses/delete/{id}` | Supprimer un cours (retourne CResponse<?>) | `ADMIN` |
| `GET` | `/courses/read` | Liste de tous les cours (avec filtre et pagination) | `USER, ADMIN, LEARNER` |
| `POST` | `/courses/enroll/{courseId}` | ⭐ **S'inscrire à un cours (gratuit)** | `USER, ADMIN, LEARNER` |

---

#### 📝 **Quiz & Évaluations** (`/quiz`, `/evaluations`, `/questions`, `/answers`)

| Méthode | Endpoint | Description | Sécurité |
|---------|----------|-------------|----------|
| `POST` | `/quiz/create` | Créer un quiz complet (questions/réponses) | `ADMIN` |
| `GET` | `/quiz/course/{courseId}` | Liste des quiz d'un cours | `USER, ADMIN, LEARNER` |
| `POST` | `/quiz/submit` | Soumettre un quiz et obtenir le score | `USER, ADMIN, LEARNER` |
| `POST` | `/evaluations/save` | Créer une évaluation (ancien système) | `permitAll()` |
| `GET` | `/evaluations/get-all` | Liste de toutes les évaluations | `USER, ADMIN, LEARNER` |
| `POST` | `/questions/save` | Créer une question | `ADMIN` |
| `GET` | `/questions/get-all` | Liste de toutes les questions | `USER, ADMIN, LEARNER` |
| `POST` | `/answers/save` | Enregistrer une réponse | `ADMIN` |
| `POST` | `/answers/save-learner-test` | Enregistrer les réponses d'un apprenant | `USER, ADMIN, LEARNER` |
| `GET` | `/answers/get-all` | Liste de toutes les réponses | `USER, ADMIN, LEARNER` |

---

#### 🏆 **Certificats** (`/certificates`)

| Méthode | Endpoint | Description | Sécurité |
|---------|----------|-------------|----------|
| `GET` | `/certificates/download/{quizId}` | Télécharger un certificat PDF | `USER, LEARNER, ADMIN` |

---

#### 🧪 **Labs / Environnements Pratiques** (`/api/labs`)

| Méthode | Endpoint | Description | Sécurité |
|---------|----------|-------------|----------|
| `GET` | `/api/labs/` | Liste de tous les labs disponibles | `USER, ADMIN, LEARNER` |
| `POST` | `/api/labs/start/{labId}` | Démarrer une session de lab | `USER, ADMIN, LEARNER` |
| `POST` | `/api/labs/stop/{sessionId}` | Arrêter une session de lab | `USER, ADMIN, LEARNER` |
| `POST` | `/api/labs/submit/{sessionId}` | Soumettre le résultat d'un lab | `USER, ADMIN, LEARNER` |
| `GET` | `/api/labs/my-sessions` | Historique des sessions de l'utilisateur | `USER, ADMIN, LEARNER` |

---

#### 📊 **Tableau de Bord** (`/dashboard`)

| Méthode | Endpoint | Description | Sécurité |
|---------|----------|-------------|----------|
| `GET` | `/dashboard/summary` | Statistiques (étudiant ou admin selon le rôle) | `authenticated()` |

---

#### 📁 **Fichiers & Upload** (`/api/files`, `/downloads`)

| Méthode | Endpoint | Description | Sécurité |
|---------|----------|-------------|----------|
| `GET` | `/api/files/{filename}` | Servir un fichier local | `permitAll()` |
| `POST` | `/api/files/upload` | Uploader un fichier vers S3 | `authenticated()` |
| `GET` | `/downloads/{folderName}/{fileName}` | Télécharger un fichier | `permitAll()` |

---

#### 📑 **Chapitres** (`/chapters`)

| Méthode | Endpoint | Description | Sécurité |
|---------|----------|-------------|----------|
| `POST` | `/chapters/save` | Créer un chapitre (avec PDF) | `ADMIN` |
| `GET` | `/chapters/course/{courseId}` | Liste des chapitres d'un cours | `USER, ADMIN, LEARNER` |

---

#### 🏷️ **Catégories** (`/categorie`)

| Méthode | Endpoint | Description | Sécurité |
|---------|----------|-------------|----------|
| `GET` | `/categorie/read` | Liste de toutes les catégories | `USER, ADMIN, LEARNER` |
| `GET` | `/categorie/read/{id}` | Récupérer une catégorie par ID | `USER, ADMIN, LEARNER` |
| `POST` | `/categorie/save` | Créer une catégorie | `ADMIN` |
| `PUT` | `/categorie/update` | Mettre à jour une catégorie | `ADMIN` |
| `DELETE` | `/categorie/delete/{id}` | Supprimer une catégorie | `ADMIN` |

---

#### 👥 **Apprenants & Cohortes** (`/apprenants`, `/cohorte`)

| Méthode | Endpoint | Description | Sécurité |
|---------|----------|-------------|----------|
| `POST` | `/apprenants/save` | Créer un apprenant | `authenticated()` |
| `POST` | `/apprenants/save` | Créer un apprenant | `ADMIN` |
| `GET` | `/apprenants/get-all` | Liste de tous les apprenants | `USER, ADMIN, LEARNER` |
| `GET` | `/apprenants/get-by-cohorte/{cohorteId}/{page}/{size}` | Apprenants par cohorte (paginé) | `USER, ADMIN, LEARNER` |
| `GET` | `/cohorte/read` | Liste de toutes les cohortes | `USER, ADMIN, LEARNER` |
| `GET` | `/cohorte/read/{id}` | Récupérer une cohorte par ID | `USER, ADMIN, LEARNER` |
| `DELETE` | `/cohorte/delete` | Supprimer une cohorte | `ADMIN` |
| `POST` | `/cohorte/save` | Créer une cohorte | `ADMIN` |
| `PUT` | `/cohorte/update` | Mettre à jour une cohorte | `ADMIN` |
| `DELETE` | `/cohorte/delete` | Supprimer une cohorte | `ADMIN` |

---

#### ⚙️ **Configuration** (`/configurations`)

| Méthode | Endpoint | Description | Sécurité |
|---------|----------|-------------|----------|
| `GET` | `/configurations/get-config` | Récupérer la configuration de la plateforme | `permitAll()` |
| `POST` | `/configurations/update` | Mettre à jour la configuration | `ADMIN` |

---

#### 📄 **Réponses** (`/reponses`)

| Méthode | Endpoint | Description | Sécurité |
|---------|----------|-------------|----------|
| `POST` | `/reponses/save` | Enregistrer une réponse | `ADMIN` |
| `GET` | `/reponses/get-all` | Liste de toutes les réponses | `USER, ADMIN, LEARNER` |

---

#### 📖 **Chapitres Apprenants** (`/learnerchapter`)

| Méthode | Endpoint | Description | Sécurité |
|---------|----------|-------------|----------|
| `POST` | `/learnerchapter/save` | Marquer un chapitre comme complété | `USER, ADMIN, LEARNER` |

---

#### 📚 **Documentation API** (Swagger)

| Méthode | Endpoint | Description | Sécurité |
|---------|----------|-------------|----------|
| `GET` | `/swagger-ui.html` | Interface Swagger UI | `permitAll()` |
| `GET` | `/swagger-ui/**` | Assets Swagger UI | `permitAll()` |
| `GET` | `/v3/api-docs/**` | Documentation OpenAPI (JSON) | `permitAll()` |

---

### Résumé des Endpoints

- **Total** : **55+ endpoints**
- **Publics** (`permitAll()`) : **~12 endpoints** (auth, config, swagger, downloads, evaluations/save)
- **Authentifiés** (`authenticated()`) : **~5 endpoints** (resource, change-pass, create-learner)
- **RBAC** (`hasRole()` / `hasAnyRole()`) : **~38 endpoints** (majorité des endpoints métier)

---

## 🔧 3. DÉPENDANCES CLÉS (pom.xml)

### Informations du Projet

- **Group ID** : `com.odc`
- **Artifact ID** : `aws_learning`
- **Version** : `0.0.1-SNAPSHOT`
- **Packaging** : `jar`
- **Final Name** : `awsodclearning`

### Versions Principales

| Technologie | Version | Description |
|-------------|---------|-------------|
| **Java** | `11` | Version du JDK |
| **Spring Boot** | `2.7.14` | Framework principal |
| **Maven** | (hérité) | Gestionnaire de dépendances |

---

### Dépendances Core Spring Boot

| Dépendance | Version | Usage |
|------------|---------|-------|
| `spring-boot-starter-web` | 2.7.14 | REST API, Tomcat embarqué |
| `spring-boot-starter-data-jpa` | 2.7.14 | JPA/Hibernate, Spring Data |
| `spring-boot-starter-security` | 2.7.14 | Spring Security, authentification |
| `spring-boot-starter-mail` | 2.7.14 | Envoi d'emails (SMTP) |
| `spring-boot-starter-tomcat` | 2.7.14 | Serveur Tomcat embarqué |
| `spring-boot-starter-test` | 2.7.14 | Tests (scope: test) |
| `spring-security-test` | (hérité) | Tests de sécurité (scope: test) |

---

### Base de Données

| Dépendance | Version | Usage |
|------------|---------|-------|
| `mysql-connector-j` | (runtime) | Driver MySQL 8.0 |
| **ORM** | Hibernate (via Spring Data JPA) | Mapping objet-relationnel |
| **Pool de Connexions** | HikariCP (par défaut) | Gestion des connexions |

---

### Sécurité & Authentification

| Dépendance | Version | Usage |
|------------|---------|-------|
| `jjwt-api` | `0.11.5` | API JWT (JSON Web Tokens) |
| `jjwt-impl` | `0.11.5` | Implémentation JWT |
| `jjwt-jackson` | `0.11.5` | Sérialisation JSON pour JWT |

---

### Documentation API

| Dépendance | Version | Usage |
|------------|---------|-------|
| `springdoc-openapi-ui` | `1.7.0` | Swagger UI pour Spring Boot 2.x |

---

### Génération de PDF

| Dépendance | Version | Usage |
|------------|---------|-------|
| `itextpdf` | `5.5.13.3` | Génération de certificats PDF |

---

### Cloud & Stockage

| Dépendance | Version | Usage |
|------------|---------|-------|
| `aws-java-sdk-s3` | `1.12.793` | AWS S3 SDK (v1.x) pour upload de fichiers |

---

### Paiement (⚠️ Non utilisée - à supprimer si souhaité)

| Dépendance | Version | Usage |
|------------|---------|-------|
| `stripe-java` | `31.0.0` | ⚠️ **Conservée mais non utilisée** (module E-commerce supprimé) |

---

### Utilitaires

| Dépendance | Version | Usage |
|------------|---------|-------|
| `lombok` | (hérité) | Génération automatique de code (getters, setters, etc.) |
| `commons-lang3` | (hérité) | Utilitaires Apache Commons |
| `javax.servlet-api` | `4.0.1` | API Servlet (pour filtres) |

---

### Plugins Maven

| Plugin | Version | Usage |
|--------|---------|-------|
| `spring-boot-maven-plugin` | (hérité) | Packaging JAR exécutable |
| `maven-surefire-plugin` | `2.10` | Exécution des tests |

---

### Configuration Docker (Recommandée)

- **MySQL 8.0** : Image `mysql:8.0`
- **Port** : `3306`
- **Base de données** : `odcawslearning`
- **Credentials** : `root` / `root` (par défaut)

---

## 📋 Notes Importantes

### ⚠️ Modules Supprimés

- ❌ **E-commerce** : Toutes les entités, services, controllers liés aux commandes et paiements ont été supprimés
- ❌ **Stripe** : Configuration supprimée (dépendance conservée dans `pom.xml` mais non utilisée)
- ✅ **Inscription gratuite** : Nouveau endpoint `POST /courses/enroll/{courseId}` pour inscription directe

### ⭐ Modules Ajoutés Récemment

- ✅ **Labs** : Module complet pour les environnements pratiques (sandbox)
- ✅ **Quiz** : Système d'évaluation avec calcul de scores
- ✅ **Certificats** : Génération de PDF pour les quiz réussis
- ✅ **Dashboard** : Statistiques personnalisées par rôle

---

## 🔗 URLs Importantes

- **API Base** : `http://localhost:8080/awsodclearning`
- **Swagger UI** : `http://localhost:8080/awsodclearning/swagger-ui.html`
- **API Docs JSON** : `http://localhost:8080/awsodclearning/v3/api-docs`
- **Frontend** : `http://localhost:4200` (présumé)

---

## 📝 Format des Réponses

Tous les endpoints retournent un objet `CResponse<T>` avec :
- `ok: boolean` - Indique si la requête a réussi
- `data: T` - Données de la réponse
- `message: String` - Message descriptif

---

*Documentation générée le : Date actuelle*
*Version de l'application : 0.0.1-SNAPSHOT*
