-- ============================================================================
-- Script d'Insertion de Données de Test pour ODL
-- Projet : Orange Digital Learning (ODL)
-- ============================================================================
-- Ce script insère des données de test pour permettre de visualiser
-- le fonctionnement de l'application sur le dashboard.
-- ============================================================================
-- IMPORTANT : Exécutez ce script APRÈS avoir créé la base de données
-- et laissé Hibernate créer les tables automatiquement.
-- ============================================================================

USE odcawslearning;

-- ============================================================================
-- ÉTAPE 1 : Insertion des Catégories (3 catégories)
-- ============================================================================
-- Les catégories doivent être créées en premier car les cours y font référence

INSERT INTO categorie (title, description, activate, created_at, last_modified_at, created_by, modified_by) VALUES
('Cloud Computing', 'Formations sur les technologies cloud : AWS, Azure, GCP. Apprenez à déployer, gérer et sécuriser des applications dans le cloud.', true, NOW(), NOW(), 'system', 'system'),
('Développement Web', 'Maîtrisez les technologies web modernes : React, Angular, Node.js, et les frameworks backend. Devenez un développeur full-stack compétent.', true, NOW(), NOW(), 'system', 'system'),
('Cybersécurité', 'Protégez les systèmes et les données contre les menaces. Apprenez l''éthique du hacking, la gestion des vulnérabilités et la sécurité réseau.', true, NOW(), NOW(), 'system', 'system');

-- ============================================================================
-- ÉTAPE 2 : Insertion d'une Cohorte (1 cohorte)
-- ============================================================================
-- La cohorte doit être créée avant les apprenants qui y sont liés

INSERT INTO cohorte (nom, description, date_debut, date_fin, activate, created_at, last_modified_at, created_by, modified_by) VALUES
('Cohorte 2025', 'Première cohorte de l''année 2025. Formation intensive de 6 mois sur les technologies cloud et le développement web.', '2025-01-15 09:00:00', '2025-07-15 18:00:00', true, NOW(), NOW(), 'system', 'system');

-- ============================================================================
-- ÉTAPE 3 : Insertion des Apprenants (5 apprenants)
-- ============================================================================
-- Les apprenants sont liés à la cohorte créée ci-dessus

INSERT INTO apprenants (nom, prenom, email, numero, profession, niveau_etude, filiere, cohorte_id, activate, created_at, last_modified_at, created_by, modified_by) VALUES
('Diallo', 'Amadou', 'amadou.diallo@example.com', '+221771234567', 'Étudiant', 'Licence', 'Informatique', 1, true, NOW(), NOW(), 'system', 'system'),
('Sarr', 'Fatou', 'fatou.sarr@example.com', '+221772345678', 'Développeur Junior', 'Master', 'Génie Logiciel', 1, true, NOW(), NOW(), 'system', 'system'),
('Ndiaye', 'Ibrahima', 'ibrahima.ndiaye@example.com', '+221773456789', 'Ingénieur', 'Master', 'Réseaux et Télécommunications', 1, true, NOW(), NOW(), 'system', 'system'),
('Ba', 'Aissatou', 'aissatou.ba@example.com', '+221774567890', 'Consultante', 'Master', 'Systèmes d''Information', 1, true, NOW(), NOW(), 'system', 'system'),
('Fall', 'Moussa', 'moussa.fall@example.com', '+221775678901', 'Étudiant', 'Licence', 'Mathématiques-Informatique', 1, true, NOW(), NOW(), 'system', 'system');

-- ============================================================================
-- ÉTAPE 4 : Insertion des Utilisateurs/Formateurs (2 formateurs)
-- ============================================================================
-- Les formateurs sont des utilisateurs avec le rôle USER ou ADMIN
-- Le mot de passe est "password123" hashé avec BCrypt
-- Hash BCrypt pour "password123" : $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy

INSERT INTO user (full_name, email, password, phone, admin, activate, avatar, role, learner_id, created_at, last_modified_at) VALUES
('Dr. Mamadou Kane', 'mamadou.kane@odl.sn', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '+221771111111', true, true, NULL, 'ADMIN', NULL, NOW(), NOW()),
('Prof. Awa Diop', 'awa.diop@odl.sn', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '+221772222222', true, true, NULL, 'USER', NULL, NOW(), NOW());

-- ============================================================================
-- ÉTAPE 5 : Insertion des Cours (4 cours)
-- ============================================================================
-- Les cours sont liés aux catégories créées précédemment
-- COURSE_TYPE peut être : REGISTER, LINK, ou PDF

INSERT INTO courses (title, description, image_path, duration, course_type, categorie_id, activate, created_at, last_modified_at, created_by, modified_by) VALUES
('AWS Fundamentals', 'Introduction complète à Amazon Web Services. Découvrez les services de base : EC2, S3, RDS, et Lambda. Parfait pour débuter dans le cloud computing.', '/images/aws-fundamentals.jpg', 40, 'REGISTER', 1, true, NOW(), NOW(), 'system', 'system'),
('React.js Avancé', 'Maîtrisez React avec les hooks, le state management avec Redux, et les patterns avancés. Créez des applications web modernes et performantes.', '/images/react-advanced.jpg', 60, 'REGISTER', 2, true, NOW(), NOW(), 'system', 'system'),
('Sécurité des Applications Web', 'Apprenez à sécuriser vos applications web contre les vulnérabilités courantes : XSS, CSRF, SQL Injection, et bien plus.', '/images/web-security.jpg', 50, 'PDF', 3, true, NOW(), NOW(), 'system', 'system'),
('Docker et Kubernetes', 'Conteneurisez vos applications avec Docker et orchestrez-les avec Kubernetes. Déployez des applications scalables et résilientes.', '/images/docker-k8s.jpg', 45, 'LINK', 1, true, NOW(), NOW(), 'system', 'system');

-- ============================================================================
-- VÉRIFICATION DES DONNÉES INSÉRÉES
-- ============================================================================

-- Afficher le nombre d'enregistrements par table
SELECT 'Catégories' AS Table_Name, COUNT(*) AS Count FROM categorie
UNION ALL
SELECT 'Cohortes', COUNT(*) FROM cohorte
UNION ALL
SELECT 'Apprenants', COUNT(*) FROM apprenants
UNION ALL
SELECT 'Utilisateurs', COUNT(*) FROM user
UNION ALL
SELECT 'Cours', COUNT(*) FROM courses;

-- ============================================================================
-- NOTES IMPORTANTES :
-- ============================================================================
-- 1. Les mots de passe des formateurs sont : "password123"
--    (hash BCrypt : $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy)
--
-- 2. Les IDs sont auto-générés, donc les valeurs peuvent varier.
--    Si vous réexécutez ce script, ajustez les IDs dans les INSERT suivants.
--
-- 3. Les dates sont générées automatiquement avec NOW().
--
-- 4. Les images des cours sont des chemins fictifs. Vous devrez uploader
--    de vraies images via l'interface pour qu'elles s'affichent.
--
-- 5. Pour créer des comptes utilisateurs pour les apprenants, utilisez
--    l'endpoint /auth/create-learner/{cohorteId} via l'API.
-- ============================================================================

