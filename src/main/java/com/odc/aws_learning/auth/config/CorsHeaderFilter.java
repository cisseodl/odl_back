package com.odc.aws_learning.auth.config;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Filtre qui ajoute les en-têtes CORS à toutes les réponses, y compris en cas d'erreur (401, 413, 500).
 * S'exécute en premier pour garantir que admin.smart-odc.com et les autres origines autorisées
 * reçoivent toujours Access-Control-Allow-Origin (évite "blocked by CORS policy").
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsHeaderFilter extends OncePerRequestFilter {

    private static final List<String> ALLOWED_ORIGINS = List.of(
            "https://admin.smart-odc.com",
            "https://smart-odc.com",
            "https://api.smart-odc.com",
            "https://pi.smart-odc.com",
            "http://localhost:3000",
            "http://localhost:3001",
            "http://127.0.0.1:3000",
            "http://127.0.0.1:3001"
    );

    private static boolean isAllowedOrigin(String origin) {
        if (origin == null || origin.isEmpty()) return false;
        if (ALLOWED_ORIGINS.contains(origin)) return true;
        if (origin.endsWith(".smart-odc.com") || origin.endsWith(".amplifyapp.com") || origin.endsWith(".elasticbeanstalk.com")) {
            return true;
        }
        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String origin = request.getHeader("Origin");
        if (origin != null && isAllowedOrigin(origin)) {
            response.setHeader("Access-Control-Allow-Origin", origin);
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, PATCH, HEAD");
            response.setHeader("Access-Control-Allow-Headers", "*");
            response.setHeader("Access-Control-Max-Age", "3600");
            response.setHeader("Access-Control-Expose-Headers", "Authorization, Content-Type");
        }

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            filterChain.doFilter(request, response);
            return;
        }

        filterChain.doFilter(request, response);
    }
}
