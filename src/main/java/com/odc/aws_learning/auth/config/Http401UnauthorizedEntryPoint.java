package com.odc.aws_learning.auth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Retourne 401 Unauthorized avec un corps JSON lorsque la requête n'est pas authentifiée
 * (token absent, invalide ou expiré). Permet au front de distinguer 401 (reconnexion) et 403 (accès refusé).
 */
@Component
@RequiredArgsConstructor
public class Http401UnauthorizedEntryPoint implements AuthenticationEntryPoint {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        Map<String, Object> body = Map.of(
                "ok", false,
                "ko", true,
                "message", "Non authentifié. Token absent, invalide ou expiré."
        );
        response.getWriter().write(OBJECT_MAPPER.writeValueAsString(body));
    }
}
