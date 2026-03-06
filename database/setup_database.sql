-- ============================================================================
-- Script de Configuration de la Base de Données MySQL pour ODL
-- Projet : Orange Digital Center (ODL)
-- ============================================================================
-- Ce script crée la base de données et configure l'utilisateur nécessaire
-- pour le fonctionnement de l'application backend Spring Boot.
-- ============================================================================

-- ============================================================================
-- ÉTAPE 1 : Création de la base de données
-- ============================================================================
-- Supprime la base de données si elle existe déjà (ATTENTION : supprime toutes les données)
DROP DATABASE IF EXISTS odcawslearning;

-- Crée la base de données avec l'encodage UTF-8 (recommandé pour les caractères internationaux)
CREATE DATABASE odcawslearning 
    CHARACTER SET utf8mb4 
    COLLATE utf8mb4_unicode_ci;

-- Sélectionne la base de données créée pour les opérations suivantes
USE odcawslearning;

-- ============================================================================
-- ÉTAPE 2 : Configuration de l'utilisateur (OPTION 1 : Utiliser root)
-- ============================================================================
-- Si vous utilisez l'utilisateur 'root' (configuration par défaut dans application.properties)
-- Vous pouvez ignorer cette section et passer directement à l'ÉTAPE 3.
-- 
-- ATTENTION : Utiliser root en production n'est PAS recommandé pour des raisons de sécurité.
-- Il est préférable de créer un utilisateur dédié (voir OPTION 2 ci-dessous).

-- ============================================================================
-- ÉTAPE 2 : Configuration de l'utilisateur (OPTION 2 : Créer un utilisateur dédié - RECOMMANDÉ)
-- ============================================================================
-- Supprime l'utilisateur s'il existe déjà
DROP USER IF EXISTS 'odc_learning_user'@'localhost';

-- Crée un nouvel utilisateur avec un mot de passe sécurisé
-- REMPLACEZ 'VotreMotDePasseSecurise123!' par un mot de passe fort de votre choix
CREATE USER 'ahmed123'@'localhost' IDENTIFIED BY 'ahmed123!';

-- Donne tous les privilèges sur la base de données odcawslearning à l'utilisateur créé
-- Cela permet à l'application de créer/modifier/supprimer les tables via Hibernate
GRANT ALL PRIVILEGES ON odcawslearning.* TO 'ahmed123'@'localhost';

-- Applique les changements de privilèges
FLUSH PRIVILEGES;

-- ============================================================================
-- ÉTAPE 3 : Vérification
-- ============================================================================
-- Affiche la liste des bases de données pour vérifier que odcawslearning existe
SHOW DATABASES LIKE 'odcawslearning';

-- Affiche les utilisateurs créés (si vous avez utilisé l'OPTION 2)
-- SELECT user, host FROM mysql.user WHERE user = 'odc_learning_user';

-- ============================================================================
-- NOTES IMPORTANTES :
-- ============================================================================
-- 1. Si vous utilisez l'utilisateur 'root' (OPTION 1) :
--    - Le mot de passe est vide dans application.properties (ligne 15)
--    - Assurez-vous que votre MySQL local accepte les connexions root sans mot de passe
--    - Sinon, modifiez application.properties pour ajouter le mot de passe
--
-- 2. Si vous créez un utilisateur dédié (OPTION 2) :
--    - Modifiez application.properties :
--      spring.datasource.username=odc_learning_user
--      spring.datasource.password=VotreMotDePasseSecurise123!
--
-- 3. Port MySQL : 3306 (par défaut, configuré dans application.properties ligne 13)
--
-- 4. Hibernate créera automatiquement les tables au premier démarrage grâce à :
--    spring.jpa.hibernate.ddl-auto=update
--
-- 5. Encodage : utf8mb4_unicode_ci permet de stocker des caractères spéciaux,
--    emojis, et caractères de toutes les langues.
-- ============================================================================

