package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.dto.SessionDto;
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
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Testing session controller.")
@WithMockUser
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
    public void givenAnExistIdSession_whenCallFindById_thenResponseOKStatusWithSessionDto() throws Exception {
        when(sessionService.getById(any(Long.class))).thenReturn(session);

        mockMvc.perform(get(SessionControllerTest.SESSION_PATH + "/{id}", "1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(sessionMapper.toDto(session))));


    }

    @Test
    public void givenAnIdSession_whenCallFindById_thenResponseNotFound() throws Exception {
        when(sessionService.getById(any(Long.class))).thenReturn(null);

        mockMvc.perform(get(SessionControllerTest.SESSION_PATH + "/{id}", "1"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void givenAString_whenCallFindById_thenResponseBadRequest() throws Exception {
        mockMvc.perform(get(SessionControllerTest.SESSION_PATH + "/{id}", "test"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void whenCallFindAll_thenResponseOkStatusWithAListOfSessions() throws Exception {
        List<Session> sessions = new ArrayList<>();
        sessions.add(session);
        List<SessionDto> dtoSessions = this.sessionMapper.toDto(sessions);
        when(sessionService.findAll()).thenReturn(sessions);


        MvcResult result = mockMvc.perform(get(SessionControllerTest.SESSION_PATH))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        List<SessionDto> sessionsDtoReceived = Arrays.asList(objectMapper.readValue(result.getResponse().getContentAsString(), SessionDto[].class));

        assertThat(result.getResponse().getContentAsString()).isEqualTo(objectMapper.writeValueAsString(dtoSessions));
        assertThat(sessionsDtoReceived.size()).isEqualTo(1);
        assertThat(sessionsDtoReceived.get(0).getName()).isEqualTo(session.getName());
    }

    @Test
    @Disabled
    public void givenValidSessionDto_whenCallCreate_thenResponseOkStatusWithTheNewSessionDtoCreated() throws Exception {


//        MvcResult result = mockMvc.perform(post(SessionControllerTest.SESSION_PATH).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString()))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andReturn();


    }

    // Create
    // - Ok
    // - Session Dto
    // - Bad Request (Unvalid SessionDto)

    // Update
    // - OK
    // - BadRequest NumberFormatException
    // - Bad Request (Unvalid SessionDto)

    // Delete
    // - OK
    // - BadRequest NumberFormatException
    // - Bad Request (Unvalid SessionDto)

    // Participate
    // - OK
    // - BadRequest NumberFormatException

    // NoLongerPartcipate
    // - OK
    // - BadRequest NumberFormatException

}
