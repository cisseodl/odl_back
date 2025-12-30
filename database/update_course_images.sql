-- ============================================================================
-- Script de Mise à Jour des Images des Cours - ODL
-- Projet : Orange Digital Learning (ODL)
-- ============================================================================
-- Ce script met à jour les chemins d'images des cours dans la base de données
-- pour utiliser les vraies images copiées dans le dossier uploads/cours/
-- ============================================================================
-- IMPORTANT : Exécutez ce script APRÈS avoir copié les images dans uploads/cours/
-- ============================================================================

USE odcawslearning;

-- ============================================================================
-- Mise à jour des images des cours
-- ============================================================================
-- Les images sont stockées avec juste le nom du fichier
-- Le frontend construit l'URL : /downloads/cours/{imagePath}

-- Cours 1 : AWS Fundamentals (Cloud Computing)
UPDATE courses 
SET image_path = 'amazon-web-services.png'
WHERE title = 'AWS Fundamentals';

-- Cours 2 : React.js Avancé (Développement Web)
UPDATE courses 
SET image_path = 'JavaScript-Logo.png'
WHERE title = 'React.js Avancé';

-- Cours 3 : Sécurité des Applications Web (Cybersécurité)
UPDATE courses 
SET image_path = 'computer-vision-banner.png'
WHERE title = 'Sécurité des Applications Web';

-- Cours 4 : Docker et Kubernetes (Cloud Computing)
UPDATE courses 
SET image_path = 'kubernate.png'
WHERE title = 'Docker et Kubernetes';

-- ============================================================================
-- Vérification des mises à jour
-- ============================================================================
SELECT id, title, image_path, categorie_id 
FROM courses 
ORDER BY id;

-- ============================================================================
-- NOTES IMPORTANTES :
-- ============================================================================
-- 1. Les images doivent être copiées dans : odc_learning_api-master/uploads/cours/
-- 2. Le frontend construit l'URL : http://localhost:8080/awsodclearning/downloads/cours/{imagePath}
-- 3. Si les images ne s'affichent pas, vérifiez :
--    - Que les fichiers existent dans uploads/cours/
--    - Que le backend est démarré
--    - Que le chemin dans image_path correspond exactement au nom du fichier
-- ============================================================================

