package com.odc.aws_learning.auth.config;

import org.springframework.context.annotation.Configuration;

/**
 * Configuration CORS désactivée - La configuration CORS est maintenant gérée
 * uniquement par SecurityConfiguration pour éviter les conflits.
 * 
 * Si vous avez besoin de réactiver cette configuration, assurez-vous qu'elle
 * ne rentre pas en conflit avec SecurityConfiguration.corsConfigurationSource()
 */
@Configuration
// Désactivé pour éviter les conflits avec SecurityConfiguration
// La configuration CORS est maintenant gérée uniquement dans SecurityConfiguration
public class CorsConfig {
    // Configuration CORS déplacée vers SecurityConfiguration.java
    // pour éviter les conflits entre plusieurs configurations CORS
}
