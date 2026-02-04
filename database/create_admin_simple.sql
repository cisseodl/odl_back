-- ============================================================================
-- Script SQL SIMPLE pour créer l'utilisateur ADMIN dans RDS
-- ============================================================================
-- À exécuter dans DBeaver connecté à RDS
-- Host: awseb-e-rafruf9ypt-stack-awsebrdsdatabase-rfrjq9mjmb0m.ck5a2240egow.us-east-1.rds.amazonaws.com
-- Database: odcawslearning
-- ============================================================================

-- Étape 1: Supprimer l'utilisateur existant s'il existe
DELETE FROM admins WHERE user_id IN (SELECT id FROM user WHERE email = 'cisseodl@gmail.com');
DELETE FROM user WHERE email = 'cisseodl@gmail.com';

-- Étape 2: Créer l'utilisateur admin
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
    -- Hash BCrypt temporaire (sera mis à jour par Spring Boot au redémarrage)
    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
    '0000000000',
    true,
    NULL,
    'ADMIN',
    NOW(),
    NOW()
);

-- Étape 3: Récupérer l'ID de l'utilisateur créé
SET @user_id = LAST_INSERT_ID();

-- Étape 4: Créer l'entité Admin
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

-- Étape 5: Récupérer l'ID de l'admin créé
SET @admin_id = LAST_INSERT_ID();

-- Étape 6: Lier l'admin à l'utilisateur
UPDATE user 
SET admin_id = @admin_id 
WHERE id = @user_id;

-- Étape 7: Vérification
SELECT 
    '✅ Admin créé avec succès!' AS Status,
    u.id AS user_id,
    u.email,
    u.full_name,
    u.role,
    a.id AS admin_id
FROM user u
LEFT JOIN admins a ON u.id = a.user_id
WHERE u.email = 'cisseodl@gmail.com';

-- ============================================================================
-- Identifiants de connexion:
-- Email: cisseodl@gmail.com
-- Mot de passe: cisse@2025
-- 
-- ⚠️ IMPORTANT: Redémarrez le backend sur Elastic Beanstalk après avoir
--    exécuté ce script pour que le hash du mot de passe soit correctement
--    généré par Spring Boot.
-- ============================================================================
