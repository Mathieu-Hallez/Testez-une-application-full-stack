package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.payload.response.JwtResponse;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    private static final String AUTHENTIFICATION_PATH = "/api/auth";

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
    private Authentication authentication;
    @MockBean
    private JwtUtils jwtUtils;

    private static User user;
    private static User adminUser;

    @BeforeAll
    public static void setUp() {
        user = new User(1L, "test@test.com", "user-lastname", "user-firstname",
                "123456", false, LocalDateTime.now(), null);
        adminUser = new User(1L, "test@test.com", "user-lastname", "user-firstname",
                "123456", true, LocalDateTime.now(), null);
    }

    @Test
    public void givenEmailAndPassword_whenLoginUser_thenResponseJwtResponseObjectWithStatusOk() throws Exception {

        //Given
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@test.com");
        loginRequest.setPassword("123456");

        when(authentication.getPrincipal()).thenReturn(new UserDetailsImpl(user.getId(), user.getEmail(), user.getFirstName(), user.getLastName(), user.isAdmin(), user.getPassword()));
        when(authentication.isAuthenticated()).thenReturn(false);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(jwtUtils.generateJwtToken(any(Authentication.class))).thenReturn("jwt");

        //When
        MvcResult result = mockMvc.perform(
                post(AUTHENTIFICATION_PATH + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest))
        // Then
        ).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        JwtResponse jwtResponseReceived = objectMapper.readValue(result.getResponse().getContentAsString(), JwtResponse.class);
        assertEquals(loginRequest.getEmail(), jwtResponseReceived.getUsername());
        assertFalse(jwtResponseReceived.getAdmin());

    }

    @Test
    public void givenEmailAndPassword_whenLoginUser_thenResponseJwtResponseObjectAdminWithStatusOk() throws Exception {

        //Given
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@test.com");
        loginRequest.setPassword("123456");

        when(authentication.getPrincipal()).thenReturn(new UserDetailsImpl(adminUser.getId(), adminUser.getEmail(), adminUser.getFirstName(), adminUser.getLastName(), true, adminUser.getPassword()));
        when(authentication.isAuthenticated()).thenReturn(false);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(adminUser));
        when(jwtUtils.generateJwtToken(any(Authentication.class))).thenReturn("jwt");

        //When
        MvcResult result = mockMvc.perform(
                post(AUTHENTIFICATION_PATH + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest))
                // Then
        ).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        JwtResponse jwtResponseReceived = objectMapper.readValue(result.getResponse().getContentAsString(), JwtResponse.class);
        assertEquals(loginRequest.getEmail(), jwtResponseReceived.getUsername());
        assertTrue(jwtResponseReceived.getAdmin());
    }

    @Test
    public void givenSignUpRequest_whenRegisterUser_thenReturnResponseStatusOkWithMessageResponse() throws Exception {
        SignupRequest signUpRequest = new SignupRequest();
        signUpRequest.setEmail("test@test.com");
        signUpRequest.setPassword("123456");
        signUpRequest.setFirstName("firstname");
        signUpRequest.setLastName("lastname");

        //when existsByEmail
        when(userRepository.existsByEmail(anyString())).thenReturn(false);

        MvcResult result = mockMvc.perform(post(AUTHENTIFICATION_PATH + "/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpRequest))
        ).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
    }
}
