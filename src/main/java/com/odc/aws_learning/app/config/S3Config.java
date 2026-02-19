package com.odc.aws_learning.app.config;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Client S3 utilisant uniquement le Rôle IAM de l'instance (Elastic Beanstalk aws-elasticbeanstalk-ec2-role).
 * Aucune clé statique (Access Key / Secret Key) : évite InvalidAccessKeyId si d'anciennes variables
 * (AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_KEY, AWS_ACCESS_KEY, AWS_SECRET_KEY) traînent dans l'environnement.
 */
@Configuration
public class S3Config {

    private static final Logger logger = LoggerFactory.getLogger(S3Config.class);

    @Value("${cloud.aws.region.static:us-east-1}")
    private String region;

    @Bean
    public AmazonS3 amazonS3() {
        logger.info("Initialisation du client S3 avec le rôle IAM (DefaultAWSCredentialsProviderChain), région: {}", region);
        return AmazonS3ClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new DefaultAWSCredentialsProviderChain())
                .build();
    }
}
