-- ============================================================================
-- Script SQL pour créer l'utilisateur ADMIN dans AWS RDS
-- ============================================================================
-- Ce script doit être exécuté dans DBeaver connecté à la base RDS
-- Base de données: odcawslearning
-- Host: awseb-e-rafruf9ypt-stack-awsebrdsdatabase-rfrjq9mjmb0m.ck5a2240egow.us-east-1.rds.amazonaws.com
--
-- Identifiants de connexion:
-- Email: cisseodl@gmail.com
-- Mot de passe: cisse@2025
-- ============================================================================

-- Étape 1: Vérifier l'état actuel
SELECT '=== ÉTAT ACTUEL ===' AS Status;
SELECT COUNT(*) AS nombre_utilisateurs FROM user;
SELECT COUNT(*) AS nombre_admins FROM admins;

-- Étape 2: Vérifier si l'utilisateur existe déjà
SELECT '=== VÉRIFICATION UTILISATEUR ===' AS Status;
SELECT * FROM user WHERE email = 'cisseodl@gmail.com';

-- Étape 3: Supprimer l'utilisateur existant s'il existe (pour repartir à zéro)
DELETE FROM admins WHERE user_id IN (SELECT id FROM user WHERE email = 'cisseodl@gmail.com');
DELETE FROM user WHERE email = 'cisseodl@gmail.com';

-- Étape 4: Créer l'utilisateur admin
-- IMPORTANT: Le hash BCrypt sera généré par Spring Boot au prochain redémarrage
-- Ce hash temporaire permet de créer l'utilisateur, mais le vrai hash sera appliqué
-- par le CommandLineRunner dans AwsLearningApplication.java

INSERT INTO user (
    full_name,
    email,
    password,
    phone,
    activate,
    avatar,
    role,
    created_at,
    last_modified_at
) VALUES (
    'CisseOdl',
    'cisseodl@gmail.com',
    -- Hash BCrypt temporaire pour "password123" (sera remplacé au démarrage par Spring Boot)
    -- Le vrai hash pour "cisse@2025" sera généré par Spring Security au démarrage
    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
    '0000000000',
    true,
    NULL,
    'ADMIN',
    NOW(),
    NOW()
);

-- Étape 5: Récupérer l'ID de l'utilisateur créé
SET @user_id = LAST_INSERT_ID();
SELECT CONCAT('ID utilisateur créé: ', @user_id) AS Status;

-- Étape 6: Créer l'entité Admin liée à l'utilisateur
INSERT INTO admins (
    user_id,
    activate,
    created_at,
    last_modified_at
) VALUES (
    @user_id,
    true,
    NOW(),
    NOW()
);

-- Étape 7: Récupérer l'ID de l'admin créé
SET @admin_id = LAST_INSERT_ID();
SELECT CONCAT('ID admin créé: ', @admin_id) AS Status;

-- Étape 8: Mettre à jour la relation dans la table user
UPDATE user 
SET admin_id = @admin_id 
WHERE id = @user_id;

-- Étape 9: Vérification finale
SELECT '=== VÉRIFICATION FINALE ===' AS Status;
SELECT 
    u.id AS user_id,
    u.full_name,
    u.email,
    u.role,
    u.activate AS user_active,
    u.admin_id,
    a.id AS admin_id_from_admins,
    a.activate AS admin_active
FROM user u
LEFT JOIN admins a ON u.id = a.user_id
WHERE u.email = 'cisseodl@gmail.com';

-- Étape 10: Afficher un message de succès
SELECT '✅ Utilisateur admin créé avec succès!' AS Status;
SELECT '📧 Email: cisseodl@gmail.com' AS Identifiants;
SELECT '🔑 Mot de passe: cisse@2025' AS Identifiants;
SELECT '⚠️  IMPORTANT: Redémarrez le backend sur Elastic Beanstalk pour que le hash du mot de passe soit correctement généré' AS Note;

-- ============================================================================
-- NOTES IMPORTANTES:
-- ============================================================================
-- 1. Ce script crée l'utilisateur admin dans la base RDS AWS
--
-- 2. Le hash du mot de passe dans ce script est TEMPORAIRE
--    Le vrai hash BCrypt pour "cisse@2025" sera généré par Spring Boot
--    au prochain redémarrage du backend via le CommandLineRunner
--
-- 3. Après avoir exécuté ce script:
--    a) Redémarrez l'application sur Elastic Beanstalk
--    b) Le CommandLineRunner dans AwsLearningApplication.java va:
--       - Détecter que l'utilisateur existe déjà
--       - Mettre à jour le hash du mot de passe avec le bon hash BCrypt
--
-- 4. Identifiants de connexion finaux:
--    Email: cisseodl@gmail.com
--    Mot de passe: cisse@2025
--
-- 5. Pour accéder à la base RDS depuis DBeaver:
--    - Host: awseb-e-rafruf9ypt-stack-awsebrdsdatabase-rfrjq9mjmb0m.ck5a2240egow.us-east-1.rds.amazonaws.com
--    - Port: 3306
--    - Database: odcawslearning
--    - Username: root (ou celui configuré dans RDS)
--    - Password: (celui configuré dans RDS, récupéré depuis les variables d'environnement)
-- ============================================================================
