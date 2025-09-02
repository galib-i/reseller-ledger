package com.galibi.resellerledger.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import com.galibi.resellerledger.entities.User;

class JwtServiceTest {

    private static final String TEST_EMAIL = "user@test.com";
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        // Use ReflectionTestUtils to set the private secretKey field for testing
        ReflectionTestUtils.setField(jwtService, "secretKey",
                "YXR0YWNrcGljdHVyZWRzb2Z0cGFpbnRzZXJpb3Vzc29mdHN0b3Zld2hvbGVmYXJtZXI=");
    }

    @Test
    void generateToken_shouldCreateTokenWithCorrectUsername_whenUserProvided() {
        User user = new User();
        user.setEmail(TEST_EMAIL);

        String token = jwtService.generateToken(user);

        assertNotNull(token);
        String extractedUsername = jwtService.extractUsername(token);
        assertEquals(TEST_EMAIL, extractedUsername);
    }

    @Test
    void isTokenValid_shouldReturnTrue_forValidToken() {
        User user = new User();
        user.setEmail(TEST_EMAIL);
        String token = jwtService.generateToken(user);

        boolean isValid = jwtService.isTokenValid(token, user);

        assertTrue(isValid);
    }
}
