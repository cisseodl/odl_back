package com.odc.aws_learning;

import com.odc.aws_learning.app.entity.LabDefinition;
import com.odc.aws_learning.app.repository.LabDefinitionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

/**
 * Runner pour charger les données initiales des Labs (définitions d'exercices pratiques).
 * 
 * Ce runner s'exécute au démarrage de l'application et insère 2 labs exemples
 * si la base de données est vide, permettant de tester immédiatement les endpoints.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LabDataLoader implements CommandLineRunner {

    private final LabDefinitionRepository labDefinitionRepository;

    @Override
    public void run(String... args) {
        try {
            // Vérifier si le repository est vide
            long count = labDefinitionRepository.count();
            
            if (count == 0) {
                log.info("[LabDataLoader] Aucun lab trouvé en base. Création des labs exemples...");
                
                // Créer le Lab 1 : Déploiement d'un serveur Web Nginx
                LabDefinition lab1 = new LabDefinition();
                lab1.setTitle("Déploiement d'un serveur Web Nginx");
                lab1.setDescription("Apprenez à lancer un conteneur et exposer le port 80.");
                lab1.setDockerImageName("nginx:latest");
                lab1.setInstructions(
                        "# Instructions pour le Lab Nginx\n\n" +
                        "## Objectif\n" +
                        "Déployer un serveur web Nginx dans un conteneur Docker et l'exposer sur le port 80.\n\n" +
                        "## Étapes\n" +
                        "1. Lancez le conteneur avec la commande appropriée\n" +
                        "2. Vérifiez que le serveur répond sur le port 80\n" +
                        "3. Accédez à la page d'accueil par défaut de Nginx\n\n" +
                        "## Commandes utiles\n" +
                        "- `docker run -d -p 80:80 nginx:latest`\n" +
                        "- `curl http://localhost`\n" +
                        "- `docker ps` pour voir les conteneurs actifs"
                );
                lab1.setEstimatedDurationMinutes(30);
                lab1.setActivate(true);
                
                // Créer le Lab 2 : Introduction à Python & Boto3
                LabDefinition lab2 = new LabDefinition();
                lab2.setTitle("Introduction à Python & Boto3");
                lab2.setDescription("Scripting AWS avec la librairie Boto3.");
                lab2.setDockerImageName("python:3.9-slim");
                lab2.setInstructions(
                        "# Instructions pour le Lab Python & Boto3\n\n" +
                        "## Objectif\n" +
                        "Apprendre à utiliser la librairie Boto3 pour interagir avec les services AWS.\n\n" +
                        "## Étapes\n" +
                        "1. Installez Boto3 dans l'environnement Python\n" +
                        "2. Configurez vos credentials AWS (via variables d'environnement ou fichier ~/.aws/credentials)\n" +
                        "3. Créez un script Python qui liste vos buckets S3\n" +
                        "4. Exécutez le script et vérifiez le résultat\n\n" +
                        "## Exemple de code\n" +
                        "```python\n" +
                        "import boto3\n" +
                        "\n" +
                        "s3 = boto3.client('s3')\n" +
                        "response = s3.list_buckets()\n" +
                        "for bucket in response['Buckets']:\n" +
                        "    print(f\"Bucket: {bucket['Name']}\")\n" +
                        "```\n\n" +
                        "## Commandes utiles\n" +
                        "- `pip install boto3`\n" +
                        "- `python script.py`"
                );
                lab2.setEstimatedDurationMinutes(45);
                lab2.setActivate(true);
                
                // Sauvegarder les labs
                labDefinitionRepository.save(lab1);
                labDefinitionRepository.save(lab2);
                
                log.info("========================================");
                log.info("[LabDataLoader] ✅ {} labs créés avec succès !", 2);
                log.info("  - Lab 1: {}", lab1.getTitle());
                log.info("  - Lab 2: {}", lab2.getTitle());
                log.info("========================================");
                
            } else {
                log.info("[LabDataLoader] {} lab(s) déjà présent(s) en base. Aucune création nécessaire.", count);
            }
            
        } catch (DataAccessException e) {
            // Erreur spécifique d'accès à la base de données (connexion, SQL, etc.)
            log.warn("[LabDataLoader] ⚠️ Impossible d'accéder à la base de données pour charger les labs. " +
                    "L'application continuera de démarrer. Erreur: {}", e.getMessage());
            log.debug("[LabDataLoader] Détails de l'erreur:", e);
        } catch (Exception e) {
            // Toute autre exception (ne doit pas empêcher le démarrage de l'application)
            log.error("[LabDataLoader] ❌ Erreur inattendue lors du chargement des données initiales: {}", 
                    e.getMessage(), e);
            log.warn("[LabDataLoader] L'application continuera de démarrer malgré cette erreur.");
        }
    }
}
