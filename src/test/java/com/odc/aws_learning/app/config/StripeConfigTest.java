package com.odc.aws_learning.app.config;

import com.stripe.Stripe;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestPropertySource(properties = "stripe.api.key=sk_test_fake_key_for_testing")
class StripeConfigTest {

    @Autowired
    private StripeConfig stripeConfig;

    @AfterEach
    void tearDown() {
        // Nettoyer la clé globale pour éviter les fuites entre tests
        Stripe.apiKey = null;
    }

    @Test
    void stripeApiKeyIsInitializedFromProperties() {
        // Le contexte Spring déclenche déjà @PostConstruct, donc Stripe.apiKey doit être initialisée
        assertEquals("sk_test_fake_key_for_testing", Stripe.apiKey,
                "La clé Stripe initialisée doit provenir de la propriété de test");
    }
}
