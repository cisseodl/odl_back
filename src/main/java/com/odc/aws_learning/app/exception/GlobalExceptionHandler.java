package com.odc.aws_learning.app.exception;

import com.odc.aws_learning.auth.base.response.CResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Gestionnaire global des exceptions pour standardiser les réponses d'erreur
 * 
 * @author ODC Team
 * @version 1.0
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Gère les erreurs de validation (@Valid)
     * 
     * @param ex Exception de validation
     * @return ResponseEntity avec message d'erreur formaté
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CResponse<?>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        String errorMessage = "Erreurs de validation: " + 
            errors.entrySet().stream()
                .map(e -> e.getKey() + ": " + e.getValue())
                .collect(Collectors.joining(", "));
        
        logger.warn("Erreur de validation: {}", errorMessage);
        
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(CResponse.error(errorMessage));
    }

    /**
     * Gère les violations de contraintes
     * 
     * @param ex Exception de violation de contrainte
     * @return ResponseEntity avec message d'erreur
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<CResponse<?>> handleConstraintViolationException(ConstraintViolationException ex) {
        String errorMessage = ex.getConstraintViolations().stream()
            .map(ConstraintViolation::getMessage)
            .collect(Collectors.joining(", "));
        
        logger.error("Violation de contrainte: {}", errorMessage, ex);
        
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(CResponse.error("Violation de contrainte: " + errorMessage));
    }

    /**
     * Gère les erreurs IllegalArgumentException
     * 
     * @param ex Exception IllegalArgumentException
     * @return ResponseEntity avec message d'erreur
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<CResponse<?>> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(CResponse.error(ex.getMessage()));
    }

    /**
     * Gère les erreurs RuntimeException
     * 
     * @param ex Exception RuntimeException
     * @return ResponseEntity avec message d'erreur
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<CResponse<?>> handleRuntimeException(RuntimeException ex) {
        logger.error("========================================");
        logger.error("=== RUNTIME EXCEPTION CAPTURÉE ===");
        logger.error("Type: {}", ex.getClass().getName());
        logger.error("Message: {}", ex.getMessage());
        logger.error("Cause: {}", (ex.getCause() != null ? ex.getCause().getMessage() : "N/A"));
        logger.error("========================================", ex);
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(CResponse.error("Erreur serveur: " + ex.getMessage()));
    }

    /**
     * Gère toutes les autres exceptions
     * 
     * @param ex Exception générique
     * @return ResponseEntity avec message d'erreur
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<CResponse<?>> handleGenericException(Exception ex) {
        logger.error("========================================");
        logger.error("=== EXCEPTION GÉNÉRIQUE CAPTURÉE ===");
        logger.error("Type: {}", ex.getClass().getName());
        logger.error("Message: {}", ex.getMessage());
        logger.error("Cause: {}", (ex.getCause() != null ? ex.getCause().getMessage() : "N/A"));
        logger.error("========================================", ex);
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(CResponse.error("Erreur inattendue: " + ex.getMessage()));
    }
}
