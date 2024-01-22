package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

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

//        @Test
//        public void shouldNotAllowAccessToUnauthenticatedUsers() throws Exception {
//            mockMvc.perform(MockMvcRequestBuilders.get("/test")).andExpect(status().isForbidden());
//        }

        @Test
        public void givenEmailAndPassword_whenLoginUser_thenResponseJwtResponseObject() throws Exception {

            //Given
            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setEmail("yoga@studio.com");
            loginRequest.setPassword("test!1234");

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
