package com.odc.aws_learning.app.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Config {

    private static final Logger logger = LoggerFactory.getLogger(S3Config.class);

    @Value("${cloud.aws.credentials.access-key:}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secret-key:}")
    private String secretKey;

    @Value("${cloud.aws.region.static:us-east-1}")
    private String region;

    @Bean
    public AmazonS3 amazonS3() {
        try {
            AmazonS3ClientBuilder builder = AmazonS3ClientBuilder.standard()
                    .withRegion(region);

            // Utiliser les credentials statiques si fournis, sinon utiliser IAM role (Elastic Beanstalk)
            if (accessKey != null && !accessKey.isEmpty() && 
                !accessKey.equals("changeMe") && 
                secretKey != null && !secretKey.isEmpty() && 
                !secretKey.equals("changeMe")) {
                logger.info("Utilisation des credentials AWS statiques pour S3");
                BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
                builder.withCredentials(new AWSStaticCredentialsProvider(credentials));
            } else {
                logger.info("Utilisation des credentials IAM (DefaultAWSCredentialsProviderChain) pour S3");
                builder.withCredentials(new DefaultAWSCredentialsProviderChain());
            }

            AmazonS3 client = builder.build();
            logger.info("Client S3 initialisé avec succès pour la région: {}", region);
            return client;
        } catch (Exception e) {
            logger.error("Erreur lors de l'initialisation du client S3: {}", e.getMessage(), e);
            // Créer un client avec les credentials par défaut même en cas d'erreur
            // pour permettre à l'application de démarrer
            try {
                logger.warn("Tentative de création du client S3 avec credentials par défaut");
                return AmazonS3ClientBuilder.standard()
                        .withRegion(region)
                        .withCredentials(new DefaultAWSCredentialsProviderChain())
                        .build();
            } catch (Exception ex) {
                logger.error("Impossible de créer le client S3: {}", ex.getMessage(), ex);
                throw new RuntimeException("Impossible d'initialiser le client S3", ex);
            }
        }
    }
}
