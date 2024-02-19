package com.openclassrooms.starterjwt.security.jwt;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class JwtUtilsTest {

    @Autowired
    private JwtUtils jwtUtils;

    @MockBean
    private Authentication authentication;

    @BeforeEach
    public void setup() {
        UserDetailsImpl userDetailsImp = UserDetailsImpl
                .builder()
                .id(1L)
                .username("username")
                .lastName("lastname")
                .firstName("firstname")
                .password("12345")
                .build();

        when(authentication.getPrincipal()).thenReturn(userDetailsImp);
        when(authentication.isAuthenticated()).thenReturn(false);
    }

    @Test
    public void givenAuthenticationObject_whenGenerateJwtToken_thenReturnJwtString() {
        String jwtToken = jwtUtils.generateJwtToken(authentication);
        assertNotNull(jwtToken);
        assertEquals("username", jwtUtils.getUserNameFromJwtToken(jwtToken));
        assertTrue(jwtUtils.validateJwtToken(jwtToken));
    }
}
