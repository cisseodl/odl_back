package com.odc.aws_learning.auth.config;

import com.odc.aws_learning.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserService userService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request
                        // Autoriser toutes les requêtes OPTIONS (preflight CORS)
                        .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .antMatchers(
                                SecurityConstants.AUTH_LOGIN_URL,
                                SecurityConstants.DOWNLOAD_URL,
                                SecurityConstants.CONTACT_URL,
                                SecurityConstants.CHECK_USER_URL,
                                SecurityConstants.CONFIG_URL,
                                "/auth/signup", // Autoriser l'inscription sans authentification
                                "/auth/signin", // Autoriser la connexion sans authentification
                                "/auth/check-availability", // Autoriser la vérification de disponibilité
                                "/courses/read", // Endpoint public pour lire la liste des cours (sans modules/leçons)
                                "/courses/read/**", // Endpoint public pour lire les détails d'un cours (avec modules/leçons) - consultation publique autorisée
                                "/modules/course/**", // Endpoint public pour lire les modules d'un cours - consultation publique autorisée
                                "/api/categories/read", // Endpoint public pour lire les catégories
                                "/api/categories/read/**", // Endpoint public pour lire une catégorie par ID
                                "/cohorte/read", // Endpoint public pour lire les cohortes
                                "/api/v1/rubriques/read", // Endpoint public pour lire les rubriques (piliers)
                                "/api/v1/rubriques/read/**", // Endpoint public pour lire une rubrique par ID
                                "/api/dashboard/public-stats", // Endpoint public pour les statistiques publiques
                                "/api/odc-formations/read", // Endpoint public pour lire les formations ODC
                                "/api/reviews/all", // Endpoint public pour lire toutes les revues
                                "/reviews/course/**", // Endpoint public pour lire les avis d'un cours
                                "/api/testimonials", // Endpoint public pour lire tous les témoignages
                                "/api/files/**",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-ui.html"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // IMPORTANT: Utiliser setAllowedOriginPatterns au lieu de setAllowedOrigins
        // car setAllowedOrigins ne fonctionne pas avec allowCredentials(true)
        // et setAllowedOriginPatterns permet d'utiliser des patterns avec wildcards
        configuration.setAllowedOriginPatterns(Arrays.asList(
            "http://localhost:*",
            "http://127.0.0.1:*",
            "https://main.d7kfdxiyupcrp.amplifyapp.com",
            "https://*.amplifyapp.com", // Autoriser toutes les applications Amplify
            "https://*.elasticbeanstalk.com", // Autoriser les environnements Elastic Beanstalk
            "https://api.smart-odc.com", // Domaine personnalisé API
            "https://pi.smart-odc.com", // Domaine frontend
            "https://admin.smart-odc.com", // Domaine admin explicitement (IMPORTANT pour résoudre l'erreur CORS)
            "https://smart-odc.com", // Domaine racine smart-odc.com
            "https://*.smart-odc.com" // Autoriser tous les sous-domaines smart-odc.com
        ));
        
        // Méthodes HTTP explicites incluant OPTIONS pour les requêtes preflight
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "HEAD"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setExposedHeaders(Arrays.asList(
            "Authorization", 
            "Content-Type", 
            "Access-Control-Allow-Origin", 
            "Access-Control-Allow-Credentials",
            "Access-Control-Allow-Methods",
            "Access-Control-Allow-Headers"
        ));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}