package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
public class AuthControllerTest {

    @SpringBootTest
    @AutoConfigureMockMvc
    static public class AuthControllerUnitTest {

        @Autowired
        private ObjectMapper objectMapper;

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private UserService userService;
        @MockBean
        private UserRepository userRepository;
        @MockBean
        private AuthenticationManager authenticationManager;
        @MockBean
        private JwtUtils jwtUtils;

//        @Test
//        public void shouldNotAllowAccessToUnauthenticatedUsers() throws Exception {
//            mockMvc.perform(MockMvcRequestBuilders.get("/test")).andExpect(status().isForbidden());
//        }

        private static User user;

        @BeforeAll
        public static void setUp() {
            user = new User(1L, "test@test.com", "user-lastname", "user-firstname",
                    "123456", false, LocalDateTime.now(), null);
        }

        @Test
        public void givenEmailAndPassword_whenLoginUser_thenResponseJwtResponseObject() throws Exception {

            //Given
            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setEmail("test@test.com");
            loginRequest.setPassword("123456");
            Authentication authentication = new Authentication() {
                @Override
                public Collection<? extends GrantedAuthority> getAuthorities() {
                    return null;
                }            @Override
                public Object getCredentials() {
                    return null;
                }            @Override
                public Object getDetails() {
                    return null;
                }            @Override
                public Object getPrincipal() {
                    return new UserDetailsImpl(user.getId(), user.getEmail(), user.getFirstName(), user.getLastName(), user.isAdmin(), user.getPassword());
                }            @Override
                public boolean isAuthenticated() {
                    return false;
                }            @Override
                public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {            }            @Override
                public String getName() {
                    return null;
                }
            };

            when(authenticationManager.authenticate(any())).thenReturn(authentication);
            when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
            when(jwtUtils.generateJwtToken(any(Authentication.class))).thenReturn("jwt");

            //When
            mockMvc.perform(
                    post("/api/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(loginRequest))
            // Then
            ).andExpect(MockMvcResultMatchers.status().isOk());

        }

    }
}
