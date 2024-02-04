package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Testing session controller.")
@Disabled
public class SessionControllerTest {

    private static final String SESSION_PATH = "/api/session";

    @Autowired
    private SessionMapper sessionMapper;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SessionService sessionService;

    private Session session;
    private User user;
    private Teacher teacher;

    @BeforeEach
    public void setup() {
        user = User.builder()
                .id(1L)
                .email("test@test.com")
                .lastName("lastname")
                .firstName("firstname")
                .password("1234")
                .admin(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(null)
                .build();
        teacher = Teacher.builder()
                .id(1L)
                .lastName("lastname")
                .firstName("firstname")
                .createdAt(LocalDateTime.now())
                .updatedAt(null)
                .build();
        session = Session.builder()
                .name("session1")
                .id(1L)
                .date(Date.from(Instant.now()))
                .description("description")
                .teacher(teacher)
                .users(new ArrayList<User>())
                .createdAt(LocalDateTime.now())
                .updatedAt(null)
                .build();
    }

    @Test
    @WithMockUser
    public void givenAnExistIdSession_whenCallFindById_thenResponseOKStatusWithSessionDto() throws Exception {
        when(sessionService.getById(any(Long.class))).thenReturn(session);

        mockMvc.perform(get(SessionControllerTest.SESSION_PATH + "/{id}", "1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(sessionMapper.toDto(session))));


    }
}
