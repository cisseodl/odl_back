-- Script pour supprimer les 2 labs créés automatiquement
-- À exécuter après la mise à jour du code

-- Supprimer les sessions de labs associées d'abord (si elles existent)
DELETE FROM lab_session WHERE lab_definition_id IN (
    SELECT id FROM lab_definition 
    WHERE title IN (
        'Déploiement d''un serveur Web Nginx',
        'Introduction à Python & Boto3'
    )
);

-- Supprimer les labs créés automatiquement
DELETE FROM lab_definition 
WHERE title IN (
    'Déploiement d''un serveur Web Nginx',
    'Introduction à Python & Boto3'
);

