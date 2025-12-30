-- ============================================================================
-- Script de Correction des Images des Cours - ODL
-- Projet : Orange Digital Learning (ODL)
-- ============================================================================
-- Ce script corrige les chemins d'images dans la base de données
-- pour utiliser les vrais noms de fichiers copiés dans uploads/cours/
-- ============================================================================
-- IMPORTANT : Exécutez ce script APRÈS avoir copié les images dans uploads/cours/
-- ============================================================================

USE odcawslearning;

-- ============================================================================
-- Correction des images des cours avec les noms EXACTS des fichiers
-- ============================================================================
-- Les images sont stockées avec juste le nom du fichier (avec extension)
-- Le frontend construit l'URL : /downloads/cours/{imagePath}

-- Cours 1 : AWS Fundamentals (Cloud Computing)
-- Image : amazon-web-services.png ✅ (déjà fonctionne)
UPDATE courses 
SET image_path = 'amazon-web-services.png'
WHERE title = 'AWS Fundamentals';

-- Cours 2 : React.js Avancé (Développement Web)
-- Image : JavaScript-Logo.png (extension .png)
UPDATE courses 
SET image_path = 'JavaScript-Logo.png'
WHERE title = 'React.js Avancé';

-- Cours 3 : Sécurité des Applications Web (Cybersécurité)
-- Image : computer-vision-banner.png (extension .png)
UPDATE courses 
SET image_path = 'computer-vision-banner.png'
WHERE title = 'Sécurité des Applications Web';

-- Cours 4 : Docker et Kubernetes (Cloud Computing)
-- Image : kubernate.png (extension .png)
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
-- 2. Noms de fichiers EXACTS (avec extension) :
--    - amazon-web-services.png
--    - JavaScript-Logo.png
--    - computer-vision-banner.png
--    - kubernate.png
-- 3. Le frontend construit l'URL : http://localhost:8080/awsodclearning/downloads/cours/{imagePath}
-- 4. Vérifiez que les fichiers existent physiquement dans uploads/cours/ avant d'exécuter
-- ============================================================================

