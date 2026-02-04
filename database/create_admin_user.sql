-- ============================================================================
-- Script SQL pour créer l'utilisateur ADMIN par défaut
-- ============================================================================
-- Ce script crée l'utilisateur admin avec les identifiants par défaut
-- Email: cisseodl@gmail.com
-- Mot de passe: cisse@2025
--
-- IMPORTANT: Le mot de passe doit être hashé avec BCrypt
-- Hash BCrypt pour "cisse@2025" généré par Spring Security
-- ============================================================================

-- Étape 1: Vérifier si l'utilisateur existe déjà
SELECT 'Vérification de l''utilisateur existant...' AS Status;
SELECT * FROM user WHERE email = 'cisseodl@gmail.com';

-- Étape 2: Supprimer l'utilisateur existant s'il existe (optionnel)
-- DELETE FROM user WHERE email = 'cisseodl@gmail.com';

-- Étape 3: Créer l'utilisateur admin
-- Note: Le hash BCrypt pour "cisse@2025" doit être généré par Spring Boot
-- Pour l'instant, on utilise un hash temporaire qui sera remplacé au démarrage

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
    -- Hash BCrypt temporaire (sera remplacé par Spring Boot au démarrage)
    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
    '0000000000',
    true,
    NULL,
    'ADMIN',
    NOW(),
    NOW()
)
ON DUPLICATE KEY UPDATE
    full_name = 'CisseOdl',
    activate = true,
    role = 'ADMIN',
    last_modified_at = NOW();

-- Étape 4: Récupérer l'ID de l'utilisateur créé
SET @user_id = (SELECT id FROM user WHERE email = 'cisseodl@gmail.com');

-- Étape 5: Créer l'entité Admin liée à l'utilisateur
-- Supprimer l'admin existant s'il existe
DELETE FROM admins WHERE user_id = @user_id;

-- Créer le nouvel admin
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

-- Étape 6: Mettre à jour la relation dans la table user
UPDATE user SET admin_id = (SELECT id FROM admins WHERE user_id = @user_id) WHERE id = @user_id;

-- Étape 7: Vérification
SELECT 'Utilisateur admin créé avec succès!' AS Status;
SELECT 
    u.id,
    u.full_name,
    u.email,
    u.role,
    u.activate,
    a.id AS admin_id
FROM user u
LEFT JOIN admins a ON u.id = a.user_id
WHERE u.email = 'cisseodl@gmail.com';

-- ============================================================================
-- NOTES IMPORTANTES:
-- ============================================================================
-- 1. Le mot de passe hashé dans ce script est temporaire
--    Le vrai hash BCrypt sera généré par Spring Boot au démarrage
--    via le CommandLineRunner dans AwsLearningApplication.java
--
-- 2. Identifiants de connexion:
--    Email: cisseodl@gmail.com
--    Mot de passe: cisse@2025
--
-- 3. Si le backend est démarré, il créera automatiquement cet utilisateur
--    s'il n'existe pas déjà (grâce au CommandLineRunner)
--
-- 4. Pour utiliser ce script:
--    - Ouvrez DBeaver
--    - Connectez-vous à la base de données odcawslearning
--    - Exécutez ce script
--    - Redémarrez le backend Spring Boot
-- ============================================================================
