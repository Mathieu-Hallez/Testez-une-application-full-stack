package com.openclassrooms.starterjwt.security.jwt;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class JwtUtilsTest {

    private static Authentication authentication;

    @Autowired
    private JwtUtils jwtUtils;
//    @InjectMocks
//    private static Jwts jwts;

//    private static String jwtSecret = "openclassrooms";
//    private static int jwtExpirationMs = 86400000;

    private String jwt = "";

    @BeforeAll
    public static void setup() {
        UserDetailsImpl userDetailsImp = UserDetailsImpl
                .builder()
                .id(1L)
                .username("username")
                .lastName("lastname")
                .firstName("firstname")
                .password("12345")
                .build();

        authentication = new Authentication() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return null;
            }

            @Override
            public Object getCredentials() {
                return null;
            }

            @Override
            public Object getDetails() {
                return null;
            }

            @Override
            public Object getPrincipal() {
                return userDetailsImp;
            }

            @Override
            public boolean isAuthenticated() {
                return false;
            }

            @Override
            public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

            }

            @Override
            public String getName() {
                return null;
            }
        };
    }

    @Test
    public void givenAuthentificationObject_whenGenerateJwtToken_thenReturnJwtString() {
//        token = Jwts.builder()
//                .setSubject((((UserDetailsImpl) authentication.getPrincipal()).getUsername()))
//                .setIssuedAt(new Date())
//                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
//                .signWith(SignatureAlgorithm.HS512, jwtSecret)
//                .compact();
//        assertThat(token).isNotNull();
//        assertThat(jwtUtils.getUserNameFromJwtToken(token)).isEqualTo("username");

        String jwtToken = jwtUtils.generateJwtToken(authentication);
        assertNotNull(jwtToken);
        assertEquals("username", jwtUtils.getUserNameFromJwtToken(jwtToken));
        assertTrue(jwtUtils.validateJwtToken(jwtToken));
    }
}
