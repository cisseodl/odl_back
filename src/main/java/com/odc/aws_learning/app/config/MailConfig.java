package com.odc.aws_learning.app.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {
    
    private static final Logger logger = LoggerFactory.getLogger(MailConfig.class);

    @Bean
    @ConditionalOnProperty(
        name = "spring.mail.enabled",
        havingValue = "true",
        matchIfMissing = false
    )
    public JavaMailSender javaMailSender(
            @Value("${spring.mail.host:smtp.gmail.com}") String host,
            @Value("${spring.mail.port:587}") int port,
            @Value("${spring.mail.username:}") String username,
            @Value("${spring.mail.password:}") String password) {
        
        logger.info("========================================");
        logger.info("=== CONFIGURATION DU SERVICE EMAIL ===");
        logger.info("Host: {}", host);
        logger.info("Port: {}", port);
        logger.info("Username: {}", username);
        logger.info("Password: {}", (password != null && !password.isEmpty() ? "***CONFIGURÉ***" : "❌ VIDE"));
        
        if (username == null || username.trim().isEmpty()) {
            logger.error("❌ ERREUR: spring.mail.username est vide dans application.properties");
        }
        
        if (password == null || password.trim().isEmpty()) {
            logger.error("❌ ERREUR: spring.mail.password est vide dans application.properties");
        }
        
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(username);
        mailSender.setPassword(password);
        
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.starttls.required", "true");
        props.put("mail.smtp.from", username);
        // Ne pas tester la connexion au démarrage
        props.put("mail.smtp.connectiontimeout", "10000");
        props.put("mail.smtp.timeout", "10000");
        props.put("mail.smtp.writetimeout", "10000");
        // Debug SMTP
        props.put("mail.debug", "false");
        
        logger.info("✅ Bean JavaMailSender créé avec succès");
        logger.info("========================================");
        
        return mailSender;
    }
}

